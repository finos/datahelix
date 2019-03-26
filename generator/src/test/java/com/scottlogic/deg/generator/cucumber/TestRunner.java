package com.scottlogic.deg.generator.cucumber;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import com.scottlogic.deg.generator.cucumber.utils.GeneratorCucumber;

@RunWith(GeneratorCucumber.class)
@CucumberOptions(
    plugin = {"null_summary"},
    features = {"src/test/java/com/scottlogic/deg/generator/cucumber"},
    glue={"com.scottlogic.deg.generator.cucumber.steps"},
    monochrome = true,
    tags = "not @ignore"
)

public class TestRunner {
}
