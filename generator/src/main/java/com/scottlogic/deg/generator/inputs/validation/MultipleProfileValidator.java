package com.scottlogic.deg.generator.inputs.validation;

import com.scottlogic.deg.common.profile.Profile;

import java.util.ArrayList;
import java.util.Collection;

public class MultipleProfileValidator implements ProfileValidator{
    private final Collection<ProfileValidator> validators;

    public MultipleProfileValidator() {
        this.validators = new ArrayList<>();
    }

    public MultipleProfileValidator(Collection<ProfileValidator> validators) {
        this.validators = validators;
    }

    @Override
    public void validate(Profile profile) {
        validators.forEach(v->v.validate(profile));
    }
}
