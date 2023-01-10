package com.oasis.cac.vas.service.psql.portalaccount;


import com.oasis.cac.vas.dto.PortalAccountDto;
import com.oasis.cac.vas.models.PortalAccount;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PortalAccountService {

    PortalAccount save(PortalAccountDto portalAccountDto);

    PortalAccount saveOrUpdate(PortalAccountDto portalAccountDto);

    List<PortalAccount> findAll();

    PortalAccount findPortalAccountByPortalAccountId(String portalAccountId);

    PortalAccount findPortalAccountByName(String name);

    String getUniqueId();
}
