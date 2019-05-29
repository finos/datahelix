package com.scottlogic.deg.orchestrator.cucumber.testframework;

import com.scottlogic.deg.orchestrator.cucumber.testframework.utils.GeneratorCucumber;
import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;

@RunWith(GeneratorCucumber.class)
@CucumberOptions(
    plugin = {"null_summary"},
    features = {"src/test/java/com/scottlogic/deg/orchestrator/cucumber/features"},
    glue={"com.scottlogic.deg.orchestrator.cucumber.testframework.steps"},
    monochrome = true,
    tags = "not @ignore"
)

public class TestRunner {
}
