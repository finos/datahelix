package com.scottlogic.deg.orchestrator.violate;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.scottlogic.deg.orchestrator.guice.AllConfigSource;
import com.scottlogic.deg.generator.guice.GeneratorModule;
import com.scottlogic.deg.generator.inputs.profileviolation.IndividualConstraintRuleViolator;
import com.scottlogic.deg.generator.inputs.profileviolation.IndividualRuleProfileViolator;
import com.scottlogic.deg.generator.inputs.profileviolation.ProfileViolator;
import com.scottlogic.deg.generator.inputs.profileviolation.RuleViolator;
import com.scottlogic.deg.generator.violations.filters.ViolationFilter;
import com.scottlogic.deg.profile.guice.ProfileModule;

import java.util.List;

public class ViolateModule extends AbstractModule {
    private final ViolateConfigSource configSource;

    public ViolateModule(ViolateConfigSource configSource) {
        this.configSource = configSource;
    }

    @Override
    protected void configure() {
        bind(AllConfigSource.class).toInstance(configSource);
        bind(ViolateConfigSource.class).toInstance(configSource);

        bind(ProfileViolator.class).to(IndividualRuleProfileViolator.class);
        bind(RuleViolator.class).to(IndividualConstraintRuleViolator.class);

        bind(new TypeLiteral<List<ViolationFilter>>(){}).toProvider(ViolationFiltersProvider.class);

        install(new GeneratorModule(configSource));
        install(new ProfileModule(configSource));
    }
}
