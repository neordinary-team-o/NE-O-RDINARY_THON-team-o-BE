package com.woojin.nerdinary_taem_o.domain.badge.enums;

public enum BadgeType {
    // 희귀도 뱃지 (DIG 즉시)
    SURFACE, DRIFT, RARE, OBSCURE, RELIC,
    // 업적 뱃지 (스케줄러)
    RISING_PICK, HIDDEN_FINDER, TREND_CATCHER, SCENE_PREDICTOR, LEGEND_FINDER;

    public boolean isRarity() {
        return switch (this) {
            case SURFACE, DRIFT, RARE, OBSCURE, RELIC -> true;
            default -> false;
        };
    }
}
