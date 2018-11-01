package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpec;
import com.scottlogic.deg.generator.restrictions.RowSpec;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DecisionTreeWalker {
    private final ConstraintReducer constraintReducer;
    private final RowSpecMerger rowSpecMerger;

    public DecisionTreeWalker(
            ConstraintReducer constraintReducer,
            RowSpecMerger rowSpecMerger
    ) {
        this.constraintReducer = constraintReducer;
        this.rowSpecMerger = rowSpecMerger;
    }

    public Stream<RowSpec> walk(DecisionTree tree) {
        final DecisionTreeWalkerHelper helper = new DecisionTreeWalkerHelper(tree.getFields());
        return helper.walk(tree.getRootNode());
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

        public Stream<RowSpec> walk(ConstraintNode constraint) {
            return walk(constraint, getIdentityRowSpec());
        }

        private Stream<RowSpec> walk(ConstraintNode option, RowSpec accumulatedSpec) {
            final Optional<RowSpec> nominalRowSpec = option.getOrCreateRowSpec(() -> constraintReducer.reduceConstraintsToRowSpec(
                    profileFields,
                    option.getAtomicConstraints()
            ));

            if (!nominalRowSpec.isPresent()) {
                return Stream.empty();
            }

            final Optional<RowSpec> mergedRowSpecOpt = rowSpecMerger.merge(
                    Arrays.asList(
                            nominalRowSpec.get(),
                            accumulatedSpec
                    )
            );

            if (!mergedRowSpecOpt.isPresent()) {
                return Stream.empty();
            }

            final RowSpec mergedRowSpec = mergedRowSpecOpt.get();

            if (option.getDecisions().isEmpty()) {
                return Stream.of(mergedRowSpec);
            }

            return option.getDecisions()
                .stream()
                .reduce(
                    Stream.of(mergedRowSpec),
                    (acc, decisionNode) -> acc.flatMap(aRowSpecFromCartesianProductsSoFar -> walk(decisionNode, aRowSpecFromCartesianProductsSoFar)),
                    Stream::concat);
        }

        private Stream<RowSpec> walk(DecisionNode decision, RowSpec accumulatedSpec) {
            return decision
                    .getOptions()
                    .stream()
                    .flatMap(option -> walk(option, accumulatedSpec));
        }
    }
}
