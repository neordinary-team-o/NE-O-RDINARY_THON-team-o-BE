package com.woojin.nerdinary_taem_o.common.exception.model;

import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;

public class SessionNotInProgressException extends BusinessException {

    public SessionNotInProgressException() {
        super(ErrorCode.SESSION_NOT_IN_PROGRESS);
    }
}
