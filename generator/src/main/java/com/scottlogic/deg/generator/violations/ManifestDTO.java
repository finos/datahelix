package com.scottlogic.deg.generator.violations;

import java.util.Collection;

public class ManifestDTO {
    public final Collection<TestCaseDTO> cases;

    public ManifestDTO(Collection<TestCaseDTO> cases) {
        this.cases = cases;
    }

    public static class TestCaseDTO {
        public final String filePath;
        public final Collection<String> violatedRules;

        public TestCaseDTO(String filePath, Collection<String> violatedRules) {
            this.filePath = filePath;
            this.violatedRules = violatedRules;
        }
    }
}

