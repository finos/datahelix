package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.analysis.FieldDependencyAnalyser;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.FixFieldStrategy;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.HierarchicalDependencyFixFieldStrategy;

public class FixFieldStrategyProvider implements Provider<FixFieldStrategy> {
    private Provider<Profile> profile;

    @Inject
    public FixFieldStrategyProvider(Provider<Profile> profile) {
        this.profile = profile;
    }

    @Override
    public FixFieldStrategy get() {
        return new HierarchicalDependencyFixFieldStrategy(profile.get(), new FieldDependencyAnalyser());
    }
}
