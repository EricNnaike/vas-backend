package com.oasis.cac.vas.dto.licensed_partner;

import lombok.Data;

@Data
public class SelectedPhoneNumberDto {

    private String id;
    private String name;
    private String alpha2;
    private String alpha3;
    private String internationalPhoneNumber;

}
