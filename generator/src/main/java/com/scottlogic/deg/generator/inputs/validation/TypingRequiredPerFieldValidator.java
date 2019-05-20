package com.scottlogic.deg.generator.inputs.validation;

import com.google.inject.Inject;
import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.inputs.validation.messages.StringValidationMessage;

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
 * see {@link https://github.com/ScottLogic/datahelix/issues/767 #767} for more details
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

        List<String> untypedFields = profile.fields.stream()
            .filter(field -> !sufficientlyRestrictsFieldTypes(decisionTree.getRootNode(), field))
            .map(nonCompliantField -> nonCompliantField.name + " is untyped; add an ofType, equalTo or inSet constraint, or mark it as null")
            .collect(Collectors.toList());

        if (!untypedFields.isEmpty()){
            throw new ValidationException(untypedFields);
        }

    }

    private static boolean sufficientlyRestrictsFieldTypes(ConstraintNode node, Field fieldToCheck) {
        // a constraint node is sufficient if any of its constraints, or any of its decision nodes, are sufficient
        return
            node.getAtomicConstraints().stream()
                .anyMatch(constraint -> sufficientlyRestrictsFieldTypes(constraint, fieldToCheck))
            ||
            node.getDecisions().stream()
                .anyMatch(decisionNode -> sufficientlyRestrictsFieldTypes(decisionNode, fieldToCheck));
    }

    private static boolean sufficientlyRestrictsFieldTypes(DecisionNode node, Field fieldToCheck) {
        // a decision node is sufficient if all of its branches are sufficient
        return
            node.getOptions().stream()
                .allMatch(constraintNode -> sufficientlyRestrictsFieldTypes(constraintNode, fieldToCheck));
    }

    private static boolean sufficientlyRestrictsFieldTypes(AtomicConstraint constraint, Field fieldToCheck) {
        return
            constraint.getField().equals(fieldToCheck)
            && (
                constraint instanceof IsOfTypeConstraint
                || constraint instanceof IsNullConstraint
                || constraint instanceof IsInSetConstraint // covers the equalTo case as well as inSet
            );
    }
}
