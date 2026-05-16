package com.woojin.nerdinary_taem_o.domain.dig.service;

import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;
import com.woojin.nerdinary_taem_o.common.exception.model.BusinessException;
import com.woojin.nerdinary_taem_o.domain.dig.repository.DigRepository;
import com.woojin.nerdinary_taem_o.domain.song.client.YoutubeClient;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DigRefreshServiceTests {

    @Mock
    private DigRepository digRepository;

    @Mock
    private YoutubeClient youtubeClient;

    @InjectMocks
    private DigRefreshService digRefreshService;

    @Test
    void refreshGrowthRateThrowsDigNotFoundWhenMissing() {
        when(digRepository.findByIdWithSong(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> digRefreshService.refreshGrowthRate(99L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.DIG_NOT_FOUND);
    }
}
