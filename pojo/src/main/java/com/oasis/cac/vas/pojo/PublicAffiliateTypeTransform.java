package com.oasis.cac.vas.pojo;

import com.oasis.cac.vas.pojo.public_search.AffiliateResponsePojo;
import lombok.Data;

import java.util.List;

@Data
public class PublicAffiliateTypeTransform {

    public String type;

    public List<AffiliateResponsePojo> affiliateResponsePojoList;
}
