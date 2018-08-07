package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.TestCaseDataRow;

import com.scottlogic.deg.generator.restrictions.*;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

class GeneratorTests {
    @Test
    void shouldReturnSingleRowWithSingleNullObject_IfInputHasSingleFieldWhichMustBeNull() {
        Field field = new Field("test0");
        ProfileFields fields = new ProfileFields(Collections.singletonList(field));
        RowSpec rowSpec = new RowSpec(fields, makeMap(field, getFieldSpecThatMustBeNull()));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        Object testField = testRow.values.iterator().next();
        Assert.assertNull(testField);
    }

    @Test
    void shouldReturnMultipleNullObjects_IfInputHasMultipleFieldsWhichMustBeNull() {
        ArrayList<Field> fields = new ArrayList<>();
        ArrayList<FieldSpec> fieldSpecs = new ArrayList<>();
        int fieldCount = 5;
        for (int i = 0; i < fieldCount; ++i) {
            fields.add(new Field(Integer.toString(i)));
            fieldSpecs.add(getFieldSpecThatMustBeNull());
        }
        RowSpec rowSpec = new RowSpec(new ProfileFields(fields), makeMap(fields, fieldSpecs));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(5, testRow.values.size());
        List<Object> values = new ArrayList<>(testRow.values);
        for (Object obj : values) {
            Assert.assertNull(obj);
        }
    }

    @Test
    void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleFieldWhichMustBelongToSingleMemberSetAndStrategyIsMinimal() {
        Set<String> validValues = new HashSet<>(Collections.singletonList("legal"));
        Field field = new Field("test1");
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)),
                makeMap(field, getFieldSpecThatMustBelongToSetOfString(validValues, null)));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec, GenerationStrategy.Minimal);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        Object testField = testRow.values.iterator().next();
        Assert.assertTrue(testField instanceof String);
        Assert.assertEquals("legal", testField);
    }

    @Test
    void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleFieldWhichMustBelongToSingleMemberSetAndStrategyIsExhaustive() {
        Set<String> validValues = new HashSet<>(Collections.singletonList("legal"));
        Field field = new Field("test2");
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)),
                makeMap(field, getFieldSpecThatMustBelongToSetOfString(validValues, null)));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        Object testField = testRow.values.iterator().next();
        Assert.assertTrue(testField instanceof String);
        Assert.assertEquals("legal", testField);
    }

    @Test
    void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleFieldWhichMustBelongToMultipleMemberSetAndStrategyIsMinimal() {
        Set<String> validValues = new HashSet<>(Arrays.asList("Somerset", "Gloucestershire", "Gwynedd", "Lothian"));
        Field field = new Field("test3");
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)),
                makeMap(field, getFieldSpecThatMustBelongToSetOfString(validValues, null)));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec, GenerationStrategy.Minimal);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        Object testField = testRow.values.iterator().next();
        Assert.assertTrue(testField instanceof String);
        Assert.assertTrue(validValues.contains(testField));
    }

    @Test
    void shouldReturnMultipleRowsWithCorrectContents_IfInputHasSingleFieldWhichMustBelongToMultipleMemberSetAndStrategyIsExhaustive() {
        Set<String> validValues = new HashSet<>(Arrays.asList("Somerset", "Gloucestershire", "Gwynedd", "Lothian"));
        Field field = new Field("test4");
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)),
                makeMap(field, getFieldSpecThatMustBelongToSetOfString(validValues, null)));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(4, testOutput.size());
        List<TestCaseDataRow> testRows = new ArrayList<>(testOutput);
        ArrayList<String> seenValues = new ArrayList<>();
        for (TestCaseDataRow testRow : testRows) {
            Assert.assertNotNull(testRow);
            Assert.assertEquals(1, testRow.values.size());
            Object testField = testRow.values.iterator().next();
            Assert.assertTrue(testField instanceof String);
            Assert.assertTrue(validValues.contains(testField));
            Assert.assertFalse(seenValues.stream().anyMatch(v -> v.equals(testField)));
            seenValues.add((String)testField);
        }
    }

    @Test
    void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleFieldWhichMustBelongToMultipleMemberSetWithBlacklistAndStrategyIsMinimal() {
        Set<String> validValues = new HashSet<>(Arrays.asList("Somerset", "Gloucestershire", "Gwynedd", "Lothian"));
        Set<String> invalidValues = new HashSet<>(Arrays.asList("Gloucestershire", "Lothian", "Kent"));
        Field field = new Field("test5");
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)),
                makeMap(field, getFieldSpecThatMustBelongToSetOfString(validValues, invalidValues)));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec, GenerationStrategy.Minimal);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        Object testField = testRow.values.iterator().next();
        Assert.assertTrue(testField instanceof String);
        Assert.assertTrue(validValues.contains(testField));
        Assert.assertFalse(invalidValues.contains(testField));
    }

    @Test
    void shouldReturnMultipleRowsWithCorrectContents_IfInputHasSingleFieldWhichMustBelongToMultipleMemberSetWithBlacklistAndStrategyIsExhaustive() {
        Set<String> validValues = new HashSet<>(Arrays.asList("Somerset", "Gloucestershire", "Gwynedd", "Lothian"));
        Set<String> invalidValues = new HashSet<>(Arrays.asList("Gloucestershire", "Lothian", "Kent"));
        Field field = new Field("test6");
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)),
                makeMap(field, getFieldSpecThatMustBelongToSetOfString(validValues, invalidValues)));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(2, testOutput.size());
        List<TestCaseDataRow> testRows = new ArrayList<>(testOutput);
        ArrayList<String> seenValues = new ArrayList<>();
        for (TestCaseDataRow testRow : testRows) {
            Assert.assertNotNull(testRow);
            Assert.assertEquals(1, testRow.values.size());
            Object testField = testRow.values.iterator().next();
            Assert.assertTrue(testField instanceof String);
            Assert.assertTrue(validValues.contains(testField));
            Assert.assertFalse(invalidValues.contains(testField));
            Assert.assertFalse(seenValues.stream().anyMatch(v -> v.equals(testField)));
            seenValues.add((String)testField);
        }
    }

    @Test
    void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleNumericFieldWithInclusiveMinBound() {
        Field field = new Field("test7");
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)),
                makeMap(field, getFieldSpecThatMustMeetNumericCriteria(
                        new NumericRestrictions.NumericLimit(BigDecimal.TEN, true), null)));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        Object testField = testRow.values.iterator().next();
        Assert.assertTrue(testField instanceof BigDecimal);
        Assert.assertEquals(BigDecimal.TEN, testField);
    }

    @Test
    void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleNumericFieldWithExclusiveMinBound() {
        Field field = new Field("test8");
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)),
                makeMap(field, getFieldSpecThatMustMeetNumericCriteria(
                        new NumericRestrictions.NumericLimit(BigDecimal.TEN, false), null)));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        Object testField = testRow.values.iterator().next();
        Assert.assertTrue(testField instanceof BigDecimal);
        Assert.assertTrue(((BigDecimal)testField).compareTo(BigDecimal.TEN) > 0);
    }

    @Test
    void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleNumericFieldWithInclusiveMaxBound() {
        Field field = new Field("test9");
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)),
                makeMap(field, getFieldSpecThatMustMeetNumericCriteria(null,
                        new NumericRestrictions.NumericLimit(BigDecimal.TEN, true))));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        Object testField = testRow.values.iterator().next();
        Assert.assertTrue(testField instanceof BigDecimal);
        Assert.assertEquals(BigDecimal.TEN, testField);
    }

    @Test
    void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleNumericFieldWithExclusiveMaxBound() {
        Field field = new Field("test10");
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)),
                makeMap(field, getFieldSpecThatMustMeetNumericCriteria(null,
                        new NumericRestrictions.NumericLimit(BigDecimal.TEN, false))));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        Object testField = testRow.values.iterator().next();
        Assert.assertTrue(testField instanceof BigDecimal);
        Assert.assertTrue(((BigDecimal)testField).compareTo(BigDecimal.TEN) < 0);
    }

    @Test
    void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleNumericFieldWithInclusiveMinBoundAndInclusiveMaxBound() {
        Field field = new Field("test11");
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)),
                makeMap(field, getFieldSpecThatMustMeetNumericCriteria(
                        new NumericRestrictions.NumericLimit(BigDecimal.TEN, true),
                        new NumericRestrictions.NumericLimit(BigDecimal.valueOf(20), true))));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        Object testField = testRow.values.iterator().next();
        Assert.assertTrue(testField instanceof BigDecimal);
        Assert.assertEquals(BigDecimal.TEN, testField);
    }

    @Test
    void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleNumericFieldWithInclusiveMinBoundAndExclusiveMaxBound() {
        Field field = new Field("test12");
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)),
                makeMap(field, getFieldSpecThatMustMeetNumericCriteria(
                        new NumericRestrictions.NumericLimit(BigDecimal.TEN, true),
                        new NumericRestrictions.NumericLimit(BigDecimal.valueOf(20), false))));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        Object testField = testRow.values.iterator().next();
        Assert.assertTrue(testField instanceof BigDecimal);
        Assert.assertEquals(BigDecimal.TEN, testField);
    }

    @Test
    void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleNumericFieldWithExclusiveMinBoundAndInclusiveMaxBound() {
        Field field = new Field("test13");
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)),
                makeMap(field, getFieldSpecThatMustMeetNumericCriteria(
                        new NumericRestrictions.NumericLimit(BigDecimal.TEN, false),
                        new NumericRestrictions.NumericLimit(BigDecimal.valueOf(20), true))));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        Object testField = testRow.values.iterator().next();
        Assert.assertTrue(testField instanceof BigDecimal);
        Assert.assertTrue(((BigDecimal)testField).compareTo(BigDecimal.TEN) > 0);
    }

    @Test
    void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleNumericFieldWithExclusiveMinBoundAndExclusiveMaxBound() {
        Field field = new Field("test14");
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)),
                makeMap(field, getFieldSpecThatMustMeetNumericCriteria(
                        new NumericRestrictions.NumericLimit(BigDecimal.TEN, false),
                        new NumericRestrictions.NumericLimit(BigDecimal.valueOf(20), false))));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        Object testField = testRow.values.iterator().next();
        Assert.assertTrue(testField instanceof BigDecimal);
        Assert.assertTrue(((BigDecimal)testField).compareTo(BigDecimal.TEN) > 0);
    }

    @Test
    void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleNumericFieldWithBoundsOverSmallRange() {
        Field field = new Field("test14");
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)),
                makeMap(field, getFieldSpecThatMustMeetNumericCriteria(
                        new NumericRestrictions.NumericLimit(BigDecimal.valueOf(0.6), false),
                        new NumericRestrictions.NumericLimit(BigDecimal.valueOf(0.7), false))));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        Object testField = testRow.values.iterator().next();
        Assert.assertTrue(testField instanceof BigDecimal);
        Assert.assertTrue(((BigDecimal)testField).compareTo(BigDecimal.valueOf(0.6)) > 0);
        Assert.assertTrue(((BigDecimal)testField).compareTo(BigDecimal.valueOf(0.7)) < 0);
    }

    @Test
    void shouldReturnSingleRowWithCorrectContents_IfInputHasTwoSetFieldsAndStrategyIsMinimal() {
        List<String> validValues0 = Arrays.asList("Somerset", "Gloucestershire", "Kent");
        List<String> validValues1 = Arrays.asList("Porthmadog", "Llandudno");
        List<Field> fields = Arrays.asList(new Field("counties"), new Field("towns"));
        List<FieldSpec> specs = Arrays.asList(
                getFieldSpecThatMustBelongToSetOfString(new HashSet<>(validValues0), null),
                getFieldSpecThatMustBelongToSetOfString(new HashSet<>(validValues1), null));
        RowSpec rowSpec = new RowSpec(new ProfileFields(fields), makeMap(fields, specs));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec, GenerationStrategy.Minimal);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(2, testRow.values.size());
        ArrayList<Object> values = new ArrayList<>(testRow.values);
        String v = (String)values.get(0);
        Assert.assertTrue(validValues0.contains(v));
        v = (String)values.get(1);
        Assert.assertTrue(validValues1.contains(v));
    }

    @Test
    void shouldReturnMultipleRowsWithCorrectContents_IfInputHasTwoSetFieldsAndStrategyIsExhaustive() {
        List<String> validValues0 = Arrays.asList("Somerset", "Gloucestershire", "Kent");
        List<String> validValues1 = Arrays.asList("Porthmadog", "Llandudno");
        ArrayList<String> allPossibleCombinations = new ArrayList<>();
        for (String valuei : validValues0) {
            for (String valuej : validValues1) {
                allPossibleCombinations.add(valuei + "/" + valuej);
            }
        }
        List<Field> fields = Arrays.asList(new Field("counties"), new Field("towns"));
        List<FieldSpec> specs = Arrays.asList(
                getFieldSpecThatMustBelongToSetOfString(new HashSet<>(validValues0), null),
                getFieldSpecThatMustBelongToSetOfString(new HashSet<>(validValues1), null));
        RowSpec rowSpec = new RowSpec(new ProfileFields(fields), makeMap(fields, specs));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(6, testOutput.size());
        ArrayList<TestCaseDataRow> outputRows = new ArrayList<>(testOutput);
        ArrayList<String> seenCombinations = new ArrayList<>();
        for (TestCaseDataRow testRow : outputRows) {
            Assert.assertNotNull(testRow);
            Assert.assertEquals(2, testRow.values.size());
            ArrayList<Object> values = new ArrayList<>(testRow.values);
            String v = (String)values.get(0);
            Assert.assertTrue(validValues0.contains(v));
            v = (String)values.get(1);
            Assert.assertTrue(validValues1.contains(v));
            String combination = values.get(0) + "/" + values.get(1);
            Assert.assertTrue(allPossibleCombinations.contains(combination));
            Assert.assertFalse(seenCombinations.contains(combination));
            seenCombinations.add(combination);
        }
    }

    @Test
    void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleStringFieldWithRegexWithoutWildcards() {
        String matchValue = "Prince";
        Field field = new Field("test16");
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)),
                makeMap(field, getFieldSpecThatMatchesRegex(matchValue, null)));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        Assert.assertEquals(matchValue, testRow.values.iterator().next());
    }

    @Test
    void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleStringFieldWithRegexWithBoundedWildcards() {
        String matchValueStart = "Prince";
        String matchValue = matchValueStart + ".{0,10}";
        Field field = new Field("test17");
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)),
                makeMap(field, getFieldSpecThatMatchesRegex(matchValue, null)));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        Assert.assertTrue(((String)testRow.values.iterator().next()).startsWith(matchValueStart));
    }

    @Test
    void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleStringFieldWithRegexWithUnboundedWildcards() {
        String matchValueStart = "Prince";
        String matchValue = matchValueStart + ".+";
        Field field = new Field("test18");
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)),
                makeMap(field, getFieldSpecThatMatchesRegex(matchValue, null)));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        Assert.assertTrue(((String)testRow.values.iterator().next()).startsWith(matchValueStart));
    }

    @Test
    void shouldReturnNoRows_IfInputHasSingleStringFieldWithRegexAndBlacklistMatchingAllPossibleValues() {
        String matchValue = "Prince";
        Field field = new Field("test19");
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)),
                makeMap(field, getFieldSpecThatMatchesRegex(matchValue, Collections.singletonList(matchValue))));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(0, testOutput.size());
    }

    @Test
    void shouldReturnSingleRowWithNonBlacklistedValue_IfInputHasSingleStringFieldWithRegexAndBlacklist() {
        String matchPattern = "[ab]{2}";
        List<String> illegalValues = Collections.singletonList("aa");
        Field field = new Field("test20");
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)),
                makeMap(field, getFieldSpecThatMatchesRegex(matchPattern, illegalValues)));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        List<String> legalValues = Arrays.asList("ab", "ba", "bb");
        String value = (String)testRow.values.iterator().next();
        Assert.assertTrue(legalValues.contains(value));
    }

    @Test
    void shouldReturnNoRows_IfInputHasSingleFieldWithNumericMinRestrictionAndNonNumericRegex() {
        String matchPattern = "Prince";
        Field field = new Field("test21");
        FieldSpec spec = getFieldSpecThatMustMeetNumericCriteria(getFieldSpecThatMatchesRegex(matchPattern, null),
                new NumericRestrictions.NumericLimit(BigDecimal.TEN, true), null);
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)), makeMap(field, spec));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(0, testOutput.size());
    }

    @Test
    void shouldReturnNoRows_IfInputHasSingleFieldWithNumericMaxRestrictionAndNonNumericRegex() {
        String matchPattern = "Prince";
        Field field = new Field("test22");
        FieldSpec spec = getFieldSpecThatMustMeetNumericCriteria(
                getFieldSpecThatMatchesRegex(matchPattern, null), null,
                new NumericRestrictions.NumericLimit(BigDecimal.TEN, true));
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)), makeMap(field, spec));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(0, testOutput.size());
    }

    @Test
    void shouldReturnNoRows_IfInputHasSingleFieldWithNumericRangeRestrictionAndNonNumericRegex() {
        String matchPattern = "Prince";
        Field field = new Field("test23");
        FieldSpec spec = getFieldSpecThatMustMeetNumericCriteria(
                getFieldSpecThatMatchesRegex(matchPattern, null),
                new NumericRestrictions.NumericLimit(BigDecimal.ZERO, true),
                new NumericRestrictions.NumericLimit(BigDecimal.TEN, true));
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)), makeMap(field, spec));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(0, testOutput.size());
    }

    @Test
    void shouldReturnSingleRowWithCorrectData_IfInputHasSingleFieldWithNumericMinRestrictionAndSuitableRegex() {
        String matchPattern = "[1-9][0-9]*\\.[0-9]{2}";
        Field field = new Field("test24");
        FieldSpec spec = getFieldSpecThatMustMeetNumericCriteria(
                getFieldSpecThatMatchesRegex(matchPattern, null),
                new NumericRestrictions.NumericLimit(BigDecimal.TEN, true), null);
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)), makeMap(field, spec));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        String testValue = (String)testRow.values.iterator().next();
        Assert.assertTrue(Pattern.compile(matchPattern).matcher(testValue).matches());
        Assert.assertTrue(Double.parseDouble(testValue) >= 10);
    }

    @Test
    void shouldReturnSingleRowWithCorrectData_IfInputHasSingleFieldWithNumericMaxRestrictionAndSuitableRegex() {
        String matchPattern = "[1-9][0-9]*\\.[0-9]{2}";
        Field field = new Field("test25");
        FieldSpec spec = getFieldSpecThatMustMeetNumericCriteria(
                getFieldSpecThatMatchesRegex(matchPattern, null), null,
                new NumericRestrictions.NumericLimit(BigDecimal.TEN, true));
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)), makeMap(field, spec));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        String testValue = (String)testRow.values.iterator().next();
        Assert.assertTrue(Pattern.compile(matchPattern).matcher(testValue).matches());
        Assert.assertTrue(Double.parseDouble(testValue) <= 10);
    }

    @Test
    void shouldReturnSingleRowWithCorrectData_IfInputHasSingleFieldWithNumericRangeRestrictionAndSuitableRegex() {
        String matchPattern = "[1-9][0-9]*\\.[0-9]{2}";
        Field field = new Field("test25");
        FieldSpec spec = getFieldSpecThatMustMeetNumericCriteria(
                getFieldSpecThatMatchesRegex(matchPattern, null),
                new NumericRestrictions.NumericLimit(BigDecimal.ZERO, true),
                new NumericRestrictions.NumericLimit(BigDecimal.TEN, true));
        RowSpec rowSpec = new RowSpec(new ProfileFields(Collections.singletonList(field)), makeMap(field, spec));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        String testValue = (String)testRow.values.iterator().next();
        Assert.assertTrue(Pattern.compile(matchPattern).matcher(testValue).matches());
        double d = Double.parseDouble(testValue);
        Assert.assertTrue(d <= 10);
        Assert.assertTrue(d >= 0);
    }

    private Map<Field, FieldSpec> makeMap(Field field, FieldSpec fieldSpec) {
        HashMap<Field, FieldSpec> map = new HashMap<>();
        map.put(field, fieldSpec);
        return map;
    }

    private Map<Field, FieldSpec> makeMap(List<Field> fields, List<FieldSpec> fieldSpecs) {
        HashMap<Field, FieldSpec> map = new HashMap<>();
        for (int i = 0; i < fields.size() && i < fieldSpecs.size(); ++i) {
            map.put(fields.get(i), fieldSpecs.get(i));
        }
        return map;
    }

    private FieldSpec getFieldSpecThatMustBeNull(FieldSpec fieldSpec)
    {
        NullRestrictions nullRestrictions = new NullRestrictions();
        nullRestrictions.nullness = NullRestrictions.Nullness.MustBeNull;
        fieldSpec.setNullRestrictions(nullRestrictions);
        return fieldSpec;
    }

    private FieldSpec getFieldSpecThatMustBeNull() {
        return getFieldSpecThatMustBeNull(new FieldSpec());
    }

    private FieldSpec getFieldSpecThatMustBelongToSetOfString(
            FieldSpec fieldSpec,
            Set<String> members,
            Set<String> notMembers) {
        SetRestrictions restrictions = new SetRestrictions();
        restrictions.whitelist = new HashSet<>(members);
        if (notMembers != null) {
            restrictions.blacklist = new HashSet<>(notMembers);
        }
        fieldSpec.setSetRestrictions(restrictions);
        return fieldSpec;
    }

    private FieldSpec getFieldSpecThatMustBelongToSetOfString(Set<String> members, Set<String> notMembers) {
        return getFieldSpecThatMustBelongToSetOfString(new FieldSpec(), members, notMembers);
    }

    private FieldSpec getFieldSpecThatMustMeetNumericCriteria(
            FieldSpec fieldSpec,
            NumericRestrictions.NumericLimit min,
            NumericRestrictions.NumericLimit max) {
        NumericRestrictions restrictions = new NumericRestrictions();
        restrictions.min = min;
        restrictions.max = max;
        fieldSpec.setNumericRestrictions(restrictions);
        return fieldSpec;
    }

    private FieldSpec getFieldSpecThatMustMeetNumericCriteria(
            NumericRestrictions.NumericLimit min,
            NumericRestrictions.NumericLimit max) {
        return getFieldSpecThatMustMeetNumericCriteria(new FieldSpec(), min, max);
    }

    // this method does not work with backslashed character classes like \d, hence "simplePattern".
    private FieldSpec getFieldSpecThatMatchesRegex(FieldSpec fieldSpec, String simplePattern, Collection<String> blacklist) {
        Automaton automaton = new RegExp(simplePattern).toAutomaton();
        StringRestrictions restrictions = new StringRestrictions();
        restrictions.automaton = automaton;
        fieldSpec.setStringRestrictions(restrictions);
        if (blacklist != null) {
            SetRestrictions setRestrictions = new SetRestrictions();
            setRestrictions.blacklist = new HashSet<>(blacklist);
            fieldSpec.setSetRestrictions(setRestrictions);
        }
        return fieldSpec;
    }

    private FieldSpec getFieldSpecThatMatchesRegex(String simplePattern, Collection<String> blacklist) {
        return getFieldSpecThatMatchesRegex(new FieldSpec(), simplePattern, blacklist);
    }
}
