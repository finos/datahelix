package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.inputs.AtomicConstraintReaderLookup;
import com.scottlogic.deg.generator.inputs.ConstraintReader;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.schemas.v3.AtomicConstraintType;
import com.scottlogic.deg.schemas.v3.ConstraintDTO;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AtomicConstraintTests {

    AtomicConstraintReaderLookup atomicConstraintReaderLookup;
    ProfileFields profileFields;

    @BeforeAll
    public void before() {

        atomicConstraintReaderLookup = new AtomicConstraintReaderLookup();

        List<Field> fields = new ArrayList<>();

        fields.add(new Field("test"));

        profileFields = new ProfileFields(fields);
    }

    private static Stream<Arguments> testProvider() {

        ConstraintDTO stringValueDto = new ConstraintDTO();
        stringValueDto.field = "test";
        stringValueDto.is = AtomicConstraintType.ISEQUALTOCONSTANT.toString();
        stringValueDto.value = "value";

        ConstraintDTO numberValueDto = new ConstraintDTO();
        numberValueDto.field = "test";
        numberValueDto.value = 10;

        ConstraintDTO dateValueDto = new ConstraintDTO();
        dateValueDto.field = "test";

        Map date = new HashMap();
        date.put("date", "2020-01-01T01:02:03.456");
        dateValueDto.value = date;

        ConstraintDTO multipleValuesDto = new ConstraintDTO();
        multipleValuesDto.field = "test";
        multipleValuesDto.values = Arrays.asList("A", "B");

        ConstraintDTO typeValueDto = new ConstraintDTO();
        typeValueDto.field = "test";
        typeValueDto.value = "string";

        ConstraintDTO notValueDto = new ConstraintDTO();
        notValueDto.field = "test";
        notValueDto.not = stringValueDto;

        return Stream.of(
                Arguments.of(AtomicConstraintType.ISEQUALTOCONSTANT, stringValueDto, IsEqualToConstantConstraint.class),
                Arguments.of(AtomicConstraintType.ISINSET, multipleValuesDto, IsInSetConstraint.class),
                Arguments.of(AtomicConstraintType.ISNULL, stringValueDto, IsNullConstraint.class),
                Arguments.of(AtomicConstraintType.ISOFTYPE, typeValueDto, IsOfTypeConstraint.class),
                Arguments.of(AtomicConstraintType.MATCHESREGEX, stringValueDto, MatchesRegexConstraint.class),
                Arguments.of(AtomicConstraintType.FORMATTEDAS, stringValueDto, FormatConstraint.class),
                Arguments.of(AtomicConstraintType.HASLENGTH, numberValueDto, StringHasLengthConstraint.class),
                Arguments.of(AtomicConstraintType.ISSTRINGLONGERTHAN, numberValueDto, IsStringLongerThanConstraint.class),
                Arguments.of(AtomicConstraintType.ISSTRINGSHORTERTHAN, numberValueDto, IsStringShorterThanConstraint.class),
                Arguments.of(AtomicConstraintType.ISGREATERTHANCONSTANT, numberValueDto, IsGreaterThanConstantConstraint.class),
                Arguments.of(AtomicConstraintType.ISGREATERTHANOREQUALTOCONSTANT, numberValueDto, IsGreaterThanOrEqualToConstantConstraint.class),
                Arguments.of(AtomicConstraintType.ISLESSTHANCONSTANT, numberValueDto, IsLessThanConstantConstraint.class),
                Arguments.of(AtomicConstraintType.ISLESSTHANOREQUALTOCONSTANT, numberValueDto, IsLessThanOrEqualToConstantConstraint.class),
                Arguments.of(AtomicConstraintType.ISAFTERCONSTANTDATETIME, dateValueDto, IsAfterConstantDateTimeConstraint.class),
                Arguments.of(AtomicConstraintType.ISAFTEROREQUALTOCONSTANTDATETIME, dateValueDto, IsAfterOrEqualToConstantDateTimeConstraint.class),
                Arguments.of(AtomicConstraintType.ISBEFORECONSTANTDATETIME, dateValueDto, IsBeforeConstantDateTimeConstraint.class),
                Arguments.of(AtomicConstraintType.ISBEFORECONSTANTDATETIME, dateValueDto, IsBeforeConstantDateTimeConstraint.class),
                Arguments.of(AtomicConstraintType.ISBEFOREOREQUALTOCONSTANTDATETIME, dateValueDto, IsBeforeOrEqualToConstantDateTimeConstraint.class));
    }

    @Test
    public void shouldRecogniseAllAtomicConstraints() {

        List<String> missingConstraints = new ArrayList<String>();

        for (AtomicConstraintType type : AtomicConstraintType.values()) {
            ConstraintReader reader = atomicConstraintReaderLookup.getByTypeCode(type.toString());
            // Assert.assertNotNull("No reader found for constraint type '" + type.toString() + "'", reader);
            if (reader == null) {
                missingConstraints.add(type.toString());
            }
        }

        String message = "No constraint reader is associated with the following constraints: " +
                String.join(", ", missingConstraints);

        Assert.assertThat(
                message,
                missingConstraints,
                hasSize(0));

    }

    @DisplayName("Should return correct constraint type")
    @ParameterizedTest(name = "{0} should return {1}")
    @MethodSource("testProvider")
    public void testAtomicConstraintReader(AtomicConstraintType type, ConstraintDTO dto, Class<?> constraintType) {
        ConstraintReader reader = atomicConstraintReaderLookup.getByTypeCode(type.toString());

        try {
            Constraint constraint = reader.apply(dto, profileFields);

            Assert.assertThat("Expected " + constraintType.getName() + " but got " + constraint.getClass().getName(),
                    constraint,
                    instanceOf(constraintType));

        } catch (InvalidProfileException ex) {
            Assert.fail(ex.getMessage());
        }
    }

}
