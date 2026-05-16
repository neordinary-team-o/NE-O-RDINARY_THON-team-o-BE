package com.woojin.nerdinary_taem_o.domain.dig.entity;

import com.woojin.nerdinary_taem_o.common.converter.DigSnapshotConverter;
import com.woojin.nerdinary_taem_o.common.entity.BaseTimeEntity;
import com.woojin.nerdinary_taem_o.domain.track.entity.Track;
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
    uniqueConstraints = @UniqueConstraint(name = "uq_user_track", columnNames = {"user_id", "track_id"})
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
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    // 서버 타임스탬프 — 클라이언트 값 사용 금지
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dugAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DigSource source;

    @Column(length = 100)
    private String comment;

    @Convert(converter = DigSnapshotConverter.class)
    @Column(nullable = false, columnDefinition = "TEXT")
    private DigSnapshot snapshot;
}
