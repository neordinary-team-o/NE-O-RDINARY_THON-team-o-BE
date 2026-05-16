package com.woojin.nerdinary_taem_o.api.docs;

import com.woojin.nerdinary_taem_o.common.dto.ApiResponse;
import com.woojin.nerdinary_taem_o.domain.user.dto.LoginRequest;
import com.woojin.nerdinary_taem_o.domain.user.dto.LoginResponse;
import com.woojin.nerdinary_taem_o.domain.user.dto.SignupRequest;
import com.woojin.nerdinary_taem_o.domain.user.dto.SignupResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "회원가입 및 로그인 API")
public interface AuthControllerDocs {

    @Operation(
            summary = "회원가입",
            description = "닉네임과 4자리 비밀번호로 사용자를 생성합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "회원가입 요청 정보",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SignupRequest.class),
                    examples = @ExampleObject(
                            name = "회원가입 요청",
                            value = """
                                    {
                                      "nickname": "hongdae",
                                      "password": "1234"
                                    }
                                    """
                    )
            )
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "회원가입 성공 응답",
                                    value = """
                                            {
                                              "success": true,
                                              "data": {
                                                "userId": 1,
                                                "nickname": "hongdae"
                                              },
                                              "error": null
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "이미 사용 중인 닉네임"
            )
    })
    ResponseEntity<ApiResponse<SignupResponse>> signup(SignupRequest request);

    @Operation(
            summary = "로그인",
            description = "닉네임과 4자리 비밀번호로 로그인합니다. MVP에서는 응답의 userId를 이후 API 요청에 사용합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "로그인 요청 정보",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LoginRequest.class),
                    examples = @ExampleObject(
                            name = "로그인 요청",
                            value = """
                                    {
                                      "nickname": "hongdae",
                                      "password": "1234"
                                    }
                                    """
                    )
            )
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "로그인 성공 응답",
                                    value = """
                                            {
                                              "success": true,
                                              "data": {
                                                "userId": 1,
                                                "nickname": "hongdae"
                                              },
                                              "error": null
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "닉네임 또는 비밀번호 불일치"
            )
    })
    ResponseEntity<ApiResponse<LoginResponse>> login(LoginRequest request);
}
