package com.oasis.cac.vas.dao.psql;

import com.oasis.cac.vas.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.oasis.cac.vas.utils.oasisenum.RoleTypeConstant;

@Repository
public interface RolesDao extends JpaRepository<Role, Long> {

    @Query("SELECT r FROM Role as r WHERE r.roleType = ?1")
    Role findByRoleType(RoleTypeConstant roleType);

}
