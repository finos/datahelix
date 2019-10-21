package com.scottlogic.deg.profile.custom;

import com.google.inject.Inject;
import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.custom.CustomGenerator;
import com.scottlogic.deg.custom.CustomGeneratorList;
import com.scottlogic.deg.generator.profile.constraints.atomic.AtomicConstraint;

import java.util.List;

public class CustomConstraintFactory {

    private final List<CustomGenerator> customGenerators;

    @Inject
    public CustomConstraintFactory(CustomGeneratorList customGeneratorList){
        customGenerators = customGeneratorList.get();
    }

    public AtomicConstraint create(Field field, String generatorName){
        CustomGenerator customGenerator = customGenerators.stream()
            .filter(cg -> cg.generatorName().equals(generatorName))
            .findFirst()
            .orElseThrow(() -> new ValidationException("Custom generator " + generatorName + " does not exist it needs to be created and added to the CustomGeneratorList class"));

        return new CustomConstraint(field, customGenerator);
    }
}
