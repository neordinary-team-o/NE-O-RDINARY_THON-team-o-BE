package com.woojin.nerdinary_taem_o.domain.song.service;

import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;
import com.woojin.nerdinary_taem_o.common.exception.model.BusinessException;
import com.woojin.nerdinary_taem_o.domain.song.client.GeminiMetadataClient;
import com.woojin.nerdinary_taem_o.domain.song.client.YoutubeClient;
import com.woojin.nerdinary_taem_o.domain.song.dto.SongSearchRequest;
import com.woojin.nerdinary_taem_o.domain.song.dto.SongSearchResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SongSearchServiceTests {

    @Mock
    private YoutubeClient youtubeClient;

    @Mock
    private GeminiMetadataClient geminiMetadataClient;

    @InjectMocks
    private SongSearchService songSearchService;

    @Test
    void searchReturnsTopSongWithMetadataViewCountAndThumbnail() {
        List<YoutubeClient.YoutubeSearchItem> youtubeItems = List.of(
                new YoutubeClient.YoutubeSearchItem(
                        "video1",
                        "raw title 1",
                        "channel",
                        LocalDate.of(2024, 3, 1),
                        "https://i.ytimg.com/vi/video1/hqdefault.jpg"
                )
        );
        when(youtubeClient.searchTopMusicVideos("한로로 0+0", 1)).thenReturn(youtubeItems);
        when(youtubeClient.fetchViewCounts(List.of("video1"))).thenReturn(Map.of("video1", 100L));
        when(geminiMetadataClient.refine(eq("raw title 1"), eq("channel")))
                .thenReturn(new GeminiMetadataClient.RefinedMetadata("artist 1", "title 1"));

        SongSearchResponse response = songSearchService.search(new SongSearchRequest(" 한로로 0+0 "));

        assertThat(response.videoId()).isEqualTo("video1");
        assertThat(response.title()).isEqualTo("title 1");
        assertThat(response.artist()).isEqualTo("artist 1");
        assertThat(response.viewCount()).isEqualTo(100L);
        assertThat(response.uploadDate()).isEqualTo(LocalDate.of(2024, 3, 1));
        assertThat(response.thumbnailUrl()).isEqualTo("https://i.ytimg.com/vi/video1/hqdefault.jpg");
        verify(youtubeClient).searchTopMusicVideos("한로로 0+0", 1);
    }

    @Test
    void searchRejectsBlankKeyword() {
        assertThatThrownBy(() -> songSearchService.search(new SongSearchRequest(" ")))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_INPUT);

        verifyNoInteractions(youtubeClient, geminiMetadataClient);
    }
}
