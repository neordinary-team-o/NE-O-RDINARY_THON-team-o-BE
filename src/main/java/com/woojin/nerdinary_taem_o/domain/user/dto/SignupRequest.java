package com.woojin.nerdinary_taem_o.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "회원가입 요청")
public record SignupRequest(
        @Schema(description = "닉네임", example = "hongdae", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "닉네임은 필수입니다.")
        String nickname,

        @Schema(description = "4자리 비밀번호", example = "1234", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 4, max = 4, message = "비밀번호는 4자리여야 합니다.")
        String password
) {
}
