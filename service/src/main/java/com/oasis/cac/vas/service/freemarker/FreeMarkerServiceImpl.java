package com.oasis.cac.vas.service.freemarker;

import com.oasis.cac.vas.pojo.PublicResponseTransform;
import com.oasis.cac.vas.pojo.public_search.PublicSearchResponse;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import freemarker.template.Configuration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FreeMarkerServiceImpl implements FreeMarkerService {

    @Qualifier("freeMarkerConfiguration")
    @Autowired
    private Configuration freemarkerConfig;

    @Override
    public String getHtml(String templateName, Map<String, Object> params) {
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates/freemarker/");
            Template template = freemarkerConfig.getTemplate(templateName);

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String, Object> getContents(List<PublicResponseTransform> publicResponseTransformList) {
        if(publicResponseTransformList.size() > 0) {
            PublicResponseTransform publicSearchResponse = publicResponseTransformList.get(0);
            Map<String, Object> map = new HashMap<>();
            map.put("affiliates", publicSearchResponse.getAffiliateTypeTransformList());
            map.put("companyName", publicSearchResponse.getCompanyName());
            map.put("rcNumber", publicSearchResponse.getRcNumber());
            return map;
        }

        return null;
    }

}
