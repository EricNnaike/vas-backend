package com.oasis.cac.vas.pojo.public_search;

import lombok.Data;

@Data
public class AffiliateResponsePojo {

    private String gender;
    private String phoneNumber;
    private String email;
    private String firstname;
    private String surname;
    private String othername;
    private String address;
    private String dob;
    private String affiliateType;
    private String dateOfAppointment;

    @Override
    public String toString() {
        return "AffiliateResponsePojo{" +
                "gender='" + gender + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", othername='" + othername + '\'' +
                ", address='" + address + '\'' +
                ", dob='" + dob + '\'' +
                ", affiliateType='" + affiliateType + '\'' +
                ", dateOfAppointment='" + dateOfAppointment + '\'' +
                '}';
    }
}
