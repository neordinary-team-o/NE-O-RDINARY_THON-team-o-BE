package com.woojin.nerdinary_taem_o.domain.dig.dto;

import com.woojin.nerdinary_taem_o.domain.dig.entity.AchievementBadge;
import com.woojin.nerdinary_taem_o.domain.dig.entity.Dig;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "곡 발굴 등록 응답")
public record DigCreateResponse(
        @Schema(description = "발굴 기록 ID", example = "1")
        Long digId,

        @Schema(description = "곡 ID", example = "1")
        Long songId,

        @Schema(description = "곡 제목", example = "마지막처럼")
        String title,

        @Schema(description = "아티스트명", example = "BLACKPINK")
        String artist,

        @Schema(description = "썸네일 URL", example = "https://i.ytimg.com/vi/Amq-qlqbjYA/hqdefault.jpg")
        String thumbnailUrl,

        @Schema(description = "발굴 당시 조회수", example = "123456789")
        Long snapshotViewCount,

        @Schema(description = "현재 조회수", example = "123456789")
        Long currentViewCount,

        @Schema(description = "등록 직후 성장률", example = "0.0")
        Double growthRate,

        @Schema(description = "달성 배지. 등록 직후에는 null입니다.", nullable = true)
        AchievementBadge achievementBadge,

        @Schema(description = "발굴 시각", example = "2026-05-17T03:20:00")
        LocalDateTime dugAt
) {

    public static DigCreateResponse from(Dig dig) {
        return new DigCreateResponse(
                dig.getId(),
                dig.getSong().getId(),
                dig.getSong().getTitle(),
                dig.getSong().getArtist().getName(),
                dig.getSong().getThumbnailUrl(),
                dig.getSnapshotViewCount(),
                dig.getSong().getCurrentViewCount(),
                0.0,
                null,
                dig.getDugAt()
        );
    }
}
