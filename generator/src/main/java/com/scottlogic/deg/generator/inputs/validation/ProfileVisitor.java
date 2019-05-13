package com.scottlogic.deg.generator.inputs.validation;

import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.atomic.*;

public interface ProfileVisitor {

    void visit(IsOfTypeConstraint constraint);
    void visit(IsAfterConstantDateTimeConstraint constraint);
    void visit(IsBeforeConstantDateTimeConstraint constraint);
    void visit(IsAfterOrEqualToConstantDateTimeConstraint constraint);
    void visit(IsBeforeOrEqualToConstantDateTimeConstraint constraint);
    void visit(IsInSetConstraint constraint);
    void visit(IsStringShorterThanConstraint constraint);
    void visit(IsStringLongerThanConstraint constraint);
    void visit(IsNullConstraint constraint);
    void visit(NotConstraint constraint);
    void visit(IsGranularToNumericConstraint constraint);
    void visit(IsLessThanConstantConstraint constraint);
    void visit(IsGreaterThanConstantConstraint constraint);
    void visit(IsLessThanOrEqualToConstantConstraint constraint);
    void visit(IsGreaterThanOrEqualToConstantConstraint constraint);

    default void visit(Constraint sc){}
}
