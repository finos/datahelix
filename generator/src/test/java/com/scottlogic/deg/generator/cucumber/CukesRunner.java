package com.scottlogic.deg.generator.cucumber;

import cucumber.api.junit.*;
import org.junit.runner.RunWith;
@RunWith(Cucumber.class)
@Cucumber.Options(
        features={"src/test/java"}
)
public class CukesRunner {}