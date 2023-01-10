package com.oasis.cac.vas.dao.psql;

import com.oasis.cac.vas.models.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  EmailVerificationDao extends JpaRepository<EmailVerificationToken, Long> {

    EmailVerificationToken findByTokenAndTokenExpired(String token, boolean expired);
}
