package com.woojin.nerdinary_taem_o.domain.dig.dto.response;

import com.woojin.nerdinary_taem_o.domain.achievement.entity.Achievement;
import com.woojin.nerdinary_taem_o.domain.dig.entity.Dig;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public record DigCardResponse(Long digId,
                              String songTitle,
                              String artistName,
                              LocalDate digDate,
                              Long viewsAtDig,
                              Long currentViews,
                              Double growthRate,
                              String rarityBadge,
                              String comment,
                              boolean hasAchievement,
                              String achievementName) {

    public static DigCardResponse of(Dig dig, Long currentViews, Achievement achievement) {
        long views = dig.getViewsAtDig();
        double growthRate = (currentViews != null && views > 0)
                ? (double) (currentViews - views) / views * 100.0
                : 0.0;

        return DigCardResponse.builder()
                .digId(dig.getId())
                .songTitle(dig.getSong().getTitle())
                .artistName(dig.getSong().getArtist().getName())
                .digDate(dig.getDigDate().toLocalDate())
                .viewsAtDig(views)
                .currentViews(currentViews != null ? currentViews : views)
                .growthRate(growthRate)
                .rarityBadge(dig.getRarityBadge().name())
                .comment(dig.getComment())
                .hasAchievement(achievement != null)
                .achievementName(achievement != null ? achievement.getName().name() : null)
                .build();
    }
}
