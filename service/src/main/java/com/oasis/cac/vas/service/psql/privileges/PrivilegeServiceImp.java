package com.oasis.cac.vas.service.psql.privileges;

import com.oasis.cac.vas.dao.psql.PrivilegeDao;
import com.oasis.cac.vas.dto.PrivilegeDto;
import com.oasis.cac.vas.models.Privilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;


@Service
public class PrivilegeServiceImp implements PrivilegeService {

    @Autowired
    private PrivilegeDao privilegeDao;

    @Override
    public Privilege save(PrivilegeDto privilegeDto) {
        Privilege privilege = new Privilege();
        privilege.setName(privilegeDto.getName());
        return this.privilegeDao.save(privilege);
    }

    @Override
    public List<Privilege> findAll() {
        return this.privilegeDao.findAll();
    }

    @Override
    public Collection<Privilege> saveAll(List<PrivilegeDto> privilegeDtoList) {
        return null;
    }

    @Override
    public Privilege findName(String name) {
        return this.privilegeDao.findPrivilegeByName(name.toLowerCase());
    }
}
