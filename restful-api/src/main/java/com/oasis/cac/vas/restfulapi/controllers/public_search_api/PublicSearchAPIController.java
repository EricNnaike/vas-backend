package com.oasis.cac.vas.restfulapi.controllers.public_search_api;

import com.oasis.cac.vas.dto.PublicSearchDto;
import com.oasis.cac.vas.pojo.BasePublicSearchResponse;
import com.oasis.cac.vas.pojo.count_builder.CountBuilder;
import com.oasis.cac.vas.pojo.public_search.PublicSearchResponse;
import com.oasis.cac.vas.service.encrypt_service.EncryptService;
import com.oasis.cac.vas.service.external.public_search_service.PublicSearchService;
import com.oasis.cac.vas.utils.MessageUtil;
import com.oasis.cac.vas.utils.controllers.PublicBaseApiController;
import com.oasis.cac.vas.utils.errors.ApiError;
import com.oasis.cac.vas.utils.errors.CustomBadRequestException;
import com.oasis.cac.vas.utils.errors.MyApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
public class PublicSearchAPIController extends PublicBaseApiController {

    private static final Logger logger = LoggerFactory.getLogger(PublicSearchAPIController.class.getSimpleName());

    @Autowired
    private MessageUtil messageUtil;


    @Autowired
    private PublicSearchService publicSearchService;

    @Autowired
    private EncryptService encryptService;


    @Autowired
    private MyApiResponse myApiResponse;


    @PostMapping("/company-search/search")
    public ResponseEntity<?> search(@Valid @RequestBody PublicSearchDto publicSearchDto,
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

            try {
                List<PublicSearchResponse> publicSearchResponseList = this.publicSearchService.doSearch(publicSearchDto);

                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, this.messageUtil.getMessage("public.search.successful", lang),
                        true, new ArrayList<>(), publicSearchResponseList);

            } catch (Exception e) {
                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, this.messageUtil.getMessage("public.search.network.call.failed", lang),
                        false, new ArrayList<>(), null);
            }


            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

        } catch (NullPointerException e) {
            return this.myApiResponse.internalServerErrorResponse();
        }
    }


    @PostMapping("/company-search/search-count")
    public ResponseEntity<?> searchWithCount(@Valid @RequestBody PublicSearchDto publicSearchDto,
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

            publicSearchDto.setUseDefault(true);

            List<PublicSearchResponse> publicSearchResponseList = this.publicSearchService.doSearch(publicSearchDto);

            if(publicSearchResponseList.size() <= 0){
                apiError = new ApiError(HttpStatus.OK.value(),
                        HttpStatus.OK,
                        this.messageUtil.getMessage("public.search.successful", lang),
                        false,
                        new ArrayList<>(),
                        null);

                return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
            }

            List<CountBuilder> countBuilderList = this.publicSearchService.getItemCount(publicSearchResponseList);
            BasePublicSearchResponse basePublicSearchResponse = new BasePublicSearchResponse();
            basePublicSearchResponse.setCompanyName(publicSearchResponseList.get(0).getCompanyName());
            basePublicSearchResponse.setRegistrationDate(publicSearchResponseList.get(0).getRegistrationDate());
            basePublicSearchResponse.setRcNumber(publicSearchResponseList.get(0).getRcNumber());
            basePublicSearchResponse.setCountBuilderList(countBuilderList);


            apiError = new ApiError(HttpStatus.OK.value(),
                    HttpStatus.OK,
                    this.messageUtil.getMessage("public.search.successful", lang),
                    true,
                    new ArrayList<>(),
                    basePublicSearchResponse);

            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

        } catch (NullPointerException | IOException e) {
            return myApiResponse.internalServerErrorResponse();
        }
    }

    @GetMapping("/company-search/rc-number/{rcNumber}")
    public ResponseEntity<?> rcNumberSearch(@PathVariable String rcNumber,
                                            HttpServletResponse res,
                                            HttpServletRequest request,
                                            Authentication authentication) {
        ApiError apiError = null;
        String lang = "en";

        try {


            if (rcNumber.isEmpty()) {
                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, this.messageUtil.getMessage("public.search.successful", lang),
                        true, new ArrayList<>(), null);
                return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
            }

            PublicSearchDto publicSearchDto = new PublicSearchDto();
            publicSearchDto.setClassification(-1);
            publicSearchDto.setSearchByCompanyName(rcNumber);

            List<PublicSearchResponse> publicSearchResponseList;

            try {

                publicSearchResponseList = this.publicSearchService.doSearch(publicSearchDto);

                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, this.messageUtil.getMessage("public.search.successful", lang),
                        true, new ArrayList<>(), publicSearchResponseList);

            } catch (Exception e) {

                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, this.messageUtil.getMessage("public.search.network.call.failed", lang),
                        false, new ArrayList<>(), null);

            }

            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

        } catch (NullPointerException e) {

            logger.info(e.getMessage());

            return myApiResponse.internalServerErrorResponse();
        }
    }

}
