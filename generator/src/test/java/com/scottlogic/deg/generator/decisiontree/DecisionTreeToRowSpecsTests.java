package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.ConditionalConstraint;
import com.scottlogic.deg.generator.constraints.IsEqualToConstantConstraint;
import com.scottlogic.deg.generator.constraints.IsInSetConstraint;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpec;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class DecisionTreeToRowSpecsTests {
    final DecisionTreeWalker dTreeWalker = new DecisionTreeWalker(new ConstraintReducer(), new FieldSpecMerger());
    final DecisionTreeGenerator dTreeGenerator = new DecisionTreeGenerator();

    @Test
    public void test() {
        final IDecisionTreeProfile dTree = dTreeGenerator.analyse(makeProfile());
        final Iterable<RowSpec> rowSpecs = dTreeWalker.walk(dTree);
        Assert.assertThat(rowSpecs, Is.is(IsNull.notNullValue()));
    }

    private Profile makeProfile() {
        final Field country = new Field("country");
        final Field currency = new Field("currency");
        final Field city = new Field("city");
        return new Profile(
                Arrays.asList(country, currency, city),
                Arrays.asList(
                        new Rule(
                                "US country constrains city",
                                Collections.singletonList(
                                        new ConditionalConstraint(
                                                new IsEqualToConstantConstraint(
                                                        country,
                                                        "US"
                                                ),
                                                new IsInSetConstraint(
                                                        city,
                                                        new HashSet<>(Arrays.asList("New York", "Washington DC"))
                                                )
                                        )
                                )
                        ),
                        new Rule(
                                "GB country constrains city",
                                Collections.singletonList(
                                        new ConditionalConstraint(
                                                new IsEqualToConstantConstraint(
                                                        country,
                                                        "GB"
                                                ),
                                                new IsInSetConstraint(
                                                        city,
                                                        new HashSet<>(Arrays.asList("Bristol", "London"))
                                                )
                                        )
                                )
                        ),
                        new Rule(
                                "US country constrains currency",
                                Collections.singletonList(
                                        new ConditionalConstraint(
                                                new IsEqualToConstantConstraint(
                                                        country,
                                                        "US"
                                                ),
                                                new IsEqualToConstantConstraint(
                                                        currency,
                                                        "USD"
                                                )
                                        )
                                )
                        ),
                        new Rule(
                                "GB country constrains currency",
                                Collections.singletonList(
                                        new ConditionalConstraint(
                                                new IsEqualToConstantConstraint(
                                                        country,
                                                        "GB"
                                                ),
                                                new IsEqualToConstantConstraint(
                                                        currency,
                                                        "GBP"
                                                )
                                        )
                                )
                        )
                )
        );
    }
}
