package com.oasis.cac.vas.service.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;


@Service
public class GoogleVerificationServiceImpl implements GoogleVerificationService {


    private static final JacksonFactory jacksonFactory = new JacksonFactory();
    private static final HttpTransport transport = new NetHttpTransport();
    private static final Logger logger = LoggerFactory.getLogger(GoogleVerificationServiceImpl.class);

    @Value("${googleClientId}")
    private String GOOGLE_CLIENT_ID;

    @Override
    public Optional<String> verify(String idTokenString) {

        try {

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                    .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                    .build();

            GoogleIdToken idToken = null;
            logger.info(idTokenString);
            idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Print user identifier
                String userId = payload.getSubject();

                // Get profile information from payload
                String email = payload.getEmail();
                Optional<String> stringOptional = Optional.of(email);
                return  stringOptional;
            }

            return Optional.empty();

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();

        }

    }
}
