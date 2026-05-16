package com.woojin.nerdinary_taem_o.domain.dig.dto;

public record DigDetailDto(
        String thumbnailUrl,
        String title,
        String artistName,
        String diggedAt,
        long elapsedMonths,
        String achievementName,
        long viewCountAtDig,
        long currentViewCount,
        double growthRate,
        String narrativeMessage
) {}