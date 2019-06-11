package com.scottlogic.deg.profile.reader;

import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.RuleInformation;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.common.util.Defaults;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;
import com.scottlogic.deg.profile.v0_1.ConstraintDTO;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseAtomicConstraintReaderLookupTests {

    AtomicConstraintReaderLookup atomicConstraintReaderLookup;
    ProfileFields profileFields;

    @BeforeAll
    public void before() {

        atomicConstraintReaderLookup = new BaseAtomicConstraintReaderLookup();

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
        stringValueDto.is = AtomicConstraintType.IS_EQUAL_TO_CONSTANT.toString();
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
                Arguments.of(AtomicConstraintType.IS_EQUAL_TO_CONSTANT, stringValueDto, IsInSetConstraint.class),
                Arguments.of(AtomicConstraintType.IS_IN_SET, multipleValuesDto, IsInSetConstraint.class),
                Arguments.of(AtomicConstraintType.IS_NULL, stringValueDto, IsNullConstraint.class),
                Arguments.of(AtomicConstraintType.IS_OF_TYPE, typeValueDto, IsOfTypeConstraint.class),
                Arguments.of(AtomicConstraintType.IS_OF_TYPE, integerTypeValueDto, AndConstraint.class),
                Arguments.of(AtomicConstraintType.IS_OF_TYPE, decimalTypeValueDto, IsOfTypeConstraint.class),
                Arguments.of(AtomicConstraintType.MATCHES_REGEX, stringValueDto, MatchesRegexConstraint.class),
                Arguments.of(AtomicConstraintType.FORMATTED_AS, stringValueDto, FormatConstraint.class),
                Arguments.of(AtomicConstraintType.HAS_LENGTH, numberValueDto, StringHasLengthConstraint.class),
                Arguments.of(AtomicConstraintType.IS_STRING_LONGER_THAN, numberValueDto, IsStringLongerThanConstraint.class),
                Arguments.of(AtomicConstraintType.IS_STRING_SHORTER_THAN, numberValueDto, IsStringShorterThanConstraint.class),
                Arguments.of(AtomicConstraintType.IS_GREATER_THAN_CONSTANT, numberValueDto, IsGreaterThanConstantConstraint.class),
                Arguments.of(AtomicConstraintType.IS_GREATER_THAN_OR_EQUAL_TO_CONSTANT, numberValueDto, IsGreaterThanOrEqualToConstantConstraint.class),
                Arguments.of(AtomicConstraintType.IS_LESS_THAN_CONSTANT, numberValueDto, IsLessThanConstantConstraint.class),
                Arguments.of(AtomicConstraintType.IS_LESS_THAN_OR_EQUAL_TO_CONSTANT, numberValueDto, IsLessThanOrEqualToConstantConstraint.class),
                Arguments.of(AtomicConstraintType.IS_AFTER_CONSTANT_DATE_TIME, dateValueDto, IsAfterConstantDateTimeConstraint.class),
                Arguments.of(AtomicConstraintType.IS_AFTER_OR_EQUAL_TO_CONSTANT_DATE_TIME, dateValueDto, IsAfterOrEqualToConstantDateTimeConstraint.class),
                Arguments.of(AtomicConstraintType.IS_BEFORE_CONSTANT_DATE_TIME, dateValueDto, IsBeforeConstantDateTimeConstraint.class),
                Arguments.of(AtomicConstraintType.IS_BEFORE_CONSTANT_DATE_TIME, dateValueDto, IsBeforeConstantDateTimeConstraint.class),
                Arguments.of(AtomicConstraintType.IS_BEFORE_OR_EQUAL_TO_CONSTANT_DATE_TIME, dateValueDto, IsBeforeOrEqualToConstantDateTimeConstraint.class));
    }

    private static Stream<Arguments> stringLengthInvalidOperandProvider() {

        ConstraintDTO numberValueDto = new ConstraintDTO();
        numberValueDto.field = "test";
        numberValueDto.value = new BigDecimal(10.11);

        ConstraintDTO stringValueDto = new ConstraintDTO();
        stringValueDto.field = "test";
        stringValueDto.value = "value";

        ConstraintDTO nullValueDto = new ConstraintDTO();
        nullValueDto.field = "test";
        nullValueDto.value = null;

        return Stream.of(
            Arguments.of(AtomicConstraintType.HAS_LENGTH, numberValueDto),
            Arguments.of(AtomicConstraintType.IS_STRING_LONGER_THAN, numberValueDto),
            Arguments.of(AtomicConstraintType.IS_STRING_SHORTER_THAN, numberValueDto),

            Arguments.of(AtomicConstraintType.HAS_LENGTH, stringValueDto),
            Arguments.of(AtomicConstraintType.IS_STRING_LONGER_THAN, stringValueDto),
            Arguments.of(AtomicConstraintType.IS_STRING_SHORTER_THAN, stringValueDto),

            Arguments.of(AtomicConstraintType.HAS_LENGTH, nullValueDto),
            Arguments.of(AtomicConstraintType.IS_STRING_LONGER_THAN, nullValueDto),
            Arguments.of(AtomicConstraintType.IS_STRING_SHORTER_THAN, nullValueDto));
    }

    private static Stream<Arguments> ofTypeInvalidValueProvider() {
        ConstraintDTO numericTypeValueDto = new ConstraintDTO();
        numericTypeValueDto.field = "test";
        numericTypeValueDto.value = "numeric";

        ConstraintDTO invalidTypeValueDto = new ConstraintDTO();
        invalidTypeValueDto.field = "test";
        invalidTypeValueDto.value = "garbage";

        return Stream.of(
            Arguments.of(AtomicConstraintType.IS_OF_TYPE, numericTypeValueDto),
            Arguments.of(AtomicConstraintType.IS_OF_TYPE, invalidTypeValueDto)
        );
    }

    private static Stream<Arguments> numericOutOfBoundsOperandProvider() {

        ConstraintDTO maxValueDtoPlusOne = new ConstraintDTO();
        maxValueDtoPlusOne.field = "test";
        maxValueDtoPlusOne.value = Defaults.NUMERIC_MAX.add(BigDecimal.ONE);

        ConstraintDTO minValueDtoMinusOne = new ConstraintDTO();
        minValueDtoMinusOne.field = "test";
        minValueDtoMinusOne.value = Defaults.NUMERIC_MIN.subtract(BigDecimal.ONE);

        return Stream.of(
            Arguments.of(AtomicConstraintType.IS_EQUAL_TO_CONSTANT, maxValueDtoPlusOne),
            Arguments.of(AtomicConstraintType.IS_IN_SET, maxValueDtoPlusOne),
            Arguments.of(AtomicConstraintType.IS_GREATER_THAN_OR_EQUAL_TO_CONSTANT, maxValueDtoPlusOne),
            Arguments.of(AtomicConstraintType.IS_GREATER_THAN_CONSTANT, maxValueDtoPlusOne),
            Arguments.of(AtomicConstraintType.IS_LESS_THAN_OR_EQUAL_TO_CONSTANT, maxValueDtoPlusOne),
            Arguments.of(AtomicConstraintType.IS_LESS_THAN_CONSTANT, maxValueDtoPlusOne),

            Arguments.of(AtomicConstraintType.IS_EQUAL_TO_CONSTANT, minValueDtoMinusOne),
            Arguments.of(AtomicConstraintType.IS_IN_SET, minValueDtoMinusOne),
            Arguments.of(AtomicConstraintType.IS_GREATER_THAN_OR_EQUAL_TO_CONSTANT, minValueDtoMinusOne),
            Arguments.of(AtomicConstraintType.IS_GREATER_THAN_CONSTANT, minValueDtoMinusOne),
            Arguments.of(AtomicConstraintType.IS_LESS_THAN_OR_EQUAL_TO_CONSTANT, minValueDtoMinusOne),
            Arguments.of(AtomicConstraintType.IS_LESS_THAN_CONSTANT, minValueDtoMinusOne)
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
            Arguments.of(AtomicConstraintType.HAS_LENGTH, integerAsDecimalDto),
            Arguments.of(AtomicConstraintType.IS_STRING_LONGER_THAN, integerAsDecimalDto),
            Arguments.of(AtomicConstraintType.IS_STRING_SHORTER_THAN, integerAsDecimalDto),

            Arguments.of(AtomicConstraintType.HAS_LENGTH, integerAsIntegerDto),
            Arguments.of(AtomicConstraintType.IS_STRING_LONGER_THAN, integerAsIntegerDto),
            Arguments.of(AtomicConstraintType.IS_STRING_SHORTER_THAN, integerAsIntegerDto),

            Arguments.of(AtomicConstraintType.HAS_LENGTH, integerAsDecimalWith0Fraction),
            Arguments.of(AtomicConstraintType.IS_STRING_LONGER_THAN, integerAsDecimalWith0Fraction),
            Arguments.of(AtomicConstraintType.IS_STRING_SHORTER_THAN, integerAsDecimalWith0Fraction));
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
            Set<RuleInformation> ruleInformation = Collections.singleton(new RuleInformation());

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

        Set<RuleInformation> ruleInformation = Collections.singleton(new RuleInformation());

        Assertions.assertThrows(InvalidProfileException.class, () -> reader.apply(dto, profileFields, ruleInformation));
    }

    @DisplayName("Should fail when value property is numeric and out of bounds")
    @ParameterizedTest(name = "{0} should be invalid")
    @MethodSource("numericOutOfBoundsOperandProvider")
    public void testAtomicConstraintReaderWithOutOfBoundValues(AtomicConstraintType type, ConstraintDTO dto) {
        ConstraintReader reader = atomicConstraintReaderLookup.getByTypeCode(type.toString());

        Set<RuleInformation> ruleInformation = Collections.singleton(new RuleInformation());

        Assertions.assertThrows(InvalidProfileException.class, () -> reader.apply(dto, profileFields, ruleInformation));
    }

    @DisplayName("Should pass when string lengths have an integer operand")
    @ParameterizedTest(name = "{0} should be valid")
    @MethodSource("stringLengthValidOperandProvider")
    public void testAtomicConstraintReaderWithValidOperands(AtomicConstraintType type, ConstraintDTO dto) {
        ConstraintReader reader = atomicConstraintReaderLookup.getByTypeCode(type.toString());

        Set<RuleInformation> ruleInformation = Collections.singleton(new RuleInformation());

        Assertions.assertDoesNotThrow(() -> reader.apply(dto, profileFields, ruleInformation));
    }

    @Test
    public void shouldRejectDatesAt0000() {
        assertRejectedDateTimeParse("0000-01-01T00:00:00.000");
        assertRejectedDateTimeParse("0000-01-01T00:00:00.000Z");
    }

    @Test
    public void shouldRejectDatesBefore0000() {
        assertRejectedDateTimeParse("-00001-01-01T00:00:00.000");
        assertRejectedDateTimeParse("-00001-01-01T00:00:00.000Z");
    }

    @Test
    public void shouldRejectDatesAfter9999(){
        assertRejectedDateTimeParse("10000-01-01T00:00:00.000");
        assertRejectedDateTimeParse("10000-01-01T00:00:00.000Z");
    }

    @Test
    public void shouldAcceptDatesAtStartOf0001() {
        assertSuccessfulDateParse(
            "0001-01-01T00:00:00.000",
            OffsetDateTime.of(1, 1, 1, 00, 00, 00, 0, ZoneOffset.UTC));

        assertSuccessfulDateParse(
            "0001-01-01T00:00:00.000Z",
            OffsetDateTime.of(1, 1, 1, 00, 00, 00, 0, ZoneOffset.UTC));
    }

    @Test
    public void shouldAcceptDatesAtEndOf9999() {
        assertSuccessfulDateParse(
            "9999-12-31T23:59:59.999",
            OffsetDateTime.of(9999, 12, 31, 23, 59, 59, 999000000, ZoneOffset.UTC));

        assertSuccessfulDateParse(
            "9999-12-31T23:59:59.999Z",
            OffsetDateTime.of(9999, 12, 31, 23, 59, 59, 999000000, ZoneOffset.UTC));
    }

    @Test
    public void shouldRejectInvalidDates(){
        assertRejectedDateTimeParse("2019-02-29T00:00:00.000");
        assertRejectedDateTimeParse("2019-02-29T00:00:00.000Z");
    }

    @Test
    public void shouldRejectInvalidTimes(){
        assertRejectedDateTimeParse("2019-01-01T24:00:00.000");
        assertRejectedDateTimeParse("2019-01-01T24:00:00.000Z");
    }

    @Test
    public void shouldRejectPartiallySpecifiedDateTimes(){
        assertRejectedDateTimeParse("2010-01-01");
        assertRejectedDateTimeParse("2010-01-01T12:30:00");
    }

    @Test
    public void shouldAssumeUTCWhenOffsetNotSpecified() {
        assertSuccessfulDateParse(
            "2018-04-01T00:00:00.000",
            OffsetDateTime.of(2018, 04, 01, 00, 00, 00, 0, ZoneOffset.UTC));
    }

    @Test
    public void shouldHandleExplicitHourOffsets() {
        assertSuccessfulDateParse(
            "2018-04-01T00:00:00.000+03",
            OffsetDateTime.of(2018, 04, 01, 00, 00, 00, 0, ZoneOffset.ofHours(3)));
    }

    private void assertSuccessfulDateParse(String dateString, OffsetDateTime expectedDateTime) {
        OffsetDateTime actualDateTime = tryParseConstraintDateTimeValue(createDateObject(dateString));

        Assert.assertThat(actualDateTime, equalTo(expectedDateTime));
    }

    private void assertRejectedDateTimeParse(String dateString) {
        Assertions.assertThrows(
            InvalidProfileException.class,
            () -> tryParseConstraintDateTimeValue(createDateObject(dateString)));
    }

    private OffsetDateTime tryParseConstraintDateTimeValue(Object value) {
        ConstraintReader reader = atomicConstraintReaderLookup.getByTypeCode(
            AtomicConstraintType.IS_AFTER_CONSTANT_DATE_TIME.toString());

        ConstraintDTO dateDto = new ConstraintDTO();
        dateDto.field = "test";
        dateDto.value = value;

        Set<RuleInformation> ruleInformation = Collections.singleton(new RuleInformation());

        IsAfterConstantDateTimeConstraint constraint = (IsAfterConstantDateTimeConstraint) reader.apply(dateDto, profileFields, ruleInformation);

        return constraint.referenceValue;
    }
}
