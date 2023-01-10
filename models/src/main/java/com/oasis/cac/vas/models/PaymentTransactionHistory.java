package com.oasis.cac.vas.models;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "payment_transaction_history")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class PaymentTransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String transactionRef;

    private boolean isDownloaded;

    private int classification;

    private boolean isVerified;

    private Long amountInKobo;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
    })
    @JoinColumn(name="portal_method_id")
    private PaymentMethod paymentMethod;


    private String rcNumber;

    private String companyName;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private String userQuery;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created")
    private Date dateCreated = new Date();

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_updated")
    private Date dateUpdated = new Date();
}
