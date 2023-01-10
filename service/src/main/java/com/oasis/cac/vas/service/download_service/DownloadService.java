package com.oasis.cac.vas.service.download_service;

import com.oasis.cac.vas.dto.PublicSearchDto;
import com.oasis.cac.vas.models.PaymentTransactionHistory;

public interface DownloadService {

    PublicSearchDto generatePublicSearchDto(PaymentTransactionHistory paymentTransactionHistory);
}
