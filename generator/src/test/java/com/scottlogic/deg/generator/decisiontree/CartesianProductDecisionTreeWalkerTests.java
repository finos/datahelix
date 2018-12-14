package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.generator.constraints.atomic.IsEqualToConstantConstraint;
import com.scottlogic.deg.generator.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpecMerger;
import com.scottlogic.deg.generator.walker.CartesianProductDecisionTreeWalker;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.notNullValue;

class CartesianProductDecisionTreeWalkerTests {
    private final FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();
    private final CartesianProductDecisionTreeWalker dTreeWalker = new CartesianProductDecisionTreeWalker(
            new ConstraintReducer(
                    new FieldSpecFactory(),
                    fieldSpecMerger
            ),
            new RowSpecMerger(
                    fieldSpecMerger
            )
    );
    private final DecisionTreeGenerator dTreeGenerator = new DecisionTreeGenerator();

    @Test
    void test() {
        final Field country = new Field("country");
        final Field currency = new Field("currency");
        final Field city = new Field("city");

        ProfileFields fields = new ProfileFields(Arrays.asList(country, currency, city));

        List<Rule> dummyRules = Arrays.asList(
            new Rule(
                "US country constrains city",
                Collections.singletonList(
                    new ConditionalConstraint(
                        new IsEqualToConstantConstraint(
                            country,
                            "US"),
                        new IsInSetConstraint(
                            city,
                            new HashSet<>(Arrays.asList("New York", "Washington DC")))))),
            new Rule(
                "GB country constrains city",
                Collections.singletonList(
                    new ConditionalConstraint(
                        new IsEqualToConstantConstraint(
                            country,
                            "GB"),
                        new IsInSetConstraint(
                            city,
                            new HashSet<>(Arrays.asList("Bristol", "London")))))),
            new Rule(
                "US country constrains currency",
                Collections.singletonList(
                    new ConditionalConstraint(
                        new IsEqualToConstantConstraint(
                            country,
                            "US"),
                        new IsEqualToConstantConstraint(
                            currency,
                            "USD")))),
            new Rule(
                "GB country constrains currency",
                Collections.singletonList(
                    new ConditionalConstraint(
                        new IsEqualToConstantConstraint(
                            country,
                            "GB"),
                        new IsEqualToConstantConstraint(
                            currency,
                            "GBP")))));

        Profile profile = new Profile(fields, dummyRules);

        final DecisionTreeCollection analysedProfile = this.dTreeGenerator.analyse(profile);

        DecisionTree merged = analysedProfile.getMergedTree();

        final List<RowSpec> rowSpecs = dTreeWalker
            .walk(merged)
            .collect(Collectors.toList());

        Assert.assertThat(rowSpecs, notNullValue());
    }
}
