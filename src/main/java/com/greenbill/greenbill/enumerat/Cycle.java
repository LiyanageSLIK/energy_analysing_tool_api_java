package com.greenbill.greenbill.enumerat;

import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Getter
public enum Cycle {
    ONE_MONTH(1),
    THREE_MONTH(3),
    SIX_MONTH(6),
    ONE_YEAR(12),
    TWO_YEAR(24)
    ;
    private final Integer month;

    Cycle(Integer month) {
        this.month = month;
    }

}
