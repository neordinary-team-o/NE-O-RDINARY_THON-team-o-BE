package com.woojin.nerdinary_taem_o.domain.dig.repository;

import com.woojin.nerdinary_taem_o.domain.dig.entity.Dig;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DigRepository extends JpaRepository<Dig, Long> {

    boolean existsByUserIdAndSongId(Long userId, Long songId);

    @Query("SELECT d FROM Dig d JOIN FETCH d.song s JOIN FETCH s.artist WHERE d.user.id = :userId ORDER BY d.dugAt DESC")
    Page<Dig> findMyDigsWithSong(@Param("userId") Long userId, Pageable pageable);

    boolean existsByUser_IdAndSong_Id(Long userId, Long songId);

    @Query("SELECT d FROM Dig d JOIN FETCH d.song s JOIN FETCH s.artist WHERE d.achievementBadge IS NULL")
    List<Dig> findAllPendingAchievement();

    @Query("SELECT d FROM Dig d JOIN FETCH d.song s JOIN FETCH s.artist WHERE d.id = :digId")
    Optional<Dig> findByIdWithSong(@Param("digId") Long digId);

    @Query("""
        SELECT d FROM Dig d
        JOIN FETCH d.song s
        JOIN FETCH s.artist
        WHERE d.user.id = :userId
        AND LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
        ORDER BY d.dugAt DESC
        """)
    List<Dig> searchMyDigsByTitle(@Param("userId") Long userId, @Param("keyword") String keyword);

    // DigRepository.java 에 추가
    @Query("""
        SELECT d FROM Dig d
        JOIN FETCH d.song s
        JOIN FETCH s.artist
        WHERE d.user.id = :userId
        """)
    List<Dig> findAllByUserIdWithSong(@Param("userId") Long userId);
}
