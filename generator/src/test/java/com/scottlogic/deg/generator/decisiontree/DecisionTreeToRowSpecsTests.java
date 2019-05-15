package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.Rule;
import com.scottlogic.deg.common.profile.RuleInformation;
import com.scottlogic.deg.common.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpecMerger;
import com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory;
import com.scottlogic.deg.generator.walker.CartesianProductDecisionTreeWalker;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class DecisionTreeToRowSpecsTests {
    private final FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();
    private final CartesianProductDecisionTreeWalker dTreeWalker = new CartesianProductDecisionTreeWalker(
        new ConstraintReducer(
            new FieldSpecFactory(new FieldSpecMerger(), new StringRestrictionsFactory()),
            fieldSpecMerger
        ),
        new RowSpecMerger(
            fieldSpecMerger
        )
    );
    private final ProfileDecisionTreeFactory dTreeGenerator = new ProfileDecisionTreeFactory();

    @Test
    public void test() {
        final DecisionTree dTree = dTreeGenerator.analyse(makeProfile());
        final List<RowSpec> rowSpecs = dTreeWalker
            .walk(dTree)
            .collect(Collectors.toList());
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
                    rule("US country constrains city"),
                    Collections.singletonList(
                        new ConditionalConstraint(
                            new IsInSetConstraint(
                                country,
                                Collections.singleton("US"),
                                rules()
                            ),
                            new IsInSetConstraint(
                                city,
                                new HashSet<>(Arrays.asList("New York", "Washington DC")),
                                rules()
                            )
                        )
                    )
                ),
                new Rule(
                    rule("GB country constrains city"),
                    Collections.singletonList(
                        new ConditionalConstraint(
                            new IsInSetConstraint(
                                country,
                                Collections.singleton("GB"),
                                rules()
                            ),
                            new IsInSetConstraint(
                                city,
                                new HashSet<>(Arrays.asList("Bristol", "London")),
                                rules()
                            )
                        )
                    )
                ),
                new Rule(
                    rule("US country constrains currency"),
                    Collections.singletonList(
                        new ConditionalConstraint(
                            new IsInSetConstraint(
                                country,
                                Collections.singleton("US"),
                                rules()
                            ),
                            new IsInSetConstraint(
                                currency,
                                Collections.singleton("USD"),
                                rules()
                            )
                        )
                    )
                ),
                new Rule(
                    rule("GB country constrains currency"),
                    Collections.singletonList(
                        new ConditionalConstraint(
                            new IsInSetConstraint(
                                country,
                                Collections.singleton("GB"),
                                rules()
                            ),
                            new IsInSetConstraint(
                                currency,
                                Collections.singleton("GBP"),
                                rules()
                            )
                        )
                    )
                )
            )
        );
    }

    private static Set<RuleInformation> rules(){
        return Collections.singleton(rule("rules"));
    }

    private static RuleInformation rule(String description){
        return new RuleInformation(description);
    }
}
