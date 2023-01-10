package com.oasis.cac.vas.restfulapi.controllers.auth;

import com.oasis.cac.vas.auth.config.dto.AngularSSODto;
import com.oasis.cac.vas.dto.AdminPortalDto;
import com.oasis.cac.vas.dto.PortalUserSignUpDto;
import com.oasis.cac.vas.models.PortalUser;
import com.oasis.cac.vas.service.psql.portaluser.PortalUserService;
import com.oasis.cac.vas.service.util.GoogleVerificationService;
import com.oasis.cac.vas.utils.MessageUtil;
import com.oasis.cac.vas.utils.controllers.PublicBaseApiController;
import com.oasis.cac.vas.utils.errors.ApiError;
import com.oasis.cac.vas.utils.errors.CustomBadRequestException;
import com.oasis.cac.vas.utils.oasisenum.PortalAccountTypeConstant;
import com.oasis.cac.vas.utils.oasisenum.RoleTypeConstant;
import org.passay.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class PublicSocialAuthController extends PublicBaseApiController {

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private PortalUserService portalUserService;

    @Autowired
    private GoogleVerificationService googleVerificationService;

    private static final Logger logger = LoggerFactory.getLogger(PublicSocialAuthController.class);

    @PostMapping("/auth/connect/google/signup")
    public ResponseEntity<?> me(@Valid @RequestBody AngularSSODto angularSSODto,
                                BindingResult bindingResult) {

        ApiError apiError = null;

        try {

            if (bindingResult.hasErrors()) {

                bindingResult.getAllErrors().forEach(objectError -> {
                    logger.info(objectError.toString());
                });

                throw new CustomBadRequestException();

            } else {

               PortalUser portalUser = this.portalUserService.findPortalUserByEmail(angularSSODto.getEmail());

               if(portalUser != null) {
                   apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("email.already.exists", "en"),
                           false, new ArrayList<>(), null);
               } else {

                   Optional<String> emailOptional = this.googleVerificationService.verify(angularSSODto.getAuthToken());

                   if(emailOptional.isPresent()) {
                       // Payload
                       PortalUserSignUpDto portalUserSignUpDto = new PortalUserSignUpDto();
                       String[] roles = new String[]{"" + RoleTypeConstant.USER};
                       portalUserSignUpDto.setRoles(roles);
                       portalUserSignUpDto.setEmailVerified(false);
                       portalUserSignUpDto.setEmail(emailOptional.get());
                       portalUserSignUpDto.setCompanyName("");
                       portalUserSignUpDto.setFirstName("");
                       portalUserSignUpDto.setLastName("");

                       // PasswordValidator passwordValidator = new PasswordValidator(new LengthRule(5));

                       CharacterRule digits = new CharacterRule(EnglishCharacterData.Digit);
                       PasswordGenerator passwordGenerator = new PasswordGenerator();
                       String password = passwordGenerator.generatePassword(10, digits);

                       portalUserSignUpDto.setPassword(password);
                       portalUserSignUpDto.setEmailVerified(true);
                       portalUserSignUpDto.setPortalAccountTypeConstant(PortalAccountTypeConstant.GENERAL_USER);
                       this.portalUserService.createViaSocial(portalUserSignUpDto);

                       apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("account.created", "en"),
                               true, new ArrayList<>(), null);
                   } else {
                       apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("google.signup.failed ", "en"),
                               false, new ArrayList<>(), null);
                   }
               }

            }


            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

        } catch (Exception e) {

            e.printStackTrace();

            apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, messageUtil.getMessage("user.unauthorized", "en"),
                    false, new ArrayList<>(), null);
            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
        }

    }


}
