package com.scottlogic.deg.generator.cucumber.testframework;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import com.scottlogic.deg.generator.cucumber.testframework.utils.GeneratorCucumber;

@RunWith(GeneratorCucumber.class)
@CucumberOptions(
    plugin = {"null_summary"},
    features = {"src/test/java/com/scottlogic/deg/generator/cucumber/features"},
    glue={"com.scottlogic.deg.generator.cucumber.testframework.steps"},
    monochrome = true,
    tags = "not @ignore"
)

public class TestRunner {
}
