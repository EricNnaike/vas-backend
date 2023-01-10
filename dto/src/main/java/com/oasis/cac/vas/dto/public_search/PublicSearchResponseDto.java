package com.oasis.cac.vas.dto.public_search;

import lombok.Data;

import java.util.Arrays;

@Data
public class PublicSearchResponseDto {

    public String address;
    public AffiliateDto[] affiliates;
    public String branchAddress;
    public String city;
    public String classification;
    public int classificationId;
    public String companyName;
    public String companyType;
    public String email;
    public String headOfficeAddress;
    public String headOfficeCity;
    public String headOfficeLga;
    public String headOfficeState;
    public String lga;
    public int numberOfAffiliates;
    public String rcNumber;
    public String registrationDate;
    public Long shareCapital;
    public String shareCapitalInWords;
    public String state;
    public String status;

    @Override
    public String toString() {
        return "PublicSearchResponseDto{" +
                "address='" + address + '\'' +
                ", affiliates=" + Arrays.toString(affiliates) +
                ", branchAddress='" + branchAddress + '\'' +
                ", city='" + city + '\'' +
                ", classification='" + classification + '\'' +
                ", classificationId=" + classificationId +
                ", companyName='" + companyName + '\'' +
                ", companyType='" + companyType + '\'' +
                ", email='" + email + '\'' +
                ", headOfficeAddress='" + headOfficeAddress + '\'' +
                ", headOfficeCity='" + headOfficeCity + '\'' +
                ", headOfficeLga='" + headOfficeLga + '\'' +
                ", headOfficeState='" + headOfficeState + '\'' +
                ", lga='" + lga + '\'' +
                ", numberOfAffiliates=" + numberOfAffiliates +
                ", rcNumber='" + rcNumber + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", shareCapital=" + shareCapital +
                ", shareCapitalInWords='" + shareCapitalInWords + '\'' +
                ", state='" + state + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
