package com.oasis.cac.vas.email_sender.service.mail_composer;

import com.oasis.cac.vas.email_sender.pojo.MailPojo;

public interface MailComposerService {

    MailPojo preLicensedPartnerEmail(Long userId, String noReplyEmail, String email, String code, String domainUrl, String emailValidForInMinutes);
}
