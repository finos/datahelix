package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.Rule;
import com.scottlogic.deg.common.profile.RuleInformation;
import com.scottlogic.deg.common.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.RowSpecDataBagGenerator;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpecMerger;
import com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory;
import com.scottlogic.deg.generator.walker.CartesianProductDecisionTreeWalker;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CartesianProductDecisionTreeWalkerTests {
    private final FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();
    private RowSpecDataBagGenerator dataBagSourceFactory = mock(RowSpecDataBagGenerator.class);
    private final CartesianProductDecisionTreeWalker dTreeWalker = new CartesianProductDecisionTreeWalker(
            new ConstraintReducer(
                    new FieldSpecFactory(new StringRestrictionsFactory()),
                    fieldSpecMerger
            ),
            new RowSpecMerger(
                    fieldSpecMerger
            ),
        dataBagSourceFactory);
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
                            Collections.singleton("US"),
                            rules()),
                        new IsInSetConstraint(
                            city,
                            new HashSet<>(Arrays.asList("New York", "Washington DC")),
                            rules())))),
            new Rule(
                rule("GB country constrains city"),
                Collections.singletonList(
                    new ConditionalConstraint(
                        new IsInSetConstraint(
                            country,
                            Collections.singleton("GB"),
                            rules()),
                        new IsInSetConstraint(
                            city,
                            new HashSet<>(Arrays.asList("Bristol", "London")),
                            rules())))),
            new Rule(
                rule("US country constrains currency"),
                Collections.singletonList(
                    new ConditionalConstraint(
                        new IsInSetConstraint(
                            country,
                            Collections.singleton("US"),
                            rules()),
                        new IsInSetConstraint(
                            currency,
                            Collections.singleton("USD"),
                            rules())))),
            new Rule(
                rule("GB country constrains currency"),
                Collections.singletonList(
                    new ConditionalConstraint(
                        new IsInSetConstraint(
                            country,
                            Collections.singleton("GB"),
                            rules()),
                        new IsInSetConstraint(
                            currency,
                            Collections.singleton("GBP"),
                            rules())))));

        Profile profile = new Profile(fields, dummyRules);

        final DecisionTree merged = this.dTreeGenerator.analyse(profile);

        final List<DataBag> rowSpecs = dTreeWalker
            .walk(merged)
            .collect(Collectors.toList());

        Assert.assertThat(rowSpecs, notNullValue());
    }

    private static Set<RuleInformation> rules(){
        return Collections.singleton(rule("rules"));
    }

    private static RuleInformation rule(String description){
        return new RuleInformation(description);
    }
}
