package com.woojin.nerdinary_taem_o.domain.song.service;

import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;
import com.woojin.nerdinary_taem_o.common.exception.model.BusinessException;
import com.woojin.nerdinary_taem_o.domain.song.client.GeminiMetadataClient;
import com.woojin.nerdinary_taem_o.domain.song.client.YoutubeClient;
import com.woojin.nerdinary_taem_o.domain.song.dto.SongSearchRequest;
import com.woojin.nerdinary_taem_o.domain.song.dto.SongSearchResponse;
import com.woojin.nerdinary_taem_o.domain.song.dto.SongSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SongSearchService {

    private static final int SEARCH_RESULT_LIMIT = 1;

    private final YoutubeClient youtubeClient;
    private final GeminiMetadataClient geminiMetadataClient;

    public SongSearchResponse search(SongSearchRequest request) {
        validate(request);

        List<YoutubeClient.YoutubeSearchItem> youtubeResults =
                youtubeClient.searchTopMusicVideos(request.keyword().trim(), SEARCH_RESULT_LIMIT);
        Map<String, Long> viewCounts = youtubeClient.fetchViewCounts(
                youtubeResults.stream().map(YoutubeClient.YoutubeSearchItem::videoId).toList()
        );

        YoutubeClient.YoutubeSearchItem item = youtubeResults.get(0);
        GeminiMetadataClient.RefinedMetadata metadata =
                geminiMetadataClient.refine(item.rawTitle(), item.channelTitle());
        SongSearchResult result = new SongSearchResult(
                item.videoId(),
                metadata.title(),
                metadata.artist(),
                viewCounts.getOrDefault(item.videoId(), 0L),
                item.uploadDate(),
                item.thumbnailUrl()
        );

        return SongSearchResponse.from(result);
    }

    private void validate(SongSearchRequest request) {
        if (request == null || request.keyword() == null || request.keyword().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "검색어를 입력해주세요");
        }
    }
}
