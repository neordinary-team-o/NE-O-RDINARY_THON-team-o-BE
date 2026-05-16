package com.woojin.nerdinary_taem_o.domain.song.entity;

import com.woojin.nerdinary_taem_o.common.entity.BaseTimeEntity;
import com.woojin.nerdinary_taem_o.common.enums.Platform;
import com.woojin.nerdinary_taem_o.domain.artist.entity.Artist;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Column(nullable = false)
    private String title;

    private String externalUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Platform platform;

    private LocalDate uploadDate;
}
