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
                new RuleDecision(
                        decisionTreeProfile
                                .getDecisionTrees()
                                .stream()
                                .map(IRuleDecisionTree::getRootOption)
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

//        private Stream<RowSpec> walk(Collection<IRuleDecisionTree> ruleDecisionTrees) {
//            return ;
//        }

//        private Stream<RowSpec> walk(IRuleDecisionTree ruleDecisionTree) {
//            return walk(ruleDecisionTree.getRootOption());
//        }

        private Stream<RowSpec> walk(IRuleOption ruleOption, RowSpec accumulatedSpec) {
            final RowSpec nominalRowSpec = constraintReducer.reduceConstraintsToRowSpec(
                    profileFields,
                    ruleOption.getAtomicConstraints()
            );

            final RowSpec mergedRowSpec = RowSpec.merge(
                    fieldSpecMerger,
                    nominalRowSpec,
                    accumulatedSpec
            );

            if (ruleOption.getDecisions().isEmpty()) {
                System.out.printf("reached leaf %s\n", mergedRowSpec.toString());
                return Stream.of(mergedRowSpec);
            }

            return ruleOption
                    .getDecisions()
                    .stream()
                    .flatMap(decision -> walk(decision, mergedRowSpec));

//            if (ruleOption.getDecisions().isEmpty()) {
//                return Stream.of(mergedRowSpec);
//            }

//            final Set<IRuleDecision> decisionsNotYetTaken = new HashSet<>(ruleOption.getDecisions());
//
//            for (IRuleDecision decision : decisionsNotYetTaken) {
//
//            }

//            final Map<Field, FieldSpec> fieldToFieldSpec = profileFields.stream()
//                    .collect(Collectors.toMap(Function.identity(), field -> new FieldSpec()));

//            Stream<RowSpec> cartesianProductsSoFar = Stream.of(new RowSpec(profileFields, fieldToFieldSpec));
//            Stream<RowSpec> cartesianProductsSoFar = Stream.empty();
//            Stream<RowSpec> cartesianProductsSoFar = Stream.of(mergedRowSpec);
//            // !A, A
//            for (IRuleDecision decision : ruleOption.getDecisions()) {
//
////                if (cartesianProductsSoFar.is)
//
//                // !B, B
//
//                cartesianProductsSoFar = cartesianProductsSoFar
//                        .flatMap(
//                                aRowSpecFromCartesianProductsSoFar -> walk(decision, aRowSpecFromCartesianProductsSoFar)
//                                        .map(
//                                                aRowSpecFromCurrentDecision -> RowSpec.merge(
//                                                        fieldSpecMerger,
//                                                        aRowSpecFromCartesianProductsSoFar,
//                                                        aRowSpecFromCurrentDecision
//                                        )
//                                )
//                        );
//
//            }
//
//            return cartesianProductsSoFar;


//            final List<RowSpec> rowSpecs = new ArrayList<>();
//
//            for (IRuleDecision decision : ruleOption.getDecisions()) {
//                Stream<RowSpec> rowSpecsFromThisDecision = walk(decision, nominalRowSpec);
//            }


//                    .reduce(Arrays.<RowSpec>asList(nominalRowSpec), (acc, decision, ) -> rowspecs.)


//            return ruleOption
//                    .getDecisions()
//                    .stream()
//                    .flatMap(x -> walk(x, nominalRowSpec));
        }

        private RowSpec getIdentityRowSpec() {
            final Map<Field, FieldSpec> fieldToFieldSpec = profileFields.stream()
                    .collect(Collectors.toMap(Function.identity(), field -> new FieldSpec()));

            return new RowSpec(profileFields, fieldToFieldSpec);
        }

        public Stream<RowSpec> walk(IRuleDecision decision) {
            return walk(decision, getIdentityRowSpec());
        }

        private Stream<RowSpec> walk(IRuleDecision decision, RowSpec accumulatedSpec) {
//            return decision
//                    .getOptions()
//                    .stream()
//                    .flatMap(this::walk)
//                    .map(x -> RowSpec.merge(fieldSpecMerger, accumulatedSpec, x));

            //            return decision
//                    .getOptions()
//                    .stream()
//                    .flatMap(this::walk)
//                    .map(x -> RowSpec.merge(fieldSpecMerger, accumulatedSpec, x));

//            final RowSpec identityRowSpec = getIdentityRowSpec();

//            Stream<RowSpec> cartesianProductsSoFar = Stream.of(identityRowSpec);
//            Stream<RowSpec> cartesianProductsSoFar = Stream.of(accumulatedSpec);

//            final RowSpec nominalRowSpec = constraintReducer.reduceConstraintsToRowSpec(
//                    profileFields,
//                    ruleOption.getAtomicConstraints()
//            );

//            final String uuid = UUID.randomUUID().toString().substring(0, 3);

            return decision.getOptions()
                    .stream()
                    .reduce(
                            Stream.of(accumulatedSpec),
                            (acc, option) -> acc.flatMap(aRowSpecFromCartesianProductsSoFar -> walk(option, aRowSpecFromCartesianProductsSoFar)),
                            (acc1, acc2) -> Stream.concat(acc1, acc2)
                    );

            /*
            System.out.printf("START walk decision %s\n", uuid);
            for (IRuleOption option : decision.getOptions()) {
                cartesianProductsSoFar = cartesianProductsSoFar
                        .peek((rowSpec -> System.out.printf("RS %s\n", rowSpec.toString())))
                        .flatMap(
                                aRowSpecFromCartesianProductsSoFar -> walk(option, aRowSpecFromCartesianProductsSoFar)
//                                        .map(
//                                                aRowSpecFromCurrentOption -> RowSpec.merge(
//                                                        fieldSpecMerger,
//                                                        aRowSpecFromCartesianProductsSoFar,
//                                                        aRowSpecFromCurrentOption
//                                                )
//                                        )
                        );
            }
            System.out.printf("END   walk decision %s\n", uuid);

            return cartesianProductsSoFar;

//            if (ruleOption.getDecisions().isEmpty()) {
//                return Stream.of(nominalRowSpec);
//            }
//
//            Stream<RowSpec> cartesianProductsSoFar = Stream.of(nominalRowSpec);
*/
        }
    }
}
