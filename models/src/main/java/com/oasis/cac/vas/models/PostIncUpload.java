package com.oasis.cac.vas.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "POST_INC_UPLOADS")
public class PostIncUpload implements Serializable
{
    public enum PostIncProcess{CHANGE_OF_BN_ADDRESS, CHANGE_OF_COMPANY_ADDRESS, CHANGE_OF_IT_ADDRESS,
        CHANGE_OF_PROPRIETOR, CHANGE_OF_DIRECTOR, CHANGE_OF_TRUSTEE,ANNUAL_RETURN_BN,ANNUAL_RETURN_IT,ANNUAL_RETURN_COMPANY,
        CHANGE_OF_NAME_IT, CHANGE_OF_NAME_BN, CHANGE_OF_NAME_COMPANY}

    public enum EntityStatus{VALID, INVALID, RE_UPLOADED}
    public enum ApprovalStatus{VERIFIED, UNVERIFIED}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PostIncProcess postIncProcess;

    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;

    private Long entityId;

    @Enumerated(EnumType.STRING)
    private EntityStatus entityStatus;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    private String name;

    @Column(length = 1000)
    private String uploadUrl;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "PROCESS_TYPE_FK", referencedColumnName = "id")
    private Process_Type processType;

    @Column(name = "DOC_NAME")
    private String docName;

    private Long recordId;

    private String segmentName;

    private Long segmentId;

    private String filetype;

}