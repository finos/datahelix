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

    public Iterable<RowSpec> walk(IDecisionTreeProfile decisionTreeProfile) {
        final DecisionTreeWalkerHelper helper = new DecisionTreeWalkerHelper(decisionTreeProfile.getFields());
        return helper.walk(
                new RuleOption(
                        Collections.emptyList(),
                        decisionTreeProfile
                                .getDecisionTrees()
                                .stream()
                                .map(IRuleDecisionTree::getRootOption)
                                .map(Arrays::asList)
                                .map(RuleDecision::new)
                                .collect(Collectors.toList())
                )
        )
                .collect(Collectors.toList());
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

        private Stream<RowSpec> walk(IRuleOption option) {
            return walk(option, getIdentityRowSpec());
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

            return option.getDecisions()
                    .stream()
                    .reduce(
                            Stream.of(mergedRowSpec),
                            (acc, decision) -> acc.flatMap(aRowSpecFromCartesianProductsSoFar -> walk(decision, aRowSpecFromCartesianProductsSoFar)),
                            Stream::concat
                    );
        }

        private Stream<RowSpec> walk(IRuleDecision decision, RowSpec accumulatedSpec) {
            return decision
                    .getOptions()
                    .stream()
                    .flatMap(option -> walk(option, accumulatedSpec));
        }
    }
}
