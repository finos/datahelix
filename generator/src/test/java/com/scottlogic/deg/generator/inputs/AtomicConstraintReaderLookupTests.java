package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.schemas.v0_1.AtomicConstraintType;
import com.scottlogic.deg.schemas.v0_1.ConstraintDTO;
import com.scottlogic.deg.schemas.v0_1.RuleDTO;
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

        Map date = new HashMap();
        date.put("date", "2020-01-01T01:02:03.456");
        dateValueDto.value = date;

        ConstraintDTO multipleValuesDto = new ConstraintDTO();
        multipleValuesDto.field = "test";
        multipleValuesDto.values = Arrays.asList("A", "B");

        ConstraintDTO typeValueDto = new ConstraintDTO();
        typeValueDto.field = "test";
        typeValueDto.value = "string";

        ConstraintDTO integerTypeValueDto = new ConstraintDTO();
        integerTypeValueDto.field = "test";
        integerTypeValueDto.value = "integer";

        ConstraintDTO decimalTypeValueDto = new ConstraintDTO();
        decimalTypeValueDto.field = "test";
        decimalTypeValueDto.value = "decimal";

        ConstraintDTO notValueDto = new ConstraintDTO();
        notValueDto.field = "test";
        notValueDto.not = stringValueDto;

        return Stream.of(
                Arguments.of(AtomicConstraintType.ISEQUALTOCONSTANT, stringValueDto, IsInSetConstraint.class),
                Arguments.of(AtomicConstraintType.ISINSET, multipleValuesDto, IsInSetConstraint.class),
                Arguments.of(AtomicConstraintType.ISNULL, stringValueDto, IsNullConstraint.class),
                Arguments.of(AtomicConstraintType.ISOFTYPE, typeValueDto, IsOfTypeConstraint.class),
                Arguments.of(AtomicConstraintType.ISOFTYPE, integerTypeValueDto, AndConstraint.class),
                Arguments.of(AtomicConstraintType.ISOFTYPE, decimalTypeValueDto, IsOfTypeConstraint.class),
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

    private static Stream<Arguments> ofTypeInvalidValueProvider() {
        ConstraintDTO numericTypeValueDto = new ConstraintDTO();
        numericTypeValueDto.field = "test";
        numericTypeValueDto.value = "numeric";

        ConstraintDTO invalidTypeValueDto = new ConstraintDTO();
        invalidTypeValueDto.field = "test";
        invalidTypeValueDto.value = "garbage";

        return Stream.of(
            Arguments.of(AtomicConstraintType.ISOFTYPE, numericTypeValueDto),
            Arguments.of(AtomicConstraintType.ISOFTYPE, invalidTypeValueDto)
        );
    }

    private static Stream<Arguments> numericOutOfBoundsOperandProvider() {

        ConstraintDTO maxValueDtoPlusOne = new ConstraintDTO();
        maxValueDtoPlusOne.field = "test";
        maxValueDtoPlusOne.value = GenerationConfig.Constants.NUMERIC_MAX.add(BigDecimal.ONE);

        ConstraintDTO minValueDtoMinusOne = new ConstraintDTO();
        minValueDtoMinusOne.field = "test";
        minValueDtoMinusOne.value = GenerationConfig.Constants.NUMERIC_MIN.subtract(BigDecimal.ONE);

        return Stream.of(
            Arguments.of(AtomicConstraintType.ISEQUALTOCONSTANT, maxValueDtoPlusOne),
            Arguments.of(AtomicConstraintType.ISINSET, maxValueDtoPlusOne),
            Arguments.of(AtomicConstraintType.ISGREATERTHANOREQUALTOCONSTANT, maxValueDtoPlusOne),
            Arguments.of(AtomicConstraintType.ISGREATERTHANCONSTANT, maxValueDtoPlusOne),
            Arguments.of(AtomicConstraintType.ISLESSTHANOREQUALTOCONSTANT, maxValueDtoPlusOne),
            Arguments.of(AtomicConstraintType.ISLESSTHANCONSTANT, maxValueDtoPlusOne),

            Arguments.of(AtomicConstraintType.ISEQUALTOCONSTANT, minValueDtoMinusOne),
            Arguments.of(AtomicConstraintType.ISINSET, minValueDtoMinusOne),
            Arguments.of(AtomicConstraintType.ISGREATERTHANOREQUALTOCONSTANT, minValueDtoMinusOne),
            Arguments.of(AtomicConstraintType.ISGREATERTHANCONSTANT, minValueDtoMinusOne),
            Arguments.of(AtomicConstraintType.ISLESSTHANOREQUALTOCONSTANT, minValueDtoMinusOne),
            Arguments.of(AtomicConstraintType.ISLESSTHANCONSTANT, minValueDtoMinusOne)
        );
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

    @DisplayName("Should fail when operators have an invalid operand")
    @ParameterizedTest(name = "{0} should be invalid")
    @MethodSource({"stringLengthInvalidOperandProvider", "ofTypeInvalidValueProvider"})
    public void testAtomicConstraintReaderWithInvalidOperands(AtomicConstraintType type, ConstraintDTO dto) {
        ConstraintReader reader = atomicConstraintReaderLookup.getByTypeCode(type.toString());

        RuleDTO rule = new RuleDTO();
        rule.rule = "rule";
        Set<RuleInformation> ruleInformation = Collections.singleton(new RuleInformation(rule));

        Assertions.assertThrows(InvalidProfileException.class, () -> reader.apply(dto, profileFields, ruleInformation));
    }

    @DisplayName("Should fail when value property is numeric and out of bounds")
    @ParameterizedTest(name = "{0} should be invalid")
    @MethodSource("numericOutOfBoundsOperandProvider")
    public void testAtomicConstraintReaderWithOutOfBoundValues(AtomicConstraintType type, ConstraintDTO dto) {
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
    public void parseDate_withDateAtYear0000_shouldThrowInvalidProfileException() {
        Assertions.assertThrows(
            InvalidProfileException.class,
            () -> AtomicConstraintReaderLookup.parseDate("0000-01-01T00:00:00.000"));
    }

    @Test
    public void parseDate_withDateBefore0000_shouldThrowInvalidProfileException() {
        Assertions.assertThrows(
            InvalidProfileException.class,
            () -> AtomicConstraintReaderLookup.parseDate("-00001-01-01T00:00:00.000"));
    }

    @Test
    public void parseDate_withDateAfter9999_shouldThrowInvalidProfileException(){
        Assertions.assertThrows(
            InvalidProfileException.class,
            () -> AtomicConstraintReaderLookup.parseDate("10000-01-01T00:00:00.000"));
    }

    @Test
    public void parseDate_withDateAtStartOf0001_shouldNotThrow(){
        Assertions.assertDoesNotThrow(
            () -> AtomicConstraintReaderLookup.parseDate("0001-01-01T00:00:00.000"));
    }

    @Test
    public void parseDate_withDateAtEndOf9999_shouldNotThrow(){
        Assertions.assertDoesNotThrow(
            () -> AtomicConstraintReaderLookup.parseDate("9999-12-31T23:59:59.999"));
    }

    @Test
    public void parseDate_withAnInvalidDateShouldThrow(){
        Assertions.assertThrows(
            InvalidProfileException.class,
            () -> AtomicConstraintReaderLookup.parseDate("2019-02-29T00:00:00.000"));
    }

    @Test
    public void parseDate_withAnInvalidTimeShouldThrow(){
        Assertions.assertThrows(
            InvalidProfileException.class,
            () -> AtomicConstraintReaderLookup.parseDate("2019-01-01T24:00:00.000"));
    }
}
