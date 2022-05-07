package com.pql.ptools.context.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum  PqlEnum {

    /**
     *
     */
    PQL("pql"),
    HZY("hzy"),
    FJK("fjk"),
    ;


    private final String desc;
}
