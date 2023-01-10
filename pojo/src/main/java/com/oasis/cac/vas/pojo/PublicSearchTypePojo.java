package com.oasis.cac.vas.pojo;

import com.oasis.cac.vas.utils.oasisenum.MyJSONTypeConstant;
import lombok.Data;

@Data
public class PublicSearchTypePojo {

    private String json;

    private MyJSONTypeConstant jsonTypes;

    @Override
    public String toString() {
        return "PublicSearchTypePojo{" +
                "json='" + json + '\'' +
                ", jsonTypes=" + jsonTypes +
                '}';
    }

}
