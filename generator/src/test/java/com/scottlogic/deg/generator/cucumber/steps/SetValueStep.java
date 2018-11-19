package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.cucumber.utils.DegTestState;
import com.scottlogic.deg.generator.cucumber.utils.GeneratorTestUtilities;
import cucumber.api.java.en.When;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SetValueStep {

    private DegTestState state;

    public SetValueStep(DegTestState state){
        this.state = state;
    }

    @When("{fieldVar} is in set:")
    public void whenFieldIsConstrainedBySetValue(String fieldName, List<String> values) {
        this.state.addConstraint(fieldName, "in set", this.getSetValues(values));
    }

    @When("{fieldVar} is anything but in set:")
    public void whenFieldIsNotConstrainedBySetValue(String fieldName, List<String> values) {
        this.state.addNotConstraint(fieldName, "in set", this.getSetValues(values));
    }

    private Collection<Object> getSetValues(List<String> values) {
        return values.stream()
            .map(String::trim)
            .map(GeneratorTestUtilities::parseInput)
            .collect(Collectors.toSet());
    }

}
