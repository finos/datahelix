Copyright 2019 Scott Logic Ltd /
/
Licensed under the Apache License, Version 2.0 (the \"License\");/
you may not use this file except in compliance with the License./
You may obtain a copy of the License at/
/
    http://www.apache.org/licenses/LICENSE-2.0/
/
Unless required by applicable law or agreed to in writing, software/
distributed under the License is distributed on an \"AS IS\" BASIS,/
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied./
See the License for the specific language governing permissions and/
limitations under the License.
package com.scottlogic.deg.generator.walker;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.util.FlatMappingSpliterator;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpecMerger;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.RowSpecDataBagGenerator;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CartesianProductDecisionTreeWalker implements DecisionTreeWalker {
    private final ConstraintReducer constraintReducer;
    private final RowSpecMerger rowSpecMerger;
    private final RowSpecDataBagGenerator dataBagSourceFactory;

    @Inject
    public CartesianProductDecisionTreeWalker(
        ConstraintReducer constraintReducer,
        RowSpecMerger rowSpecMerger, RowSpecDataBagGenerator dataBagSourceFactory) {
        this.constraintReducer = constraintReducer;
        this.rowSpecMerger = rowSpecMerger;
        this.dataBagSourceFactory = dataBagSourceFactory;
    }

    public Stream<DataBag> walk(DecisionTree tree) {
        final DecisionTreeWalkerHelper helper = new DecisionTreeWalkerHelper(tree.getFields());
        Stream<RowSpec> rowSpecs = helper.walk(tree.getRootNode());

        return FlatMappingSpliterator.flatMap(
            rowSpecs,
            dataBagSourceFactory::createDataBags);
    }

    private class DecisionTreeWalkerHelper {
        private final ProfileFields profileFields;

        private DecisionTreeWalkerHelper(ProfileFields profileFields) {
            this.profileFields = profileFields;
        }

        private RowSpec getIdentityRowSpec() {
            final Map<Field, FieldSpec> fieldToFieldSpec = profileFields.stream()
                    .collect(Collectors.toMap(Function.identity(), field -> FieldSpec.Empty));

            return new RowSpec(profileFields, fieldToFieldSpec);
        }

        public Stream<RowSpec> walk(ConstraintNode constraint) {
            return walk(constraint, getIdentityRowSpec());
        }

        public Stream<RowSpec> walk(ConstraintNode option, RowSpec accumulatedSpec) {
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
                    (acc, decisionNode) -> FlatMappingSpliterator.flatMap(
                        acc,
                        aRowSpecFromCartesianProductsSoFar -> walk(decisionNode, aRowSpecFromCartesianProductsSoFar)),
                    Stream::concat);
        }

        private Stream<RowSpec> walk(DecisionNode decision, RowSpec accumulatedSpec) {
            return FlatMappingSpliterator.flatMap(decision
                    .getOptions()
                    .stream(),
                    option -> walk(option, accumulatedSpec));
        }
    }
}
