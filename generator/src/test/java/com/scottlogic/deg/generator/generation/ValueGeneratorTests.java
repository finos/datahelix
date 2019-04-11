package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.generation.rows.Value;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.StringConstraintsCollection;
import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertTrue;

class ValueGeneratorTests {
    private final NullRestrictions notNull = new NullRestrictions(Nullness.MUST_NOT_BE_NULL);
    private final FieldSpecSource fieldSpecSource = FieldSpecSource.Empty;

    @Test
    void generate_rootFieldSpecContainsDuplicateMustContainRestriction_returnsValuesWithNoDuplicates() {
        FieldSpec fieldSpec = FieldSpec.Empty
            .withNullRestrictions(notNull, fieldSpecSource)
            .withSetRestrictions(
                new SetRestrictions(
                    new HashSet<>(
                        Arrays.asList(1, 5, 10)
                    ),
                    null
                ),
                fieldSpecSource)
            .withMustContainRestriction(
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
                                fieldSpecSource
                            ).withNullRestrictions(notNull, fieldSpecSource)
                        )
                    )
                )
            );
        GenerationConfig config = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE)
        );
        ValueGenerator fieldSpecFulfiller = new ValueGenerator(
            config,
            new StandardFieldValueSourceEvaluator(),
            new JavaUtilRandomNumberGenerator());

        final Set<Value> result = fieldSpecFulfiller.generate(new Field("First Field"), fieldSpec)
            .collect(Collectors.toSet());

        Set<Value> expectedRows = new HashSet<>(
            Arrays.asList(
                new Value(
                    new Field("First Field"), 1),
                new Value(
                    new Field("First Field"),5),
                new Value(
                    new Field("First Field"), 10)
            )
        );

        assertThat(result, sameBeanAs(expectedRows));
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
                            new SetRestrictions(
                                new HashSet<>(
                                    Arrays.asList(15, 25)),
                                null),
                            fieldSpecSource),
                        rootFieldSpec.withSetRestrictions(
                            new SetRestrictions(
                                null,
                                new HashSet<>(
                                    Arrays.asList(15, 25))),
                            fieldSpecSource
                        )
                    )
                )
            )
        );
        GenerationConfig generationConfig = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE)
        );
        ValueGenerator fieldSpecFulfiller = new ValueGenerator(
            generationConfig,
            new StandardFieldValueSourceEvaluator(),
            new JavaUtilRandomNumberGenerator());

        final Set<Value> result = fieldSpecFulfiller.generate(new Field("First Field"), fieldSpec)
            .collect(Collectors.toSet());

        Set<Value> expectedRows = new HashSet<>(
            Arrays.asList(
                new Value(
                    new Field("First Field"),
                    new BigDecimal("10.00000000000000000001")),
                new Value(
                    new Field("First Field"),
                    new BigDecimal("10.00000000000000000002")),
                new Value(
                    new Field("First Field"),
                    new BigDecimal("15")),
                new Value(
                    new Field("First Field"),
                    new BigDecimal("25")),
                new Value(
                    new Field("First Field"),
                    new BigDecimal("29.99999999999999999998")),
                new Value(
                    new Field("First Field"),
                    new BigDecimal("29.99999999999999999999"))
            )
        );

        assertThat(result, sameBeanAs(expectedRows));
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
                                new SetRestrictions(
                                    new HashSet<>(
                                        Arrays.asList("Test One", "Test Two")
                                    ),
                                    null
                                ),
                                fieldSpecSource
                            )
                        )
                    )
                )
            );
        GenerationConfig generationConfig = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE)
        );
        ValueGenerator fieldSpecFulfiller = new ValueGenerator(
            generationConfig,
            new StandardFieldValueSourceEvaluator(),
            new JavaUtilRandomNumberGenerator());

        final Set<Value> result = fieldSpecFulfiller.generate(new Field("First Field"), fieldSpec).collect(Collectors.toSet());

        assertTrue(result.size() > 2);
        assertTrue(
            result.contains(
                new Value(
                    new Field("First Field"),"Test One")
            ) &&
                result.contains(
                    new Value(
                        new Field("First Field"),"Test Two")));
    }

    @Test
    void generate_fieldSpecContainsStringValuesForMustContainRestrictionAndFieldSpecContainsRegexConstraint_returnsValuesFromMustContainRestriction() {
        FieldSpec fieldSpec = FieldSpec.Empty
            .withNullRestrictions(notNull, fieldSpecSource)
            .withStringRestrictions(
                new StringRestrictions(new StringConstraintsCollection(Collections.emptySet())) {{
                    stringGenerator = new RegexStringGenerator("/[ab]{2}/", true);
                }},
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
                                new SetRestrictions(
                                    new HashSet<>(
                                        Arrays.asList("ba", "ab")
                                    ),
                                    null
                                ),
                                fieldSpecSource
                            )
                                .withNullRestrictions(notNull, fieldSpecSource)
                        )
                    )
                )
            );
        GenerationConfig generationConfig = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE)
        );
        ValueGenerator fieldSpecFulfiller = new ValueGenerator(
            generationConfig,
            new StandardFieldValueSourceEvaluator(),
            new JavaUtilRandomNumberGenerator());

        final Set<Value> result = fieldSpecFulfiller.generate(new Field("First Field"), fieldSpec).collect(Collectors.toSet());

        Set<Value> expectedRows = new HashSet<>(
            Arrays.asList(
                new Value(
                    new Field("First Field"),"ba"),
                new Value(
                    new Field("First Field"), "ab")
            )
        );

        assertThat(result, sameBeanAs(expectedRows));
    }

    @Test
    void generate_fieldSpecMustContainRestrictionNullAndSetRestrictionsHasValues_returnsRowsWithValuesInSetRestrictions() {
        FieldSpec fieldSpec = FieldSpec.Empty
            .withNullRestrictions(notNull, fieldSpecSource)
            .withSetRestrictions(
                new SetRestrictions(
                    new HashSet<>(
                        Arrays.asList(
                            10, 20, 30
                        )
                    ),
                    null
                ),
                fieldSpecSource);
        GenerationConfig generationConfig = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE)
        );
        ValueGenerator fieldSpecFulfiller = new ValueGenerator(
            generationConfig,
            new StandardFieldValueSourceEvaluator(),
            new JavaUtilRandomNumberGenerator());

        final Set<Value> result = fieldSpecFulfiller.generate(new Field("First Field"), fieldSpec).collect(Collectors.toSet());

        Set<Value> expectedRows = new HashSet<>(
            Arrays.asList(
                new Value(
                    new Field("First Field"),10),
                new Value(
                    new Field("First Field"), 20),
                new Value(
                    new Field("First Field"),30)
            )
        );

        assertThat(result, sameBeanAs(expectedRows));
    }

    @Test
    void generate_fieldSpecMustContainRestrictionNullAndNumericRestrictionApplied_returnsExpectedRowsForNumericRestriction() {
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
        GenerationConfig generationConfig = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE)
        );
        ValueGenerator fieldSpecFulfiller = new ValueGenerator(
            generationConfig,
            new StandardFieldValueSourceEvaluator(),
            new JavaUtilRandomNumberGenerator());

        final Set<Value> result = fieldSpecFulfiller.generate(new Field("First Field"), fieldSpec).collect(Collectors.toSet());

        Set<Value> expectedRows = new HashSet<>(
            Arrays.asList(
                new Value(
                    new Field("First Field"),
                    new BigDecimal("10.00000000000000000001")),
                new Value(
                    new Field("First Field"),
                    new BigDecimal("10.00000000000000000002")),
                new Value(
                    new Field("First Field"),
                    new BigDecimal("29.99999999999999999998")),
                new Value(
                    new Field("First Field"),
                    new BigDecimal("29.99999999999999999999")),
                new Value(
                    new Field("First Field"),
                    null, null, fieldSpec.getFieldSpecSource())
            )
        );

        assertThat(result, sameBeanAs(expectedRows));
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

        GenerationConfig generationConfig = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.FULL_SEQUENTIAL,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE)
        );
        ValueGenerator fieldSpecFulfiller = new ValueGenerator(
            generationConfig,
            new StandardFieldValueSourceEvaluator(),
            new JavaUtilRandomNumberGenerator());

        //Act
        final Set<Value> result = fieldSpecFulfiller.generate(new Field("First Field"), rootFieldSpec).collect(Collectors.toSet());

        //Assert
        ArrayList<Object> valuesForFirstField = new ArrayList<>();
        result.forEach(row -> valuesForFirstField.add(row.getValue()));
        
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

        GenerationConfig generationConfig = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.FULL_SEQUENTIAL,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE)
        );
        ValueGenerator fieldSpecFulfiller = new ValueGenerator(
            generationConfig,
            new StandardFieldValueSourceEvaluator(),
            new JavaUtilRandomNumberGenerator());

        //Act
        final Set<Value> result = fieldSpecFulfiller.generate(new Field("First Field"), rootFieldSpec).collect(Collectors.toSet());

        //Assert
        ArrayList<Object> valuesForFirstField = new ArrayList<>();
        result.forEach(row -> {
            valuesForFirstField.add(row.getValue());
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
