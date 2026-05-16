package com.woojin.nerdinary_taem_o.domain.song.entity;

import com.woojin.nerdinary_taem_o.common.entity.BaseTimeEntity;
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

    // 현재 조회수 업데이트 (스케줄러 전용)
    public void updateCurrentViewCount(Long viewCount) {
        this.currentViewCount = viewCount;
    }
}