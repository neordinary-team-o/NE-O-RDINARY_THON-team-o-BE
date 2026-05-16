package com.woojin.nerdinary_taem_o.domain.song.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "곡 검색 요청")
public record SongSearchRequest(
        @Schema(description = "검색할 곡명 또는 키워드", example = "마지막처럼", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "검색어를 입력해주세요.")
        String keyword
) {
}
