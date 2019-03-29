package com.scottlogic.deg.generator.cucumber.testframework.steps;

import com.scottlogic.deg.generator.cucumber.testframework.utils.CucumberTestHelper;
import com.scottlogic.deg.generator.cucumber.testframework.utils.CucumberTestState;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.regex.Pattern;

public class RegexValueStep {

    private final CucumberTestState state;
    private final CucumberTestHelper helper;

    public RegexValueStep(CucumberTestState state, CucumberTestHelper helper){
        this.state = state;
        this.helper = helper;
    }

    @When("{fieldVar} is {operator} {regex}")
    public void whenFieldIsConstrainedByRegex(String fieldName, String constraintName, String value) throws Exception {
        this.state.addConstraint(fieldName, constraintName, value);
    }

    @When("{fieldVar} is anything but {operator} {regex}")
    public void whenFieldIsNotConstrainedByRegex(String fieldName, String constraintName, String value) throws Exception {
        this.state.addNotConstraint(fieldName, constraintName, value);
    }

    @Then("{fieldVar} contains strings matching {regex}")
    public void producedDataShouldContainStringValuesMatchingRegex(String fieldName, String regex){
        Pattern pattern = Pattern.compile(regex);

        helper.assertFieldContainsNullOrMatching(
            fieldName,
            String.class,
            value -> pattern.matcher(value).matches());
    }

    @Then("{fieldVar} contains anything but strings matching {regex}")
    public void producedDataShouldContainStringValuesNotMatchingRegex(String fieldName, String regex){
        Pattern pattern = Pattern.compile(regex);

        helper.assertFieldContainsNullOrMatching(
            fieldName,
            String.class,
            value -> !pattern.matcher(value).matches());
    }
}
