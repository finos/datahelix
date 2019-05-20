package com.scottlogic.deg.generator.inputs.validation;

import com.scottlogic.deg.common.profile.Profile;

import java.util.Arrays;
import java.util.Collection;

public class MultipleProfileValidator implements ProfileValidator{
    private final Collection<ProfileValidator> validators;

    public MultipleProfileValidator(ProfileValidator... validators) {
        this.validators = Arrays.asList(validators);
    }

    public MultipleProfileValidator(Collection<ProfileValidator> validators) {
        this.validators = validators;
    }

    @Override
    public void validate(Profile profile) {
        validators.forEach(v->v.validate(profile));
    }
}
