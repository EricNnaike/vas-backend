package com.oasis.cac.vas.service.psql.portalaccount;

import com.oasis.cac.vas.dao.psql.PortalAccountDao;
import com.oasis.cac.vas.dao.psql.RolesDao;
import com.oasis.cac.vas.dto.PortalAccountDto;
import com.oasis.cac.vas.models.PortalAccount;
import com.oasis.cac.vas.service.sequence.portal_account_id.PortalAccountSequence;
import com.oasis.cac.vas.service.sequence.portal_account_id.PortalAccountSequenceService;
import com.oasis.cac.vas.utils.oasisenum.PortalAccountTypeConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Service
@Transactional
public class PortalAccountServiceImp implements PortalAccountService {

    private static final Logger logger = Logger.getLogger(PortalAccountServiceImp.class.getSimpleName());

    @Autowired
    private PortalAccountDao portalAccountDao;

    @Autowired
    private RolesDao rolesDao;

    @Autowired
    PortalAccountSequenceService portalAccountSequenceService;


    public void setPortalAccountDao(PortalAccountDao portalAccountDao) {
        this.portalAccountDao = portalAccountDao;
    }

    public RolesDao getRolesDao() {
        return rolesDao;
    }

    public void setRolesDao(RolesDao rolesDao) {
        this.rolesDao = rolesDao;
    }


    @Override
    public PortalAccount findPortalAccountByPortalAccountId(String portalAccountId) {
        return portalAccountDao.findPortalAccountByCode(portalAccountId);
    }

    @Override
    public PortalAccount findPortalAccountByName(String name) {
        return portalAccountDao.findPortalAccountByPortalAccountName(name.toLowerCase());
    }

    @Override
    public String getUniqueId() {
        return String.format("ACC_%04d%02d%02d%05d",
                LocalDate.now().getYear(),
                LocalDate.now().getMonthValue(),
                LocalDate.now().getDayOfMonth(),
                portalAccountSequenceService.getNextId()
        );
    }


    @Override
    public PortalAccount save(PortalAccountDto portalAccountDto) {
        PortalAccount portalAccount = new PortalAccount();
        portalAccount.setName(portalAccountDto.getName());

        String portalAccountId = String.format("PA_%04d%02d%02d%05d",
                LocalDate.now().getYear(),
                LocalDate.now().getMonthValue(),
                LocalDate.now().getDayOfMonth(),
                portalAccountSequenceService.getNextId()
        );

        portalAccount.setCode(portalAccountId);
        portalAccount.setPortalAccountType(PortalAccountTypeConstant.valueOf(portalAccountDto.getPortalAccountTypes()));
        portalAccount = portalAccountDao.save(portalAccount);
        return portalAccount;
    }


    @Override
    public PortalAccount saveOrUpdate(PortalAccountDto portalAccountDto) {
        return null;
    }

    @Override
    public List<PortalAccount> findAll() {
        return this.portalAccountDao.findAll();
    }

}
