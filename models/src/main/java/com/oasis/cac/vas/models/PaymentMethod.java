package com.oasis.cac.vas.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "payment_methods")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String name;

    private boolean isEnabled;

    private boolean isLiveActive;

    private String liveVerifyUrl;

    private boolean isVerificationInLine;

    private String liveSecret;

    private String livePublicKey;

    private String testVerifyUrl;

    private String testSecret;

    private String testPublicKey;


    @OneToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }, mappedBy = "paymentMethod")
    private Set<PaymentTransactionHistory> paymentTransactionHistories;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created")
    private Date dateCreated = new Date();

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_updated")
    private Date dateUpdated = new Date();

}
