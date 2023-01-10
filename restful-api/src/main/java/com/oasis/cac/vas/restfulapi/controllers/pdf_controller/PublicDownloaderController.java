package com.oasis.cac.vas.restfulapi.controllers.pdf_controller;

import com.oasis.cac.vas.dto.PublicSearchDto;
import com.oasis.cac.vas.models.PaymentTransactionHistory;
import com.oasis.cac.vas.pojo.PublicAffiliateTypeTransform;
import com.oasis.cac.vas.pojo.PublicResponseTransform;
import com.oasis.cac.vas.pojo.public_search.AffiliateResponsePojo;
import com.oasis.cac.vas.pojo.public_search.PublicSearchResponse;
import com.oasis.cac.vas.service.download_service.DownloadService;
import com.oasis.cac.vas.service.external.public_search_service.PublicSearchService;
import com.oasis.cac.vas.service.freemarker.FreeMarkerService;
import com.oasis.cac.vas.service.payment_transaction_ref.PaymentTransactionRefService;
import com.oasis.cac.vas.utils.MessageUtil;
import com.oasis.cac.vas.utils.controllers.PublicBaseApiController;
import com.oasis.cac.vas.utils.errors.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.itextpdf.html2pdf.HtmlConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class PublicDownloaderController extends PublicBaseApiController {

    private static final Logger logger = LoggerFactory.getLogger(PublicDownloaderController.class.getSimpleName());

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private FreeMarkerService freeMarkerService;

    @Autowired
    private PublicSearchService publicSearchService;

    @Autowired
    private PaymentTransactionRefService paymentTransactionRefService;

    @Autowired
    private DownloadService downloadService;

    //Just download
    @GetMapping("/download/{transactionRef}")
    public void downloadForPdf(@PathVariable String transactionRef,
                               HttpServletResponse response,
                               HttpServletRequest request) {

        ApiError apiError = null;
        String lang = "en";

        if (transactionRef == null || transactionRef.isEmpty()) {
            logger.info(this.messageUtil.getMessage("transaction.ref.notfound", lang));
        }

        try {

            Optional<PaymentTransactionHistory> optional = this.paymentTransactionRefService.findByRef(transactionRef);

            if (optional.isPresent()) {

                PaymentTransactionHistory paymentTransactionHistory = optional.get();

                PublicSearchDto publicSearchDto = this.downloadService.generatePublicSearchDto(paymentTransactionHistory);

                try {

                    List<PublicSearchResponse> publicSearchResponseList = this.publicSearchService.doSearch(publicSearchDto);

                    List<PublicResponseTransform> publicResponseTransformList = new ArrayList<>();

                    for(PublicSearchResponse publicSearchResponse: publicSearchResponseList) {

                        PublicResponseTransform publicResponseTransform = new PublicResponseTransform();
                        publicResponseTransform.setCity(publicSearchResponse.getCity());
                        publicResponseTransform.setClassification(publicSearchResponse.getClassification());
                        publicResponseTransform.setCompanyName(publicSearchResponse.getCompanyName());
                        publicResponseTransform.setCountryOfResidence(publicSearchResponse.getCountryOfResidence());
                        publicResponseTransform.setCompanyType(publicSearchResponse.getCompanyType());
                        publicResponseTransform.setState(publicSearchResponse.getState());
                        publicResponseTransform.setRcNumber(publicSearchResponse.getRcNumber());

                        List<PublicAffiliateTypeTransform> affiliateTypeTransformList = new ArrayList<>();

                        for(AffiliateResponsePojo affiliateResponsePojo: publicSearchResponse.affiliateResponsePojos) {
                            // logger.info(affiliateResponsePojo.toString());

                            PublicAffiliateTypeTransform publicAffiliateTypeTransform = new PublicAffiliateTypeTransform();

                            if(affiliateTypeTransformList.size() <= 0) {
                                publicAffiliateTypeTransform.setType(affiliateResponsePojo.getAffiliateType());
                                List<AffiliateResponsePojo> affiliateResponsePojoList = new ArrayList<>();
                                affiliateResponsePojoList.add(affiliateResponsePojo);
                                publicAffiliateTypeTransform.setAffiliateResponsePojoList(affiliateResponsePojoList);
                                affiliateTypeTransformList.add(publicAffiliateTypeTransform);
                            } else {
                                List<PublicAffiliateTypeTransform> foundList = affiliateTypeTransformList.stream().filter(e -> Objects.equals(e.type, affiliateResponsePojo.getAffiliateType())).collect(Collectors.toList());
                                if(foundList.size() > 0) {
                                    int index = affiliateTypeTransformList.indexOf(foundList.get(0));
                                    affiliateTypeTransformList.get(index).getAffiliateResponsePojoList().add(affiliateResponsePojo);
                                } else {
                                    publicAffiliateTypeTransform.setType(affiliateResponsePojo.getAffiliateType());
                                    List<AffiliateResponsePojo> affiliateResponsePojoList = new ArrayList<>();
                                    affiliateResponsePojoList.add(affiliateResponsePojo);
                                    publicAffiliateTypeTransform.setAffiliateResponsePojoList(affiliateResponsePojoList);
                                    affiliateTypeTransformList.add(publicAffiliateTypeTransform);
                                }
                            }
                        }

                        publicResponseTransform.setAffiliateTypeTransformList(affiliateTypeTransformList);

                        publicResponseTransformList.add(publicResponseTransform);

                    }

                    Map<String, Object> map = this.freeMarkerService.getContents(publicResponseTransformList);

                    if (map != null) {

                        String filename = "report.pdf";

                        String html = this.freeMarkerService.getHtml("search_result.ftl", map);

                        logger.info("html generated...");

                        response.setContentType("application/pdf");

                        response.addHeader("Content-Disposition", "attachment; filename=" + filename);

                        HtmlConverter.convertToPdf(html, response.getOutputStream());
                    } else {
                        logger.info(this.messageUtil.getMessage("transaction.ref.notfound", lang));
                    }

                } catch (Exception e) {
                    logger.info(this.messageUtil.getMessage("public.search.network.call.failed", lang));
                }

            } else {

                logger.info(this.messageUtil.getMessage("transaction.ref.notfound", lang));
            }

        } catch (NullPointerException e) {
            logger.info(this.messageUtil.getMessage("server.error", lang));
        }
    }
}
