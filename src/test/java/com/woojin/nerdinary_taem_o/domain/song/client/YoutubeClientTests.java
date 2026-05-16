package com.woojin.nerdinary_taem_o.domain.song.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class YoutubeClientTests {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final YoutubeClient youtubeClient = new YoutubeClient();

    @Test
    void selectThumbnailUrlPrefersHighThumbnail() throws Exception {
        JsonNode thumbnails = objectMapper.readTree("""
                {
                  "default": {"url": "default.jpg"},
                  "medium": {"url": "medium.jpg"},
                  "high": {"url": "high.jpg"}
                }
                """);

        String thumbnailUrl = youtubeClient.selectThumbnailUrl(thumbnails);

        assertThat(thumbnailUrl).isEqualTo("high.jpg");
    }

    @Test
    void selectThumbnailUrlFallsBackToMediumThenDefault() throws Exception {
        JsonNode mediumOnly = objectMapper.readTree("""
                {
                  "default": {"url": "default.jpg"},
                  "medium": {"url": "medium.jpg"}
                }
                """);
        JsonNode defaultOnly = objectMapper.readTree("""
                {
                  "default": {"url": "default.jpg"}
                }
                """);

        assertThat(youtubeClient.selectThumbnailUrl(mediumOnly)).isEqualTo("medium.jpg");
        assertThat(youtubeClient.selectThumbnailUrl(defaultOnly)).isEqualTo("default.jpg");
    }
}
