package com.woojin.nerdinary_taem_o.domain.artist.entity;

import com.woojin.nerdinary_taem_o.common.entity.BaseTimeEntity;
import com.woojin.nerdinary_taem_o.common.enums.Platform;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "artists")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Artist extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String externalId;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Platform platform;
}
