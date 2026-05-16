package com.woojin.nerdinary_taem_o.domain.user.repository;

import com.woojin.nerdinary_taem_o.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickname(String nickname);

    boolean existsByNickname(String nickname);
}
