package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.schemas.v3.ConstraintDTO;

@FunctionalInterface
interface IConstraintReader {
    IConstraint apply(
        ConstraintDTO dto,
        FieldLookup fields)
        throws InvalidProfileException;
}
