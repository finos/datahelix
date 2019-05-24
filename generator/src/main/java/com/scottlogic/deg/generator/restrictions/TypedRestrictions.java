package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;

public interface TypedRestrictions extends Restrictions {

    boolean match(Object o);

    boolean isInstanceOf(Object o);
}
