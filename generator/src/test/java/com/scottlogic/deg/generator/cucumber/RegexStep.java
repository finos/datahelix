package com.scottlogic.deg.generator.cucumber;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.MatchesRegexConstraint;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeGenerator;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeProfile;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.IDataGenerator;
import com.scottlogic.deg.generator.inputs.MainConstraintReader;
import com.scottlogic.deg.generator.outputs.TestCaseDataRow;
import com.scottlogic.deg.generator.outputs.TestCaseDataSet;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;
import com.scottlogic.deg.schemas.v3.ConstraintDTO;
import io.cucumber.datatable.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.junit.Assert;

import java.util.*;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.equalTo;

public class RegexStep {
    private final List<Field> profileFields = new ArrayList<>();
    private final List<IConstraint> constraints = new ArrayList<>();

    public RegexStep() {}

    @Before
    public void BeforeEach() {
        this.profileFields.clear();
        this.constraints.clear();
    }

    @Given("^there is a field (.+)$")
    public void thereIsAFieldFoo(String fieldName) throws Throwable {
        this.profileFields.add(new Field(fieldName));
    }


    @And("^(\\S+) is (\\S+) /(.+?)/$")
    public void fieldIsConstrainedWithRegex(
        String fieldName,
        String constraintTypeId,
        String regexStr)
        throws Throwable {

        ConstraintDTO dto = new ConstraintDTO();
        dto.field = fieldName;
        dto.is = constraintTypeId;
        dto.value = regexStr;

        IConstraint constraint = new MainConstraintReader().apply(
            dto,
            new ProfileFields(this.profileFields));

        this.constraints.add(constraint);
    }

    @Then("^the following data should be generated:$")
    public void theFollowingDataShouldBeGenerated(DataTable expectedResultsTable) throws Throwable {
        Profile profile = new Profile(
            new ProfileFields(this.profileFields),
            Collections.singleton(new Rule("TEST_RULE", this.constraints)));

        final DecisionTreeProfile analysedProfile = new DecisionTreeGenerator().analyse(profile);

        final IDataGenerator dataGenerator = new DataGenerator(
            new RowSpecMerger(
                new FieldSpecMerger()),
            new ConstraintReducer(
                new FieldSpecFactory(),
                new FieldSpecMerger()));

        final TestCaseGenerationResult generationResult = dataGenerator.generateData(profile, analysedProfile);

        final TestCaseDataSet dataSet = generationResult.datasets.iterator().next();

        List<TestCaseDataRow> allActualRows = new ArrayList<>();

        dataSet.iterator().forEachRemaining(allActualRows::add);

        List<Map<String, String>> expectedRows = expectedResultsTable.asMaps(String.class, String.class);

        Assert.assertThat(allActualRows.size(), equalTo(expectedRows.size()));

        IntStream
            .range(
                0,
                Math.min(allActualRows.size(), expectedRows.size()))
            .forEach(i -> {
                TestCaseDataRow actualRow = allActualRows.get(i);
                Map<String, String> expectedRow = expectedRows.get(i);

                int fieldIndex = 0;
                for (Field field : this.profileFields) {
                    Object actualValue = actualRow.values.get(fieldIndex);
                    String expectedValueAsString = expectedRow.get(field.name);

                    Assert.assertThat(actualValue, equalTo(expectedValueAsString));

                    fieldIndex++;
                }
            });
    }
}
