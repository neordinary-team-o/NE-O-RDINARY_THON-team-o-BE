package com.woojin.nerdinary_taem_o.domain.song.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;
import com.woojin.nerdinary_taem_o.common.exception.model.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class YoutubeClient {

    private static final String SEARCH_URL = "https://www.googleapis.com/youtube/v3/search";
    private static final String VIDEOS_URL = "https://www.googleapis.com/youtube/v3/videos";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${youtube.api-key:}")
    private String apiKey;

    public List<YoutubeSearchItem> searchTopMusicVideos(String keyword, int maxResults) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new BusinessException(ErrorCode.EXTERNAL_API_FAILED, "YouTube API 키가 설정되지 않았습니다");
        }

        String query = encode(keyword + " Official");
        URI uri = URI.create(SEARCH_URL
                + "?part=snippet"
                + "&q=" + query
                + "&type=video"
                + "&videoCategoryId=10"
                + "&maxResults=" + maxResults
                + "&key=" + encode(apiKey));

        JsonNode root = sendGet(uri, "YouTube 검색");
        JsonNode items = root.path("items");
        if (!items.isArray() || items.isEmpty()) {
            throw new BusinessException(ErrorCode.SONG_SEARCH_EMPTY);
        }

        List<YoutubeSearchItem> results = new ArrayList<>();
        for (JsonNode item : items) {
            String videoId = item.path("id").path("videoId").asText();
            JsonNode snippet = item.path("snippet");
            if (videoId.isBlank() || snippet.isMissingNode()) {
                continue;
            }

            results.add(new YoutubeSearchItem(
                    videoId,
                    HtmlUtils.htmlUnescape(snippet.path("title").asText()),
                    HtmlUtils.htmlUnescape(snippet.path("channelTitle").asText()),
                    OffsetDateTime.parse(snippet.path("publishedAt").asText()).toLocalDate(),
                    selectThumbnailUrl(snippet.path("thumbnails"))
            ));
        }

        if (results.isEmpty()) {
            throw new BusinessException(ErrorCode.SONG_SEARCH_EMPTY);
        }

        return results;
    }

    public Map<String, Long> fetchViewCounts(List<String> videoIds) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new BusinessException(ErrorCode.EXTERNAL_API_FAILED, "YouTube API 키가 설정되지 않았습니다");
        }
        if (videoIds == null || videoIds.isEmpty()) {
            return Map.of();
        }

        String ids = videoIds.stream()
                .map(String::trim)
                .filter(id -> !id.isBlank())
                .collect(Collectors.joining(","));
        URI uri = URI.create(VIDEOS_URL
                + "?part=statistics"
                + "&id=" + encode(ids)
                + "&key=" + encode(apiKey));

        JsonNode root = sendGet(uri, "YouTube 조회수");
        Map<String, Long> viewCounts = new HashMap<>();
        for (JsonNode item : root.path("items")) {
            String videoId = item.path("id").asText();
            long viewCount = item.path("statistics").path("viewCount").asLong(0L);
            if (!videoId.isBlank()) {
                viewCounts.put(videoId, viewCount);
            }
        }
        return viewCounts;
    }

    private JsonNode sendGet(URI uri, String apiName) {
        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new BusinessException(ErrorCode.EXTERNAL_API_FAILED, apiName + " API 호출에 실패했습니다");
            }
            return objectMapper.readTree(response.body());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.EXTERNAL_API_FAILED, apiName + " API 응답 처리 실패: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ErrorCode.EXTERNAL_API_FAILED, apiName + " API 호출이 중단되었습니다");
        }
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    String selectThumbnailUrl(JsonNode thumbnails) {
        String high = thumbnails.path("high").path("url").asText();
        if (!high.isBlank()) {
            return high;
        }

        String medium = thumbnails.path("medium").path("url").asText();
        if (!medium.isBlank()) {
            return medium;
        }

        String defaultThumbnail = thumbnails.path("default").path("url").asText();
        if (!defaultThumbnail.isBlank()) {
            return defaultThumbnail;
        }

        return null;
    }

    public record YoutubeSearchItem(
            String videoId,
            String rawTitle,
            String channelTitle,
            java.time.LocalDate uploadDate,
            String thumbnailUrl
    ) {
    }
}
