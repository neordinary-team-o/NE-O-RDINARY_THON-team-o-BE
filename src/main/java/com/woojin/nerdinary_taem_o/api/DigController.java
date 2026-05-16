package com.woojin.nerdinary_taem_o.api;

import com.woojin.nerdinary_taem_o.common.dto.ApiResponse;
import com.woojin.nerdinary_taem_o.common.dto.PageResponse;
import com.woojin.nerdinary_taem_o.domain.dig.dto.CreateDigRequest;
import com.woojin.nerdinary_taem_o.domain.dig.dto.CreateDigResponse;
import com.woojin.nerdinary_taem_o.domain.dig.dto.response.DigCardResponse;
import com.woojin.nerdinary_taem_o.domain.dig.service.DigCheckService;
import com.woojin.nerdinary_taem_o.domain.dig.service.DigService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/digs")
@RequiredArgsConstructor
public class DigController {

    private final DigService digService;
    private final DigCheckService digCheckService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateDigResponse>> create(@RequestBody CreateDigRequest request) {
        CreateDigResponse response = digService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    /**
     * GET /api/v1/digs/my
     * 내 DIG 카드 목록 - 성장률 내림차순
     * userId는 JWT 토큰에서 추출 (SecurityContext)
     */
    @GetMapping("/my")
    public ResponseEntity<PageResponse<DigCardResponse>> getMyDigCards(
            @RequestParam Long userId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(digCheckService.getMyDigCards(userId, pageable));
    }
}
