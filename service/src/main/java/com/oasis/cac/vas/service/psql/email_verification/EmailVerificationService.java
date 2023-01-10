package com.oasis.cac.vas.service.psql.email_verification;

import com.oasis.cac.vas.models.EmailVerificationToken;
import com.oasis.cac.vas.models.PortalUser;


public interface EmailVerificationService {

        boolean validatePasswordResetToken(String token, Long userId);

        boolean validatePasswordResetToken(String token, String userId);

        EmailVerificationToken generateToken(PortalUser portalUser);
}
