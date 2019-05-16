package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.profile.constraintdetail.Nullness;
import com.scottlogic.deg.generator.DataBagValueSource;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;
import com.scottlogic.deg.generator.config.details.CombinationStrategyType;
import com.scottlogic.deg.generator.config.details.DataGenerationType;
import com.scottlogic.deg.generator.config.details.TreeWalkerType;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertTrue;

class FieldSpecValueGeneratorTests {
    private final NullRestrictions notNull = new NullRestrictions(Nullness.MUST_NOT_BE_NULL);
    private final FieldSpecSource fieldSpecSource = FieldSpecSource.Empty;

    @Test
    void generate_rootFieldSpecContainsDuplicateMustContainRestriction_returnsValuesWithNoDuplicates() {
        FieldSpec fieldSpec = FieldSpec.Empty
            .withNullRestrictions(notNull, fieldSpecSource)
            .withSetRestrictions(
                SetRestrictions.fromWhitelist(
                    new HashSet<>(
                        Arrays.asList(1, 5, 10)
                    )),
                fieldSpecSource)
            .withMustContainRestriction(
                new MustContainRestriction(
                    new HashSet<>(
                        Collections.singletonList(
                            FieldSpec.Empty.withSetRestrictions(
                                SetRestrictions.fromWhitelist(
                                    new HashSet<>(
                                        Collections.singletonList(5)
                                    )),
                                fieldSpecSource
                            ).withNullRestrictions(notNull, fieldSpecSource)
                        )
                    )
                )
            );
        GenerationConfigSource config = new TestGenerationConfigSource(
                DataGenerationType.INTERESTING,
                TreeWalkerType.REDUCTIVE,
                CombinationStrategyType.EXHAUSTIVE);
        FieldSpecValueGenerator fieldSpecFulfiller = new FieldSpecValueGenerator(
            config,
            new StandardFieldValueSourceEvaluator(),
            new JavaUtilRandomNumberGenerator());

        final Set<DataBag> result = fieldSpecFulfiller.generate(new Field("First Field"), fieldSpec)
            .collect(Collectors.toSet());

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

        assertThat(result, sameBeanAs(expectedDataBags));
    }

    @Test
    void generate_fieldSpecContainsMultipleMustContainRestrictionsWithNumericRestrictions_returnsInterestingValuesWithMustContainRestrictionsApplied() {
        FieldSpec rootFieldSpec = FieldSpec.Empty
            .withNullRestrictions(notNull, fieldSpecSource)
            .withNumericRestrictions(
                new NumericRestrictions() {{
                    min = new NumericLimit<>(new BigDecimal(10), false);
                    max = new NumericLimit<>(new BigDecimal(30), false);
                }},
                fieldSpecSource)
            .withTypeRestrictions(
                new DataTypeRestrictions(Collections.singletonList(
                    IsOfTypeConstraint.Types.NUMERIC
                )),
                fieldSpecSource);

        FieldSpec fieldSpec = rootFieldSpec.withMustContainRestriction(
            new MustContainRestriction(
                new HashSet<>(
                    Arrays.asList(
                        rootFieldSpec.withSetRestrictions(
                            SetRestrictions.fromWhitelist(
                                new HashSet<>(
                                    Arrays.asList(15, 25))),
                            fieldSpecSource),
                        rootFieldSpec.withSetRestrictions(
                            SetRestrictions.fromBlacklist(
                                new HashSet<>(
                                    Arrays.asList(15, 25))),
                            fieldSpecSource
                        )
                    )
                )
            )
        );
        GenerationConfigSource generationConfig =
            new TestGenerationConfigSource(
                DataGenerationType.INTERESTING,
                TreeWalkerType.REDUCTIVE,
                CombinationStrategyType.EXHAUSTIVE);
        FieldSpecValueGenerator fieldSpecFulfiller = new FieldSpecValueGenerator(
            generationConfig,
            new StandardFieldValueSourceEvaluator(),
            new JavaUtilRandomNumberGenerator());

        final Set<DataBag> result = fieldSpecFulfiller.generate(new Field("First Field"), fieldSpec)
            .collect(Collectors.toSet());

        Set<DataBag> expectedDataBags = new HashSet<>(
            Arrays.asList(
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(new BigDecimal("10.00000000000000000001"), new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build(),
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(new BigDecimal("10.00000000000000000002"), new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build(),
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(new BigDecimal("15"), new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build(),
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(new BigDecimal("25"), new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build(),
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(new BigDecimal("29.99999999999999999998"), new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build(),
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(new BigDecimal("29.99999999999999999999"), new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build()
            )
        );

        assertThat(result, sameBeanAs(expectedDataBags));
    }

    @Test
    void generate_fieldSpecContainsStringValuesForMustContainRestrictionAndFieldSpecIsStringType_returnsValuesThatIncludeValuesInMustContain() {
        FieldSpec fieldSpec = FieldSpec.Empty
            .withNullRestrictions(notNull, fieldSpecSource)
            .withTypeRestrictions(
                new DataTypeRestrictions(
                    Collections.singletonList(
                        IsOfTypeConstraint.Types.STRING
                    )
                ),
                fieldSpecSource)
            .withMustContainRestriction(
                new MustContainRestriction(
                    new HashSet<>(
                        Collections.singletonList(
                            FieldSpec.Empty.withSetRestrictions(
                                SetRestrictions.fromWhitelist(
                                    new HashSet<>(
                                        Arrays.asList("Test One", "Test Two")
                                    )),
                                fieldSpecSource
                            )
                        )
                    )
                )
            );
        GenerationConfigSource generationConfig =
            new TestGenerationConfigSource(
                DataGenerationType.INTERESTING,
                TreeWalkerType.REDUCTIVE,
                CombinationStrategyType.EXHAUSTIVE);
        FieldSpecValueGenerator fieldSpecFulfiller = new FieldSpecValueGenerator(
            generationConfig,
            new StandardFieldValueSourceEvaluator(),
            new JavaUtilRandomNumberGenerator());

        final Set<DataBag> result = fieldSpecFulfiller.generate(new Field("First Field"), fieldSpec).collect(Collectors.toSet());

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
        FieldSpec fieldSpec = FieldSpec.Empty
            .withNullRestrictions(notNull, fieldSpecSource)
            .withStringRestrictions(
                new StringRestrictionsFactory().forStringMatching(Pattern.compile("/[ab]{2}/"), true),
                fieldSpecSource)
            .withTypeRestrictions(
                new DataTypeRestrictions(
                    Collections.singletonList(
                        IsOfTypeConstraint.Types.STRING
                    )
                ),
                fieldSpecSource)
            .withMustContainRestriction(
                new MustContainRestriction(
                    new HashSet<>(
                        Collections.singletonList(
                            FieldSpec.Empty.withSetRestrictions(
                                SetRestrictions.fromWhitelist(
                                    new HashSet<>(
                                        Arrays.asList("ba", "ab")
                                    )),
                                fieldSpecSource
                            )
                                .withNullRestrictions(notNull, fieldSpecSource)
                        )
                    )
                )
            );
        GenerationConfigSource generationConfig =
            new TestGenerationConfigSource(
                DataGenerationType.INTERESTING,
                TreeWalkerType.REDUCTIVE,
                CombinationStrategyType.EXHAUSTIVE);
        FieldSpecValueGenerator fieldSpecFulfiller = new FieldSpecValueGenerator(
            generationConfig,
            new StandardFieldValueSourceEvaluator(),
            new JavaUtilRandomNumberGenerator());

        final Set<DataBag> result = fieldSpecFulfiller.generate(new Field("First Field"), fieldSpec).collect(Collectors.toSet());

        Set<DataBag> expectedDataBags = new HashSet<>(
            Arrays.asList(
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

        assertThat(result, sameBeanAs(expectedDataBags));
    }

    @Test
    void generate_fieldSpecMustContainRestrictionNullAndSetRestrictionsHasValues_returnsDataBagsWithValuesInSetRestrictions() {
        FieldSpec fieldSpec = FieldSpec.Empty
            .withNullRestrictions(notNull, fieldSpecSource)
            .withSetRestrictions(
                SetRestrictions.fromWhitelist(
                    new HashSet<>(
                        Arrays.asList(10, 20, 30))),
                fieldSpecSource);
        GenerationConfigSource generationConfig =
            new TestGenerationConfigSource(
                DataGenerationType.INTERESTING,
                TreeWalkerType.REDUCTIVE,
                CombinationStrategyType.EXHAUSTIVE);
        FieldSpecValueGenerator fieldSpecFulfiller = new FieldSpecValueGenerator(
            generationConfig,
            new StandardFieldValueSourceEvaluator(),
            new JavaUtilRandomNumberGenerator());

        final Set<DataBag> result = fieldSpecFulfiller.generate(new Field("First Field"), fieldSpec).collect(Collectors.toSet());

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

        assertThat(result, sameBeanAs(expectedDataBags));
    }

    @Test
    void generate_fieldSpecMustContainRestrictionNullAndNumericRestrictionApplied_returnsExpectedDataBagsForNumericRestriction() {
        FieldSpec fieldSpec = FieldSpec.Empty
            .withNumericRestrictions(
                new NumericRestrictions() {{
                    min = new NumericLimit<>(new BigDecimal(10), false);
                    max = new NumericLimit<>(new BigDecimal(30), false);
                }},
                fieldSpecSource)
            .withTypeRestrictions(
                new DataTypeRestrictions(
                    Collections.singletonList(IsOfTypeConstraint.Types.NUMERIC)
                ),
                fieldSpecSource);
        GenerationConfigSource generationConfig =
            new TestGenerationConfigSource(
                DataGenerationType.INTERESTING,
                TreeWalkerType.REDUCTIVE,
                CombinationStrategyType.EXHAUSTIVE);
        FieldSpecValueGenerator fieldSpecFulfiller = new FieldSpecValueGenerator(
            generationConfig,
            new StandardFieldValueSourceEvaluator(),
            new JavaUtilRandomNumberGenerator());

        final Set<DataBag> result = fieldSpecFulfiller.generate(new Field("First Field"), fieldSpec).collect(Collectors.toSet());

        Set<DataBag> expectedDataBags = new HashSet<>(
            Arrays.asList(
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(new BigDecimal("10.00000000000000000001"), new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build(),
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(new BigDecimal("10.00000000000000000002"), new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build(),
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(new BigDecimal("29.99999999999999999998"), new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build(),
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(new BigDecimal("29.99999999999999999999"), new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build(),
                DataBag.startBuilding().set(
                    new Field("First Field"),
                    new DataBagValue(null, new DataBagValueSource(fieldSpec.getFieldSpecSource()))
                ).build()
            )
        );

        assertThat(result, sameBeanAs(expectedDataBags));
    }

    @Test
    void generate_fieldSpecMustContainRestrictionWithValuesEmittedByOtherSource_returnsUniqueValues() {
        //Arrange
        FieldSpec rootFieldSpec = FieldSpec.Empty
            .withNumericRestrictions(
                new NumericRestrictions() {{
                    min = new NumericLimit<>(new BigDecimal("1E-19"), false);
                    max = new NumericLimit<>(new BigDecimal("2E-19"), false);
                }},
                fieldSpecSource)
            .withTypeRestrictions(
                new DataTypeRestrictions(
                    Collections.singletonList(IsOfTypeConstraint.Types.NUMERIC)
                ),
                fieldSpecSource)
            .withNullRestrictions(new NullRestrictions(Nullness.MUST_NOT_BE_NULL), fieldSpecSource);

        MustContainRestriction mustContainRestriction = new MustContainRestriction(
            Collections.singleton(rootFieldSpec
                .withSetRestrictions(SetRestrictions.fromWhitelist(Collections.singleton(15)), fieldSpecSource)
                .withNullRestrictions(new NullRestrictions(Nullness.MUST_NOT_BE_NULL), fieldSpecSource)
            )
        );
        rootFieldSpec.withMustContainRestriction(mustContainRestriction);

        GenerationConfigSource generationConfig =
            new TestGenerationConfigSource(
                DataGenerationType.FULL_SEQUENTIAL,
                TreeWalkerType.REDUCTIVE,
                CombinationStrategyType.EXHAUSTIVE);
        FieldSpecValueGenerator fieldSpecFulfiller = new FieldSpecValueGenerator(
            generationConfig,
            new StandardFieldValueSourceEvaluator(),
            new JavaUtilRandomNumberGenerator());

        //Act
        final Set<DataBag> result = fieldSpecFulfiller.generate(new Field("First Field"), rootFieldSpec).collect(Collectors.toSet());

        //Assert
        ArrayList<Object> valuesForFirstField = new ArrayList<>();
        result.forEach(dataBag -> {
            valuesForFirstField.add(dataBag.getValue(new Field("First Field")));
        });
        
        Assert.assertThat(valuesForFirstField, containsInAnyOrder(
            new BigDecimal("1.1E-19"),
            new BigDecimal("1.2E-19"),
            new BigDecimal("1.3E-19"),
            new BigDecimal("1.4E-19"),
            new BigDecimal("1.5E-19"),
            new BigDecimal("1.6E-19"),
            new BigDecimal("1.7E-19"),
            new BigDecimal("1.8E-19"),
            new BigDecimal("1.9E-19")
        ));
    }

    @Test
    void generate_fieldSpecMustContainRestrictionWithValuesNotEmittedByOtherSource_returnsBothSets() {
        //Arrange
        FieldSpec rootFieldSpec = FieldSpec.Empty
            .withNumericRestrictions(
                new NumericRestrictions() {{
                    min = new NumericLimit<>(new BigDecimal("1E-19"), false);
                    max = new NumericLimit<>(new BigDecimal("2E-19"), false);
                }},
                fieldSpecSource)
            .withTypeRestrictions(
                new DataTypeRestrictions(
                    Collections.singletonList(IsOfTypeConstraint.Types.NUMERIC)
                ),
                fieldSpecSource)
            .withNullRestrictions(new NullRestrictions(Nullness.MUST_NOT_BE_NULL), fieldSpecSource);

        MustContainRestriction mustContainRestriction = new MustContainRestriction(
            Collections.singleton(rootFieldSpec
                .withSetRestrictions(SetRestrictions.fromWhitelist(Collections.singleton(25)), fieldSpecSource)
                .withNullRestrictions(new NullRestrictions(Nullness.MUST_NOT_BE_NULL), fieldSpecSource)
            )
        );
        rootFieldSpec.withMustContainRestriction(mustContainRestriction);

        GenerationConfigSource generationConfig =
            new TestGenerationConfigSource(
                DataGenerationType.FULL_SEQUENTIAL,
                TreeWalkerType.REDUCTIVE,
                CombinationStrategyType.EXHAUSTIVE);
        FieldSpecValueGenerator fieldSpecFulfiller = new FieldSpecValueGenerator(
            generationConfig,
            new StandardFieldValueSourceEvaluator(),
            new JavaUtilRandomNumberGenerator());

        //Act
        final Set<DataBag> result = fieldSpecFulfiller.generate(new Field("First Field"), rootFieldSpec).collect(Collectors.toSet());

        //Assert
        ArrayList<Object> valuesForFirstField = new ArrayList<>();
        result.forEach(dataBag -> {
            valuesForFirstField.add(dataBag.getValue(new Field("First Field")));
        });

        Assert.assertThat(valuesForFirstField, containsInAnyOrder(
            new BigDecimal("1.1E-19"),
            new BigDecimal("1.2E-19"),
            new BigDecimal("1.3E-19"),
            new BigDecimal("1.4E-19"),
            new BigDecimal("1.5E-19"),
            new BigDecimal("1.6E-19"),
            new BigDecimal("1.7E-19"),
            new BigDecimal("1.8E-19"),
            new BigDecimal("1.9E-19")
            ));
    }
}
