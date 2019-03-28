package com.scottlogic.deg.generator.inputs.validation;

import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.Profile;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class MultipleProfileValidator implements ProfileValidator{
    private final ProfileValidator[] validators;

    public MultipleProfileValidator(ProfileValidator... validators) {
        this.validators = validators;
    }

    @Override
    public Collection<ValidationAlert> validate(Profile profile) {
        return FlatMappingSpliterator.flatMap(
            Arrays.stream(validators),
            validator -> validator.validate(profile).stream())
            .collect(Collectors.toList());
    }
}
