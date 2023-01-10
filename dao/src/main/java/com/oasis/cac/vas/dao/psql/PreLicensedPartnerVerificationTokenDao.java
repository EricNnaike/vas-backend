package com.oasis.cac.vas.dao.psql;

import com.oasis.cac.vas.models.PreLicensedPartner;
import com.oasis.cac.vas.models.PreLicensedPartnerVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PreLicensedPartnerVerificationTokenDao extends JpaRepository<PreLicensedPartnerVerificationToken, Long> {

    Optional<PreLicensedPartnerVerificationToken> findByPreLicensedPartnerAndTokenAndTokenExpired(PreLicensedPartner preLicensedPartner, String token, boolean tokenExpired);
}
