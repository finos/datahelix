package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.schemas.v3.ConstraintDTO;

@FunctionalInterface
public interface IConstraintReader {
    Constraint apply(
        ConstraintDTO dto,
        ProfileFields fields)
        throws InvalidProfileException;
}