package com.scottlogic.deg.orchestrator.violate;

import com.google.inject.AbstractModule;
import com.scottlogic.deg.generator.generation.AllConfigSource;
import com.scottlogic.deg.generator.guice.GeneratorModule;
import com.scottlogic.deg.generator.inputs.profileviolation.IndividualConstraintRuleViolator;
import com.scottlogic.deg.generator.inputs.profileviolation.IndividualRuleProfileViolator;
import com.scottlogic.deg.generator.inputs.profileviolation.ProfileViolator;
import com.scottlogic.deg.generator.inputs.profileviolation.RuleViolator;
import com.scottlogic.deg.profile.guice.ProfileModule;

public class ViolateModule extends AbstractModule {
    private final AllConfigSource configSource;

    public ViolateModule(AllConfigSource configSource) {

        this.configSource = configSource;
    }

    @Override
    protected void configure() {

        bind(AllConfigSource.class).toInstance(configSource);

        bind(ProfileViolator.class).to(IndividualRuleProfileViolator.class);
        bind(RuleViolator.class).to(IndividualConstraintRuleViolator.class);

        install(new GeneratorModule(configSource));
        install(new ProfileModule(configSource));
    }
}
