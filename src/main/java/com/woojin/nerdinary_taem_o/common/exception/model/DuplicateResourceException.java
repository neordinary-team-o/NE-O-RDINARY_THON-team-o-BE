package com.woojin.nerdinary_taem_o.common.exception.model;

import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;

public class DuplicateResourceException extends BusinessException {

    public DuplicateResourceException(ErrorCode errorCode,
                                      String customMessage) {
        super(errorCode, customMessage);
    }

    public DuplicateResourceException(ErrorCode errorCode) {
        super(errorCode);
    }
}
