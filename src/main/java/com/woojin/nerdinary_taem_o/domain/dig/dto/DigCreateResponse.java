package com.woojin.nerdinary_taem_o.domain.dig.dto;

import com.woojin.nerdinary_taem_o.domain.dig.entity.AchievementBadge;
import com.woojin.nerdinary_taem_o.domain.dig.entity.Dig;
import java.time.LocalDateTime;

public record DigCreateResponse(
        Long digId,
        Long songId,
        String title,
        String artist,
        String thumbnailUrl,
        Long snapshotViewCount,
        Long currentViewCount,
        Double growthRate,
        AchievementBadge achievementBadge,
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
