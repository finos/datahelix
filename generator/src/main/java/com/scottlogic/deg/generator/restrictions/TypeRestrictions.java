package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;

import java.util.Set;

public interface TypeRestrictions {
    TypeRestrictions except(IsOfTypeConstraint.Types... types);

    boolean isTypeAllowed(IsOfTypeConstraint.Types type);

    TypeRestrictions intersect(TypeRestrictions other);

    Set<IsOfTypeConstraint.Types> getAllowedTypes();
}
