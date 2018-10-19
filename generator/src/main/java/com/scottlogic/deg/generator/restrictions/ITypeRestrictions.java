package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

import java.util.Set;

public interface ITypeRestrictions {
    ITypeRestrictions except(IsOfTypeConstraint.Types... types);

    boolean isTypeAllowed(IsOfTypeConstraint.Types type);

    ITypeRestrictions intersect(ITypeRestrictions other);

    Set<IsOfTypeConstraint.Types> getAllowedTypes();
}
