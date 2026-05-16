package com.woojin.nerdinary_taem_o.api.docs;

import com.woojin.nerdinary_taem_o.common.dto.ApiResponse;
import com.woojin.nerdinary_taem_o.domain.dig.dto.DigCreateRequest;
import com.woojin.nerdinary_taem_o.domain.dig.dto.DigCreateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Dig", description = "곡 발굴 API")
public interface DigControllerDocs {

    @Operation(
            summary = "곡 발굴 등록",
            description = """
                    검색 API로 받은 곡 후보를 사용자가 발굴한 곡으로 등록합니다.
                    아티스트와 곡은 기존 데이터가 있으면 재사용하고, 없으면 새로 생성합니다.
                    같은 사용자가 같은 곡을 이미 발굴한 경우 중복 등록을 막습니다.
                    """
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "발굴 등록할 곡 후보 정보",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DigCreateRequest.class),
                    examples = @ExampleObject(
                            name = "곡 발굴 등록 요청",
                            value = """
                                    {
                                      "userId": 1,
                                      "videoId": "Amq-qlqbjYA",
                                      "title": "마지막처럼",
                                      "artist": "BLACKPINK",
                                      "viewCount": 123456789,
                                      "uploadDate": "2017-06-22",
                                      "thumbnailUrl": "https://i.ytimg.com/vi/Amq-qlqbjYA/hqdefault.jpg",
                                      "comment": "초기에 발견한 곡"
                                    }
                                    """
                    )
            )
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "곡 발굴 등록 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "곡 발굴 등록 성공 응답",
                                    value = """
                                            {
                                              "success": true,
                                              "data": {
                                                "digId": 1,
                                                "songId": 1,
                                                "title": "마지막처럼",
                                                "artist": "BLACKPINK",
                                                "thumbnailUrl": "https://i.ytimg.com/vi/Amq-qlqbjYA/hqdefault.jpg",
                                                "snapshotViewCount": 123456789,
                                                "currentViewCount": 123456789,
                                                "growthRate": 0.0,
                                                "achievementBadge": null,
                                                "dugAt": "2026-05-17T03:20:00"
                                              },
                                              "error": null
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "입력값 오류"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "이미 등록한 곡"
            )
    })
    ResponseEntity<ApiResponse<DigCreateResponse>> create(DigCreateRequest request);

    @Operation(
            summary = "성장률 갱신",
            description = "YouTube API로 현재 조회수를 가져와 해당 DIG의 성장률을 갱신합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "갱신 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {"success": true, "data": null, "error": null}
                                    """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "DIG를 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "YouTube API 호출 실패")
    })
    ResponseEntity<ApiResponse<Void>> refreshGrowthRate(
            @Parameter(description = "DIG ID", required = true, example = "1") Long digId
    );
}
