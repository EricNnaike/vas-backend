package com.oasis.cac.vas.service.external.public_search_service;

import com.oasis.cac.vas.dto.CountBuilderDto;
import com.oasis.cac.vas.dto.PublicSearchDto;
import com.oasis.cac.vas.dto.public_search.PublicSearchResponseDto;
import com.oasis.cac.vas.pojo.PublicSearchTypePojo;
import com.oasis.cac.vas.pojo.count_builder.CountBuilder;
import com.oasis.cac.vas.pojo.public_search.AffiliateResponsePojo;
import com.oasis.cac.vas.pojo.public_search.PublicSearchResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PublicSearchService {

//    String getToken();

    PublicSearchResponse fetchPublicSearchResponse(PublicSearchResponseDto publicSearchResponseDto,
                                                   PublicSearchDto publicSearchDto);

    PublicSearchResponse defaultFieldsFetchPublicSearchResponse(PublicSearchResponseDto publicSearchResponseDto,
                                                                PublicSearchDto publicSearchDto);

    AffiliateResponsePojo[] getAffiliatesWithFilter(PublicSearchDto publicSearchDto,
                                                    PublicSearchResponseDto publicSearchResponseDto,
                                                    List<CountBuilderDto> countBuilderList);

    AffiliateResponsePojo[] getAffiliates(PublicSearchDto publicSearchDto,
                                          PublicSearchResponseDto publicSearchResponseDto);

    List<CountBuilder> getItemCount(List<PublicSearchResponse> publicSearchResponseList);


    List<PublicSearchResponse> doSearch(PublicSearchDto publicSearchDto) throws IOException;


    List<PublicSearchResponse> getPublicSearchPojoList(PublicSearchTypePojo publicSearchTypePojo,
                                                       PublicSearchDto publicSearchDto);

    List<PublicSearchResponse> baseLoaderPublicSearchPojoList(PublicSearchTypePojo publicSearchTypePojo,
                                                              PublicSearchDto publicSearchDto,
                                                              boolean useDefault);

    List<PublicSearchResponse> getPublicSearchPojoList(PublicSearchTypePojo publicSearchTypePojo,
                                                       PublicSearchDto publicSearchDto,
                                                       boolean useDefault);
}
