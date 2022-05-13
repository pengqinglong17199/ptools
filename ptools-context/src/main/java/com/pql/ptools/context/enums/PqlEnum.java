package com.pql.ptools.context.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum  PqlEnum {

    /**
     *
     */
    PQL("pql", "彭清龙"),
    HZY("hzy", "黄泽源"),
    FJK("fjk", "方景坤"),
    ;


    private final String desc;

    private final String name;
}
