package com.oasis.cac.vas.pojo.public_search;

import lombok.Data;

@Data
public class PublicSearchResponse {

    public String city;
    public String companyName;
    public String companyType;
    public int classification;
    public String email;
    public String headOfficeAddress;
    public String headOfficeCity;
    public String headOfficeLga;
    public String headOfficeState;
    public String registrationDate;
    public AffiliateResponsePojo[] affiliateResponsePojos;
    public String state;
    public String countryOfResidence;
    public String rcNumber;


    @Override
    public String toString() {
        return "PublicSearchResponse{" +
                "city='" + city + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyType='" + companyType + '\'' +
                ", email='" + email + '\'' +
                ", headOfficeAddress='" + headOfficeAddress + '\'' +
                ", headOfficeCity='" + headOfficeCity + '\'' +
                ", headOfficeLga='" + headOfficeLga + '\'' +
                ", headOfficeState='" + headOfficeState + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", state='" + state + '\'' +
                ", countryOfResidence='" + countryOfResidence + '\'' +
                ", rcNumber='" + rcNumber + '\'' +
                '}';
    }
}
