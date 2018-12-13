package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.ConstraintRule;
import com.scottlogic.deg.generator.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.generator.constraints.atomic.IsEqualToConstantConstraint;
import com.scottlogic.deg.generator.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpec;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;
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
    private final ProfileDecisionTreeFactory dTreeGenerator = new ProfileDecisionTreeFactory();

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
                            "US",
                            rule()),
                        new IsInSetConstraint(
                            city,
                            new HashSet<>(Arrays.asList("New York", "Washington DC")),
                            rule())))),
            new Rule(
                "GB country constrains city",
                Collections.singletonList(
                    new ConditionalConstraint(
                        new IsEqualToConstantConstraint(
                            country,
                            "GB",
                            rule()),
                        new IsInSetConstraint(
                            city,
                            new HashSet<>(Arrays.asList("Bristol", "London")),
                            rule())))),
            new Rule(
                "US country constrains currency",
                Collections.singletonList(
                    new ConditionalConstraint(
                        new IsEqualToConstantConstraint(
                            country,
                            "US",
                            rule()),
                        new IsEqualToConstantConstraint(
                            currency,
                            "USD",
                            rule())))),
            new Rule(
                "GB country constrains currency",
                Collections.singletonList(
                    new ConditionalConstraint(
                        new IsEqualToConstantConstraint(
                            country,
                            "GB",
                            rule()),
                        new IsEqualToConstantConstraint(
                            currency,
                            "GBP",
                            rule())))));

        Profile profile = new Profile(fields, dummyRules);

        final DecisionTreeCollection analysedProfile = this.dTreeGenerator.analyse(profile);

        DecisionTree merged = analysedProfile.getMergedTree();

        final List<RowSpec> rowSpecs = dTreeWalker
            .walk(merged)
            .collect(Collectors.toList());

        Assert.assertThat(rowSpecs, notNullValue());
    }

    private static ConstraintRule rule(){
        return ConstraintRule.fromDescription("rule");
    }
}
