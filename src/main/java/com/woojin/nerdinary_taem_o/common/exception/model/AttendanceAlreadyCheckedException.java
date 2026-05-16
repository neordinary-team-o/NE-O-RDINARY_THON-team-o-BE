package com.woojin.nerdinary_taem_o.common.exception.model;

import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;

public class AttendanceAlreadyCheckedException extends BusinessException {

    public AttendanceAlreadyCheckedException() {
        super(ErrorCode.ATTENDANCE_ALREADY_CHECKED);
    }
}
