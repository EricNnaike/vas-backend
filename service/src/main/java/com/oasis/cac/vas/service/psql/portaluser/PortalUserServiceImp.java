package com.oasis.cac.vas.service.psql.portaluser;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.oasis.cac.vas.dao.psql.PortalAccountDao;
import com.oasis.cac.vas.dao.psql.PortalUserDao;
import com.oasis.cac.vas.dao.psql.RolesDao;
import com.oasis.cac.vas.dto.AdminPortalDto;
import com.oasis.cac.vas.dto.LoginDto;
import com.oasis.cac.vas.dto.PortalAccountDto;
import com.oasis.cac.vas.dto.PortalUserSignUpDto;
import com.oasis.cac.vas.models.PortalAccount;
import com.oasis.cac.vas.models.PortalUser;
import com.oasis.cac.vas.models.Role;
import com.oasis.cac.vas.service.psql.portalaccount.PortalAccountService;
import com.oasis.cac.vas.service.sequence.portal_user_id.PortalUserSequenceService;
import com.oasis.cac.vas.utils.errors.CustomBadRequestException;
import com.oasis.cac.vas.utils.errors.CustomException;
import com.oasis.cac.vas.utils.errors.PortalUserAlreadyExist;
import com.oasis.cac.vas.utils.errors.ResourceNotFoundException;
import com.oasis.cac.vas.utils.oasisenum.GenericStatusConstant;
import com.oasis.cac.vas.utils.oasisenum.PortalAccountTypeConstant;
import com.oasis.cac.vas.utils.oasisenum.RoleTypeConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sound.sampled.Port;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

@Service
@Transactional
public class PortalUserServiceImp implements PortalUserService {

    private static final Logger logger = Logger.getLogger(PortalUserServiceImp.class.getSimpleName());

    @Autowired
    private PortalUserDao portalUserDao;

    @Autowired
    private PortalAccountDao portalAccountDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RolesDao rolesDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PortalAccountService portalAccountService;

    private String dateSource = "2000-09-09";

    @Autowired
    public PortalUserServiceImp(){}

    @Autowired
    private PortalUserSequenceService portalUserSequenceService;

    @Override
    public List<PortalUser> findAll() {
        return this.portalUserDao.findAll();
    }

    @Override
    public PortalUser findPortalUserByEmail(String email) {
        return portalUserDao.findPortalUserByEmail(email.toLowerCase());
    }

    @Override
    public PortalUser findPortalUserById(Long id) {
        Optional<PortalUser> optionalPortalUser = portalUserDao.findById(id);
        return optionalPortalUser.orElse(null);
    }

//
//    @Override
//    public PortalUser findByEmail(String email) {
//        PortalUser portalUser = portalUserDao.findByEmail(email.toLowerCase());
//        PortalUser user = new PortalUser();
//
//        if (portalUser != null) {
//            user.setEmail(portalUser.getEmail());
//            user.setFirstName(portalUser.getFirstName());
//            user.setLastName(portalUser.getLastName());
//            user.setCode(portalUser.getCode());
//            user.setLockedOut(portalUser.isLockedOut());
//            user.setUserStatus(portalUser.getUserStatus());
//            user.setId(portalUser.getId());
//            user.setPassword(portalUser.getPassword());
//            return user;
//        }
//        return null;
//    }
//
//
//
//    @Override
//    public PortalUser findById(Long id) {
//        Optional<PortalUser> optionalPortalUser = portalUserDao.findById(id);
//
//        PortalUser user = new PortalUser();
//
//        if (optionalPortalUser.isPresent()) {
//            PortalUser portalUser = optionalPortalUser.get();
//            user.setEmail(portalUser.getEmail());
//            user.setFirstName(portalUser.getFirstName());
//            user.setLastName(portalUser.getLastName());
//            user.setCode(portalUser.getCode());
//            user.setLockedOut(portalUser.isLockedOut());
//            user.setUserStatus(portalUser.getUserStatus());
//            user.setId(portalUser.getId());
//            user.setPassword(portalUser.getPassword());
//            return user;
//        }
//        return null;
//    }

    @Override
    public PortalUser findByCode(String code) {
        PortalUser portalUser = portalUserDao.findByCode(code);
        PortalUser user = new PortalUser();

        if (portalUser != null) {
            user.setEmail(portalUser.getEmail());
            user.setFirstName(portalUser.getFirstName());
            user.setLastName(portalUser.getLastName());
            user.setCode(portalUser.getCode());
            user.setLockedOut(portalUser.isLockedOut());
            user.setUserStatus(portalUser.getUserStatus());
            user.setId(portalUser.getId());
            user.setPassword(portalUser.getPassword());
            return user;
        }
        return null;
    }


    @Override
    public PortalUser findByPortalUserCode(String code) {
        return portalUserDao.findByCode(code);
    }

    @Override
    public String getUniqueId() {
        return String.format("USR_%04d%02d%02d%05d",
                LocalDate.now().getYear(),
                LocalDate.now().getMonthValue(),
                LocalDate.now().getDayOfMonth(),
                portalUserSequenceService.getNextId()
        );
    }

    @Override
    public Long count() {
        return 0L;
    }

    @Override
    @Transactional
    public void create(AdminPortalDto adminPortalDto) {

        if(adminPortalDto.getPortalAccountTypeConstant().equals(PortalAccountTypeConstant.GENERAL_USER)) {

            PortalAccountDto portalAccountDto = new PortalAccountDto();
            portalAccountDto.setName(adminPortalDto.getEmail());
            portalAccountDto.setPortalAccountTypes(""+adminPortalDto.getPortalAccountTypeConstant());
            PortalAccount portalAccount = this.portalAccountService.save(portalAccountDto);

            String email = adminPortalDto.getEmail();

            PortalUser portalUser = new PortalUser();
            portalUser.setUserStatus(GenericStatusConstant.ACTIVE);
            portalUser.setCode(getUniqueId());
            portalUser.setEmail(email);
            portalUser.setEmailVerified(true);
            // array to set
            Set<Role> portalUserRoles = new HashSet<>();
            String[] roles = adminPortalDto.getRoles();
            for(String roleAsString :roles) {
                Role role = this.rolesDao.findByRoleType(RoleTypeConstant.valueOf(roleAsString.toUpperCase()));
                portalUserRoles.add(role);
            }
            portalUser.setRoles(portalUserRoles);
            String encryptedPassword = this.bCryptPasswordEncoder.encode(adminPortalDto.getPassword());
            portalUser.setPassword(encryptedPassword);
            portalUser.setPortalAccount(portalAccount);
            portalUser = this.portalUserDao.save(portalUser);

            Set<PortalUser> portalUsers = new HashSet<>();
            portalUsers.add(portalUser);
            portalAccount.setPortalUsers(portalUsers);
            this.portalAccountDao.save(portalAccount);

        } else {

            PortalAccountDto portalAccountDto = new PortalAccountDto();
            portalAccountDto.setPortalAccountTypes(""+adminPortalDto.getPortalAccountTypeConstant());
            portalAccountDto.setName(adminPortalDto.getEmail());
            PortalAccount portalAccount = this.portalAccountService.save(portalAccountDto);

            PortalUser portalUser = new PortalUser();
            portalUser.setUserStatus(GenericStatusConstant.ACTIVE);
            portalUser.setCode(getUniqueId());
            portalUser.setFirstName(adminPortalDto.getFirstName());
            portalUser.setLastName(adminPortalDto.getLastName());
            portalUser.setOtherName(adminPortalDto.getOtherName());
            portalUser.setEmail(adminPortalDto.getEmail());
            portalUser.setEmailVerified(true);
            portalUser.setPortalAccount(portalAccount);
            // array to set
            Set<Role> portalUserRoles = new HashSet<>();
            String[] roles = adminPortalDto.getRoles();
            for(String roleAsString :roles) {
                Role role = this.rolesDao.findByRoleType(RoleTypeConstant.valueOf(roleAsString.toUpperCase()));
                portalUserRoles.add(role);
            }
            portalUser.setRoles(portalUserRoles);
            String encryptedPassword = this.bCryptPasswordEncoder.encode(adminPortalDto.getPassword());
            portalUser.setPassword(encryptedPassword);
            portalUser = this.portalUserDao.save(portalUser);

            Set<PortalUser> portalUsers = new HashSet<>();
            portalUsers.add(portalUser);

            portalAccount.setPortalUsers(portalUsers);
            this.portalAccountDao.save(portalAccount);
        }
    }

    @Override
    public void createViaSocial(PortalUserSignUpDto signUpDto) {

            PortalAccountDto portalAccountDto = new PortalAccountDto();
            portalAccountDto.setName(signUpDto.getEmail());
            portalAccountDto.setPortalAccountTypes(""+signUpDto.getPortalAccountTypeConstant());
            PortalAccount portalAccount = this.portalAccountService.save(portalAccountDto);

            String email = signUpDto.getEmail();

            PortalUser portalUser = new PortalUser();
            portalUser.setUserStatus(GenericStatusConstant.ACTIVE);
            portalUser.setCode(getUniqueId());
            portalUser.setEmail(email);
            portalUser.setEmailVerified(true);
            // array to set
            Set<Role> portalUserRoles = new HashSet<>();
            String[] roles = signUpDto.getRoles();
            for(String roleAsString :roles) {
                Role role = this.rolesDao.findByRoleType(RoleTypeConstant.valueOf(roleAsString.toUpperCase()));
                portalUserRoles.add(role);
            }
            portalUser.setRoles(portalUserRoles);
             String encryptedPassword = this.bCryptPasswordEncoder.encode(signUpDto.getPassword());
             portalUser.setPassword(encryptedPassword);
            portalUser.setPortalAccount(portalAccount);
            portalUser = this.portalUserDao.save(portalUser);

            Set<PortalUser> portalUsers = new HashSet<>();
            portalUsers.add(portalUser);
            portalAccount.setPortalUsers(portalUsers);
            this.portalAccountDao.save(portalAccount);
    }

    @Override
    public PortalUser updateUserProfile(Object portalUserDto) {

//            JsonParser parser = new JsonParser();
//
//            JsonObject jsonObject = parser.parse(gson.toJson(portalUserDto)).getAsJsonObject();
//
//
//            if (doesFieldExist(jsonObject, "firstName")) {
//                portalUser.setFirstName(getStringField(jsonObject, "firstName"));
//            }
//
//            if (doesFieldExist(jsonObject, "lastName")) {
//                portalUser.setLastName(getStringField(jsonObject, "lastName"));
//            }
//
//            if (doesFieldExist(jsonObject, "otherName")) {
//                portalUser.setLastName(getStringField(jsonObject, "otherName"));
//            }
//
//            if (doesFieldExist(jsonObject, "defaultProfileTypeConstant")) {
//                portalUser.setDefaultProfileTypeConstant(DefaultProfileTypeConstant.valueOf(getStringField(jsonObject, "defaultProfileTypeConstant")));
//            }
//
//            if (doesFieldExist(jsonObject, "otherPhoneNumber")) {
//                portalUser.setOtherPhoneNumber(getStringField(jsonObject, "otherPhoneNumber"));
//            }

        //return portalUserDao.save(portalUser);
        return null;
    }

    @Override
    public PortalUser loginPortalUser(LoginDto loginDto) {
        if (loginDto.getEmail().equals("")) {
            throw new ResourceNotFoundException("Enter email");
        }
        loginDto.setPassword("");
        PortalUser userEmail = portalUserDao.findPortalUserByEmail(loginDto.getEmail());
        if (userEmail == null) {
            PortalUser portalUser1 = portalUserDao.findPortalUserByEmail(loginDto.getEmail());
            if (userEmail != null) {
                throw new CustomException("Already registered as a user");
            }else {
                PortalUser portalUser = new PortalUser();
                portalUser.setEmail(portalUser.getEmail());
                portalUser.setCode(loginDto.getTwoFactorCode());
//                portalUser.getPassword(loginDto.getPassword());

            }

        }
        return null;
    }

    public boolean doesFieldExist(JsonObject json, String field) {
        return json.has(field);
    }

    public JsonArray getArrayField(JsonObject json, String field) {
        return json.get(field).getAsJsonArray();
    }

    public boolean getBooleanField(JsonObject json, String field) {
        return json.get(field).getAsBoolean();
    }

    public String getStringField(JsonObject json, String field) {
        return json.get(field).getAsString();
    }


}
