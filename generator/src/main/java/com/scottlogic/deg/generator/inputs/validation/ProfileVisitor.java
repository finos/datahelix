package com.scottlogic.deg.generator.inputs.validation;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.constraints.Constraint;

public interface ProfileVisitor {

    void visit(ProfileFields fields);
    void visit(Rule rule);
    void visit(Constraint constraint);
   // void visit(ComposedConstraint composedConstraint);


    void visit(IsOfTypeConstraint constraint);
    void visit(IsAfterConstantDateTimeConstraint constraint);
    void visit(IsBeforeConstantDateTimeConstraint constraint);

    void visit(IsInSetConstraint constraint);
    void visit(IsStringShorterThanConstraint constraint);
    void visit(IsStringLongerThanConstraint constraint);
}
