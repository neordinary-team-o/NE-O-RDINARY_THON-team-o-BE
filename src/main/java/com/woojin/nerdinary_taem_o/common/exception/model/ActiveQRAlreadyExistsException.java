package com.woojin.nerdinary_taem_o.common.exception.model;

import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;

public class ActiveQRAlreadyExistsException extends BusinessException {

    public ActiveQRAlreadyExistsException() {
        super(ErrorCode.QR_ALREADY_ACTIVE);
    }
}
