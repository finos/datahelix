package com.scottlogic.deg.generator.cucumber.testframework.steps;

import com.scottlogic.deg.generator.cucumber.testframework.utils.CucumberTestHelper;
import com.scottlogic.deg.generator.cucumber.testframework.utils.CucumberTestState;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.Objects;

public class NullValueStep {

    private final CucumberTestState state;
    private final CucumberTestHelper helper;

    public NullValueStep(CucumberTestState state, CucumberTestHelper helper){
        this.state = state;
        this.helper = helper;
    }

    @When("{fieldVar} is {operator} null")
    public void whenFieldIsConstrainedByTextValue(String fieldName, String constraintName) {
        state.addConstraint(fieldName, constraintName, null);
    }

    @When("{fieldVar} is anything but {operator} null")
    public void whenFieldIsNotConstrainedByTextValue(String fieldName, String constraintName) {
        state.addNotConstraint(fieldName, constraintName, null);
    }

    @Then("{fieldVar} contains anything but null")
    public void producedDataShouldNotContainNull(String fieldName) {
        helper.assertFieldContains(fieldName, Objects::nonNull);
    }
}
