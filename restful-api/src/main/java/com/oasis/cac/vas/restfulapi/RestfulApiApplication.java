package com.oasis.cac.vas.restfulapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication(scanBasePackages = "com.oasis.cac.vas")
@EnableJpaRepositories(basePackages = {"com.oasis.cac.vas.dao"})
@EntityScan(basePackages = {"com.oasis.cac.vas.models","com.oasis.cac.vas.auth"})
@EnableJpaAuditing
@EnableConfigurationProperties
public class RestfulApiApplication  extends SpringBootServletInitializer {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

//    @Bean
//    public LocalValidatorFactoryBean validator() {
//        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
//        bean.setValidationMessageSource(messageSource());
//        return bean;
//    }


    @Bean(name="freemarkerConfiguration")
    public freemarker.template.Configuration getFreeMarkerConfiguration() {
        freemarker.template.Configuration config = new freemarker.template.Configuration(freemarker.template.Configuration.getVersion());
        config.setClassForTemplateLoading(this.getClass(), "/templates/");
        return config;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RestfulApiApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(RestfulApiApplication.class, args);
        System.out.println("CAC VAS APPLICATION LAUNCHED!!!");
    }

}
