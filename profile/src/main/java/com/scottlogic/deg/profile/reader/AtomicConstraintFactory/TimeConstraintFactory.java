package com.scottlogic.deg.profile.reader.AtomicConstraintFactory;

import com.scottlogic.deg.common.profile.*;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.dtos.constraints.*;
import com.scottlogic.deg.profile.reader.FileReader;

public class TimeConstraintFactory extends AtomicConstraintFactory {

    TimeConstraintFactory(FileReader fileReader) {
        super(fileReader);
    }

    @Override
    Constraint getAfterOrAtConstraint(AfterOrAtConstraintDTO dto, Field field) {
        return new IsAfterOrEqualToConstantTimeConstraint(field, HelixTime.create(dto.value));
    }

    @Override
    Constraint getAfterConstraint(AfterConstraintDTO dto, Field field) {
        return new IsAfterConstantTimeConstraint(field, HelixTime.create(dto.value));
    }

    @Override
    Constraint getBeforeOrAtConstraint(BeforeOrAtConstraintDTO dto, Field field) {
        return new IsBeforeOrEqualToConstantTimeConstraint(field, HelixTime.create(dto.value));
    }

    @Override
    Constraint getBeforeConstraint(BeforeConstraintDTO dto, Field field) {
        return new IsBeforeConstantTimeConstraint(field, HelixTime.create(dto.value));
    }

    @Override
    Constraint getGranularToConstraint(GranularToConstraintDTO dto, Field field) {
        return new IsGranularToTimeConstraint(field, TimeGranularity.create((String) dto.value));
    }


}
