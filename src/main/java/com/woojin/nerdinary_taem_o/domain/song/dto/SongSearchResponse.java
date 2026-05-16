package com.woojin.nerdinary_taem_o.domain.song.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "곡 검색 응답")
public record SongSearchResponse(
        @Schema(description = "유튜브 영상 ID", example = "Amq-qlqbjYA")
        String videoId,

        @Schema(description = "정제된 곡 제목", example = "마지막처럼")
        String title,

        @Schema(description = "정제된 아티스트명", example = "BLACKPINK")
        String artist,

        @Schema(description = "검색 당시 조회수", example = "123456789")
        Long viewCount,

        @Schema(description = "유튜브 업로드 날짜", example = "2017-06-22")
        LocalDate uploadDate,

        @Schema(description = "썸네일 URL", example = "https://i.ytimg.com/vi/Amq-qlqbjYA/hqdefault.jpg")
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
