package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;

abstract class AbstractTypedRestrictions implements TypedRestrictions {

    protected abstract IsOfTypeConstraint.Types getType();

    @Override
    public boolean isInstanceOf(Object o) {
        return getType().isInstanceOf(o);
    }
}
