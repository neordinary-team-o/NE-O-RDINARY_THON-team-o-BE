package com.woojin.nerdinary_taem_o.domain.dig.entity;

import com.woojin.nerdinary_taem_o.common.entity.BaseTimeEntity;
import com.woojin.nerdinary_taem_o.domain.song.entity.Song;
import com.woojin.nerdinary_taem_o.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "digs",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "song_id"}) // 중복 발굴 방지
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Dig extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dugAt;    // 발굴 시각, 서버에서만 세팅

    @Column(length = 100)
    private String comment;         // 한줄 평가 (선택)

    // ── 발굴 당시 스냅샷 ──────────────────────────────
    @Column(nullable = false)
    private Long snapshotViewCount;     // 발굴 당시 조회수

    @Column
    private LocalDate snapshotUploadDate; // 발굴 당시 업로드 날짜

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AchievementBadge achievementBadge;  // nullable, 스케줄러가 채움

    @Column
    private Double recentGrowthRate;    // 발굴 당시 최근 성장률 (%)

    @Column(nullable = false)
    private Integer digScore;           // 0 ~ 100

    @Column
    private LocalDateTime achievedAt;           // nullable

    // ─────────────────────────────────────────────────
    @PrePersist
    private void prePersist() {
        this.dugAt = LocalDateTime.now();   // 서버 타임스탬프 강제
    }

    // 업적 달성 시 스케줄러에서 호출
    public void achieve() {
        this.achievementBadge = AchievementBadge.TRENDSETTER;
        this.achievedAt = LocalDateTime.now();
    }
}