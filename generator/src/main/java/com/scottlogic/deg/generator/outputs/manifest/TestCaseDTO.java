package com.scottlogic.deg.generator.outputs.manifest;

import java.util.Collection;

public class TestCaseDTO {
    public final String filePath;
    public final Collection<String> violatedRules;
    public final Collection<String> violatedConstraints;

    public TestCaseDTO(String filePath, Collection<String> violatedRules, Collection<String> violatedConstraints) {
        this.filePath = filePath;
        this.violatedRules = violatedRules;
        this.violatedConstraints = violatedConstraints;
    }
}
