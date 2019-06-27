package com.scottlogic.deg.orchestrator.cucumber.testframework.steps;

import com.scottlogic.deg.orchestrator.cucumber.testframework.utils.CucumberTestState;
import com.scottlogic.deg.profile.reader.InvalidProfileException;
import cucumber.api.java.en.And;

import java.io.IOException;

public class JSONTestStep {

    private final CucumberTestState state;

    public JSONTestStep(CucumberTestState state){
        this.state = state;
    }

    @And("^there is a constraint:$")
    public void jsonConstraint(String jsonConstraint) throws IOException {
        String constraintProfile = "[" + jsonConstraint + "]";
        this.jsonConstraints(constraintProfile);
    }

    @And("^there are constraints:$")
    public void jsonConstraints(String jsonConstraints) throws IOException {
        this.state.addConstraintsFromJson("{ \"constraints\" : " + jsonConstraints + "}");
    }
}
