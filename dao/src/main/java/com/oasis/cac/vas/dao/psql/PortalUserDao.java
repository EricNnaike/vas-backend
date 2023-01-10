package com.oasis.cac.vas.dao.psql;

import com.oasis.cac.vas.models.PortalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PortalUserDao extends JpaRepository<PortalUser, Long> {

    @Query("SELECT p FROM PortalUser as p WHERE lower(p.email) = ?1")
    PortalUser findPortalUserByEmail(String name);

    PortalUser findByEmail(String email);

    PortalUser findByCode(String code);
}
