package com.oasis.cac.vas.dao.psql;

import com.oasis.cac.vas.models.PreLicensedPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreLicensedPartnerDao extends JpaRepository<PreLicensedPartner, Long> {

    PreLicensedPartner findByEmail(String email);
}
