package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.ConstraintRule;
import com.scottlogic.deg.schemas.v3.ConstraintDTO;
import com.scottlogic.deg.schemas.v3.RuleDTO;

@FunctionalInterface
public interface IConstraintReader {
    Constraint apply(
        ConstraintDTO dto,
        ProfileFields fields,
        ConstraintRule rule)
        throws InvalidProfileException;
}