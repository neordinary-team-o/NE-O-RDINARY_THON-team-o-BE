package com.woojin.nerdinary_taem_o.api.docs;

import com.woojin.nerdinary_taem_o.common.dto.ApiResponse;
import com.woojin.nerdinary_taem_o.common.dto.PageResponse;
import com.woojin.nerdinary_taem_o.domain.dig.dto.DigCardDto;
import com.woojin.nerdinary_taem_o.domain.dig.dto.DigDetailDto;
import com.woojin.nerdinary_taem_o.domain.dig.dto.DigSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Dig Search", description = "내가 발굴한 곡 조회 및 검색 API")
public interface DigSearchControllerDocs {

    @Operation(
            summary = "내가 발굴한 곡 목록 조회",
            description = """
                    홈 화면에서 사용자가 발굴한 곡 목록을 6개씩 조회합니다.
                    목록은 성장률 기준으로 정렬되며, page 값을 증가시켜 다음 목록을 조회할 수 있습니다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "내 발굴 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "내 발굴 목록 조회 성공 응답",
                                    value = """
                                            {
                                              "success": true,
                                              "data": {
                                                "content": [
                                                  {
                                                    "digId": 1,
                                                    "title": "마지막처럼",
                                                    "thumbnailUrl": "https://i.ytimg.com/vi/Amq-qlqbjYA/hqdefault.jpg"
                                                  }
                                                ],
                                                "page": 0,
                                                "size": 6,
                                                "totalElements": 1,
                                                "totalPages": 1
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
            )
    })
    ResponseEntity<ApiResponse<PageResponse<DigCardDto>>> getMyDigs(
            @Parameter(description = "사용자 ID", required = true, example = "1") Long userId,
            @Parameter(description = "페이지 번호. 0부터 시작합니다.", example = "0") int page
    );

    @Operation(
            summary = "발굴 상세 조회",
            description = """
                    홈 화면에서 곡 카드를 선택했을 때 발굴 상세 정보를 조회합니다.
                    발굴 당시 조회수, 현재 조회수, 성장률, 발굴 후 경과 기간과 안내 문구를 반환합니다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "발굴 상세 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "발굴 상세 조회 성공 응답",
                                    value = """
                                            {
                                              "success": true,
                                              "data": {
                                                "thumbnailUrl": "https://i.ytimg.com/vi/Amq-qlqbjYA/hqdefault.jpg",
                                                "title": "마지막처럼",
                                                "artistName": "BLACKPINK",
                                                "diggedAt": "26.05.17",
                                                "elapsedMonths": 0,
                                                "achievementName": null,
                                                "viewCountAtDig": 123456789,
                                                "currentViewCount": 160000000,
                                                "growthRate": 29.6,
                                                "narrativeMessage": "당신이 123,456,789명이 듣던 시절 발견한 음악이 지금 16,000만명에게 재생되고 있습니다. 당신의 귀는 시대보다 0개월 빨랐습니다."
                                              },
                                              "error": null
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "존재하지 않는 DIG 또는 입력값 오류"
            )
    })
    ResponseEntity<ApiResponse<DigDetailDto>> getDigDetail(
            @Parameter(description = "DIG ID", required = true, example = "1") Long digId
    );

    @Operation(
            summary = "내가 발굴한 곡 검색",
            description = "사용자가 발굴한 곡 목록에서 키워드가 포함된 곡을 검색합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "내 발굴 곡 검색 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "내 발굴 곡 검색 성공 응답",
                                    value = """
                                            {
                                              "success": true,
                                              "data": [
                                                {
                                                  "digId": 1,
                                                  "thumbnailUrl": "https://i.ytimg.com/vi/Amq-qlqbjYA/hqdefault.jpg",
                                                  "title": "마지막처럼"
                                                }
                                              ],
                                              "error": null
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "입력값 오류"
            )
    })
    ResponseEntity<ApiResponse<List<DigSearchDto>>> searchMyDigs(
            @Parameter(description = "사용자 ID", required = true, example = "1") Long userId,
            @Parameter(description = "검색 키워드", required = true, example = "마지막") String keyword
    );
}
