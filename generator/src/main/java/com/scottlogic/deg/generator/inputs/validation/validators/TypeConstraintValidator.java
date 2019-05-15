package com.scottlogic.deg.generator.inputs.validation.validators;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.inputs.validation.*;
import com.scottlogic.deg.generator.inputs.validation.messages.*;
import com.scottlogic.deg.generator.restrictions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TypeConstraintValidator implements ConstraintValidatorAlerts {

    public final ValidationType validationType = ValidationType.TYPE;
    private final List<ValidationAlert> alerts;

    private TypeRestrictions currentRestrictions;


    public TypeConstraintValidator(){
        this.alerts = new ArrayList<>();
        this.currentRestrictions = new DataTypeRestrictions(Arrays.asList(IsOfTypeConstraint.Types.values()));
    }


    public void isOfType(Field field, IsOfTypeConstraint.Types type) {

        TypeRestrictions candidateRestrictions = new DataTypeRestrictions(Arrays.asList(type));
        TypeRestrictionsMerger merger = new TypeRestrictionsMerger();

        MergeResult<TypeRestrictions> result = merger.merge(currentRestrictions, candidateRestrictions);
        if(result.successful && !result.restrictions.getAllowedTypes().isEmpty()){
            currentRestrictions = result.restrictions;
        } else {
            logError(field, new TypeConstraintValidationMessages(
                candidateRestrictions.getAllowedTypes().iterator().next(),
                currentRestrictions.getAllowedTypes().iterator().next()));
        }
    }

    private void logError(Field field, StandardValidationMessages message){
        alerts.add(new ValidationAlert(
            Criticality.ERROR,
            message,
            validationType,
            field));
    }

    @Override
    public List<ValidationAlert> getAlerts() {
        return alerts;
    }
}
