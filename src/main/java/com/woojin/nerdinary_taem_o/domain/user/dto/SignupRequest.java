package com.woojin.nerdinary_taem_o.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 요청")
public record SignupRequest(
        @Schema(description = "닉네임", example = "hongdae", requiredMode = Schema.RequiredMode.REQUIRED)
        String nickname,

        @Schema(description = "4자리 비밀번호", example = "1234", requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {
}
