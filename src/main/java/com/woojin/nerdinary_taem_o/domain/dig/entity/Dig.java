package com.woojin.nerdinary_taem_o.domain.dig.entity;

import com.woojin.nerdinary_taem_o.common.entity.BaseTimeEntity;
import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;
import com.woojin.nerdinary_taem_o.common.exception.model.BusinessException;
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

    @Column(name = "youtube_video_id", nullable = false)
    private String videoId;

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

    public static Dig create(User user, Song song, Long snapshotViewCount,
                             LocalDate snapshotUploadDate, Double recentGrowthRate,
                             Integer digScore, String comment, String videoId) {
        return new Dig(user, song, snapshotViewCount, snapshotUploadDate,
                recentGrowthRate, digScore, comment, videoId);
    }

    private Dig(User user, Song song, Long snapshotViewCount,
                LocalDate snapshotUploadDate, Double recentGrowthRate,
                Integer digScore, String comment, String videoId) {

        validate(user, song, snapshotViewCount, digScore, comment, videoId);
        this.user = user;
        this.song = song;
        this.snapshotViewCount = snapshotViewCount;
        this.snapshotUploadDate = snapshotUploadDate;
        this.recentGrowthRate = recentGrowthRate;
        this.digScore = digScore;
        this.comment = comment;
        this.videoId = videoId;
    }

    public void updateGrowthRate(Double growthRate) {
        if (growthRate < 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "성장률은 0 이상이어야 합니다.");
        }
        this.recentGrowthRate = growthRate;
    }
    @PrePersist
    private void prePersist() {
        this.dugAt = LocalDateTime.now();
    }

    public void achieve() {
        this.achievementBadge = AchievementBadge.TRENDSETTER;
        this.achievedAt = LocalDateTime.now();
    }

    private void validate(User user, Song song, Long snapshotViewCount,
                                 Integer digScore, String comment, String videoId) {
        if (user == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,
                    "유저 정보는 필수입니다.");
        }
        if (song == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,
                    "곡 정보는 필수입니다.");
        }
        if (snapshotViewCount == null || snapshotViewCount < 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,
                    "발굴 당시 조회수는 0 이상이어야 합니다.");
        }
        if (digScore == null || digScore < 0 || digScore > 100) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,
                    "DIG SCORE는 0~100 사이여야 합니다.");
        }
        if (comment != null && comment.length() > 100) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,
                    "한줄 평가는 100자 이하여야 합니다.");
        }
        if (videoId == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,
                    "유튜브 영상의 고유 id는 필수입니다.");
        }
    }
}