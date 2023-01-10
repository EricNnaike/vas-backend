package com.oasis.cac.vas.models;

import com.oasis.cac.vas.utils.oasisenum.GenericStatusConstant;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "portal_user")
public class PortalUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    private String code;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String otherName;

    @Email
    private String email;

    @NotBlank
    private String password;

    private boolean isEmailVerified;


    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinColumn(name="portal_account_id", nullable=false)
    private PortalAccount portalAccount;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "portal_user_roles",
            joinColumns = { @JoinColumn(name = "portal_user_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<Role> roles = new HashSet<>();

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Column(length = 32, columnDefinition = "varchar(32) default 'INACTIVE'")
    @Enumerated(EnumType.STRING)
    private GenericStatusConstant userStatus = GenericStatusConstant.INACTIVE;

    private boolean isLockedOut;

    @Column(columnDefinition = "int default 0")
    private int failedLoginAttempts;

    @Column(columnDefinition = "boolean default true")
    private boolean isAccountNonLocked;


    @Column(name = "when_login_attempt_failed_last")
    @CreationTimestamp
    private Date whenLoginAttemptFailedLast;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created")
    private Date dateCreated = new Date();

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_updated")
    private Date dateUpdated = new Date();

    public boolean isLockedOut() {
        return isLockedOut;
    }

    public void setLockedOut(boolean lockedOut) {
        isLockedOut = lockedOut;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public GenericStatusConstant getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(GenericStatusConstant userStatus) {
        this.userStatus = userStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public Date getWhenLoginAttemptFailedLast() {
        return whenLoginAttemptFailedLast;
    }

    public void setWhenLoginAttemptFailedLast(Date whenLoginAttemptFailedLast) {
        this.whenLoginAttemptFailedLast = whenLoginAttemptFailedLast;
    }

    public PortalAccount getPortalAccount() {
        return portalAccount;
    }

    public void setPortalAccount(PortalAccount portalAccount) {
        this.portalAccount = portalAccount;
    }

    @Override
    public String toString() {
        return "PortalUser{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", otherName='" + otherName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isEmailVerified=" + isEmailVerified +
                ", userStatus=" + userStatus +
                ", isLockedOut=" + isLockedOut +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                '}';
    }
}
