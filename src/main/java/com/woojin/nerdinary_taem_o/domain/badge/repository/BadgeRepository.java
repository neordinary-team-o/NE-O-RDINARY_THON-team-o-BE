package com.woojin.nerdinary_taem_o.domain.badge.repository;

import com.woojin.nerdinary_taem_o.domain.badge.entity.Badge;
import com.woojin.nerdinary_taem_o.domain.badge.enums.BadgeType;
import com.woojin.nerdinary_taem_o.domain.dig.entity.Dig;
import com.woojin.nerdinary_taem_o.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

    List<Badge> findByUser(User user);

    List<Badge> findByDig(Dig dig);

    boolean existsByDigAndBadgeType(Dig dig, BadgeType badgeType);
}
