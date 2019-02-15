package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.schemas.v3.AtomicConstraintType;
import com.scottlogic.deg.schemas.v3.ConstraintDTO;
import com.scottlogic.deg.schemas.v3.RuleDTO;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AtomicConstraintReaderLookupTests {

    AtomicConstraintReaderLookup atomicConstraintReaderLookup;
    ProfileFields profileFields;

    @BeforeAll
    public void before() {
        atomicConstraintReaderLookup = new AtomicConstraintReaderLookup();

        List<Field> fields = new ArrayList<>();

        fields.add(new Field("test"));

        profileFields = new ProfileFields(fields);
    }

    private static Object createDateObject(String dateStr) {
        Map<String, String> date = new HashMap<>();
        date.put("date", dateStr);
        return date;
    }

    private static Stream<Arguments> testProvider() {
        ConstraintDTO stringValueDto = new ConstraintDTO();
        stringValueDto.field = "test";
        stringValueDto.is = AtomicConstraintType.ISEQUALTOCONSTANT.toString();
        stringValueDto.value = "value";

        ConstraintDTO numberValueDto = new ConstraintDTO();
        numberValueDto.field = "test";
        numberValueDto.value = BigDecimal.valueOf(10);

        ConstraintDTO dateValueDto = new ConstraintDTO();
        dateValueDto.field = "test";
        dateValueDto.value = createDateObject("2020-01-01T01:02:03.456");

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
                Arguments.of(AtomicConstraintType.ISEQUALTOCONSTANT, stringValueDto, IsInSetConstraint.class),
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

    private static Stream<Arguments> stringLengthInvalidOperandProvider() {
        ConstraintDTO numberValueDto = new ConstraintDTO();
        numberValueDto.field = "test";
        numberValueDto.value = new BigDecimal(10.11);

        ConstraintDTO stringValueDto = new ConstraintDTO();
        stringValueDto.field = "test";
        stringValueDto.value = "value";

        ConstraintDTO nullValueDto = new ConstraintDTO();
        stringValueDto.field = "test";
        stringValueDto.value = null;

        return Stream.of(
            Arguments.of(AtomicConstraintType.HASLENGTH, numberValueDto),
            Arguments.of(AtomicConstraintType.ISSTRINGLONGERTHAN, numberValueDto),
            Arguments.of(AtomicConstraintType.ISSTRINGSHORTERTHAN, numberValueDto),

            Arguments.of(AtomicConstraintType.HASLENGTH, stringValueDto),
            Arguments.of(AtomicConstraintType.ISSTRINGLONGERTHAN, stringValueDto),
            Arguments.of(AtomicConstraintType.ISSTRINGSHORTERTHAN, stringValueDto),

            Arguments.of(AtomicConstraintType.HASLENGTH, nullValueDto),
            Arguments.of(AtomicConstraintType.ISSTRINGLONGERTHAN, nullValueDto),
            Arguments.of(AtomicConstraintType.ISSTRINGSHORTERTHAN, nullValueDto));
    }

    private static Stream<Arguments> stringLengthValidOperandProvider() {
        ConstraintDTO integerAsDecimalDto = new ConstraintDTO();
        integerAsDecimalDto.field = "test";
        integerAsDecimalDto.value = new BigDecimal(10);

        ConstraintDTO integerAsIntegerDto = new ConstraintDTO();
        integerAsIntegerDto.field = "test";
        integerAsIntegerDto.value = 10;

        ConstraintDTO integerAsDecimalWith0Fraction = new ConstraintDTO();
        integerAsDecimalWith0Fraction.field = "test";
        integerAsDecimalWith0Fraction.value = new BigDecimal(10.0);

        return Stream.of(
            Arguments.of(AtomicConstraintType.HASLENGTH, integerAsDecimalDto),
            Arguments.of(AtomicConstraintType.ISSTRINGLONGERTHAN, integerAsDecimalDto),
            Arguments.of(AtomicConstraintType.ISSTRINGSHORTERTHAN, integerAsDecimalDto),

            Arguments.of(AtomicConstraintType.HASLENGTH, integerAsIntegerDto),
            Arguments.of(AtomicConstraintType.ISSTRINGLONGERTHAN, integerAsIntegerDto),
            Arguments.of(AtomicConstraintType.ISSTRINGSHORTERTHAN, integerAsIntegerDto),

            Arguments.of(AtomicConstraintType.HASLENGTH, integerAsDecimalWith0Fraction),
            Arguments.of(AtomicConstraintType.ISSTRINGLONGERTHAN, integerAsDecimalWith0Fraction),
            Arguments.of(AtomicConstraintType.ISSTRINGSHORTERTHAN, integerAsDecimalWith0Fraction));
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
            RuleDTO rule = new RuleDTO();
            rule.rule = "rule";
            Set<RuleInformation> ruleInformation = Collections.singleton(new RuleInformation(rule));

            Constraint constraint = reader.apply(dto, profileFields, ruleInformation);

            Assert.assertThat("Expected " + constraintType.getName() + " but got " + constraint.getClass().getName(),
                    constraint,
                    instanceOf(constraintType));

        } catch (InvalidProfileException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @DisplayName("Should fail when string lengths have an invalid operand")
    @ParameterizedTest(name = "{0} should be invalid")
    @MethodSource("stringLengthInvalidOperandProvider")
    public void testAtomicConstraintReaderWithInvalidOperands(AtomicConstraintType type, ConstraintDTO dto) {
        ConstraintReader reader = atomicConstraintReaderLookup.getByTypeCode(type.toString());

        RuleDTO rule = new RuleDTO();
        rule.rule = "rule";
        Set<RuleInformation> ruleInformation = Collections.singleton(new RuleInformation(rule));

        Assertions.assertThrows(InvalidProfileException.class, () -> reader.apply(dto, profileFields, ruleInformation));
    }

    @DisplayName("Should pass when string lengths have an integer operand")
    @ParameterizedTest(name = "{0} should be valid")
    @MethodSource("stringLengthValidOperandProvider")
    public void testAtomicConstraintReaderWithValidOperands(AtomicConstraintType type, ConstraintDTO dto) {
        ConstraintReader reader = atomicConstraintReaderLookup.getByTypeCode(type.toString());

        RuleDTO rule = new RuleDTO();
        rule.rule = "rule";
        Set<RuleInformation> ruleInformation = Collections.singleton(new RuleInformation(rule));

        Assertions.assertDoesNotThrow(() -> reader.apply(dto, profileFields, ruleInformation));
    }

    @Test
    void shouldParseValidDates() {
        assertUnsuccessfulParse(createDateObject("2010-01-01"));
        assertUnsuccessfulParse(createDateObject("2010-01-01T12:30:00"));
        assertSuccessfulParse(createDateObject("2010-01-01T12:30:00.000"));
        assertSuccessfulParse(createDateObject("2010-01-01T12:30:00.000Z"));
        assertSuccessfulParse(createDateObject("2010-01-01T12:30:00.000+01"));
    }

    private void assertSuccessfulParse(Object value) {
        Assertions.assertDoesNotThrow(
            () -> tryParseConstraintValue(value));
    }

    private void assertUnsuccessfulParse(Object value) {
        Assertions.assertThrows(
            InvalidProfileException.class,
            () -> tryParseConstraintValue(value));
    }

    private void tryParseConstraintValue(Object value) throws InvalidProfileException {
        ConstraintReader reader = atomicConstraintReaderLookup.getByTypeCode(
            AtomicConstraintType.ISEQUALTOCONSTANT.toString());

        ConstraintDTO dateDto = new ConstraintDTO();
        dateDto.field = "test";
        dateDto.value = value;

        RuleDTO rule = new RuleDTO();
        rule.rule = "rule";
        Set<RuleInformation> ruleInformation = Collections.singleton(new RuleInformation(rule));

        reader.apply(dateDto, profileFields, ruleInformation);
    }
}
