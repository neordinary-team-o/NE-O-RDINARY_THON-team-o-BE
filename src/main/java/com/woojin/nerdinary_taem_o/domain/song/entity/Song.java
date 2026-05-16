package com.woojin.nerdinary_taem_o.domain.song.entity;

import com.woojin.nerdinary_taem_o.common.entity.BaseTimeEntity;
import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;
import com.woojin.nerdinary_taem_o.common.exception.model.BusinessException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "songs")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Song extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Column
    private LocalDate uploadDate;   // YouTube 업로드 날짜 (DIG SCORE 계산용)

    @Column
    private Long currentViewCount;  // 스케줄러가 주기적으로 업데이트

    public static Song create(String title, String thumbnailUrl,
                              Artist artist, LocalDate uploadDate,
                              Long currentViewCount) {
        return new Song(title, thumbnailUrl, artist, uploadDate, currentViewCount);
    }

    private Song(String title, String thumbnailUrl, Artist artist,
                 LocalDate uploadDate, Long currentViewCount) {

        validate(title, artist, currentViewCount);
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.artist = artist;
        this.uploadDate = uploadDate;
        this.currentViewCount = currentViewCount;
    }

    private void validate(String title, Artist artist, Long currentViewCount) {
        if (title == null || title.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,
                    "곡 제목은 필수입니다.");
        }
        if (artist == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,
                    "아티스트 정보는 필수입니다.");
        }
        if (currentViewCount != null && currentViewCount < 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,
                    "조회수는 0 이상이어야 합니다.");
        }
    }

    public void updateCurrentViewCount(Long viewCount) {
        if (viewCount < 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,
                    "조회수는 0 이상이어야 합니다.");
        }
        this.currentViewCount = viewCount;
    }
}
