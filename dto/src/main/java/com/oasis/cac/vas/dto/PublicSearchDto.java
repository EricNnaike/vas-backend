package com.oasis.cac.vas.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class PublicSearchDto {

    //request field
    @NotBlank
    public String searchByCompanyName;

    //Filter
    public String[] filter;

    //boolean
    public String[] fields;

    //classification
    public int classification;

    public boolean useDefault;

    public boolean isSearchWithUserQueries;

    public List<CountBuilderDto> userQueries;

}
