package com.woojin.nerdinary_taem_o.domain.dig.dto;

import java.time.LocalDate;

public record CreateDigRequest(
        Long userId,
        String videoId,
        String title,
        String artist,
        Long viewCount,
        LocalDate uploadDate,
        String thumbnailUrl,
        String comment
) {
}
