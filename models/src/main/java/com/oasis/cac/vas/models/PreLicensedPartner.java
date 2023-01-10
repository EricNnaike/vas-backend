package com.oasis.cac.vas.models;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;


@Entity
@Data
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Table(name = "pre_licensed_user")
public class PreLicensedPartner {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    private String email;

    @NotBlank
    private String fullName;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }, mappedBy = "preLicensedPartner")
    private Set<PreLicensedPartnerVerificationToken> preLicensedPartnerVerificationTokens;


    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private String userRegistrationInformation;


    @Column(columnDefinition = "integer default 1")
    private int currentFormWizardStep;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created")
    private Date dateCreated = new Date();

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_updated")
    private Date dateUpdated = new Date();

}
