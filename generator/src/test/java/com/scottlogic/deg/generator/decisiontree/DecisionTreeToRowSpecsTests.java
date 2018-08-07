package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.ConditionalConstraint;
import com.scottlogic.deg.generator.constraints.IsEqualToConstantConstraint;
import com.scottlogic.deg.generator.constraints.IsInSetConstraint;
import com.scottlogic.deg.generator.restrictions.RowSpec;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class DecisionTreeToRowSpecsTests {
    final DecisionTreeWalker dTreeWalker = new DecisionTreeWalker();
    final DecisionTreeGenerator dTreeGenerator = new DecisionTreeGenerator();

    private Profile makeProfile() {
        final Field country = new Field("country");
        final Field currency = new Field("currency");
        final Field city = new Field("city");
        return new Profile(
                Arrays.asList(country, currency, city),
                Arrays.asList(
                        new Rule(
                                "country constrains city",
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
                        )
                )
        );
    }

    @Test
    public void test() {
        final IDecisionTreeProfile dTree = dTreeGenerator.analyse(makeProfile());
        final Iterable<RowSpec> rowSpecs = dTreeWalker.walk(dTree);
    }
}
