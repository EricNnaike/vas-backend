package com.oasis.cac.vas.service.util;

import java.util.Optional;

public interface GoogleVerificationService {

    Optional<String> verify(String authToken);
}
