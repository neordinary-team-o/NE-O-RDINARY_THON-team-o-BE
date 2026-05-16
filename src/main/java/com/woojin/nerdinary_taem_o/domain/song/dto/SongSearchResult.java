package com.woojin.nerdinary_taem_o.domain.song.dto;

import java.time.LocalDate;

public record SongSearchResult(
        String videoId,
        String title,
        String artist,
        Long viewCount,
        LocalDate uploadDate,
        String thumbnailUrl
) {
}
