package com.woojin.nerdinary_taem_o.domain.dig.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateDigResponse(
        Long digId,
        Long songId,
        String title,
        String artist,
        Long viewsAtDig,
        LocalDate uploadDate,
        LocalDateTime digDate,
        String comment
) {
}
