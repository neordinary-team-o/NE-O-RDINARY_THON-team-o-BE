package com.woojin.nerdinary_taem_o.domain.dig.service;

import com.woojin.nerdinary_taem_o.common.dto.PageResponse;
import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;
import com.woojin.nerdinary_taem_o.common.exception.model.EntityNotFoundException;
import com.woojin.nerdinary_taem_o.domain.dig.dto.DigCardDto;
import com.woojin.nerdinary_taem_o.domain.dig.dto.DigDetailDto;
import com.woojin.nerdinary_taem_o.domain.dig.dto.DigSearchDto;
import com.woojin.nerdinary_taem_o.domain.dig.entity.AchievementBadge;
import com.woojin.nerdinary_taem_o.domain.dig.entity.Dig;
import com.woojin.nerdinary_taem_o.domain.dig.repository.DigRepository;
import com.woojin.nerdinary_taem_o.domain.song.entity.Song;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DigSearchService {

    private static final int HOME_PAGE_SIZE = 6;

    private final DigRepository digRepository;

    @Transactional(readOnly = true)
    public PageResponse<DigCardDto> getMyDigs(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, HOME_PAGE_SIZE);
        Page<Dig> digPage = digRepository.findMyDigsWithSong(userId, pageable);

        List<DigCardDto> content = digPage.getContent()
                .stream()
                .map(DigCardDto::from)
                .toList();

        return PageResponse.of(digPage, content);
    }

    @Transactional(readOnly = true)
    public DigDetailDto getDigDetail(Long digId) {
        Dig dig = digRepository.findByIdWithSong(digId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.DIG_NOT_FOUND));

        Song song = dig.getSong();
        long snapshot = dig.getSnapshotViewCount();
        long current = song.getCurrentViewCount();
        long elapsed = calculateElapsedMonths(dig.getDugAt());
        double growthRate = calculateGrowthRate(snapshot, current);

        return new DigDetailDto(
                song.getThumbnailUrl(),
                song.getTitle(),
                song.getArtist().getName(),
                formatDigDate(dig.getDugAt()),
                elapsed,
                resolveAchievementName(dig.getAchievementBadge()),
                snapshot,
                current,
                growthRate,
                buildNarrative(snapshot, current, elapsed)
        );
    }

    @Transactional(readOnly = true)
    public List<DigSearchDto> searchMyDigs(Long userId, String keyword) {
        return digRepository.searchMyDigsByTitle(userId, keyword)
                .stream()
                .map(DigSearchDto::from)
                .toList();
    }

    private long calculateElapsedMonths(LocalDateTime dugAt) {
        return ChronoUnit.MONTHS.between(dugAt, LocalDateTime.now());
    }

    private double calculateGrowthRate(long snapshot, long current) {
        if (snapshot == 0) return 0;
        return Math.round((current - snapshot) * 1000.0 / snapshot) / 10.0;
    }

    private String formatDigDate(LocalDateTime dugAt) {
        return dugAt.format(DateTimeFormatter.ofPattern("yy.MM.dd"));
    }

    private String resolveAchievementName(AchievementBadge badge) {
        return badge != null ? badge.name() : null;
    }

    private String buildNarrative(long snapshot, long current, long elapsed) {
        return String.format(
                "당신이 %,d명이 듣던 시절 발견한 음악이 지금 %,d만명에게 재생되고 있습니다. " +
                        "당신의 귀는 시대보다 %d개월 빨랐습니다.",
                snapshot, current / 10000, elapsed
        );
    }
}
