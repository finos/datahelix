package com.scottlogic.deg.generator.inputs.validation;

import com.google.inject.Inject;
import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;

import static com.scottlogic.deg.generator.config.detail.CombinationStrategyType.MINIMAL;

public class UniquenessValidator implements ProfileValidator {

    private final GenerationConfigSource configSource;

    @Inject
    public UniquenessValidator(GenerationConfigSource configSource) {
        this.configSource = configSource;
    }

    @Override
    public void validate(Profile profile) {
        if (configSource.getCombinationStrategyType() != MINIMAL && anyUnique(profile)){
            throw new ValidationException("Unique fields do not work when not using Minimal combination strategy");
        }
    }

    private boolean anyUnique(Profile profile) {
        return profile.getFields().stream().anyMatch(Field::isUnique);
    }
}
