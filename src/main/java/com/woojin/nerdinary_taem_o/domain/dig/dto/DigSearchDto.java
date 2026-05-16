package com.woojin.nerdinary_taem_o.domain.dig.dto;

import com.woojin.nerdinary_taem_o.domain.dig.entity.Dig;

public record DigSearchDto(
        Long digId,
        String thumbnailUrl,
        String title
) {
    public static DigSearchDto from(Dig dig) {
        return new DigSearchDto(
                dig.getId(),
                dig.getSong().getThumbnailUrl(),
                dig.getSong().getTitle()
        );
    }
}