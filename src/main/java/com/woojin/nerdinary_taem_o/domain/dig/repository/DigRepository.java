package com.woojin.nerdinary_taem_o.domain.dig.repository;

import com.woojin.nerdinary_taem_o.domain.dig.entity.Dig;
import com.woojin.nerdinary_taem_o.domain.track.entity.Track;
import com.woojin.nerdinary_taem_o.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DigRepository extends JpaRepository<Dig, Long> {

    boolean existsByUserAndTrack(User user, Track track);

    Optional<Dig> findByUserAndTrack(User user, Track track);

    List<Dig> findByUserOrderByDugAtDesc(User user);

    List<Dig> findByUserOrderByDugAtAsc(User user);
}
