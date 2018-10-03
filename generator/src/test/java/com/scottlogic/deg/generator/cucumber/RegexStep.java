package com.scottlogic.deg.generator.cucumber;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.inputs.MainConstraintReader;
import com.scottlogic.deg.schemas.v3.ConstraintDTO;
import cucumber.api.java.en.When;

public class RegexStep {

    private DegTestState state;

    public RegexStep(DegTestState state){
        this.state = state;
    }

    @When("^(.+) should (.+) /(.+?)/$")
    public void fieldIsConstrainedWithRegex(
            String fieldName,
            String constraintTypeId,
            String regexStr)
            throws Throwable {

        ConstraintDTO dto = new ConstraintDTO();
        dto.field = fieldName;
        dto.is = DegTestOperatorMapping.getOperator(constraintTypeId);
        dto.value = regexStr;

        IConstraint constraint = new MainConstraintReader().apply(
                dto,
                new ProfileFields(this.state.profileFields));

        this.state.constraints.add(constraint);
    }
}
