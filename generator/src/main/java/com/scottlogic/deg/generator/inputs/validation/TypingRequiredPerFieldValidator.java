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

package com.scottlogic.deg.generator.inputs.validation;

import com.google.inject.Inject;
import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.generator.decisiontree.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rejects a profile if there are any fields that aren't positively assigned at least one type.
 *
 * This is complicated by conditional logic/alternation. The gold standard is to only allow a profile
 * if there are no ways to select logical paths that result in a field not having determinable type(s).
 * In reality this is computationally challenging, so this class employs a heuristic approach. It will
 * reject something like:
 *
 *   x number
 *   if (x equalTo 2) then y string
 *   x equalTo 2
 *
 * But at least accepts semi-complicated cases like:
 *
 *   anyOf
 *      x number
 *      x string
 *
 * Crucially, it should never permit an invalid case.
 *
 * see {@link https://github.com/finos/datahelix/issues/767 #767} for more details
 */
public class TypingRequiredPerFieldValidator implements ProfileValidator {
    private final DecisionTreeFactory decisionTreeFactory;

    @Inject
    public TypingRequiredPerFieldValidator(DecisionTreeFactory decisionTreeFactory) {
        this.decisionTreeFactory = decisionTreeFactory;
    }

    @Override
    public void validate(Profile profile) {
        final DecisionTree decisionTree = decisionTreeFactory.analyse(profile);

        List<String> untypedFields = profile.getFields().stream()
            .filter(field -> !sufficientlyRestrictsFieldTypes(decisionTree.getRootNode(), field))
            .map(nonCompliantField -> nonCompliantField.name +
                " is untyped; add an ofType, or add its type to the field definition")
            .collect(Collectors.toList());

        if (!untypedFields.isEmpty()){
            throw new ValidationException(untypedFields);
        }
    }

    private static boolean sufficientlyRestrictsFieldTypes(ConstraintNode node, Field fieldToCheck) {
        return node.getAtomicConstraints().stream()
            .filter(atomicConstraint -> atomicConstraint.getField().equals(fieldToCheck))
            .anyMatch(atomicConstraint -> atomicConstraint instanceof IsOfTypeConstraint);
    }
}
