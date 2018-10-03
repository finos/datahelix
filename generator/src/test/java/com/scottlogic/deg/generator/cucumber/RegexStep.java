package com.scottlogic.deg.generator.cucumber;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.inputs.MainConstraintReader;
import com.scottlogic.deg.schemas.v3.AtomicConstraintType;
import com.scottlogic.deg.schemas.v3.ConstraintDTO;
import cucumber.api.java.en.When;

public class RegexStep {

    private DegTestState state;

    public RegexStep(DegTestState state){
        this.state = state;
    }

    @When("^(.+) should match regex /(.+?)/$")
    public void fieldShouldMatchRegex(
            String fieldName,
            String regexStr)
            throws Exception {
        this.addRegexConstraint(fieldName, AtomicConstraintType.MATCHESREGEX, regexStr);
    }

    @When("^(.+) should contain regex /(.+?)/$")
    public void fieldShouldContainRegex(
            String fieldName,
            String regexStr)
            throws Exception {
        this.addRegexConstraint(fieldName, AtomicConstraintType.CONTAINSREGEX, regexStr);
    }

    private void addRegexConstraint(
            String fieldName,
            AtomicConstraintType type,
            String regexStr)
            throws Exception {
        ConstraintDTO dto = new ConstraintDTO();
        dto.field = fieldName;
        dto.is = type.toString();
        dto.value = regexStr;

        IConstraint constraint = new MainConstraintReader().apply(
                dto,
                new ProfileFields(this.state.profileFields));

        this.state.constraints.add(constraint);
    }
}
