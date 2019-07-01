package com.scottlogic.deg.common.profile.constraints.atomic;

import com.scottlogic.deg.common.profile.Field;

import java.util.Set;

public class IsInNameSetConstraint extends IsInSetConstraint {

    public IsInNameSetConstraint(Field field, Set<Object> legalValues) {
        super(field, legalValues);
    }

}
