package com.oasis.cac.vas.restfulapi.controllers.portal_user;

import com.google.gson.Gson;
import com.oasis.cac.vas.dto.licensed_partner.LicensedPartnerCompanyDetailsDto;
import com.oasis.cac.vas.dto.licensed_partner.LicensedPartnerFileUploadDto;
import com.oasis.cac.vas.dto.licensed_partner.LicensedPartnerRegistrationDto;
import com.oasis.cac.vas.dto.licensed_partner.LicensedPartnerRepresentativeDetails;
import com.oasis.cac.vas.models.PreLicensedPartner;
import com.oasis.cac.vas.service.psql.pre_licensed_partner.PreLicensedPartnerService;
import com.oasis.cac.vas.utils.MessageUtil;
import com.oasis.cac.vas.utils.controllers.PublicBaseApiController;
import com.oasis.cac.vas.utils.errors.ApiError;
import com.oasis.cac.vas.utils.errors.CustomBadRequestException;
import com.oasis.cac.vas.utils.errors.MyApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.logging.Logger;

@RestController
public class PublicLicensedUserController extends PublicBaseApiController {

    private static final Logger logger = Logger.getLogger(PublicLicensedUserController.class.getSimpleName());

    @Autowired
    private MyApiResponse myApiResponse;

    @Autowired
    private Gson gson;

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private PreLicensedPartnerService preLicensedPartnerService;

    @PostMapping("/auth/signup/licensed-partner/create/company-details")
    public ResponseEntity<?> createCompanyDetails(@Valid @RequestBody LicensedPartnerCompanyDetailsDto licensedPartnerCompanyDetailsDto,
                                                  @RequestParam String email,
                                                  BindingResult bindingResult) {
        ApiError apiError = null;
        String lang = "en";

        if (bindingResult.hasErrors()) {

            bindingResult.getAllErrors().forEach(objectError -> {
                logger.info(objectError.toString());
            });

            throw new CustomBadRequestException();
        }

        if(email == null || email.isEmpty()) {
            apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("email.required", lang),
                    false, new ArrayList<>(), null);
            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        try {

            Optional<PreLicensedPartner> optional = this.preLicensedPartnerService.findByEmail(email);

            if(optional.isPresent()) {
                PreLicensedPartner preLicensedPartner = optional.get();

                LicensedPartnerRegistrationDto licensedPartnerRegistrationDto = null;
                if(preLicensedPartner.getUserRegistrationInformation() != null && !preLicensedPartner.getUserRegistrationInformation().isEmpty()) {
                    String json = preLicensedPartner.getUserRegistrationInformation();
                    licensedPartnerRegistrationDto = this.gson.fromJson(json, LicensedPartnerRegistrationDto.class);
                    licensedPartnerRegistrationDto.setCompanyDetails(licensedPartnerCompanyDetailsDto);
                } else {
                    licensedPartnerRegistrationDto = new LicensedPartnerRegistrationDto();
                    licensedPartnerRegistrationDto.setCompanyDetails(licensedPartnerCompanyDetailsDto);
                }

                this.preLicensedPartnerService.updateForFormWizardRegistration(licensedPartnerRegistrationDto, preLicensedPartner, 1);

                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("list.of.users", lang),
                        true, new ArrayList<>(), null);


            } else {
                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("no.users.found", lang),
                        false, new ArrayList<>(), null);
            }


            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());


        } catch (NullPointerException e) {

            return this.myApiResponse.internalServerErrorResponse();
        }
    }


    @PostMapping("/auth/signup/licensed-partner/create/representative-details")
    public ResponseEntity<?> createRepresentativeDetails(@Valid @RequestBody LicensedPartnerRepresentativeDetails representativeDetails,
                                                         @RequestParam String email,
                                                         BindingResult bindingResult) {
        ApiError apiError = null;
        String lang = "en";

        if (bindingResult.hasErrors()) {

            bindingResult.getAllErrors().forEach(objectError -> {
                logger.info(objectError.toString());
            });

            throw new CustomBadRequestException();
        }

        if(email == null || email.isEmpty()) {
            apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("email.required", lang),
                    false, new ArrayList<>(), null);
            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        try {

            Optional<PreLicensedPartner> optional = this.preLicensedPartnerService.findByEmail(email);

            if(optional.isPresent()) {
                PreLicensedPartner preLicensedPartner = optional.get();

                LicensedPartnerRegistrationDto licensedPartnerRegistrationDto = null;
                if(preLicensedPartner.getUserRegistrationInformation() != null && !preLicensedPartner.getUserRegistrationInformation().isEmpty()) {

                    String json = preLicensedPartner.getUserRegistrationInformation();
                    licensedPartnerRegistrationDto = this.gson.fromJson(json, LicensedPartnerRegistrationDto.class);
                    licensedPartnerRegistrationDto.setRepresentativeDetails(representativeDetails);
                    this.preLicensedPartnerService.updateForFormWizardRegistration(licensedPartnerRegistrationDto, preLicensedPartner, 2);
                }

                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("list.of.users", lang),
                        true, new ArrayList<>(), null);
            } else {

                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("no.users.found", lang),
                        false, new ArrayList<>(), null);
            }


            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());


        } catch (NullPointerException e) {

            return this.myApiResponse.internalServerErrorResponse();
        }
    }

    @PostMapping("/auth/signup/licensed-partner/create/file-upload")
    public ResponseEntity<?> updateUrls(@Valid @RequestBody LicensedPartnerFileUploadDto licensedPartnerFileUploadDto,
                                                         @RequestParam String email,
                                                         BindingResult bindingResult) {
        ApiError apiError = null;
        String lang = "en";

        if (bindingResult.hasErrors()) {

            bindingResult.getAllErrors().forEach(objectError -> {
                logger.info(objectError.toString());
            });

            throw new CustomBadRequestException();
        }

        if(email == null || email.isEmpty()) {
            apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("email.required", lang),
                    false, new ArrayList<>(), null);
            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        try {

            Optional<PreLicensedPartner> optional = this.preLicensedPartnerService.findByEmail(email);

            if(optional.isPresent()) {

                PreLicensedPartner preLicensedPartner = optional.get();

                LicensedPartnerRegistrationDto licensedPartnerRegistrationDto = null;

                if(preLicensedPartner.getUserRegistrationInformation() != null && !preLicensedPartner.getUserRegistrationInformation().isEmpty()) {

                    String json = preLicensedPartner.getUserRegistrationInformation();
                    licensedPartnerRegistrationDto = this.gson.fromJson(json, LicensedPartnerRegistrationDto.class);

                    if(licensedPartnerRegistrationDto.getFileUploadSet() != null) {

                        if (!licensedPartnerRegistrationDto.getFileUploadSet().isEmpty()) {

                            licensedPartnerRegistrationDto.getFileUploadSet().removeIf(f -> f.getDocName().equalsIgnoreCase(licensedPartnerFileUploadDto.getDocName()));

                            licensedPartnerRegistrationDto.getFileUploadSet().add(licensedPartnerFileUploadDto);

                            if(licensedPartnerRegistrationDto.getFileUploadSet().size() > 3) {
                                this.preLicensedPartnerService.updateForFormWizardRegistration(
                                        licensedPartnerRegistrationDto,
                                        preLicensedPartner,
                                        3);
                            } else {
                                this.preLicensedPartnerService.updateForFormWizardRegistration(
                                        licensedPartnerRegistrationDto,
                                        preLicensedPartner,
                                        2);
                            }
                        } else {
                            Set<LicensedPartnerFileUploadDto> set = new HashSet<>();
                            set.add(licensedPartnerFileUploadDto);
                            licensedPartnerRegistrationDto.setFileUploadSet(set);
                            this.preLicensedPartnerService.updateForFormWizardRegistration(
                                    licensedPartnerRegistrationDto,
                                    preLicensedPartner,
                                    2);
                        }
                    } else {
                        Set<LicensedPartnerFileUploadDto> set = new HashSet<>();
                        set.add(licensedPartnerFileUploadDto);
                        licensedPartnerRegistrationDto.setFileUploadSet(set);
                        this.preLicensedPartnerService.updateForFormWizardRegistration(
                                licensedPartnerRegistrationDto,
                                preLicensedPartner,
                                2);
                    }
                }

                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("list.of.users", lang),
                        true, new ArrayList<>(), null);
            } else {

                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("no.users.found", lang),
                        false, new ArrayList<>(), null);
            }


            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());


        } catch (NullPointerException e) {

            return this.myApiResponse.internalServerErrorResponse();
        }
    }

    @GetMapping("/auth/signup/licensed-partner/patched-info/{email}")
    public ResponseEntity<?> getInformation(@PathVariable String email) {
        ApiError apiError = null;
        String lang = "en";

        if(email == null || email.isEmpty()) {
            apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("email.required", lang),
                    false, new ArrayList<>(), null);
            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        try {

            Optional<PreLicensedPartner> optional = this.preLicensedPartnerService.findByEmail(email);

            if(optional.isPresent()) {
                PreLicensedPartner preLicensedPartner = optional.get();

                LicensedPartnerRegistrationDto licensedPartnerRegistrationDto = null;
                if(preLicensedPartner.getUserRegistrationInformation() != null && !preLicensedPartner.getUserRegistrationInformation().isEmpty()) {

                    String json = preLicensedPartner.getUserRegistrationInformation();
                    licensedPartnerRegistrationDto = this.gson.fromJson(json, LicensedPartnerRegistrationDto.class);

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("step", preLicensedPartner.getCurrentFormWizardStep());
                    map.put("data", licensedPartnerRegistrationDto);

                    apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("list.of.users", lang),
                            true, new ArrayList<>(), map);
                } else {

                    apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("list.of.users", lang),
                            false, new ArrayList<>(), null);

                }
            } else {

                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("no.users.found", lang),
                        false, new ArrayList<>(), null);
            }


            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());


        } catch (NullPointerException e) {

            return this.myApiResponse.internalServerErrorResponse();
        }
    }

}
