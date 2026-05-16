package com.woojin.nerdinary_taem_o.domain.achievement.repository;

import com.woojin.nerdinary_taem_o.domain.achievement.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    @Query("SELECT a FROM Achievement a WHERE a.dig.id IN :digIds")
    List<Achievement> findByDigIdIn(@Param("digIds") List<Long> digIds);
}