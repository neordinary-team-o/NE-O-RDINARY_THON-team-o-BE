package com.woojin.nerdinary_taem_o.api;

import com.woojin.nerdinary_taem_o.api.docs.SongControllerDocs;
import com.woojin.nerdinary_taem_o.common.dto.ApiResponse;
import com.woojin.nerdinary_taem_o.domain.song.dto.SongSearchRequest;
import com.woojin.nerdinary_taem_o.domain.song.dto.SongSearchResponse;
import com.woojin.nerdinary_taem_o.domain.song.service.SongSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class SongController implements SongControllerDocs {

    private final SongSearchService songSearchService;

    @Override
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<SongSearchResponse>> search(@RequestBody SongSearchRequest request) {
        SongSearchResponse response = songSearchService.search(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
