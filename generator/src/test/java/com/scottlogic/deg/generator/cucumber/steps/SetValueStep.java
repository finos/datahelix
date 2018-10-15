package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.cucumber.utils.DegTestState;
import com.scottlogic.deg.generator.cucumber.utils.GeneratorTestUtilities;
import cucumber.api.java.en.When;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class SetValueStep {

    private DegTestState state;

    public SetValueStep(DegTestState state){
        this.state = state;
    }

    @When("{fieldVar} is {operator} {set}")
    public void whenFieldIsConstrainedBySetValue(String fieldName, String constraintName, String value) throws Exception {
        this.state.addConstraint(fieldName, constraintName, this.getSetValues(value));
    }

    @When("{fieldVar} is anything but {operator} {set}")
    public void whenFieldIsNotConstrainedBySetValue(String fieldName, String constraintName, String value) throws Exception {
        this.state.addNotConstraint(fieldName, constraintName, this.getSetValues(value));
    }

    private Collection<Object> getSetValues(String csvSet) {
        return Arrays.asList(csvSet.split(","))
            .stream()
            .map(String::trim)
            .map(GeneratorTestUtilities::parseInput)
            .collect(Collectors.toSet());
    }

}
