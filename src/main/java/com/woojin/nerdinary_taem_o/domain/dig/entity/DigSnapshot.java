package com.woojin.nerdinary_taem_o.domain.dig.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DigSnapshot {

    private Youtube youtube;

    @JsonProperty("recent_growth_rate")
    private Double recentGrowthRate;

    @JsonProperty("dig_score")
    private Integer digScore;

    @JsonProperty("collected_at")
    private String collectedAt;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Youtube {
        @JsonProperty("view_count")
        private Long viewCount;

        @JsonProperty("upload_date")
        private String uploadDate;
    }
}
