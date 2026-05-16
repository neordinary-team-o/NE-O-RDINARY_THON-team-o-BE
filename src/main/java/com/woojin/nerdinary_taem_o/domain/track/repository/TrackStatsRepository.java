package com.woojin.nerdinary_taem_o.domain.track.repository;

import com.woojin.nerdinary_taem_o.domain.track.entity.Track;
import com.woojin.nerdinary_taem_o.domain.track.entity.TrackStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackStatsRepository extends JpaRepository<TrackStats, Long> {

    Optional<TrackStats> findTopByTrackOrderByCreatedAtDesc(Track track);
}
