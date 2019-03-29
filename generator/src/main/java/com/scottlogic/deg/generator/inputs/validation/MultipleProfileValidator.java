package com.scottlogic.deg.generator.inputs.validation;

import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.Profile;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class MultipleProfileValidator implements ProfileValidator{
    private final Collection<ProfileValidator> validators;

    public MultipleProfileValidator(ProfileValidator... validators) {
        this.validators = Arrays.asList(validators);
    }

    public MultipleProfileValidator(Collection<ProfileValidator> validators) {
        this.validators = validators;
    }

    @Override
    public Collection<ValidationAlert> validate(Profile profile) {
        return FlatMappingSpliterator.flatMap(
            validators.stream(),
            validator -> validator.validate(profile).stream())
            .collect(Collectors.toList());
    }
}
