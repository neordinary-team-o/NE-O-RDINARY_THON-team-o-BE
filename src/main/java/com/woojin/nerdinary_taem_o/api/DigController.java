package com.woojin.nerdinary_taem_o.api;

import com.woojin.nerdinary_taem_o.common.dto.ApiResponse;
import com.woojin.nerdinary_taem_o.domain.dig.dto.DigCreateRequest;
import com.woojin.nerdinary_taem_o.domain.dig.dto.DigCreateResponse;
import com.woojin.nerdinary_taem_o.domain.dig.service.DigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/digs")
@RequiredArgsConstructor
public class DigController {

    private final DigService digService;

    @PostMapping
    public ResponseEntity<ApiResponse<DigCreateResponse>> create(@RequestBody DigCreateRequest request) {
        DigCreateResponse response = digService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }
}
