package com.woojin.nerdinary_taem_o.domain.dig.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "곡 발굴 등록 요청")
public record DigCreateRequest(
        @Schema(description = "사용자 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long userId,

        @Schema(description = "유튜브 영상 ID. 현재 DB에는 저장하지 않습니다.", example = "Amq-qlqbjYA")
        String videoId,

        @Schema(description = "곡 제목", example = "마지막처럼", requiredMode = Schema.RequiredMode.REQUIRED)
        String title,

        @Schema(description = "아티스트명", example = "BLACKPINK", requiredMode = Schema.RequiredMode.REQUIRED)
        String artist,

        @Schema(description = "발굴 당시 조회수", example = "123456789", requiredMode = Schema.RequiredMode.REQUIRED)
        Long viewCount,

        @Schema(description = "유튜브 업로드 날짜", example = "2017-06-22")
        LocalDate uploadDate,

        @Schema(description = "썸네일 URL", example = "https://i.ytimg.com/vi/Amq-qlqbjYA/hqdefault.jpg")
        String thumbnailUrl,

        @Schema(description = "한 줄 평가. 최대 100자입니다.", example = "초기에 발견한 곡")
        String comment
) {
}
