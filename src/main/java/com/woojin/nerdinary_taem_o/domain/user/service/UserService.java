package com.woojin.nerdinary_taem_o.domain.user.service;

import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;
import com.woojin.nerdinary_taem_o.common.exception.model.BusinessException;
import com.woojin.nerdinary_taem_o.common.exception.model.DuplicateResourceException;
import com.woojin.nerdinary_taem_o.domain.user.dto.LoginRequest;
import com.woojin.nerdinary_taem_o.domain.user.dto.LoginResponse;
import com.woojin.nerdinary_taem_o.domain.user.dto.SignupRequest;
import com.woojin.nerdinary_taem_o.domain.user.dto.SignupResponse;
import com.woojin.nerdinary_taem_o.domain.user.entity.User;
import com.woojin.nerdinary_taem_o.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByNickname(request.nickname())) {
            throw new DuplicateResourceException(ErrorCode.DUPLICATE_NICKNAME);
        }

        User user = User.builder()
                .nickname(request.nickname())
                .password(request.password())
                .build();

        User saved = userRepository.save(user);
        return new SignupResponse(saved.getId(), saved.getNickname());
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByNickname(request.nickname())
                .filter(u -> u.getPassword().equals(request.password()))
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAILED));

        return new LoginResponse(user.getId(), user.getNickname());
    }
}
