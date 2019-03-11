package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.cucumber.utils.CucumberTestHelper;
import com.scottlogic.deg.generator.cucumber.utils.CucumberTestState;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.time.LocalDateTime;

public class DateValueStep {

    public static final String DATE_REGEX = "((\\d{4})-(\\d{2})-(\\d{2}T(\\d{2}:\\d{2}:\\d{2}\\.\\d{3})))$";
    private final CucumberTestState state;
    private final CucumberTestHelper helper;

    public DateValueStep(CucumberTestState state, CucumberTestHelper helper){
        this.state = state;
        this.helper = helper;
    }

    @When("{fieldVar} is {operator} {date}")
    public void whenFieldIsConstrainedByDateValue(String fieldName, String constraintName, DateObject value) throws Exception {
        this.state.addConstraint(fieldName, constraintName, value);
    }

    @When("{fieldVar} is anything but {operator} {date}")
    public void whenFieldIsNotConstrainedByDateValue(String fieldName, String constraintName, DateObject value) throws Exception {
        this.state.addNotConstraint(fieldName, constraintName, value);
    }

    @Then("{fieldVar} contains temporal data")
    public void producedDataShouldContainTemporalValuesForField(String fieldName){
        this.helper.assertFieldContainsNullOrMatching(fieldName, value -> value instanceof LocalDateTime);
    }
}

