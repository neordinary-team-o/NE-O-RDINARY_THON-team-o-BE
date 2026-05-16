package com.woojin.nerdinary_taem_o.domain.achievement.entity;

import com.woojin.nerdinary_taem_o.common.entity.BaseTimeEntity;
import com.woojin.nerdinary_taem_o.domain.achievement.enums.AchievementName;
import com.woojin.nerdinary_taem_o.domain.dig.entity.Dig;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "achievements")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Achievement extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dig_id", nullable = false, unique = true)
    private Dig dig;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AchievementName name;

    @Column(nullable = false)
    private Integer growthMultiplier;

    @Column(nullable = false)
    private LocalDateTime achievedAt;
}
