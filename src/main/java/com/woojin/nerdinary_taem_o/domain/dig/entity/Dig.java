package com.woojin.nerdinary_taem_o.domain.dig.entity;

import com.woojin.nerdinary_taem_o.common.entity.BaseTimeEntity;
import com.woojin.nerdinary_taem_o.domain.dig.enums.RarityBadge;
import com.woojin.nerdinary_taem_o.domain.song.entity.Song;
import com.woojin.nerdinary_taem_o.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "digs",
    uniqueConstraints = @UniqueConstraint(name = "uq_user_song", columnNames = {"user_id", "song_id"})
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

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime digDate;

    @Column(nullable = false)
    private Long viewsAtDig;

    @Column(nullable = false)
    private Integer daysSinceUpload;

    private Double growthRateAtDig;

    @Column(nullable = false)
    private Integer digScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private RarityBadge rarityBadge;

    @Column(length = 100)
    private String comment;
}
