package com.woojin.nerdinary_taem_o.domain.artist.repository;

import com.woojin.nerdinary_taem_o.common.enums.Platform;
import com.woojin.nerdinary_taem_o.domain.artist.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    Optional<Artist> findByNameAndPlatform(String name, Platform platform);
}
