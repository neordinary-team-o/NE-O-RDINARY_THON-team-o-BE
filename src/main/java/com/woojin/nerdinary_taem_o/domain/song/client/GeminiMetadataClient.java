package com.woojin.nerdinary_taem_o.domain.song.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;
import com.woojin.nerdinary_taem_o.common.exception.model.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class GeminiMetadataClient {

    private static final String SYSTEM_INSTRUCTION = """
            너는 음악 메타데이터 정제 봇이야. 입력되는 유튜브 영상 제목(title)과 채널명(channelTitle)을 분석해서 실제 가수의 이름(artist)과 정확한 곡 제목(title)만 추출해줘.
            반드시 아래 JSON 구조로만 답변하고, 마크다운 코드 블록을 포함한 다른 부가 설명은 절대 하지 마.
            {"artist":"추출한 아티스트 이름","title":"추출한 곡 제목"}
            """;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${gemini.api-key:}")
    private String apiKey;

    @Value("${gemini.model:gemini-2.0-flash}")
    private String model;

    public RefinedMetadata refine(String title, String channelTitle) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new BusinessException(ErrorCode.EXTERNAL_API_FAILED, "Gemini API 키가 설정되지 않았습니다");
        }

        URI uri = URI.create("https://generativelanguage.googleapis.com/v1beta/models/"
                + encode(model)
                + ":generateContent?key="
                + encode(apiKey));

        try {
            String body = objectMapper.writeValueAsString(Map.of(
                    "systemInstruction", Map.of(
                            "parts", new Object[]{Map.of("text", SYSTEM_INSTRUCTION)}
                    ),
                    "contents", new Object[]{
                            Map.of("role", "user", "parts", new Object[]{
                                    Map.of("text", objectMapper.writeValueAsString(Map.of(
                                            "title", title,
                                            "channelTitle", channelTitle
                                    )))
                            })
                    },
                    "generationConfig", Map.of("responseMimeType", "application/json")
            ));

            HttpRequest request = HttpRequest.newBuilder(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new BusinessException(ErrorCode.EXTERNAL_API_FAILED, "Gemini API 호출에 실패했습니다");
            }

            JsonNode root = objectMapper.readTree(response.body());
            String content = root.path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText();
            JsonNode refined = objectMapper.readTree(stripCodeFence(content));
            String artist = refined.path("artist").asText();
            String refinedTitle = refined.path("title").asText();
            if (artist.isBlank() || refinedTitle.isBlank()) {
                throw new BusinessException(ErrorCode.EXTERNAL_API_FAILED, "Gemini API 응답에서 artist/title을 추출하지 못했습니다");
            }
            return new RefinedMetadata(artist, refinedTitle);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.EXTERNAL_API_FAILED, "Gemini API 응답 처리 실패: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ErrorCode.EXTERNAL_API_FAILED, "Gemini API 호출이 중단되었습니다");
        }
    }

    private String stripCodeFence(String content) {
        return content
                .replace("```json", "")
                .replace("```", "")
                .trim();
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public record RefinedMetadata(
            String artist,
            String title
    ) {
    }
}
