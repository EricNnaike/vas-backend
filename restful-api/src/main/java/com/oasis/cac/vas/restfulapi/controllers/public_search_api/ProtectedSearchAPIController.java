package com.oasis.cac.vas.restfulapi.controllers.public_search_api;

import com.google.gson.Gson;
import com.oasis.cac.vas.dto.PublicSearchDto;
import com.oasis.cac.vas.pojo.public_search.PublicSearchResponse;
import com.oasis.cac.vas.service.external.public_search_service.PublicSearchService;
import com.oasis.cac.vas.utils.MessageUtil;
import com.oasis.cac.vas.utils.OkHttpUtil;
import com.oasis.cac.vas.utils.controllers.ProtectedBaseApiController;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ProtectedSearchAPIController extends ProtectedBaseApiController {

    private static final Logger logger = LoggerFactory.getLogger(ProtectedSearchAPIController.class.getSimpleName());

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private PublicSearchService publicSearchService;


}
