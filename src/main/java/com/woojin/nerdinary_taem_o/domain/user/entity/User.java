package com.woojin.nerdinary_taem_o.domain.user.entity;

import com.woojin.nerdinary_taem_o.common.entity.BaseTimeEntity;
import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;
import com.woojin.nerdinary_taem_o.common.exception.model.BusinessException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(nullable = false, length = 4)
    private String password;

    public static User create(String nickname, String password) {
        return new User(nickname, password);
    }

    private User(String nickname, String password) {
        validate(nickname, password);
        this.nickname = nickname;
        this.password = password;
    }

    private void validate(String nickname, String password) {
        if (nickname == null || nickname.isBlank() || password == null || password.isBlank()) {
            throw new BusinessException(ErrorCode.LOGIN_INVALID_INPUT,
                    ErrorCode.LOGIN_INVALID_INPUT.getMessage());
        }
    }
}
