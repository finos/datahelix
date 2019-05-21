package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.generation.StringGenerator;

public interface StringRestrictions extends Restrictions {
    MergeResult<StringRestrictions> intersect(StringRestrictions other);

    static boolean isString(Object o) {
        return o instanceof String;
    }

    default boolean isInstanceOf(Object o) {
        return StringRestrictions.isString(o);
    }

    boolean match(String x);

    @Override
    default boolean match(Object x){
        return isString(x) && match((String)x);
    }

    StringGenerator createGenerator();
}


