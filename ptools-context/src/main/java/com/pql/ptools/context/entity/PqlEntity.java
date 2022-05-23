
package com.pql.ptools.context.entity;

import com.pql.ptools.commons.lombok.annotations.EnumDesc;
import com.pql.ptools.context.enums.PqlEnum;


public class PqlEntity {

    @EnumDesc(filed = {"desc", "name"})
    private PqlEnum field1;

    @EnumDesc
    private PqlEnum field2;

}

