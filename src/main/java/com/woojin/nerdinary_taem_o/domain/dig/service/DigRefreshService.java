package com.woojin.nerdinary_taem_o.domain.dig.service;

import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;
import com.woojin.nerdinary_taem_o.common.exception.model.BusinessException;
import com.woojin.nerdinary_taem_o.common.exception.model.EntityNotFoundException;
import com.woojin.nerdinary_taem_o.domain.dig.entity.Dig;
import com.woojin.nerdinary_taem_o.domain.dig.repository.DigRepository;
import com.woojin.nerdinary_taem_o.domain.song.client.YoutubeClient;
import com.woojin.nerdinary_taem_o.domain.song.entity.Song;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DigRefreshService {

    private final DigRepository digRepository;
    private final YoutubeClient youtubeClient;

    @Transactional
    public void refreshGrowthRate(Long digId) {
        Dig dig = digRepository.findByIdWithSong(digId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.DIG_NOT_FOUND));

        Long currentViewCount = fetchCurrentViewCount(dig.getVideoId());
        updateSongViewCount(dig.getSong(), currentViewCount);
        updateDigGrowthRate(dig, currentViewCount);
    }

    private Long fetchCurrentViewCount(String youtubeId) {
        if (youtubeId == null || youtubeId.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "유튜브 ID가 없습니다.");
        }

        Map<String, Long> viewCounts = youtubeClient.fetchViewCounts(List.of(youtubeId));

        if (!viewCounts.containsKey(youtubeId)) {
            throw new BusinessException(ErrorCode.EXTERNAL_API_FAILED, "조회수를 가져올 수 없습니다.");
        }

        return viewCounts.get(youtubeId);
    }

    private void updateSongViewCount(Song song, Long currentViewCount) {
        song.updateCurrentViewCount(currentViewCount);
    }

    private void updateDigGrowthRate(Dig dig, Long currentViewCount) {
        double growthRate = calculateGrowthRate(dig.getSnapshotViewCount(), currentViewCount);
        dig.updateGrowthRate(growthRate);
    }

    private double calculateGrowthRate(long snapshot, long current) {
        if (snapshot == 0) return 0;
        return Math.round((current - snapshot) * 1000.0 / snapshot) / 10.0;
    }
}
