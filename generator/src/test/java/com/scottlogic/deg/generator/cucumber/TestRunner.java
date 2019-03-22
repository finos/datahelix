package com.scottlogic.deg.generator.cucumber;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import com.scottlogic.deg.generator.cucumber.engine.utils.GeneratorCucumber;

@RunWith(GeneratorCucumber.class)
@CucumberOptions(
    plugin = {"pretty"},
    features = {"src/test/java/com/scottlogic/deg/generator/cucumber"},
    glue={"com.scottlogic.deg.generator.cucumber.engine.steps"},
    monochrome = true,
    tags = "not @ignore"
)

public class TestRunner {
}
