package com.oasis.cac.vas.restfulapi.controllers.portal_user;

import com.google.gson.Gson;
import com.oasis.cac.vas.dto.*;
import com.oasis.cac.vas.email_sender.pojo.MailPojo;
import com.oasis.cac.vas.email_sender.service.EmailService;
import com.oasis.cac.vas.email_sender.service.MailGunService;
import com.oasis.cac.vas.email_sender.service.mail_composer.MailComposerService;
import com.oasis.cac.vas.models.PortalUser;
import com.oasis.cac.vas.models.PreLicensedPartner;
import com.oasis.cac.vas.pojo.PreLicensedPartnerInnerResponsePojo;
import com.oasis.cac.vas.service.psql.email_verification.EmailVerificationService;
import com.oasis.cac.vas.service.psql.portaluser.PortalUserService;
import com.oasis.cac.vas.service.psql.pre_licensed_partner.PreLicensedPartnerService;
import com.oasis.cac.vas.utils.MessageUtil;
import com.oasis.cac.vas.utils.controllers.PublicBaseApiController;
import com.oasis.cac.vas.utils.errors.ApiError;
import com.oasis.cac.vas.utils.errors.CustomBadRequestException;
import com.oasis.cac.vas.utils.errors.MyApiResponse;
import com.oasis.cac.vas.utils.oasisenum.RoleTypeConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class PublicPortalUserController extends PublicBaseApiController {

    private static final Logger logger = LoggerFactory.getLogger(PublicPortalUserController.class.getSimpleName());

    @Autowired
    private PortalUserService portalUserService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MailGunService mailGunService;

    @Autowired
    private PreLicensedPartnerService preLicensedPartnerService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private MessageUtil messageUtil;

    @Value("${noReplyEmail}")
    private String noReplyEmail;

    @Value("${emailValidForInMinutes}")
    private String emailValidForInMinutes;

    @Value("${domainUrl}")
    private String domainUrl;

    @Autowired
    private MailComposerService mailComposerService;

    private final Gson gson;

    @Autowired
    private MyApiResponse myApiResponse;

    @Autowired
    public PublicPortalUserController() {
        this.gson = new Gson();
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> createUser(@Valid @RequestBody AdminPortalDto adminPortalDto,
                                        HttpServletResponse res,
                                        HttpServletRequest request,
                                        Authentication authentication,
                                        BindingResult bindingResult) {
        ApiError apiError = null;
        String lang = "en";

        if (bindingResult.hasErrors()) {

            bindingResult.getAllErrors().forEach(objectError -> {
                logger.info(objectError.toString());
            });

            throw new CustomBadRequestException();
        }

        try {

            PortalUser foundUser = portalUserService.findPortalUserByEmail(adminPortalDto.getEmail());

            if (foundUser != null) {
                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("portal.user.email.exists", lang),
                        false, new ArrayList<>(), null);

                return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
            }

            try {

                String json = gson.toJson(adminPortalDto);
                System.out.println(json);
                String[] roles = new String[]{"" + RoleTypeConstant.USER};
                adminPortalDto.setRoles(roles);
                adminPortalDto.setEmailVerified(false);
                this.portalUserService.create(adminPortalDto);

                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("portal.user.account.created", lang),
                        true, new ArrayList<>(), null);
            } catch (Exception e) {
                logger.info(e.toString());
                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("portal.user.account.not.created", lang),
                        true, new ArrayList<>(), null);
            }

            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());


        } catch (NullPointerException e) {

            return this.myApiResponse.internalServerErrorResponse();
        }
    }


    @PostMapping("/auth/signup/licensed-partner")
    public ResponseEntity<?> createUserForLicensedPartner(@Valid @RequestBody PreLicensedPartnerDto preLicensedPartnerDto,
                                                          BindingResult bindingResult) {
        ApiError apiError = null;
        String lang = "en";

        if (bindingResult.hasErrors()) {

            bindingResult.getAllErrors().forEach(objectError -> {
                logger.info(objectError.toString());
            });

            throw new CustomBadRequestException();
        }

        try {

            PortalUser foundUser = portalUserService.findPortalUserByEmail(preLicensedPartnerDto.getEmail());

            if (foundUser != null) {
                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("portal.user.email.exists", lang),
                        false, new ArrayList<>(), null);

                return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
            } else {

                Optional<PreLicensedPartner> optionalPreLicensedPartner = this.preLicensedPartnerService.findByEmail(preLicensedPartnerDto.getEmail());

                if (optionalPreLicensedPartner.isPresent()) {
                    apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("licensed.partner.email.exists", lang),
                            false, new ArrayList<>(), null);

                    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
                }

            }

            try {

                PreLicensedPartnerInnerResponsePojo preLicensedPartnerInnerResponsePojo = this.preLicensedPartnerService.save(preLicensedPartnerDto);

                String code = preLicensedPartnerInnerResponsePojo.getCode();

                String email = preLicensedPartnerDto.getEmail();

                Long userId = preLicensedPartnerInnerResponsePojo.getUserId();

                MailPojo mailPojo = this.mailComposerService.preLicensedPartnerEmail(userId , this.noReplyEmail, email, code, this.domainUrl, this.emailValidForInMinutes);
                // emailService.sendActualEmail(mailPojo);
                this.mailGunService.sendMail(mailPojo);

                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("email.verification.link.sent", lang),
                        true, new ArrayList<>(), null);
            } catch (Exception e) {
                logger.info(e.toString());
                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("email.verification.link.not.sent", lang),
                        false, new ArrayList<>(), null);
            }

            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());


        } catch (NullPointerException e) {

            return this.myApiResponse.internalServerErrorResponse();
        }
    }


    @PostMapping("/auth/signup/licensed-partner/verify-email")
    public ResponseEntity<?> verifyToken(@Valid @RequestBody EmailVerificationDto emailVerificationDto,
                                         HttpServletResponse res,
                                         HttpServletRequest request,
                                         BindingResult bindingResult) {
        ApiError apiError = null;
        String lang = "en";

        try {

            if (bindingResult.hasErrors()) {

                bindingResult.getAllErrors().forEach(objectError -> {
                    logger.info(objectError.toString());
                });

                throw new CustomBadRequestException();
            }

            String token = emailVerificationDto.getCode();

            Long userId = emailVerificationDto.getUserId();

            if (StringUtils.isBlank(token)) {
                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("email.verification.token", lang),
                        false, new ArrayList<>(), null);

                return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
            }

            Optional<PreLicensedPartner> optionalPreLicensedPartner = this.preLicensedPartnerService.findById(userId);

            if (optionalPreLicensedPartner.isPresent()) {

                PreLicensedPartner preLicensedPartner = optionalPreLicensedPartner.get();

                boolean isValid = this.preLicensedPartnerService.checkIfValid(preLicensedPartner, token);

                if (isValid) {
                    apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("email.verification.successful", lang),
                            true, new ArrayList<>(), preLicensedPartner.getEmail());
                } else {
                    apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("email.verification.token.expired", lang),
                            false, new ArrayList<>(), null);
                }

            } else {

                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("pre.licensed.user.not.found", lang),
                        false, new ArrayList<>(), null);

            }


            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

        } catch (Exception e) {
            return this.myApiResponse.internalServerErrorResponse();
        }

    }


    @GetMapping("/auth/signup/licensed-partner/resend-email-verification")
    public ResponseEntity<?> resendEmailVerification(@RequestParam("email") String email,
                                                     HttpServletResponse res,
                                                     HttpServletRequest request) {

        ApiError apiError = null;

        String lang = "en";

        try {

            if (TextUtils.isBlank(email)) {
                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK,
                        messageUtil.getMessage("portal.user.email.does.not.exists", lang),
                        false, new ArrayList<>(), null);
            } else {

                Optional<PreLicensedPartner> optionalPreLicensedPartner = this.preLicensedPartnerService.findByEmail(email);

                if (optionalPreLicensedPartner.isPresent()) {

                    PreLicensedPartner preLicensedPartner = optionalPreLicensedPartner.get();

                    PreLicensedPartnerInnerResponsePojo preLicensedPartnerInnerResponsePojo = this.preLicensedPartnerService.regenerate(preLicensedPartner);

                    String code = preLicensedPartnerInnerResponsePojo.getCode();

                    Long userId = preLicensedPartnerInnerResponsePojo.getUserId();

                    MailPojo mailPojo = this.mailComposerService.preLicensedPartnerEmail(userId, this.noReplyEmail, preLicensedPartner.getEmail(), code, this.domainUrl, this.emailValidForInMinutes);

                    apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("email.verification.link.sent", lang),
                            true, new ArrayList<>(), null);

                    this.mailGunService.sendMail(mailPojo);

                    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

                } else {

                    apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("email.verification.link.not.sent", lang),
                            false, new ArrayList<>(), null);
                }

            }

            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

        } catch (Exception e) {

            logger.info("Email send failed");

            return this.myApiResponse.internalServerErrorResponse();
        }

    }

}