/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.datahelix.generator.profile.custom;

import com.google.inject.Inject;
import com.scottlogic.datahelix.generator.common.ValidationException;
import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.custom.CustomGenerator;
import com.scottlogic.datahelix.generator.custom.CustomGeneratorList;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.AtomicConstraint;

import java.util.List;

public class CustomConstraintFactory {
    private final List<CustomGenerator> customGenerators;

    @Inject
    public CustomConstraintFactory(CustomGeneratorList customGeneratorList) {
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
