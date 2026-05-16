package com.woojin.nerdinary_taem_o.domain.dig.service;

import com.woojin.nerdinary_taem_o.common.dto.PageResponse;
import com.woojin.nerdinary_taem_o.domain.achievement.entity.Achievement;
import com.woojin.nerdinary_taem_o.domain.achievement.repository.AchievementRepository;
import com.woojin.nerdinary_taem_o.domain.dig.dto.response.DigCardResponse;
import com.woojin.nerdinary_taem_o.domain.dig.entity.Dig;
import com.woojin.nerdinary_taem_o.domain.dig.entity.DigSnapshot;
import com.woojin.nerdinary_taem_o.domain.dig.repository.DigRepository;
import com.woojin.nerdinary_taem_o.domain.dig.repository.DigSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DigCheckService {

    private final DigRepository digRepository;
    private final DigSnapshotRepository digSnapshotRepository;
    private final AchievementRepository achievementRepository;

    public PageResponse<DigCardResponse> getMyDigCards(Long userId, Pageable pageable) {

        // 1. 유저의 모든 DIG 조회 (Song + Artist fetch join)
        List<Dig> digs = digRepository.findAllByUserIdWithSongAndArtist(userId);

        if (digs.isEmpty()) {
            return PageResponse.of(List.of(),
                    pageable.getPageNumber(), pageable.getPageSize(), 0, 0);
        }

        List<Long> digIds = digs.stream()
                .map(Dig::getId)
                .toList();

        // 2. 각 DIG의 최신 스냅샷 조회 → currentViews 매핑
        Map<Long, Long> currentViewsByDigId = digSnapshotRepository
                .findLatestSnapshotsByDigIds(digIds)
                .stream()
                .collect(Collectors.toMap(
                        ds -> ds.getDig().getId(),
                        DigSnapshot::getCurrentViews
                ));

        // 3. 업적 조회 → digId 기준 매핑
        Map<Long, Achievement> achievementByDigId = achievementRepository
                .findByDigIdIn(digIds)
                .stream()
                .collect(Collectors.toMap(
                        a -> a.getDig().getId(),
                        a -> a
                ));

        // 4. DTO 변환 + growthRate 계산 + 성장률 내림차순 정렬
        List<DigCardResponse> sortedCards = digs.stream()
                .map(dig -> DigCardResponse.of(
                        dig,
                        currentViewsByDigId.get(dig.getId()),
                        achievementByDigId.get(dig.getId())
                ))
                .sorted(Comparator.comparingDouble(DigCardResponse::growthRate).reversed())
                .toList();

        // 5. 인메모리 페이지네이션
        int total = sortedCards.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);

        List<DigCardResponse> pagedContent = (start >= total)
                ? List.of()
                : sortedCards.subList(start, end);

        int totalPages = (int) Math.ceil((double) total / pageable.getPageSize());

        return PageResponse.of(pagedContent,
                pageable.getPageNumber(), pageable.getPageSize(), total, totalPages);
    }
}