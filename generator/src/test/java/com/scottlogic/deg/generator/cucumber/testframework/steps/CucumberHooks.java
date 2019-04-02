package com.scottlogic.deg.generator.cucumber.testframework.steps;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import org.junit.AssumptionViolatedException;

public class CucumberHooks {
    @Before("@ignore")
    public void beforeEachScenario(Scenario scenario){
        throw new AssumptionViolatedException(String.format("Scenario '%s' is ignored (%s)", scenario.getName(), scenario.getId()));
    }
}

