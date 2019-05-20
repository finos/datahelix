package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.ValidationType;
import com.scottlogic.deg.generator.inputs.validation.messages.OutputValidationMessage;
import com.scottlogic.deg.generator.outputs.targets.OutputTargetValidationException;
import com.scottlogic.deg.generator.outputs.targets.ValidatableOutput;
import com.scottlogic.deg.generator.violations.ViolatedProfile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GenerationOutputValidator implements ProfileValidator {

    @Override
    public Collection<ValidationAlert> validate(Profile profile) {
        ValidatableOutput validatable = null;
        List<ValidationAlert> errorMessages = new ArrayList<>();

        try {
            validatable.validate(profile);
        } catch (OutputTargetValidationException validationException) {
            errorMessages.add(
                new ValidationAlert(
                    Criticality.ERROR,
                    new OutputValidationMessage(validationException.getMessage()),
                    ValidationType.OUTPUT,
                    null));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return errorMessages;
    }
}
