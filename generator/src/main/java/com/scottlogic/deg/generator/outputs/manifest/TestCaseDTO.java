package com.scottlogic.deg.generator.outputs.manifest;

import java.util.Collection;

public class TestCaseDTO {
    public final String filePath;
    public final Collection<String> violatedRules;

    public TestCaseDTO(String filePath, Collection<String> violatedRules) {
        this.filePath = filePath;
        this.violatedRules = violatedRules;
    }
}
