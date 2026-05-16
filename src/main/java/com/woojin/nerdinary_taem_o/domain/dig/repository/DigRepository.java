package com.woojin.nerdinary_taem_o.domain.dig.repository;

import com.woojin.nerdinary_taem_o.domain.dig.entity.Dig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DigRepository extends JpaRepository<Dig, Long> {

    boolean existsByUserIdAndSongId(Long userId, Long songId);
}
