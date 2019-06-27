package com.scottlogic.deg.profile.reader;

import com.scottlogic.deg.common.profile.RuleInformation;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.profile.reader.InvalidProfileException;
import com.scottlogic.deg.profile.v0_1.ConstraintDTO;

import java.util.Set;

@FunctionalInterface
public interface ConstraintReader {
    Constraint apply(
        ConstraintDTO dto,
        ProfileFields fields,
        Set<RuleInformation> rules);
}