package com.scottlogic.deg.orchestrator.cucumber.testframework.utils;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.decisiontree.MaxStringLengthInjectingDecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.ProfileDecisionTreeFactory;

public class CucumberDecisionTreeFactory extends MaxStringLengthInjectingDecisionTreeFactory {
    @Inject
    public CucumberDecisionTreeFactory(ProfileDecisionTreeFactory underlyingFactory, CucumberTestState state) {
        super(underlyingFactory, state.getMaxStringLength());
    }
}
