package com.woojin.nerdinary_taem_o.common.exception.model;

import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;

public class InsufficientDepositException extends BusinessException {

    public InsufficientDepositException(String customMessage) {
        super(ErrorCode.DEPOSIT_INSUFFICIENT, customMessage);
    }
}
