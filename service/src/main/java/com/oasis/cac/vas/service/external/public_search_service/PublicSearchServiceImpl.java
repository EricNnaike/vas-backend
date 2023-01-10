package com.oasis.cac.vas.service.external.public_search_service;

import com.google.gson.Gson;
import com.oasis.cac.vas.dto.CountBuilderDto;
import com.oasis.cac.vas.dto.PublicSearchDto;
import com.oasis.cac.vas.dto.PublicSearchTokenFetchDto;
import com.oasis.cac.vas.dto.public_search.AffiliateDto;
import com.oasis.cac.vas.dto.public_search.PublicSearchResponseDto;
import com.oasis.cac.vas.pojo.PublicSearchTypePojo;
import com.oasis.cac.vas.pojo.count_builder.CountBuilder;
import com.oasis.cac.vas.pojo.public_search.AffiliateResponsePojo;
import com.oasis.cac.vas.pojo.public_search.PublicSearchResponse;
import com.oasis.cac.vas.utils.OkHttpUtil;
import com.oasis.cac.vas.utils.Utils;
import com.oasis.cac.vas.utils.oasisenum.MyJSONTypeConstant;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PublicSearchServiceImpl implements PublicSearchService {

    private static final Logger logger = LoggerFactory.getLogger(PublicSearchServiceImpl.class.getSimpleName());

    @Autowired
    private Gson gson;

    @Value("${publicSearchToken}")
    private String publicSearchToken;


    @Autowired
    private OkHttpUtil okHttpUtil;


    @Value("${publicSearchDomain}")
    private String publicSearchDomain;


    public AffiliateResponsePojo[] getAffiliates(PublicSearchDto publicSearchDto,
                                                        PublicSearchResponseDto publicSearchResponseDto) {
        try {

            AffiliateDto[] affiliateDtos = publicSearchResponseDto.getAffiliates();

            List<AffiliateDto> affiliateDtoList = new ArrayList<>();

            for (AffiliateDto affiliateDto : affiliateDtos) {

                String affiliateType = affiliateDto.getAffiliateType();
                List<String> listOfFields = new ArrayList<>(Arrays.asList(publicSearchDto.getFields()));
                if (listOfFields.contains(affiliateType)) {
                    affiliateDtoList.add(affiliateDto);
                }
            }

            List<AffiliateResponsePojo> affiliateResponsePojoList = new ArrayList<>();


            for (AffiliateDto affiliateDto : affiliateDtoList) {

                AffiliateResponsePojo affiliateResponsePojo = new AffiliateResponsePojo();

                List<String> listOfFilters = new ArrayList<>(Arrays.asList(publicSearchDto.getFilter()));

                if (listOfFilters.contains("EMAIL")) {
                    affiliateResponsePojo.setEmail(affiliateDto.getEmail());
                }


                if (listOfFilters.contains("PHONE_NUMBER")) {
                    affiliateResponsePojo.setPhoneNumber(affiliateDto.getPhoneNumber());
                }

                if (listOfFilters.contains("GENDER")) {
                    affiliateResponsePojo.setGender(affiliateDto.getGender());
                }


                if (listOfFilters.contains("ADDRESS")) {
                    affiliateResponsePojo.setAddress(affiliateDto.getAddress());
                }


                affiliateResponsePojo.setFirstname(affiliateDto.getFirstname());
                affiliateResponsePojo.setSurname(affiliateDto.getSurname());

                affiliateResponsePojoList.add(affiliateResponsePojo);
            }


            if (affiliateDtoList.size() > 0) {
                return affiliateResponsePojoList.toArray(new AffiliateResponsePojo[affiliateResponsePojoList.size()]);
            }

            return null;

        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }
    }


    public AffiliateResponsePojo[] getAffiliatesWithFilter(PublicSearchDto publicSearchDto,
                                                           PublicSearchResponseDto publicSearchResponseDto,
                                                           List<CountBuilderDto> countBuilderList) {
        try {

            logger.info("filter working...");

            AffiliateDto[] affiliateDtos = publicSearchResponseDto.getAffiliates();

            List<AffiliateDto> affiliateDtoList = new ArrayList<>();

            List<String> thingsToCheck = new ArrayList<>(Arrays.asList("EMAIL", "PHONE_NUMBER", "ADDRESS", "GENDER"));

            List<String> listOfFields = new ArrayList<>();

            for(CountBuilderDto countBuilder: countBuilderList) {
                String value = countBuilder.getLabel().toUpperCase();
                listOfFields.add(value);
            }

            logger.info(listOfFields.toString());

            for (AffiliateDto affiliateDto: affiliateDtos) {
                String affiliateType = affiliateDto.getAffiliateType().toUpperCase();
                if (listOfFields.contains(affiliateType)) {
                    affiliateDtoList.add(affiliateDto);
                }
            }

            List<AffiliateResponsePojo> affiliateResponsePojoList = new ArrayList<>();

            for (AffiliateDto affiliateDto : affiliateDtoList) {
                String searchValue = affiliateDto.getAffiliateType().toUpperCase();
                List<CountBuilderDto> foundCountBuilders = countBuilderList.stream().filter(e -> e.getLabel().equalsIgnoreCase(searchValue)).collect(Collectors.toList());

                if(foundCountBuilders.size() > 0) {

                    int index = countBuilderList.indexOf(foundCountBuilders.get(0));

                    List<CountBuilderDto> countBuilderDtoList = countBuilderList.get(index).getInnerFields();

                    // mutate
                    this.runFilter(countBuilderDtoList, affiliateResponsePojoList, affiliateDto);
                }
            }

            if (affiliateDtoList.size() > 0) {
                return affiliateResponsePojoList.toArray(new AffiliateResponsePojo[affiliateResponsePojoList.size()]);
            }

            return null;

        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }
    }


    public PublicSearchResponse fetchPublicSearchResponse(PublicSearchResponseDto publicSearchResponseDto,
                                                          PublicSearchDto publicSearchDto) {

        try {

            PublicSearchResponse publicSearchResponse = new PublicSearchResponse();
            publicSearchResponse.setCompanyName(publicSearchResponseDto.getCompanyName());
            publicSearchResponse.setCity(publicSearchResponseDto.getCity());
            publicSearchResponse.setRcNumber(publicSearchResponseDto.getRcNumber());
            publicSearchResponse.setCompanyType(publicSearchResponseDto.getCompanyType());
            publicSearchResponse.setState(publicSearchResponseDto.getState());
            publicSearchResponse.setClassification(publicSearchResponseDto.getClassificationId());
            publicSearchResponse.setRegistrationDate(publicSearchResponseDto.getRegistrationDate());

            //Filter affiliates
            // logger.info(publicSearchDto.getUserQueries().toString());

            if(publicSearchDto.isSearchWithUserQueries) {

                AffiliateResponsePojo[] affiliateResponsePojos = getAffiliatesWithFilter(publicSearchDto, publicSearchResponseDto, publicSearchDto.getUserQueries());
                if (affiliateResponsePojos != null) {
                    publicSearchResponse.setAffiliateResponsePojos(affiliateResponsePojos);
                }
            } else {
                AffiliateResponsePojo[] affiliateResponsePojos = getAffiliates(publicSearchDto, publicSearchResponseDto);
                if (affiliateResponsePojos != null) {
                    publicSearchResponse.setAffiliateResponsePojos(affiliateResponsePojos);
                }
            }


            return publicSearchResponse;

        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }
    }


    public PublicSearchResponse defaultFieldsFetchPublicSearchResponse(PublicSearchResponseDto publicSearchResponseDto,
                                                                       PublicSearchDto publicSearchDto) {

        try {


            AffiliateDto[] affiliateDtos = publicSearchResponseDto.getAffiliates();
            List<AffiliateDto> affiliateDtoList = new ArrayList<>();
            affiliateDtoList.addAll(Arrays.asList(affiliateDtos));

            List<AffiliateResponsePojo> affiliateResponsePojoList = new ArrayList<>();

            for (AffiliateDto affiliateDto : affiliateDtoList) {

                AffiliateResponsePojo affiliateResponsePojo = new AffiliateResponsePojo();
                affiliateResponsePojo.setEmail(affiliateDto.getEmail());
                affiliateResponsePojo.setPhoneNumber(affiliateDto.getPhoneNumber());
                affiliateResponsePojo.setGender(affiliateDto.getGender());
                affiliateResponsePojo.setAddress(affiliateDto.getAddress());
                affiliateResponsePojo.setFirstname(affiliateDto.getFirstname());
                affiliateResponsePojo.setSurname(affiliateDto.getSurname());
                affiliateResponsePojo.setAffiliateType(affiliateDto.getAffiliateType());

                affiliateResponsePojoList.add(affiliateResponsePojo);
            }

            PublicSearchResponse publicSearchResponse = new PublicSearchResponse();

            publicSearchResponse.setCompanyName(publicSearchResponseDto.getCompanyName());
            publicSearchResponse.setCity(publicSearchResponseDto.getCity());
            publicSearchResponse.setRcNumber(publicSearchResponseDto.getRcNumber());
            publicSearchResponse.setCompanyType(publicSearchResponseDto.getCompanyType());
            publicSearchResponse.setState(publicSearchResponseDto.getState());
            publicSearchResponse.setClassification(publicSearchResponseDto.getClassificationId());
            publicSearchResponse.setRegistrationDate(publicSearchResponseDto.getRegistrationDate());

            AffiliateResponsePojo[] affiliateResponsePojos = affiliateResponsePojoList.toArray(new AffiliateResponsePojo[affiliateResponsePojoList.size()]);
            publicSearchResponse.setAffiliateResponsePojos(affiliateResponsePojos);

            return publicSearchResponse;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<PublicSearchResponse> getPublicSearchPojoList(PublicSearchTypePojo publicSearchTypePojo,
                                                              PublicSearchDto publicSearchDto,
                                                              boolean useDefault) {

        return baseLoaderPublicSearchPojoList(
                publicSearchTypePojo,
                publicSearchDto,
                useDefault);
    }


    @Override
    public List<PublicSearchResponse> getPublicSearchPojoList(PublicSearchTypePojo publicSearchTypePojo,
                                                              PublicSearchDto publicSearchDto) {
        return baseLoaderPublicSearchPojoList(
                publicSearchTypePojo,
                publicSearchDto,
                false);
    }


    @Override
    public List<PublicSearchResponse> baseLoaderPublicSearchPojoList(PublicSearchTypePojo publicSearchTypePojo,
                                                                     PublicSearchDto publicSearchDto,
                                                                     boolean useDefault) {

        List<PublicSearchResponse> publicSearchResponseList = new ArrayList<>();

       // logger.info(publicSearchTypePojo.getJson());

        /* // */
        if (publicSearchTypePojo.getJsonTypes().equals(MyJSONTypeConstant.JSON_ARRAY)) {

            PublicSearchResponseDto[] publicSearchResponseDtos = gson.fromJson(publicSearchTypePojo.getJson(), PublicSearchResponseDto[].class);

            for (PublicSearchResponseDto publicSearchResponseDto : publicSearchResponseDtos) {

                PublicSearchResponse publicSearchResponse;
                if (useDefault) {
                    publicSearchResponse = this.defaultFieldsFetchPublicSearchResponse(publicSearchResponseDto, publicSearchDto);
                } else {
                    publicSearchResponse = this.fetchPublicSearchResponse(publicSearchResponseDto, publicSearchDto);
                }

                publicSearchResponseList.add(publicSearchResponse);
            }

        } else {

            logger.info("JSON Object");

            PublicSearchResponseDto publicSearchResponseDto = gson.fromJson(publicSearchTypePojo.getJson(), PublicSearchResponseDto.class);

            PublicSearchResponse publicSearchResponse;
            if (useDefault) {
                publicSearchResponse = this.defaultFieldsFetchPublicSearchResponse(publicSearchResponseDto, publicSearchDto);
            } else {
                publicSearchResponse = this.fetchPublicSearchResponse(publicSearchResponseDto, publicSearchDto);
            }
            publicSearchResponseList.add(publicSearchResponse);
        }

        return publicSearchResponseList;
    }

//    @Override
//    public String getToken() {
//
//        BufferedReader bufferedReader = null;
//
//        String path = "json" + File.separator + "public_search.json";
//
//        InputStream inputStream = ResourceUtil.getResourceAsStream(path);
//
//        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//        PublicSearchTokenFetchDto publicSearchTokenFetchDto = gson.fromJson(bufferedReader, PublicSearchTokenFetchDto.class);
//
//        return publicSearchTokenFetchDto.getToken();
//    }

    @Override
    public List<CountBuilder> getItemCount(List<PublicSearchResponse> publicSearchResponseList) {

        try {

            List<CountBuilder> countBuilderList = new ArrayList<>();

            for (PublicSearchResponse publicSearchResponse : publicSearchResponseList) {

                AffiliateResponsePojo[] affiliateResponsePojos = publicSearchResponse.getAffiliateResponsePojos();

                for (AffiliateResponsePojo affiliateResponsePojo: affiliateResponsePojos) {

                    Map<String, String> map = new HashMap<>();

                    if(affiliateResponsePojo.getAffiliateType() != null) {

                        String mainTitle = affiliateResponsePojo.getAffiliateType().toLowerCase()+ "_";

                        if (affiliateResponsePojo.getEmail() != null && !affiliateResponsePojo.getEmail().isEmpty()) {
                            map.put(mainTitle + "email", "email");
                        }

                        if (affiliateResponsePojo.getAddress() != null && !affiliateResponsePojo.getAddress().isEmpty()) {
                            map.put(mainTitle + "address", "address");
                        }

                        if (affiliateResponsePojo.getGender() != null && !affiliateResponsePojo.getGender().isEmpty()) {
                            map.put( mainTitle + "gender", "gender");
                        }

                        if (affiliateResponsePojo.getPhoneNumber() != null && !affiliateResponsePojo.getPhoneNumber().isEmpty()) {

                            map.put(mainTitle + "phone_number", "phone_number");
                        }


                        updateListOfCountBuilder(
                                map,
                                affiliateResponsePojo.getAffiliateType(),
                                countBuilderList
                        );
                    }
                }

            }

            return countBuilderList;
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }
    }


    public static void updateListOfCountBuilder(Map<String, String> map,
                                                String type,
                                                List<CountBuilder> countBuilderList) {

            List<CountBuilder> foundCountBuilderList = countBuilderList.stream().filter(countBuilder -> Objects.equals(countBuilder.getLabel(), type.toLowerCase())).collect(Collectors.toList());


            if (foundCountBuilderList.size() > 0) {

                CountBuilder foundCountBuilder = foundCountBuilderList.get(0);

                int index = countBuilderList.indexOf(foundCountBuilder);

                List<CountBuilder> innerCountBuilderList = countBuilderList.get(index).getInnerFields();

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String innerLabel = entry.getKey();
                    String innerTitle = entry.getValue();

                    List<CountBuilder> foundInnerCountBuilderList = innerCountBuilderList.stream().filter(innerCountBuilder -> Objects.equals(innerCountBuilder.getLabel(), innerLabel.toLowerCase())).collect(Collectors.toList());

                    if(foundInnerCountBuilderList.size() > 0) {

                        int innerIndex = innerCountBuilderList.indexOf(foundInnerCountBuilderList.get(0));

                        CountBuilder innerCountBuilder = new CountBuilder();
                        innerCountBuilder.setLabel(Utils.customToLowerCase(innerLabel));
                        innerCountBuilder.setTitle(Utils.removeUnderscoreAndCapitalizeWords(innerTitle));
                        innerCountBuilder.setCount(1);
                        countBuilderList.get(index).getInnerFields().get(innerIndex).setCount(innerCountBuilderList.get(innerIndex).getCount() + 1);

                    } else {
                        CountBuilder innerCountBuilder = new CountBuilder();
                        innerCountBuilder.setLabel(Utils.customToLowerCase(innerLabel));
                        innerCountBuilder.setTitle(Utils.removeUnderscoreAndCapitalizeWords(innerTitle));
                        innerCountBuilder.setCount(1);
                        countBuilderList.get(index).getInnerFields().add(innerCountBuilder);
                    }
                }

            } else {

                //Base Builder
                CountBuilder countBuilder = new CountBuilder();
                countBuilder.setLabel(Utils.customToLowerCase(type));
                countBuilder.setTitle(Utils.removeUnderscoreAndCapitalizeWords(type));

                //Inner Builder
                List<CountBuilder> innerCountBuilderList = new ArrayList<>();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String innerLabel = entry.getKey();
                    String innerTitle = entry.getValue();

                    CountBuilder innerCountBuilder = new CountBuilder();
                    innerCountBuilder.setLabel(Utils.customToLowerCase(innerLabel));
                    innerCountBuilder.setTitle(Utils.removeUnderscoreAndCapitalizeWords(innerTitle));
                    innerCountBuilder.setCount(1);
                    innerCountBuilderList.add(innerCountBuilder);
                }

                // Added inner
                countBuilder.setInnerFields(innerCountBuilderList);

                countBuilderList.add(countBuilder);
            }

    }


    public static int addCountToMap(Map<String, Integer> directorsMap, String key) {

        int result = 1;
        if (directorsMap.containsKey(key)) {
            result = (int) directorsMap.get(key);
            return result + 1;
        }

        return result;
    }


    public PublicSearchTypePojo convertData(JSONObject jsonObject) {

        Object intervention = jsonObject.get("data");

        PublicSearchTypePojo publicSearchTypePojo = new PublicSearchTypePojo();
        if (intervention instanceof JSONArray) {
            JSONArray dataOfJSON = (JSONArray) jsonObject.get("data");
            String json = String.valueOf(dataOfJSON);
            publicSearchTypePojo.setJsonTypes(MyJSONTypeConstant.JSON_ARRAY);
            publicSearchTypePojo.setJson(json);
        } else if (intervention instanceof JSONObject) {
            JSONObject dataOfJSON = (JSONObject) jsonObject.get("data");
            String json = String.valueOf(dataOfJSON);
            publicSearchTypePojo.setJsonTypes(MyJSONTypeConstant.JSON_OBJECT);
            publicSearchTypePojo.setJson(json);
        }

        return publicSearchTypePojo;

    }


    @Override
    public List<PublicSearchResponse> doSearch(PublicSearchDto publicSearchDto) throws IOException {

        int classification = publicSearchDto.getClassification();

        String searchField = publicSearchDto.getSearchByCompanyName();

        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(searchField);
        StringBuilder sb = new StringBuilder();

        sb.append(publicSearchDomain);

        if (matcher.find()) {
            sb.append("/api/report/find/");
            sb.append("company?rcNumber=").append(searchField);

            if (classification > -1) {
                sb.append("&classification=").append(classification);
            }
        } else {
            sb.append("/api/report/search/company/info/").append(searchField);
        }

        String url = sb.toString();

       // logger.info(url);

        String token = "Bearer " + publicSearchToken;

            JSONObject jsonObject = this.okHttpUtil.getWithJsonResponseWithAuthorizationHeader(url, token);

            PublicSearchTypePojo publicSearchTypePojo = this.convertData(jsonObject);

//            logger.info("coming...");
//            logger.info(publicSearchTypePojo.getJson());

            return this.getPublicSearchPojoList(
                    publicSearchTypePojo,
                    publicSearchDto,
                    publicSearchDto.isUseDefault()
            );

    }



    public void runFilter(List<CountBuilderDto> countBuilderList,
                   List<AffiliateResponsePojo> affiliateResponsePojoList,
                   AffiliateDto affiliateDto) {

        List<String> listOfFilters = new ArrayList<>();
        for(CountBuilderDto countBuilder: countBuilderList) {
            if(countBuilder.isValue()) {
                listOfFilters.add(countBuilder.getTitle().toUpperCase().trim());
            }
        }

        AffiliateResponsePojo affiliateResponsePojo = new AffiliateResponsePojo();

        if (listOfFilters.contains("EMAIL".toUpperCase())) {
            // if(affiliateDto.getEmail() != null && !affiliateDto.getEmail().isEmpty()) {
            affiliateResponsePojo.setEmail(affiliateDto.getEmail());
           // }
        }

        if (listOfFilters.contains("PHONE_NUMBER")) {
            affiliateResponsePojo.setPhoneNumber(affiliateDto.getPhoneNumber());
        }

        if (listOfFilters.contains("GENDER")) {
            affiliateResponsePojo.setGender(affiliateDto.getGender());
        }


        if (listOfFilters.contains("ADDRESS")) {
            affiliateResponsePojo.setAddress(affiliateDto.getAddress());
        }


        affiliateResponsePojo.setAffiliateType(affiliateDto.getAffiliateType());
        affiliateResponsePojo.setDateOfAppointment(affiliateDto.getDateOfAppointment());
        affiliateResponsePojo.setFirstname(affiliateDto.getFirstname());
        affiliateResponsePojo.setSurname(affiliateDto.getSurname());
        affiliateResponsePojoList.add(affiliateResponsePojo);
    }
}
