package com.oasis.cac.vas.restfulapi.interceptor;

import com.oasis.cac.vas.utils.interceptor.EncryptInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class EncryptInterceptorAppConfig implements WebMvcConfigurer {

    @Autowired
    EncryptInterceptor encryptInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(encryptInterceptor);
    }
}