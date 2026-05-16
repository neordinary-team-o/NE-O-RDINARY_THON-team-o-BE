package com.woojin.nerdinary_taem_o.api.dig;

import com.woojin.nerdinary_taem_o.common.dto.PageResponse;
import com.woojin.nerdinary_taem_o.domain.dig.dto.response.DigCardResponse;
import com.woojin.nerdinary_taem_o.domain.dig.service.DigService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/digs")
@RequiredArgsConstructor
public class DigController {

    private final DigService digService;

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
        return ResponseEntity.ok(digService.getMyDigCards(userId, pageable));
    }
}