package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.generation.StringGenerator;

public interface StringRestrictions {
    static final int MAX_STRING_LENGTH = 1000;

    StringRestrictions intersect(StringRestrictions other);

    static boolean isString(Object o) {
        return o instanceof String;
    }

    boolean match(String x);

    default boolean match(Object x){
        return isString(x) && match((String)x);
    }

    StringGenerator createGenerator();
}


