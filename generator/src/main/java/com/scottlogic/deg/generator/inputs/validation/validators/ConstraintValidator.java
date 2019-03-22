package com.scottlogic.deg.generator.inputs.validation.validators;

import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;

import java.util.ArrayList;
import java.util.List;

public class ConstraintValidator {

    public final TypeConstraintValidator typeConstraintValidator;
    public final DatetimeConstraintValidator datetimeConstraintValidator;
    public final SetConstraintValidator setConstraintValidator;
    public final StringConstraintValidator stringConstraintValidator;
    public final NullConstraintValidator nullConstraintValidator;
    public final GranularityConstraintValidator granularityConstraintValidator;
    public final NumericConstraintValidator numericConstraintValidator;

    public ConstraintValidator(TypeConstraintValidator typeConstraintValidator,
                                  DatetimeConstraintValidator datetimeConstraintValidator,
                                  SetConstraintValidator setConstraintValidator,
                                  StringConstraintValidator stringConstraintValidator,
                                  NullConstraintValidator nullConstraintValidator,
                                  GranularityConstraintValidator granularityConstraintValidator,
                                  NumericConstraintValidator numericConstraintValidator)
    {
        this.typeConstraintValidator = typeConstraintValidator;
        this.datetimeConstraintValidator = datetimeConstraintValidator;
        this.setConstraintValidator = setConstraintValidator;
        this.stringConstraintValidator = stringConstraintValidator;
        this.nullConstraintValidator = nullConstraintValidator;
        this.granularityConstraintValidator = granularityConstraintValidator;
        this.numericConstraintValidator = numericConstraintValidator;
    }

    public List<ValidationAlert> getValidationAlerts(){

        List<ValidationAlert> alerts = new ArrayList<>();

        alerts.addAll(typeConstraintValidator.getAlerts());
        alerts.addAll(datetimeConstraintValidator.getAlerts());
        alerts.addAll(setConstraintValidator.getAlerts());
        alerts.addAll(stringConstraintValidator.getAlerts());
        alerts.addAll(nullConstraintValidator.getAlerts());
        alerts.addAll(granularityConstraintValidator.getAlerts());
        alerts.addAll(numericConstraintValidator.getAlerts());

        return alerts;
    }
}
