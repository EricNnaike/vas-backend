package com.oasis.cac.vas.dto.public_search;

import lombok.Data;

@Data
public class AffiliateDto {

    private String shareType;
    private String affiliateType;
    private String dateOfAppointment;
    private String identityNumber;
    private boolean isCorporate;
    private String accreditationnumber;
    private String corporationName;
    private String formerFirstName;
    private String formerName;
    private String formerNationality;
    private String formerOtherName;
    private String formerSurname;
    private boolean isChairman;
    private String otherDirectorshipDetails;
    private String address;
    private String name;
    private Long id;
    private String state;
    private String status;
    private String uuid;
    private String nationality;
    private String countryName;
    private String dateOfBirth;
    private String dateOfTermination;
    private String streetNumber;
    private String city;
    private String postcode;
    private String corporateName;
    private Long companyId;
    private String firstname;
    private String surname;
    private String othername;
    private String shareAllotted;
    private String email;
    private String lga;
    private String gender;
    private String phoneNumber;
    private String occupation;
    private String identityType;
    private String companyName;
    private String rcNumber;
    private String corporateRcNumber;
    private String companyRcNumber;
    private String searchScore;

    @Override
    public String toString() {
        return "AffiliateDto{" +
                "shareType='" + shareType + '\'' +
                ", affiliateType='" + affiliateType + '\'' +
                ", dateOfAppointment='" + dateOfAppointment + '\'' +
                ", identityNumber='" + identityNumber + '\'' +
                ", isCorporate=" + isCorporate +
                ", accreditationnumber='" + accreditationnumber + '\'' +
                ", corporationName='" + corporationName + '\'' +
                ", formerFirstName='" + formerFirstName + '\'' +
                ", formerName='" + formerName + '\'' +
                ", formerNationality='" + formerNationality + '\'' +
                ", formerOtherName='" + formerOtherName + '\'' +
                ", formerSurname='" + formerSurname + '\'' +
                ", isChairman=" + isChairman +
                ", otherDirectorshipDetails='" + otherDirectorshipDetails + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", state='" + state + '\'' +
                ", status='" + status + '\'' +
                ", uuid='" + uuid + '\'' +
                ", nationality='" + nationality + '\'' +
                ", countryName='" + countryName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", dateOfTermination='" + dateOfTermination + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", city='" + city + '\'' +
                ", postcode='" + postcode + '\'' +
                ", corporateName='" + corporateName + '\'' +
                ", companyId=" + companyId +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", othername='" + othername + '\'' +
                ", shareAllotted='" + shareAllotted + '\'' +
                ", email='" + email + '\'' +
                ", lga='" + lga + '\'' +
                ", gender='" + gender + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", occupation='" + occupation + '\'' +
                ", identityType='" + identityType + '\'' +
                ", companyName='" + companyName + '\'' +
                ", rcNumber='" + rcNumber + '\'' +
                ", corporateRcNumber='" + corporateRcNumber + '\'' +
                ", companyRcNumber='" + companyRcNumber + '\'' +
                ", searchScore='" + searchScore + '\'' +
                '}';
    }
}
