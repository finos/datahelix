package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.common.profile.Profile;

import java.util.Collection;

public class TestCaseGenerationResult
{
    public final Profile profile;
    public final Collection<TestCaseDataSet> datasets;

    public TestCaseGenerationResult(
        Profile profile,
        Collection<TestCaseDataSet> datasets) {

        this.profile = profile;
        this.datasets = datasets;
    }
}
