package com.scottlogic.deg.generator.cucumber.steps;

import cucumber.api.java.en.When;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class SetValueStep {

    private DegTestState state;

    public SetValueStep(DegTestState state){
        this.state = state;
    }

    @When("{fieldVar} is {setValueOperation} {set}")
    public void whenFieldIsConstrainedBySetValue(String fieldName, String constraintName, String value) throws Exception {
        Collection<String> set = this.getSetValues(value);
        this.state.addConstraint(fieldName, constraintName, set);
    }

    @When("{fieldVar} is not {setValueOperation} {set}")
    public void whenFieldIsNotConstrainedBySetValue(String fieldName, String constraintName, String value) throws Exception {
        this.state.addNotConstraint(fieldName, constraintName, this.getSetValues(value));
    }

    private Set<String> getSetValues(String csvSet) {
        return Arrays.asList(csvSet.replaceAll(" ", "").split(","))
            .stream()
            .map(entry -> entry.substring(1, entry.length()-1))
            .collect(Collectors.toSet());
    }

}
