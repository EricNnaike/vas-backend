package com.oasis.cac.vas.service.freemarker;

import com.oasis.cac.vas.pojo.PublicResponseTransform;
import com.oasis.cac.vas.pojo.public_search.PublicSearchResponse;
import java.util.List;
import java.util.Map;

public interface FreeMarkerService {

    String getHtml(String templateName, Map<String, Object> params);

    Map<String, Object> getContents(List<PublicResponseTransform> publicSearchResponseList);
}
