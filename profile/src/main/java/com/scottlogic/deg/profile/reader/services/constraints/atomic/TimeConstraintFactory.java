package com.scottlogic.deg.profile.reader.services.constraints.atomic;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.HelixTime;
import com.scottlogic.deg.common.profile.TimeGranularity;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.dtos.constraints.atomic.*;
import com.scottlogic.deg.profile.reader.FileReader;

public class TimeConstraintFactory extends AtomicConstraintFactory {

    TimeConstraintFactory(FileReader fileReader) {
        super(fileReader);
    }

    @Override
    AtomicConstraint createAfterOrAtConstraint(AfterOrAtConstraintDTO dto, Field field) {
        return new AfterOrEqualToConstantTimeConstraint(field, HelixTime.create(dto.value));
    }

    @Override
    AtomicConstraint createAfterConstraint(AfterConstraintDTO dto, Field field) {
        return new AfterConstantTimeConstraint(field, HelixTime.create(dto.value));
    }

    @Override
    AtomicConstraint createBeforeOrAtConstraint(BeforeOrAtConstraintDTO dto, Field field) {
        return new BeforeOrEqualToConstantTimeConstraint(field, HelixTime.create(dto.value));
    }

    @Override
    AtomicConstraint createBeforeConstraint(BeforeConstraintDTO dto, Field field) {
        return new BeforeConstantTimeConstraint(field, HelixTime.create(dto.value));
    }

    @Override
    AtomicConstraint createGranularToConstraint(GranularToConstraintDTO dto, Field field) {
        return new GranularToTimeConstraint(field, TimeGranularity.create((String) dto.value));
    }

    @Override
    Object parseValue(Object value) {
        return null;
    }

    @Override
    MatchesRegexConstraint createMatchesRegexConstraint(MatchesRegexConstraintDTO dto, Field field) {
        return null;
    }

    @Override
    ContainsRegexConstraint createContainsRegexConstraint(ContainsRegexConstraintDTO dto, Field field) {
        return null;
    }

    @Override
    OfLengthConstraint createOfLengthConstraint(OfLengthConstraintDTO dto, Field field) {
        return null;
    }

    @Override
    ShorterThanConstraint createShorterThanConstraint(ShorterThanConstraintDTO dto, Field field) {
        return null;
    }

    @Override
    LongerThanConstraint createLongerThanConstraint(LongerThanConstraintDTO dto, Field field) {
        return null;
    }

    @Override
    GreaterThanConstraint createGreaterThanConstraint(GreaterThanConstraintDTO dto, Field field) {
        return null;
    }

    @Override
    GreaterThanOrEqualToConstraint createGreaterThanOrEqualToConstraint(GreaterThanOrEqualToConstraintDTO dto, Field field) {
        return null;
    }

    @Override
    LessThanConstraint createLessThanConstraint(LessThanConstraintDTO dto, Field field) {
        return null;
    }

    @Override
    LessThanOrEqualToConstraint createLessThanOrEqualToConstraint(LessThanOrEqualToConstraintDTO dto, Field field) {
        return null;
    }

}
