package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.*;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeCollection;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeGenerator;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.IDataGenerator;
import com.scottlogic.deg.generator.generation.combination_strategies.FieldExhaustiveCombinationStrategy;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.But;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;

public class GeneralTestStep {

    private DegTestState state;

    public GeneralTestStep(DegTestState state){
        this.state = state;
    }

    @Before
    public void BeforeEach() {
        this.state.clearState();
    }

    @Given("there is a field (.+)$")
    public void thereIsAField(String fieldName) {
        this.state.profileFields.add(new Field(fieldName));
    }

    @Given("^the following fields exist:$")
    public void thereAreFields(List<String> fields) {
        fields.forEach(this::thereIsAField);
    }

    @And("^(.+) is null$")
    public void fieldIsNull(String fieldName) throws Exception{
        this.state.addConstraint(fieldName, "null", null);
    }

    @And("^(.+) is not null$")
    public void fieldIsNotNull(String fieldName) throws Exception{
        this.state.addNotConstraint(fieldName, "null", null);
    }

    @But("the profile is invalid as (.+) can't be ([a-z ]+) (((\".*\")|([0-9]+(.[0-9]+){1}))+)")
    public void fieldIsInvalid(String fieldName, String constraint, String value) {
        Object parsedValue;
        if (value.startsWith("\"") && value.endsWith("\"")) {
            parsedValue = value.substring(1, value.length() - 1);
        }  else if (value.contains(".")){
            parsedValue = Double.parseDouble(value);
        } else {
            parsedValue = Integer.parseInt(value);
        }

        try {
            this.state.addConstraint(fieldName, constraint, parsedValue);
            Assert.fail("Expected invalid profile");
        } catch (Exception e) {
            Assert.assertNotNull(e);
            this.state.testExceptions.add(e);
        }
    }

    @Then("^I am presented with an error message$")
    public void dataGeneratorShouldError() {
        if (this.state.testExceptions.size() > 0) {
            return;
        }
        try {
            getGeneratedDataAsList();
            Assert.fail("Expected Exception");
        } catch (Exception e) {
            Assert.assertNotNull(e);
            this.state.testExceptions.add(e);
        }
    }

    @And("^no data is created$")
    public void noDataIsGenerated() {
        Assert.assertThat(this.state.testExceptions.size(), greaterThan(0));
    }

    @Then("^the following data should be generated:$")
    public void theFollowingDataShouldBeGenerated(List<Map<String, String>> expectedResultsTable) throws Exception {
        List <List<String>> rowsOfResults = getGeneratedDataAsList()
            .stream()
            .map(genObj ->
                genObj.values
                    .stream()
                    .map(dataBagValue -> this.getDataBagAsString(dataBagValue))
                    .collect(Collectors.toList())
            ).collect(Collectors.toList());

        List <List<String>> expectedRowsOfResults = expectedResultsTable
            .stream()
            .map(row -> new ArrayList<>(row.values()))
            .collect(Collectors.toList());
        Assert.assertThat(rowsOfResults, containsInAnyOrder(expectedRowsOfResults.toArray()));
    }

    private List<GeneratedObject> getGeneratedDataAsList() {
        final Iterable<GeneratedObject> dataSet = this.generateData();
        List<GeneratedObject> allActualRows = new ArrayList<>();
        dataSet.iterator().forEachRemaining(allActualRows::add);
        return allActualRows;
    }

    private Iterable<GeneratedObject> generateData() {
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
        return dataGenerator.generateData(profile, analysedProfile.getMergedTree(), config);
    }

    private String getDataBagAsString(DataBagValue x){
        if (x.value == null)
            return "null";

        if (x.format == null)
            return x.value.toString();

        return String.format(x.format, x.value);
    }
}
