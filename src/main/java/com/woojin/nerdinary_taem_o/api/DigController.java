package com.woojin.nerdinary_taem_o.api;

import com.woojin.nerdinary_taem_o.api.docs.DigControllerDocs;
import com.woojin.nerdinary_taem_o.common.dto.ApiResponse;
import com.woojin.nerdinary_taem_o.domain.dig.dto.DigCreateRequest;
import com.woojin.nerdinary_taem_o.domain.dig.dto.DigCreateResponse;
import com.woojin.nerdinary_taem_o.domain.dig.service.DigRefreshService;
import com.woojin.nerdinary_taem_o.domain.dig.service.DigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/digs")
@RequiredArgsConstructor
public class DigController implements DigControllerDocs {

    private final DigService digService;
    private final DigRefreshService digRefreshService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<DigCreateResponse>> create(@Valid @RequestBody DigCreateRequest request) {
        DigCreateResponse response = digService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @Override
    @PatchMapping("/{digId}/growth-rate")
    public ResponseEntity<ApiResponse<Void>> refreshGrowthRate(
            @PathVariable Long digId
    ) {
        digRefreshService.refreshGrowthRate(digId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
