package com.woojin.nerdinary_taem_o.domain.track.repository;

import com.woojin.nerdinary_taem_o.domain.track.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackRepository extends JpaRepository<Track, Long> {

    Optional<Track> findBySpotifyId(String spotifyId);

    Optional<Track> findByYoutubeId(String youtubeId);
}
