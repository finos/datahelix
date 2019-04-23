package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.generation.StringGenerator;

public interface StringRestrictions {
    StringRestrictions intersect(StringRestrictions other);

    static boolean isString(Object o) {
        return o instanceof String;
    }

    boolean match(String x);

    default boolean match(Object x){
        return isString(x) && match((String)x);
    }

    StringGenerator createGenerator();

    boolean isContradictory();
}


