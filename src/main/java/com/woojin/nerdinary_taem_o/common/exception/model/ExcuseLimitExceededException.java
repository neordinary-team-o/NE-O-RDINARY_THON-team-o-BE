package com.woojin.nerdinary_taem_o.common.exception.model;

import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;

public class ExcuseLimitExceededException extends BusinessException {

    public ExcuseLimitExceededException(String customMessage) {
        super(ErrorCode.EXCUSE_LIMIT_EXCEEDED, customMessage);
    }
}
