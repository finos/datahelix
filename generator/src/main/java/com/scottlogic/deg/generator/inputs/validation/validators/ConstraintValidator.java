package com.scottlogic.deg.generator.inputs.validation.validators;

import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;

import java.util.ArrayList;
import java.util.List;

public class ConstraintValidator {

    public final TypeConstraintValidator typeConstraintValidator;
    public final DateTimeConstraintValidator dateTimeConstraintValidator;
    public final SetConstraintValidator setConstraintValidator;
    public final StringConstraintValidator stringConstraintValidator;
    public final NullConstraintValidator nullConstraintValidator;
    public final GranularityConstraintValidator granularityConstraintValidator;
    public final NumericConstraintValidator numericConstraintValidator;

    public ConstraintValidator(TypeConstraintValidator typeConstraintValidator,
                                  DateTimeConstraintValidator dateTimeConstraintValidator,
                                  SetConstraintValidator setConstraintValidator,
                                  StringConstraintValidator stringConstraintValidator,
                                  NullConstraintValidator nullConstraintValidator,
                                  GranularityConstraintValidator granularityConstraintValidator,
                                  NumericConstraintValidator numericConstraintValidator)
    {
        this.typeConstraintValidator = typeConstraintValidator;
        this.dateTimeConstraintValidator = dateTimeConstraintValidator;
        this.setConstraintValidator = setConstraintValidator;
        this.stringConstraintValidator = stringConstraintValidator;
        this.nullConstraintValidator = nullConstraintValidator;
        this.granularityConstraintValidator = granularityConstraintValidator;
        this.numericConstraintValidator = numericConstraintValidator;
    }

    public List<ValidationAlert> getValidationAlerts(){

        List<ValidationAlert> alerts = new ArrayList<>();

        alerts.addAll(typeConstraintValidator.getAlerts());
        alerts.addAll(dateTimeConstraintValidator.getAlerts());
        alerts.addAll(setConstraintValidator.getAlerts());
        alerts.addAll(stringConstraintValidator.getAlerts());
        alerts.addAll(nullConstraintValidator.getAlerts());
        alerts.addAll(granularityConstraintValidator.getAlerts());
        alerts.addAll(numericConstraintValidator.getAlerts());

        return alerts;
    }
}
