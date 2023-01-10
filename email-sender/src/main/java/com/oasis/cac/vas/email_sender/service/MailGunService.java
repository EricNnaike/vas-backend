package com.oasis.cac.vas.email_sender.service;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.oasis.cac.vas.email_sender.pojo.MailPojo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;

@Service
public class MailGunService {

    @Qualifier("freeMarkerConfiguration")
    @Autowired
    private Configuration freemarkerConfig;

    @Async("mailGunExecutor")
    @Transactional
    public void sendMail(MailPojo mailPojo) throws TemplateException, IOException, UnirestException {

        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates/");
        Template template = freemarkerConfig.getTemplate(mailPojo.getTemplateName());
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailPojo.getModel());

        String MAIL_GUN_API_KEY = "d41964b9c43584cbfb5b38c698c707ec-a2b91229-61196329";
        String MAIL_GUN_DOMAIN_NAME = "mg.tradecraftsecurityreport.com";

        // HttpResponse<JsonNode> request =

        Unirest.post("https://api.mailgun.net/v3/" + MAIL_GUN_DOMAIN_NAME + "/messages")
                .basicAuth("api", MAIL_GUN_API_KEY)
                .field("from", mailPojo.getFrom())
                .field("to", mailPojo.getTo())
                .field("subject", mailPojo.getSubject())
                .field("html", html)
                .asJson();

//        return request.getBody();

    }
}
