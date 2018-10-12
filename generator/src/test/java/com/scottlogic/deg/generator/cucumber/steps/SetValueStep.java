package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.cucumber.utils.DegTestState;
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

    @When("{fieldVar} is not {operator} {set}")
    public void whenFieldIsNotConstrainedBySetValue(String fieldName, String constraintName, String value) throws Exception {
        this.state.addNotConstraint(fieldName, constraintName, this.getSetValues(value));
    }

    private Collection<Object> getSetValues(String csvSet) {
        return Arrays.asList(csvSet.split(","))
            .stream()
            .map(String::trim)
            .map(value -> {
                Object parsedValue;
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    parsedValue = value.substring(1, value.length() - 1);
                } else if (value.matches("^((20\\d{2})-(\\d{2})-(\\d{2})(T(\\d{2}:\\d{2}:\\d{2}))?)$")){
                    parsedValue = value;
                } else if (value.contains(".")){
                    parsedValue = Double.parseDouble(value);
                } else {
                    parsedValue = Integer.parseInt(value);
                }
                return parsedValue;
            })
            .collect(Collectors.toSet());
    }

}
