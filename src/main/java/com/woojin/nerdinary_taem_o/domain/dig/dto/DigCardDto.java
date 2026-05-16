package com.woojin.nerdinary_taem_o.domain.dig.dto;

import com.woojin.nerdinary_taem_o.domain.dig.entity.Dig;

public record DigCardDto(
        Long digId,
        String title,
        String thumbnailUrl
) {

    public static DigCardDto from(Dig dig) {
        return new DigCardDto(
                dig.getId(),
                dig.getSong().getTitle(),
                dig.getSong().getThumbnailUrl()
        );
    }
}
