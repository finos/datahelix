package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpec;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpec;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DecisionTreeWalker {
    private final ConstraintReducer constraintReducer;
    private final FieldSpecMerger fieldSpecMerger;

    public DecisionTreeWalker(
            ConstraintReducer constraintReducer,
            FieldSpecMerger fieldSpecMerger
    ) {
        this.constraintReducer = constraintReducer;
        this.fieldSpecMerger = fieldSpecMerger;
    }

    public Stream<RowSpec> walk(IDecisionTreeProfile decisionTreeProfile) {
        final DecisionTreeWalkerHelper helper = new DecisionTreeWalkerHelper(decisionTreeProfile.getFields());
        return helper.walk(decisionTreeProfile);
    }

    private class DecisionTreeWalkerHelper {
        private final ProfileFields profileFields;

        private DecisionTreeWalkerHelper(ProfileFields profileFields) {
            this.profileFields = profileFields;
        }

        private RowSpec getIdentityRowSpec() {
            final Map<Field, FieldSpec> fieldToFieldSpec = profileFields.stream()
                    .collect(Collectors.toMap(Function.identity(), field -> new FieldSpec()));

            return new RowSpec(profileFields, fieldToFieldSpec);
        }

        private Stream<RowSpec> walk(IRuleOption option, RowSpec accumulatedSpec) {
            final RowSpec nominalRowSpec = constraintReducer.reduceConstraintsToRowSpec(
                    profileFields,
                    option.getAtomicConstraints()
            );

            final RowSpec mergedRowSpec = RowSpec.merge(
                    fieldSpecMerger,
                    nominalRowSpec,
                    accumulatedSpec
            );

            if (option.getDecisions().isEmpty()) {
                return Stream.of(mergedRowSpec);
            }

            return option.getDecisions()
                    .stream()
                    .flatMap(decision -> walk(decision, mergedRowSpec));
        }

        private Stream<RowSpec> walk(IRuleDecision decision, RowSpec accumulatedSpec) {
            return decision
                    .getOptions()
                    .stream()
                    .flatMap(option -> walk(option, accumulatedSpec));
        }

        public Stream<RowSpec> walk(IRuleDecisionTree decisionTree, RowSpec accumulatedSpec) {
            return walk(decisionTree.getRootOption(), accumulatedSpec);
        }

        public Stream<RowSpec> walk(IDecisionTreeProfile decisionTreeProfile) {
            return decisionTreeProfile.getDecisionTrees()
                    .stream()
                    .reduce(
                            Stream.of(getIdentityRowSpec()),
                            (acc, decisionTree) -> acc.flatMap(aRowSpecFromCartesianProductsSoFar -> walk(decisionTree, aRowSpecFromCartesianProductsSoFar)),
                            Stream::concat
                    );

        }
    }
}
