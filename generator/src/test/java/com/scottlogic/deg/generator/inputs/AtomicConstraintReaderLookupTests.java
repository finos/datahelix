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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;

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
    public void shouldAcceptDatesAtStartOf0001() throws InvalidProfileException {
        assertSuccessfulDateParse(
            "0001-01-01T00:00:00.000",
            OffsetDateTime.of(1, 1, 1, 00, 00, 00, 0, ZoneOffset.UTC));

        assertSuccessfulDateParse(
            "0001-01-01T00:00:00.000Z",
            OffsetDateTime.of(1, 1, 1, 00, 00, 00, 0, ZoneOffset.UTC));
    }

    @Test
    public void shouldAcceptDatesAtEndOf9999() throws InvalidProfileException {
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
    public void shouldAssumeUTCWhenOffsetNotSpecified() throws InvalidProfileException {
        assertSuccessfulDateParse(
            "2018-04-01T00:00:00.000",
            OffsetDateTime.of(2018, 04, 01, 00, 00, 00, 0, ZoneOffset.UTC));
    }

    @Test
    public void shouldHandleExplicitHourOffsets() throws InvalidProfileException {
        assertSuccessfulDateParse(
            "2018-04-01T00:00:00.000+03",
            OffsetDateTime.of(2018, 04, 01, 00, 00, 00, 0, ZoneOffset.ofHours(3)));
    }

    private void assertSuccessfulDateParse(String dateString, OffsetDateTime expectedDateTime) throws InvalidProfileException {
        OffsetDateTime actualDateTime = tryParseConstraintDateTimeValue(createDateObject(dateString));

        Assert.assertThat(actualDateTime, equalTo(expectedDateTime));
    }

    private void assertRejectedDateTimeParse(String dateString) {
        Assertions.assertThrows(
            InvalidProfileException.class,
            () -> tryParseConstraintDateTimeValue(createDateObject(dateString)));
    }

    private OffsetDateTime tryParseConstraintDateTimeValue(Object value) throws InvalidProfileException {
        ConstraintReader reader = atomicConstraintReaderLookup.getByTypeCode(
            AtomicConstraintType.ISAFTERCONSTANTDATETIME.toString());

        ConstraintDTO dateDto = new ConstraintDTO();
        dateDto.field = "test";
        dateDto.value = value;

        RuleDTO rule = new RuleDTO();
        rule.rule = "rule";
        Set<RuleInformation> ruleInformation = Collections.singleton(new RuleInformation(rule));

        IsAfterConstantDateTimeConstraint constraint = (IsAfterConstantDateTimeConstraint) reader.apply(dateDto, profileFields, ruleInformation);

        return constraint.referenceValue;
    }
}
