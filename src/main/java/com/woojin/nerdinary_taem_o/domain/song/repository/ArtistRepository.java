package com.woojin.nerdinary_taem_o.domain.song.repository;

import com.woojin.nerdinary_taem_o.domain.song.entity.Artist;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    Optional<Artist> findByName(String name);
}
