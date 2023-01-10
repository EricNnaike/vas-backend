package com.oasis.cac.vas.service.sequence.portal_account_id;

import com.oasis.cac.vas.service.sequence.SequenceServiceImp;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@PortalAccountSequence
@Component
public class PortalAccountSequenceService extends SequenceServiceImp {
     public PortalAccountSequenceService(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate,"portal_account_id");
    }
}
