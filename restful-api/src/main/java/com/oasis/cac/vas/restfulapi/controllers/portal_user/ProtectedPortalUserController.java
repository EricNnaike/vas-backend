package com.oasis.cac.vas.restfulapi.controllers.portal_user;

import com.google.gson.Gson;
import com.oasis.cac.vas.auth.config.dto.UserDetailsDto;
import com.oasis.cac.vas.auth.config.service.user_service.UserService;
import com.oasis.cac.vas.models.PortalUser;
import com.oasis.cac.vas.pojo.PaginationResponsePojo;
import com.oasis.cac.vas.service.psql.portaluser.PortalUserService;
import com.oasis.cac.vas.utils.MessageUtil;
import com.oasis.cac.vas.utils.controllers.ProtectedBaseApiController;
import com.oasis.cac.vas.utils.errors.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProtectedPortalUserController extends ProtectedBaseApiController {

    private static final Logger logger = LoggerFactory.getLogger(PublicPortalUserController.class.getSimpleName());

    @Autowired
    private PortalUserService portalUserService;

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private UserService userService;

    private Gson gson;

    @GetMapping("/users")
    public ResponseEntity<?> index(
            HttpServletResponse res,
            HttpServletRequest request,
            Authentication authentication,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {

        UserDetailsDto requestPrincipal = null;
        ApiError apiError = null;

        try {

            if(page == null) {
                page = 0;
            }

            if(size == null) {
                size = 10;
            }

            Pageable sortedByDateCreated = PageRequest.of(page, size, Sort.by("date_created").descending());

            List<PortalUser> list = new ArrayList<>();
                    // this.portalUserService.findAll(sortedByDateCreated);

            System.out.println(0);
            long totalLength = this.portalUserService.count();

            PaginationResponsePojo paginationResponsePojo = new PaginationResponsePojo();
            paginationResponsePojo.setDataList(list);
            paginationResponsePojo.setLength(totalLength);
            int pageNumber = sortedByDateCreated.getPageNumber();
            paginationResponsePojo.setPageNumber((long) pageNumber);
            int pageSize = sortedByDateCreated.getPageSize();
            paginationResponsePojo.setPageSize((long) pageSize);
//            logger.info(this.gson.toJson(paginationResponsePojo));

            apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("list.of.users", "en"),
                    true, new ArrayList<>(), paginationResponsePojo);

            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
            apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("no.users.found", "en"),
                    true, new ArrayList<>(), null);

            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

        }

    }


}