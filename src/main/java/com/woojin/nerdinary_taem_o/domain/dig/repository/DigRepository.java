package com.woojin.nerdinary_taem_o.domain.dig.repository;

import com.woojin.nerdinary_taem_o.domain.dig.entity.Dig;
import com.woojin.nerdinary_taem_o.domain.song.entity.Song;
import com.woojin.nerdinary_taem_o.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DigRepository extends JpaRepository<Dig, Long> {

    boolean existsByUserAndSong(User user, Song song);

    Optional<Dig> findByUserAndSong(User user, Song song);

    List<Dig> findByUserOrderByDigDateDesc(User user);

    List<Dig> findByUserOrderByDigDateAsc(User user);

    @Query("""
            SELECT d FROM Dig d
            JOIN FETCH d.song s
            JOIN FETCH s.artist a
            WHERE d.user.id = :userId
            """)
    List<Dig> findAllByUserIdWithSongAndArtist(@Param("userId") Long userId);
}
