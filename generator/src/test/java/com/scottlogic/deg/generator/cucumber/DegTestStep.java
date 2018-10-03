package com.scottlogic.deg.generator.cucumber;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeCollection;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeGenerator;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.IDataGenerator;
import com.scottlogic.deg.generator.generation.combination_strategies.FieldExhaustiveCombinationStrategy;
import com.scottlogic.deg.generator.inputs.MainConstraintReader;
import com.scottlogic.deg.generator.outputs.TestCaseDataRow;
import com.scottlogic.deg.generator.outputs.TestCaseDataSet;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;
import com.scottlogic.deg.schemas.v3.ConstraintDTO;
import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.equalTo;

public class DegTestStep {

    private DegTestState state;

    public DegTestStep(DegTestState state){
        this.state = state;
    }

    @Before
    public void BeforeEach() {
        this.state.profileFields.clear();
        this.state.constraints.clear();
    }

    @Given("^there is a field (.+)$")
    public void thereIsAField(String fieldName) {
        this.state.profileFields.add(new Field(fieldName));
    }

    @Given("^the following fields exist:$")
    public void thereAreFields(DataTable fields) {
        fields.asList(String.class).stream().forEach(field -> this.thereIsAField(field));
    }

    @And("^(.+) is required$")
    public void fieldIsRequired(String fieldName) throws Exception {
        ConstraintDTO nullDto = new ConstraintDTO();
        nullDto.field = fieldName;
        nullDto.is = "null";

        ConstraintDTO requiredDto = new ConstraintDTO();
        requiredDto.not = nullDto;

        IConstraint constraint = new MainConstraintReader().apply(
                requiredDto,
                new ProfileFields(this.state.profileFields));

        this.state.constraints.add(constraint);
    }

    @Then("^the following data should be generated:$")
    public void theFollowingDataShouldBeGenerated(List<Map<String, String>> expectedResultsTable) throws Throwable {
        Profile profile = new Profile(
                new ProfileFields(this.state.profileFields),
                Collections.singleton(new Rule("TEST_RULE", this.state.constraints)));

        final DecisionTreeCollection analysedProfile = new DecisionTreeGenerator().analyse(profile);

        final IDataGenerator dataGenerator = new DataGenerator(
                new RowSpecMerger(
                        new FieldSpecMerger()),
                new ConstraintReducer(
                        new FieldSpecFactory(),
                        new FieldSpecMerger()));

        final GenerationConfig config = new GenerationConfig(GenerationConfig.DataGenerationType.FullSequential, new FieldExhaustiveCombinationStrategy());

        final TestCaseGenerationResult generationResult = dataGenerator.generateData(profile, analysedProfile.getMergedTree(), config);

        final TestCaseDataSet dataSet = generationResult.datasets.iterator().next();

        List<TestCaseDataRow> allActualRows = new ArrayList<>();

        dataSet.iterator().forEachRemaining(allActualRows::add);

        Assert.assertThat("Should be " + expectedResultsTable.size() + " rows of data", allActualRows.size(), equalTo(expectedResultsTable.size()));

        IntStream
                .range(
                        0,
                        Math.min(allActualRows.size(), expectedResultsTable.size()))
                .forEach(i -> {
                    TestCaseDataRow actualRow = allActualRows.get(i);
                    Map<String, String> expectedRow = expectedResultsTable.get(i);

                    int fieldIndex = 0;
                    for (Field field : this.state.profileFields) {
                        Object actualValue = actualRow.values.get(fieldIndex).value;
                        String expectedValueAsString = expectedRow.get(field.name);

                        Assert.assertThat(actualValue, equalTo(expectedValueAsString));

                        fieldIndex++;
                    }
                });
    }

}
