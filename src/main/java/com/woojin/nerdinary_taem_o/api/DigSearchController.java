package com.woojin.nerdinary_taem_o.api;

import com.woojin.nerdinary_taem_o.api.docs.DigSearchControllerDocs;
import com.woojin.nerdinary_taem_o.common.dto.ApiResponse;
import com.woojin.nerdinary_taem_o.common.dto.PageResponse;
import com.woojin.nerdinary_taem_o.domain.dig.dto.DigCardDto;
import com.woojin.nerdinary_taem_o.domain.dig.dto.DigDetailDto;
import com.woojin.nerdinary_taem_o.domain.dig.dto.DigSearchDto;
import com.woojin.nerdinary_taem_o.domain.dig.service.DigSearchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/digs")
@RequiredArgsConstructor
public class DigSearchController implements DigSearchControllerDocs {

    private final DigSearchService digService;

    // 홈 화면 - 내 발굴 목록 (6개씩 페이징)
    @Override
    @GetMapping()
    public ResponseEntity<ApiResponse<PageResponse<DigCardDto>>> getMyDigs(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(digService.getMyDigs(userId, page))
        );
    }

    // 발굴 성공 상세 화면
    @Override
    @GetMapping("/{digId}")
    public ResponseEntity<ApiResponse<DigDetailDto>> getDigDetail(
            @PathVariable Long digId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(digService.getDigDetail(digId))
        );
    }

    @Override
    @GetMapping("/me/search")
    public ResponseEntity<ApiResponse<List<DigSearchDto>>> searchMyDigs(
            @RequestParam Long userId,
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(digService.searchMyDigs(userId, keyword))
        );
    }
}
