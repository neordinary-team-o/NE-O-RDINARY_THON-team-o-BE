package com.woojin.nerdinary_taem_o.api.docs;

import com.woojin.nerdinary_taem_o.common.dto.ApiResponse;
import com.woojin.nerdinary_taem_o.domain.song.dto.SongSearchRequest;
import com.woojin.nerdinary_taem_o.domain.song.dto.SongSearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Song", description = "곡 검색 API")
public interface SongControllerDocs {

    @Operation(
            summary = "곡 검색",
            description = """
                    사용자가 입력한 검색어에 official 키워드를 붙여 유튜브에서 곡 후보 1개를 검색합니다.
                    검색된 유튜브 제목과 채널명은 Gemini를 통해 곡명과 아티스트명으로 정제합니다.
                    """
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "곡 검색 요청 정보",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SongSearchRequest.class),
                    examples = @ExampleObject(
                            name = "곡 검색 요청",
                            value = """
                                    {
                                      "keyword": "마지막처럼"
                                    }
                                    """
                    )
            )
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "곡 검색 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "곡 검색 성공 응답",
                                    value = """
                                            {
                                              "success": true,
                                              "data": {
                                                "videoId": "Amq-qlqbjYA",
                                                "title": "마지막처럼",
                                                "artist": "BLACKPINK",
                                                "viewCount": 123456789,
                                                "uploadDate": "2017-06-22",
                                                "thumbnailUrl": "https://i.ytimg.com/vi/Amq-qlqbjYA/hqdefault.jpg"
                                              },
                                              "error": null
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "검색어 누락 또는 입력값 오류"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "검색된 곡 없음"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "502",
                    description = "유튜브 또는 Gemini API 호출 실패"
            )
    })
    ResponseEntity<ApiResponse<SongSearchResponse>> search(SongSearchRequest request);
}
