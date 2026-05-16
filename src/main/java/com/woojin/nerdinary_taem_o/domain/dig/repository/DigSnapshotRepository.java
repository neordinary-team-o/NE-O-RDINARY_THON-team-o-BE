package com.woojin.nerdinary_taem_o.domain.dig.repository;

import com.woojin.nerdinary_taem_o.domain.dig.entity.DigSnapshot;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DigSnapshotRepository extends JpaRepository<DigSnapshot, Long> {

    //   각 Dig의 가장 최신 스냅샷만 조회, 서브쿼리로 MAX값 필터링
    @Query("""
            SELECT ds FROM DigSnapshot ds
            WHERE ds.dig.id IN :digIds
              AND ds.snapshotDate = (
                  SELECT MAX(ds2.snapshotDate)
                  FROM DigSnapshot ds2
                  WHERE ds2.dig.id = ds.dig.id
              )
            """)
    List<DigSnapshot> findLatestSnapshotsByDigIds(@Param("digIds") List<Long> digIds);
}
