package com.oasis.cac.vas.service.psql.email_verification;

import com.oasis.cac.vas.dao.psql.EmailVerificationDao;
import com.oasis.cac.vas.dao.psql.PortalUserDao;
import com.oasis.cac.vas.models.EmailVerificationToken;
import com.oasis.cac.vas.models.PortalUser;
import com.oasis.cac.vas.service.sequence.email_verification.EmailVerificationTokenSequence;
import com.oasis.cac.vas.utils.oasisenum.GenericStatusConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    @Autowired
    private EmailVerificationDao emailVerificationDao;

    @Autowired
    private PortalUserDao portalUserDao;

    @Autowired
    private EmailVerificationTokenSequence emailVerificationTokenSequence;

    @Value("${emailValidForInMinutes}")
    private String emailValidForInMinutes;

    static final long ONE_MINUTE_IN_MILLIS=60000; //milli_secs

    private final Calendar cal;

    public EmailVerificationServiceImpl() {
        this.cal = Calendar.getInstance();
    }

    @Override
    public EmailVerificationToken generateToken(PortalUser portalUser) {

        String token = String.format("U_SE_%04d%02d%02d%05d",
                LocalDate.now().getYear(),
                LocalDate.now().getMonthValue(),
                LocalDate.now().getDayOfMonth(),
                emailVerificationTokenSequence.getNextId()
        );

        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setPortalUser(portalUser);
        emailVerificationToken.setToken(token);

        Calendar date = Calendar.getInstance();
        long t= date.getTimeInMillis();
        Date newDate = new Date(t + (Integer.parseInt(emailValidForInMinutes) * ONE_MINUTE_IN_MILLIS));
        emailVerificationToken.setExpiryDate(newDate);
        return emailVerificationDao.save(emailVerificationToken);
    }


    boolean updateToExpired(EmailVerificationToken emailVerificationToken) {
        if ((emailVerificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            emailVerificationToken.setTokenExpired(true);
            emailVerificationDao.save(emailVerificationToken);
            return false;
        }

        PortalUser portalUser = emailVerificationToken.getPortalUser();
        portalUser.setUserStatus(GenericStatusConstant.ACTIVE);
        portalUser.setEmailVerified(true);
        portalUserDao.save(portalUser);
        return true;
    }


    @Override
    public boolean validatePasswordResetToken(String token, Long userId) {

        EmailVerificationToken emailVerificationToken = emailVerificationDao.findByTokenAndTokenExpired(token, false);

        if(emailVerificationToken == null || (!Objects.equals(emailVerificationToken.getPortalUser().getId(), userId))){
            return false;
        }

        return this.updateToExpired(emailVerificationToken);
    }

    @Override
    public boolean validatePasswordResetToken(String token, String userId) {

        try {

            EmailVerificationToken emailVerificationToken = null;

            emailVerificationToken = emailVerificationDao.findByTokenAndTokenExpired(token, false);

            if (emailVerificationToken == null) {
                return false;
            }

            return this.updateToExpired(emailVerificationToken);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
