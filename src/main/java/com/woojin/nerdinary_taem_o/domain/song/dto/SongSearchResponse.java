package com.woojin.nerdinary_taem_o.domain.song.dto;

import java.time.LocalDate;

public record SongSearchResponse(
        String videoId,
        String title,
        String artist,
        Long viewCount,
        LocalDate uploadDate,
        String thumbnailUrl
) {

    public static SongSearchResponse from(SongSearchResult result) {
        return new SongSearchResponse(
                result.videoId(),
                result.title(),
                result.artist(),
                result.viewCount(),
                result.uploadDate(),
                result.thumbnailUrl()
        );
    }
}
