package com.scottlogic.deg.generator.inputs.visitor;

import com.scottlogic.deg.generator.constraints.IsAfterConstantDateTimeConstraint;
import com.scottlogic.deg.generator.constraints.IsBeforeConstantDateTimeConstraint;
import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

import java.util.List;

public interface IConstraintValidatorVisitor {

    List<ValidationAlert> visit(IsOfTypeConstraint constraint);
    List<ValidationAlert> visit(IsAfterConstantDateTimeConstraint constraint);
    List<ValidationAlert> visit(IsBeforeConstantDateTimeConstraint constraint);
}
