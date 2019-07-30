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

package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.Rule;
import com.scottlogic.deg.common.profile.RuleInformation;
import com.scottlogic.deg.common.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.fieldspecs.whitelist.WeightedElement;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyDistributedSet;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.RowSpecDataBagGenerator;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpecMerger;
import com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory;
import com.scottlogic.deg.generator.walker.rowspec.CartesianProductRowSpecTreeSolver;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CartesianProductRowSpecTreeSolverTests {
    private final FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();
    private RowSpecDataBagGenerator dataBagSourceFactory = mock(RowSpecDataBagGenerator.class);
    private final CartesianProductRowSpecTreeSolver dTreeWalker =
            new CartesianProductRowSpecTreeSolver(
                new ConstraintReducer(
                    new FieldSpecFactory(new StringRestrictionsFactory()),
                    fieldSpecMerger
                ), new RowSpecMerger(fieldSpecMerger));

    private final ProfileDecisionTreeFactory dTreeGenerator = new ProfileDecisionTreeFactory();

    @Test
    void test() {
        when(dataBagSourceFactory.createDataBags(any()))
            .thenReturn(
                Stream.of(mock(DataBag.class)),
                Stream.of(mock(DataBag.class)),
                Stream.of(mock(DataBag.class)),
                Stream.of(mock(DataBag.class)),
                Stream.of(mock(DataBag.class)),
                Stream.of(mock(DataBag.class)),
                Stream.of(mock(DataBag.class)),
                Stream.of(mock(DataBag.class)),
                Stream.of(mock(DataBag.class)),
                Stream.of(mock(DataBag.class)),
                Stream.of(mock(DataBag.class)),
                Stream.of(mock(DataBag.class)),
                Stream.of(mock(DataBag.class)),
                Stream.of(mock(DataBag.class)),
                Stream.of(mock(DataBag.class)),
                Stream.of(mock(DataBag.class)),
                Stream.of(mock(DataBag.class)),
                Stream.of(mock(DataBag.class)),
                Stream.of(mock(DataBag.class)),
                Stream.of(mock(DataBag.class))
            );
        final Field country = new Field("country");
        final Field currency = new Field("currency");
        final Field city = new Field("city");

        ProfileFields fields = new ProfileFields(Arrays.asList(country, currency, city));

        List<Rule> dummyRules = Arrays.asList(
            new Rule(
                rule("US country constrains city"),
                Collections.singletonList(
                    new ConditionalConstraint(
                        new IsInSetConstraint(
                            country,
                            new FrequencyDistributedSet<>(Collections.singleton(new WeightedElement<>("US", 1.0F)))
                        ),
                        new IsInSetConstraint(
                            city,
                            new FrequencyDistributedSet<>(new HashSet<>(Arrays.asList(
                                new WeightedElement<>("New York", 1.0F),
                                new WeightedElement<>("Washington DC", 1.0F)))
                        ))))),
            new Rule(
                rule("GB country constrains city"),
                Collections.singletonList(
                    new ConditionalConstraint(
                        new IsInSetConstraint(
                            country,
                            new FrequencyDistributedSet<>(Collections.singleton(new WeightedElement<>("GB", 1.0F)))
                        ),
                        new IsInSetConstraint(
                            city,
                            new FrequencyDistributedSet<>(new HashSet<>(Arrays.asList(
                                new WeightedElement<>("Bristol", 1.0F),
                                new WeightedElement<>("London", 1.0F)))
                        ))))),
            new Rule(
                rule("US country constrains currency"),
                Collections.singletonList(
                    new ConditionalConstraint(
                        new IsInSetConstraint(
                            country,
                            new FrequencyDistributedSet<>(Collections.singleton(new WeightedElement<>("US", 1.0F)))
                        ),
                        new IsInSetConstraint(
                            currency,
                            new FrequencyDistributedSet<>(Collections.singleton(new WeightedElement<>("USD", 1.0F)))
                        )))),
            new Rule(
                rule("GB country constrains currency"),
                Collections.singletonList(
                    new ConditionalConstraint(
                        new IsInSetConstraint(
                            country,
                            new FrequencyDistributedSet<>(Collections.singleton(new WeightedElement<>("GB", 1.0F)))
                        ),
                        new IsInSetConstraint(
                            currency,
                            new FrequencyDistributedSet<>(Collections.singleton(new WeightedElement<>("GBP", 1.0F)))
                        )))));

        String schemaVersion = "0.1";
        Profile profile = new Profile(schemaVersion, fields, dummyRules);

        final DecisionTree merged = this.dTreeGenerator.analyse(profile);

        final List<RowSpec> rowSpecs = dTreeWalker
            .createRowSpecs(merged)
            .collect(Collectors.toList());

        Assert.assertThat(rowSpecs, notNullValue());
    }

    private static RuleInformation rule(String description){
        return new RuleInformation(description);
    }
}
