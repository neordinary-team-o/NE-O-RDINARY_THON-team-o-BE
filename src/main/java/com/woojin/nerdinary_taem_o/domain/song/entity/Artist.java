package com.woojin.nerdinary_taem_o.domain.song.entity;

import com.woojin.nerdinary_taem_o.common.entity.BaseTimeEntity;
import com.woojin.nerdinary_taem_o.common.exception.ErrorCode;
import com.woojin.nerdinary_taem_o.common.exception.model.BusinessException;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "artists")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Artist extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String profileImageUrl;

    public static Artist create(String name, String profileImageUrl) {
        return new Artist(name, profileImageUrl);
    }

    private Artist(String name, String profileImageUrl) {
        validate(name);
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    private void validate(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT,
                    "아티스트 이름은 필수입니다.");
        }
    }
}