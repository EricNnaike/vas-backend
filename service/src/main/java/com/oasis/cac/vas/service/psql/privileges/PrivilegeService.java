package com.oasis.cac.vas.service.psql.privileges;


import com.oasis.cac.vas.dto.PrivilegeDto;
import com.oasis.cac.vas.models.Privilege;

import java.util.Collection;
import java.util.List;

public interface PrivilegeService {

    Privilege save(PrivilegeDto privilegeDto);

    List<Privilege> findAll();

    Collection<Privilege> saveAll(List<PrivilegeDto> privilegeDtoList);

    Privilege findName(String name);
}
