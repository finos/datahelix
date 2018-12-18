package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.DataBagValue;
import com.scottlogic.deg.generator.DataBagValueSource;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.restrictions.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class FieldSpecFulfillerTests {
    @Test
    void generate_fieldSpecContainsSingleMustContainRestriction_returnsValuesWithValueInMustContainRestriction() {
        FieldSpec fieldSpec = new FieldSpec(
            new SetRestrictions(
                new HashSet<>(
                    Arrays.asList(1, 10)
                ),
                null
            ),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            new MustContainRestriction(
                new HashSet<>(
                    Collections.singletonList(
                        FieldSpec.Empty.withSetRestrictions(
                            new SetRestrictions(
                                new HashSet<>(
                                    Collections.singletonList(5)
                                ),
                                null
                            ),
                            null
                        )
                    )
                )
            ),
            null
        );
        FieldSpecFulfiller fieldSpecFulfiller = new FieldSpecFulfiller(new Field("First Field"), fieldSpec);

        final Set<DataBag> result = fieldSpecFulfiller.generate(
            new GenerationConfig(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE
            )
        ).collect(Collectors.toSet());

        Set<DataBag> expectedDataBags = new HashSet<>(
            Arrays.asList(
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(1, new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build(),
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(5, new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build(),
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(10, new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build()
            )
        );

        assertEquals(expectedDataBags, result);
    }

    @Test
    void generate_fieldSpecContainsMultipleMustContainRestrictionsWithNumericRestrictions_returnsInterestingValuesWithMustContainRestrictionsApplied() {
        FieldSpec fieldSpec = new FieldSpec(
            null,
            new NumericRestrictions() {{
                min = new NumericLimit<>(new BigDecimal(10), false);
                max = new NumericLimit<>(new BigDecimal(30), false);
            }},
            null,
            new NullRestrictions(NullRestrictions.Nullness.MUST_NOT_BE_NULL),
            new DataTypeRestrictions(Collections.singletonList(
                IsOfTypeConstraint.Types.NUMERIC
            )),
            null,
            null,
            null,
            new MustContainRestriction(
                new HashSet<>(
                    Collections.singletonList(
                        FieldSpec.Empty.withSetRestrictions(
                            new SetRestrictions(
                                new HashSet<>(
                                    Arrays.asList(15, 25)
                                ),
                                null
                            ),
                            null
                        )
                    )
                )
            ),
            null
        );
        FieldSpecFulfiller fieldSpecFulfiller = new FieldSpecFulfiller(new Field("First Field"), fieldSpec);

        final Set<DataBag> result = fieldSpecFulfiller.generate(
            new GenerationConfig(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE
            )
        ).collect(Collectors.toSet());

        Set<DataBag> expectedDataBags = new HashSet<>(
            Arrays.asList(
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(11, new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build(),
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(15, new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build(),
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(25, new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build(),
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(29, new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build()
            )
        );

        assertEquals(expectedDataBags, result);
    }

    @Test
    void generate_fieldSpecContainsStringValuesForMustContainRestrictionAndFieldSpecIsStringType_returnsValuesThatIncludeValuesInMustContain() {
        FieldSpec fieldSpec = new FieldSpec(
            null,
            null,
            null,
            new NullRestrictions(NullRestrictions.Nullness.MUST_NOT_BE_NULL),
            new DataTypeRestrictions(
                Collections.singletonList(
                    IsOfTypeConstraint.Types.STRING
                )
            ),
            null,
            null,
            null,
            new MustContainRestriction(
                new HashSet<>(
                    Collections.singletonList(
                        FieldSpec.Empty.withSetRestrictions(
                            new SetRestrictions(
                                new HashSet<>(
                                    Arrays.asList("Test One", "Test Two")
                                ),
                                null
                            ),
                            null
                        )
                    )
                )
            ),
            null
        );
        FieldSpecFulfiller fieldSpecFulfiller = new FieldSpecFulfiller(new Field("First Field"), fieldSpec);

        final Set<DataBag> result = fieldSpecFulfiller.generate(
            new GenerationConfig(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE
            )
        ).collect(Collectors.toSet());

        assertTrue(result.size() > 2);
        assertTrue(
            result.contains(
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue("Test One", new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build()
            ) &&
            result.contains(
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue("Test Two", new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build()
            )
        );
    }

    @Test
    void generate_fieldSpecContainsStringValuesForMustContainRestrictionAndFieldSpecContainsRegexConstraint_returnsValuesFromMustContainRestriction() {
        FieldSpec fieldSpec = new FieldSpec(
            null,
            null,
            new StringRestrictions() {{
                stringGenerator = new RegexStringGenerator("/[ab]{2}/", true);
            }},
            new NullRestrictions(NullRestrictions.Nullness.MUST_NOT_BE_NULL),
            new DataTypeRestrictions(
                Collections.singletonList(
                    IsOfTypeConstraint.Types.STRING
                )
            ),
            null,
            null,
            null,
            new MustContainRestriction(
                new HashSet<>(
                    Collections.singletonList(
                        FieldSpec.Empty.withSetRestrictions(
                            new SetRestrictions(
                                new HashSet<>(
                                    Arrays.asList("ba", "ab")
                                ),
                                null
                            ),
                            null
                        )
                    )
                )
            ),
            null
        );
        FieldSpecFulfiller fieldSpecFulfiller = new FieldSpecFulfiller(new Field("First Field"), fieldSpec);

        final Set<DataBag> result = fieldSpecFulfiller.generate(
            new GenerationConfig(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE
            )
        ).collect(Collectors.toSet());

        Set<DataBag> expectedDataBags = new HashSet<>(
            Arrays.asList(
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue("/aa/", new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build(),
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue("ba", new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build(),
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue("ab", new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build()
            )
        );

        assertEquals(expectedDataBags, result);
    }

    @Test
    void generate_fieldSpecMustContainRestrictionNullAndSetRestrictionsHasValues_returnsDataBagsWithValuesInSetRestrictions() {
        FieldSpec fieldSpec = FieldSpec.Empty.withSetRestrictions(
            new SetRestrictions(
                new HashSet<>(
                    Arrays.asList(
                        10, 20, 30
                    )
                ),
                null
            ),
            null
        );

        FieldSpecFulfiller fieldSpecFulfiller = new FieldSpecFulfiller(new Field("First Field"), fieldSpec);

        final Set<DataBag> result = fieldSpecFulfiller.generate(
            new GenerationConfig(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE
            )
        ).collect(Collectors.toSet());

        Set<DataBag> expectedDataBags = new HashSet<>(
            Arrays.asList(
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(10, new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build(),
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(20, new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build(),
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(30, new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build()
            )
        );

        assertEquals(expectedDataBags, result);
    }

    @Test
    void generate_fieldSpecMustContainRestrictionNullAndNumericRestrictionApplied_returnsExpectedDataBagsForNumericRestriction() {
        FieldSpec fieldSpec = new FieldSpec(
            null,
            new NumericRestrictions() {{
                min = new NumericLimit<>(new BigDecimal(10), false);
                max = new NumericLimit<>(new BigDecimal(30), false);
            }},
            null,
            null,
            new DataTypeRestrictions(
                Collections.singletonList(IsOfTypeConstraint.Types.NUMERIC)
            ),
            null,
            null,
            null,
            null,
            null
        );
        FieldSpecFulfiller fieldSpecFulfiller = new FieldSpecFulfiller(new Field("First Field"), fieldSpec);

        final Set<DataBag> result = fieldSpecFulfiller.generate(
            new GenerationConfig(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE
            )
        ).collect(Collectors.toSet());

        Set<DataBag> expectedDataBags = new HashSet<>(
            Arrays.asList(
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(11, new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build(),
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(29, new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build(),
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(null, new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build()
            )
        );

        assertEquals(expectedDataBags, result);
    }
}
