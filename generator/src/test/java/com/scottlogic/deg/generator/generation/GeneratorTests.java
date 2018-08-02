package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.generation.tmpReducerOutput.*;
import com.scottlogic.deg.generator.outputs.TestCaseDataRow;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

public class GeneratorTests {
    @Test
    public void shouldReturnSingleRowWithSingleNullObject_IfInputHasSingleFieldWhichMustBeNull() {
        RowSpec rowSpec = new RowSpec(Arrays.asList(getFieldSpecThatMustBeNull("test")));
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
    public void shouldReturnMultipleNullObjects_IfInputHasMultipleFieldsWhichMustBeNull() {
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
    public void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleFieldWhichMustBelongToSingleMemberSetAndStrategyIsMinimal() {
        Set<String> validValues = new HashSet<>(Arrays.asList("legal"));
        RowSpec rowSpec = new RowSpec(Arrays.asList(
                getFieldSpecThatMustBelongToSetOfString("test", validValues, null)));
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
    public void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleFieldWhichMustBelongToSingleMemberSetAndStrategyIsExhaustive() {
        Set<String> validValues = new HashSet<>(Arrays.asList("legal"));
        RowSpec rowSpec = new RowSpec(Arrays.asList(
                getFieldSpecThatMustBelongToSetOfString("test", validValues, null)));
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
    public void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleFieldWhichMustBelongToMultipleMemberSetAndStrategyIsMinimal() {
        Set<String> validValues = new HashSet<>(Arrays.asList("Somerset", "Gloucestershire", "Gwynedd", "Lothian"));
        RowSpec rowSpec = new RowSpec(Arrays.asList(
                getFieldSpecThatMustBelongToSetOfString("test", validValues, null)));
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
    public void shouldReturnMultipleRowsWithCorrectContents_IfInputHasSingleFieldWhichMustBelongToMultipleMemberSetAndStrategyIsExhaustive() {
        Set<String> validValues = new HashSet<>(Arrays.asList("Somerset", "Gloucestershire", "Gwynedd", "Lothian"));
        RowSpec rowSpec = new RowSpec(Arrays.asList(
                getFieldSpecThatMustBelongToSetOfString("test", validValues, null)));
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
    public void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleFieldWhichMustBelongToMultipleMemberSetWithBlacklistAndStrategyIsMinimal() {
        Set<String> validValues = new HashSet<>(Arrays.asList("Somerset", "Gloucestershire", "Gwynedd", "Lothian"));
        Set<String> invalidValues = new HashSet<>(Arrays.asList("Gloucestershire", "Lothian", "Kent"));
        RowSpec rowSpec = new RowSpec(Arrays.asList(
                getFieldSpecThatMustBelongToSetOfString("test", validValues, invalidValues)));
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
    public void shouldReturnMultipleRowsWithCorrectContents_IfInputHasSingleFieldWhichMustBelongToMultipleMemberSetWithBlacklistAndStrategyIsExhaustive() {
        Set<String> validValues = new HashSet<>(Arrays.asList("Somerset", "Gloucestershire", "Gwynedd", "Lothian"));
        Set<String> invalidValues = new HashSet<>(Arrays.asList("Gloucestershire", "Lothian", "Kent"));
        RowSpec rowSpec = new RowSpec(Collections.singletonList(
                getFieldSpecThatMustBelongToSetOfString("test", validValues, invalidValues)));
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
    public void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleNumericFieldWithInclusiveMinBound() {
        RowSpec rowSpec = new RowSpec(
                Collections.singletonList(getFieldSpecThatMustMeetNumericCriteria("test",
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
    public void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleNumericFieldWithExclusiveMinBound() {
        RowSpec rowSpec = new RowSpec(
                Collections.singletonList(getFieldSpecThatMustMeetNumericCriteria("test",
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
    public void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleNumericFieldWithInclusiveMaxBound() {
        RowSpec rowSpec = new RowSpec(
                Collections.singletonList(getFieldSpecThatMustMeetNumericCriteria("test", null,
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
    public void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleNumericFieldWithExclusiveMaxBound() {
        RowSpec rowSpec = new RowSpec(
                Collections.singletonList(getFieldSpecThatMustMeetNumericCriteria("test", null,
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
    public void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleNumericFieldWithInclusiveMinBoundAndInclusiveMaxBound() {
        RowSpec rowSpec = new RowSpec(
                Collections.singletonList(getFieldSpecThatMustMeetNumericCriteria("test",
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
    public void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleNumericFieldWithInclusiveMinBoundAndExclusiveMaxBound() {
        RowSpec rowSpec = new RowSpec(
                Collections.singletonList(getFieldSpecThatMustMeetNumericCriteria("test",
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
    public void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleNumericFieldWithExclusiveMinBoundAndInclusiveMaxBound() {
        RowSpec rowSpec = new RowSpec(
                Collections.singletonList(getFieldSpecThatMustMeetNumericCriteria("test",
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
    public void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleNumericFieldWithExclusiveMinBoundAndExclusiveMaxBound() {
        RowSpec rowSpec = new RowSpec(
                Collections.singletonList(getFieldSpecThatMustMeetNumericCriteria("test",
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
    public void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleNumericFieldWithBoundsOverSmallRange() {
        RowSpec rowSpec = new RowSpec(
                Collections.singletonList(getFieldSpecThatMustMeetNumericCriteria("test",
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
    public void shouldReturnSingleRowWithCorrectContents_IfInputHasTwoSetFieldsAndStrategyIsMinimal() {
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
        Assert.assertTrue(validValues0.contains(values.get(0)));
        Assert.assertTrue(validValues1.contains(values.get(1)));
    }

    @Test
    public void shouldReturnMultipleRowsWithCorrectContents_IfInputHasTwoSetFieldsAndStrategyIsExhaustive() {
        List<String> validValues0 = Arrays.asList("Somerset", "Gloucestershire", "Kent");
        List<String> validValues1 = Arrays.asList("Porthmadog", "Llandudno");
        ArrayList<String> allPossibleCombinations = new ArrayList<>();
        for (int i = 0; i < validValues0.size(); ++i) {
            for (int j = 0; j < validValues1.size(); ++j) {
                allPossibleCombinations.add(validValues0.get(i) + "/" + validValues1.get(j));
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
            Assert.assertTrue(validValues0.contains(values.get(0)));
            Assert.assertTrue(validValues1.contains(values.get(1)));
            String combination = values.get(0) + "/" + values.get(1);
            Assert.assertTrue(allPossibleCombinations.contains(combination));
            Assert.assertFalse(seenCombinations.contains(combination));
            seenCombinations.add(combination);
        }
    }

    @Test
    public void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleStringFieldWithRegexWithoutWildcards() {
        String matchValue = "Prince";
        RowSpec rowSpec = new RowSpec(Arrays.asList(getFieldSpecThatMatchesRegex("test", matchValue, null)));
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
    public void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleStringFieldWithRegexWithBoundedWildcards() {
        String matchValueStart = "Prince";
        String matchValue = matchValueStart + ".{0,10}";
        RowSpec rowSpec = new RowSpec(Arrays.asList(getFieldSpecThatMatchesRegex("test", matchValue, null)));
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
    public void shouldReturnSingleRowWithCorrectContents_IfInputHasSingleStringFieldWithRegexWithUnboundedWildcards() {
        String matchValueStart = "Prince";
        String matchValue = matchValueStart + ".+";
        RowSpec rowSpec = new RowSpec(Arrays.asList(getFieldSpecThatMatchesRegex("test", matchValue, null)));
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
    public void shouldReturnNoRows_IfInputHasSingleStringFieldWithRegexAndBlacklistMatchingAllPossibleValues() {
        String matchValue = "Prince";
        RowSpec rowSpec = new RowSpec(Arrays.asList(getFieldSpecThatMatchesRegex("test", matchValue,
                Arrays.asList(matchValue))));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(0, testOutput.size());
    }

    @Test
    public void shouldReturnSingleRowWithNonBlacklistedValue_IfInputHasSingleStringFieldWithRegexAndBlacklist() {
        String matchPattern = "[ab]{2}";
        List<String> illegalValues = Arrays.asList("aa");
        RowSpec rowSpec = new RowSpec(Arrays.asList(
                getFieldSpecThatMatchesRegex("test", matchPattern, illegalValues)));
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
