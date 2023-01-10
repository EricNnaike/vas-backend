package com.oasis.cac.vas.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class MessageUtil {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String code, String locale) {
        return messageSource.getMessage(code, null, new Locale(locale));
    }
}
