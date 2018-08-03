package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.generation.tmpReducerOutput.*;
import com.scottlogic.deg.generator.outputs.TestCaseDataRow;

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
        RowSpec rowSpec = new RowSpec(Collections.singletonList(getFieldSpecThatMustBeNull("test0")));
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
        ArrayList<FieldSpec> specs = new ArrayList<>();
        int fieldCount = 5;
        for (int i = 0; i < fieldCount; ++i) {
            specs.add(getFieldSpecThatMustBeNull(Integer.toString(i)));
        }
        RowSpec rowSpec = new RowSpec(specs);

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
        RowSpec rowSpec = new RowSpec(Collections.singletonList(
                getFieldSpecThatMustBelongToSetOfString("test1", validValues, null)));
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
        RowSpec rowSpec = new RowSpec(Collections.singletonList(
                getFieldSpecThatMustBelongToSetOfString("test2", validValues, null)));
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
        RowSpec rowSpec = new RowSpec(Collections.singletonList(
                getFieldSpecThatMustBelongToSetOfString("test3", validValues, null)));
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
        RowSpec rowSpec = new RowSpec(Collections.singletonList(
                getFieldSpecThatMustBelongToSetOfString("test4", validValues, null)));
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
        RowSpec rowSpec = new RowSpec(Collections.singletonList(
                getFieldSpecThatMustBelongToSetOfString("test5", validValues, invalidValues)));
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
        RowSpec rowSpec = new RowSpec(Collections.singletonList(
                getFieldSpecThatMustBelongToSetOfString("test6", validValues, invalidValues)));
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
        RowSpec rowSpec = new RowSpec(
                Collections.singletonList(getFieldSpecThatMustMeetNumericCriteria("test7",
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
        RowSpec rowSpec = new RowSpec(
                Collections.singletonList(getFieldSpecThatMustMeetNumericCriteria("test8",
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
        RowSpec rowSpec = new RowSpec(
                Collections.singletonList(getFieldSpecThatMustMeetNumericCriteria("test9", null,
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
        RowSpec rowSpec = new RowSpec(
                Collections.singletonList(getFieldSpecThatMustMeetNumericCriteria("test10", null,
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
        RowSpec rowSpec = new RowSpec(
                Collections.singletonList(getFieldSpecThatMustMeetNumericCriteria("test11",
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
        RowSpec rowSpec = new RowSpec(
                Collections.singletonList(getFieldSpecThatMustMeetNumericCriteria("test12",
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
        RowSpec rowSpec = new RowSpec(
                Collections.singletonList(getFieldSpecThatMustMeetNumericCriteria("test13",
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
        RowSpec rowSpec = new RowSpec(
                Collections.singletonList(getFieldSpecThatMustMeetNumericCriteria("test14",
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
        RowSpec rowSpec = new RowSpec(
                Collections.singletonList(getFieldSpecThatMustMeetNumericCriteria("test15",
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
        RowSpec rowSpec = new RowSpec(Arrays.asList(
                getFieldSpecThatMustBelongToSetOfString("counties", new HashSet<>(validValues0), null),
                getFieldSpecThatMustBelongToSetOfString("towns", new HashSet<>(validValues1), null)));
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
        RowSpec rowSpec = new RowSpec(Arrays.asList(
                getFieldSpecThatMustBelongToSetOfString("counties", new HashSet<>(validValues0), null),
                getFieldSpecThatMustBelongToSetOfString("towns", new HashSet<>(validValues1), null)));
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
        RowSpec rowSpec = new RowSpec(Collections.singletonList(
                getFieldSpecThatMatchesRegex("test16", matchValue, null)));
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
        RowSpec rowSpec = new RowSpec(Collections.singletonList(
                getFieldSpecThatMatchesRegex("test17", matchValue, null)));
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
        RowSpec rowSpec = new RowSpec(Collections.singletonList(
                getFieldSpecThatMatchesRegex("test18", matchValue, null)));
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
        RowSpec rowSpec = new RowSpec(Collections.singletonList(getFieldSpecThatMatchesRegex("test19", matchValue,
                Collections.singletonList(matchValue))));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(0, testOutput.size());
    }

    @Test
    void shouldReturnSingleRowWithNonBlacklistedValue_IfInputHasSingleStringFieldWithRegexAndBlacklist() {
        String matchPattern = "[ab]{2}";
        List<String> illegalValues = Collections.singletonList("aa");
        RowSpec rowSpec = new RowSpec(Collections.singletonList(
                getFieldSpecThatMatchesRegex("test20", matchPattern, illegalValues)));
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
        FieldSpec spec = getFieldSpecThatMatchesRegex("test21", matchPattern, null);
        NumericRestrictions numericRestriction = new NumericRestrictions();
        numericRestriction.min = new NumericRestrictions.NumericLimit(BigDecimal.TEN, true);
        spec.setNumericRestrictions(numericRestriction);
        RowSpec rowSpec = new RowSpec(Collections.singletonList(spec));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(0, testOutput.size());
    }

    @Test
    void shouldReturnNoRows_IfInputHasSingleFieldWithNumericMaxRestrictionAndNonNumericRegex() {
        String matchPattern = "Prince";
        FieldSpec spec = getFieldSpecThatMatchesRegex("test22", matchPattern, null);
        NumericRestrictions numericRestriction = new NumericRestrictions();
        numericRestriction.max = new NumericRestrictions.NumericLimit(BigDecimal.TEN, true);
        spec.setNumericRestrictions(numericRestriction);
        RowSpec rowSpec = new RowSpec(Collections.singletonList(spec));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(0, testOutput.size());
    }

    @Test
    void shouldReturnNoRows_IfInputHasSingleFieldWithNumericRangeRestrictionAndNonNumericRegex() {
        String matchPattern = "Prince";
        FieldSpec spec = getFieldSpecThatMatchesRegex("test23", matchPattern, null);
        NumericRestrictions numericRestriction = new NumericRestrictions();
        numericRestriction.max = new NumericRestrictions.NumericLimit(BigDecimal.TEN, true);
        numericRestriction.min = new NumericRestrictions.NumericLimit(BigDecimal.ZERO, true);
        spec.setNumericRestrictions(numericRestriction);
        RowSpec rowSpec = new RowSpec(Collections.singletonList(spec));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(0, testOutput.size());
    }

    @Test
    void shouldReturnSingleRowWithCorrectData_IfInputHasSingleFieldWithNumericMinRestrictionAndSuitableRegex() {
        String matchPattern = "[1-9][0-9]*\\.[0-9]{2}";
        FieldSpec spec = getFieldSpecThatMatchesRegex("test24", matchPattern, null);
        NumericRestrictions numericRestriction = new NumericRestrictions();
        numericRestriction.min = new NumericRestrictions.NumericLimit(BigDecimal.TEN, true);
        spec.setNumericRestrictions(numericRestriction);
        RowSpec rowSpec = new RowSpec(Collections.singletonList(spec));
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
        FieldSpec spec = getFieldSpecThatMatchesRegex("test25", matchPattern, null);
        NumericRestrictions numericRestriction = new NumericRestrictions();
        numericRestriction.max = new NumericRestrictions.NumericLimit(BigDecimal.TEN, true);
        spec.setNumericRestrictions(numericRestriction);
        RowSpec rowSpec = new RowSpec(Collections.singletonList(spec));
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
        FieldSpec spec = getFieldSpecThatMatchesRegex("test25", matchPattern, null);
        NumericRestrictions numericRestriction = new NumericRestrictions();
        numericRestriction.max = new NumericRestrictions.NumericLimit(BigDecimal.TEN, true);
        numericRestriction.min = new NumericRestrictions.NumericLimit(BigDecimal.ZERO, true);
        spec.setNumericRestrictions(numericRestriction);
        RowSpec rowSpec = new RowSpec(Collections.singletonList(spec));
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

    private FieldSpec getFieldSpecThatMustBeNull(String name)
    {
        FieldSpec fieldSpec = new FieldSpec(name);
        NullRestrictions nullRestrictions = new NullRestrictions();
        nullRestrictions.nullness = NullRestrictions.Nullness.MustBeNull;
        fieldSpec.setNullRestrictions(nullRestrictions);
        return fieldSpec;
    }

    private FieldSpec getFieldSpecThatMustBelongToSetOfString(String name, Set<String> members, Set<String> notMembers) {
        FieldSpec fieldSpec = new FieldSpec(name);
        SetRestrictions restrictions = new SetRestrictions();
        restrictions.whitelist = new HashSet<>(members);
        if (notMembers != null) {
            restrictions.blacklist = new HashSet<>(notMembers);
        }
        fieldSpec.setSetRestrictions(restrictions);
        return fieldSpec;
    }

    private FieldSpec getFieldSpecThatMustMeetNumericCriteria(
            String name,
            NumericRestrictions.NumericLimit min,
            NumericRestrictions.NumericLimit max) {
        FieldSpec fieldSpec = new FieldSpec(name);
        NumericRestrictions restrictions = new NumericRestrictions();
        restrictions.min = min;
        restrictions.max = max;
        fieldSpec.setNumericRestrictions(restrictions);
        return fieldSpec;
    }

    // this method does not work with backslashed character classes like \d, hence "simplePattern".
    private FieldSpec getFieldSpecThatMatchesRegex(String name, String simplePattern, Collection<String> blacklist) {
        FieldSpec fieldSpec = new FieldSpec(name);
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
}
