package com.oasis.cac.vas.service.download_service;

import com.google.gson.Gson;
import com.oasis.cac.vas.dto.CountBuilderDto;
import com.oasis.cac.vas.dto.PublicSearchDto;
import com.oasis.cac.vas.models.PaymentTransactionHistory;
import com.oasis.cac.vas.pojo.count_builder.CountBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DownloadServiceImpl implements  DownloadService {

    private static final Logger logger = LoggerFactory.getLogger(DownloadServiceImpl.class.getSimpleName());

    @Autowired
    private Gson gson;

    public PublicSearchDto generatePublicSearchDto(PaymentTransactionHistory paymentTransactionHistory) {

        PublicSearchDto publicSearchDto = new PublicSearchDto();
        publicSearchDto.setSearchWithUserQueries(true);

        // logger.info(paymentTransactionHistory.getUserQuery());

        CountBuilder[] countBuilders = this.gson.fromJson(paymentTransactionHistory.getUserQuery(), CountBuilder[].class);
        String companyName = paymentTransactionHistory.getCompanyName();
        String rcNumber = paymentTransactionHistory.getRcNumber();

        List<CountBuilderDto> countBuilderDtoList = new ArrayList<>();
        for(CountBuilder countBuilder: countBuilders){
           // logger.info(countBuilder.toString());
            Object[] arr = countBuilder.getInnerFields().toArray();

            List<CountBuilderDto> innerList = new ArrayList<>();

            for(CountBuilder innerCountBuilder: countBuilder.getInnerFields()) {
                CountBuilderDto innerCountBuilderDto = new CountBuilderDto();
                innerCountBuilderDto.setCount(innerCountBuilder.getCount());
                innerCountBuilderDto.setDisabled(innerCountBuilder.isDisabled());
                innerCountBuilderDto.setTitle(innerCountBuilder.getTitle());
                innerCountBuilderDto.setLabel(innerCountBuilder.getLabel());
                innerCountBuilderDto.setInnerFields(null);
                innerCountBuilderDto.setValue(innerCountBuilder.isValue());
                innerList.add(innerCountBuilderDto);
            }

            CountBuilderDto countBuilderDto = new CountBuilderDto();
            countBuilderDto.setCount(countBuilder.getCount());
            countBuilderDto.setDisabled(countBuilder.isDisabled());
            countBuilderDto.setTitle(countBuilder.getTitle());
            countBuilderDto.setLabel(countBuilder.getLabel());
            countBuilderDto.setInnerFields(innerList);

            countBuilderDtoList.add(countBuilderDto);
        }


        if(!companyName.isEmpty()) {
            publicSearchDto.setSearchByCompanyName(companyName);
        } else {
            publicSearchDto.setSearchByCompanyName(rcNumber);
        }

        publicSearchDto.setUserQueries(countBuilderDtoList);

        return publicSearchDto;
    }
}
