package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.generation.string.StringGenerator;

public interface StringRestrictions extends TypedRestrictions {
    MergeResult<StringRestrictions> intersect(StringRestrictions other);

    @Override
    default boolean isInstanceOf(Object o) {
        return IsOfTypeConstraint.Types.STRING.isInstanceOf(o);
    }

    boolean match(String x);

    @Override
    default boolean match(Object x) {
        return isInstanceOf(x) && match((String) x);
    }

    StringGenerator createGenerator();
}


