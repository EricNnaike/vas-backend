package com.oasis.cac.vas.service.encrypt_service;

import java.util.List;

public interface EncryptService {

    List<Object> encryptDataList(List<Object> input);

    Object encryptDataObject(Object input);
}
