package com.oasis.cac.vas.email_sender.service.mail_composer;

import com.oasis.cac.vas.email_sender.pojo.MailPojo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MailComposerServiceImpl implements MailComposerService {

    public MailPojo preLicensedPartnerEmail(Long userId, String noReplyEmail, String email, String code, String domainUrl, String emailValidForInMinutes) {

            MailPojo mailPojo = new MailPojo();
            mailPojo.setFrom(noReplyEmail);
            mailPojo.setTo(email);
            mailPojo.setSubject("Account verification");
            mailPojo.setTemplateName("pre-licensed-partner-registration.ftl");

//            InputStream inputStream = ResourceUtil.getResourceAsStream("base64/images.json");
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//            Base64ImageDto base64ImageDto = gson.fromJson(bufferedReader, Base64ImageDto.class);

            Map<String, Object> model = new HashMap<>();
            model.put("code", code);
            model.put("userId", userId);
            String url = "/auth/email-verification/pre-licensed-partner";
            model.put("url", url);
            model.put("domainUrl", domainUrl);
            model.put("live_duration", emailValidForInMinutes);
            mailPojo.setModel(model);

            return mailPojo;
        }
}
