package com.oasis.cac.vas.pojo;

import com.oasis.cac.vas.pojo.count_builder.CountBuilder;
import lombok.Data;

import java.util.List;

@Data
public class BasePublicSearchResponse {

    private String companyName;
    private String registrationDate;
    private String rcNumber;
    private List<CountBuilder> countBuilderList;
}
