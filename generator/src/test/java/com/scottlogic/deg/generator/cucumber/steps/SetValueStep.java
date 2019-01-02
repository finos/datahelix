package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.cucumber.utils.DegTestState;
import cucumber.api.java.en.When;

import java.util.List;
import java.util.Set;

public class SetValueStep {

    private DegTestState state;

    public SetValueStep(DegTestState state){
        this.state = state;
    }

    @When("{fieldVar} is in set:")
    public void whenFieldIsConstrainedBySetValue(String fieldName, List<Object> values) {
        this.state.addConstraint(fieldName, "in set", values);
    }

    @When("{fieldVar} is anything but in set:")
    public void whenFieldIsNotConstrainedBySetValue(String fieldName, List<Object> values) {
        this.state.addNotConstraint(fieldName, "in set", values);
    }
}
