package com.woojin.nerdinary_taem_o.common.exception.model;

import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(ErrorCode errorCode,
                                   String customMessage) {
        super(errorCode, customMessage);
    }

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
