package com.scottlogic.deg.generator.outputs.manifest;

import java.util.Collection;

public class ManifestDTO {
    public final Collection<TestCaseDTO> cases;

    public ManifestDTO(Collection<TestCaseDTO> cases) {
        this.cases = cases;
    }
}

