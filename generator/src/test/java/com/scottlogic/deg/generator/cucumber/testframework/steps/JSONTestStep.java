package com.scottlogic.deg.generator.cucumber.testframework.steps;

import com.scottlogic.deg.generator.cucumber.testframework.utils.CucumberTestState;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import cucumber.api.java.en.And;

import java.io.IOException;

public class JSONTestStep {

    private final CucumberTestState state;

    public JSONTestStep(CucumberTestState state){
        this.state = state;
    }

    @And("^there is a constraint:$")
    public void jsonConstraint(String jsonConstraint) throws IOException, InvalidProfileException {
        String constraintProfile = "[" + jsonConstraint + "]";
        this.jsonConstraints(constraintProfile);
    }

    @And("^there are constraints:$")
    public void jsonConstraints(String jsonConstraints) throws IOException, InvalidProfileException {
        this.state.addConstraintsFromJson("{ \"constraints\" : " + jsonConstraints + "}");
    }
}
