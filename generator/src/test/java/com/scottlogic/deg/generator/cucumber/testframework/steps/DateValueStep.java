package com.scottlogic.deg.generator.cucumber.testframework.steps;

import com.scottlogic.deg.generator.cucumber.testframework.utils.CucumberTestHelper;
import com.scottlogic.deg.generator.cucumber.testframework.utils.CucumberTestState;
import com.scottlogic.deg.generator.cucumber.testframework.utils.GeneratorTestUtilities;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.time.OffsetDateTime;
import java.util.function.Function;

public class DateValueStep {

    public static final String DATE_REGEX = "(-?(\\d{4,19})-(\\d{2})-(\\d{2}T(\\d{2}:\\d{2}:\\d{2}\\.\\d{3}))Z?)";
    private final CucumberTestState state;
    private final CucumberTestHelper helper;

    public DateValueStep(CucumberTestState state, CucumberTestHelper helper){
        this.state = state;
        this.helper = helper;
    }

    @When("{fieldVar} is {operator} {date}")
    public void whenFieldIsConstrainedByDateValue(String fieldName, String constraintName, DateObject value) {
        state.addConstraint(fieldName, constraintName, value);
    }

    @When("{fieldVar} is anything but {operator} {date}")
    public void whenFieldIsNotConstrainedByDateValue(String fieldName, String constraintName, DateObject value) {
        state.addNotConstraint(fieldName, constraintName, value);
    }

    @Then("{fieldVar} contains datetime data")
    public void producedDataShouldContainDateTimeValuesForField(String fieldName){
        helper.assertFieldContainsNullOrMatching(fieldName, OffsetDateTime.class);
    }

    @Then("{fieldVar} contains anything but datetime data")
    public void producedDataShouldContainAnythingButStringValuesForField(String fieldName){
        helper.assertFieldContainsNullOrNotMatching(fieldName, OffsetDateTime.class);
    }

    @Then("{fieldVar} contains datetimes between {date} and {date} inclusively")
    public void producedDataShouldContainDateTimeValuesInRangeForField(String fieldName, DateObject minInclusive, DateObject maxInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            OffsetDateTime.class,
            isBetweenInclusively(minInclusive, maxInclusive));
    }

    @Then("{fieldVar} contains datetimes outside {date} and {date}")
    public void producedDataShouldContainDateTimeValuesOutOfRangeForField(String fieldName, DateObject min, DateObject max){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            OffsetDateTime.class,
            value -> !isBetweenInclusively(min, max).apply(value));
    }

    @Then("{fieldVar} contains datetimes before or at {date}")
    public void producedDataShouldContainDateTimeValuesBeforeForField(String fieldName, DateObject beforeInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            OffsetDateTime.class,
            value -> isBeforeOrAt(value, beforeInclusive));
    }

    @Then("{fieldVar} contains datetimes after or at {date}")
    public void producedDataShouldContainDateTimeValuesAfterForField(String fieldName, DateObject afterInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            OffsetDateTime.class,
            value -> isAfterOrAt(value, afterInclusive));
    }

    private Function<OffsetDateTime, Boolean> isBetweenInclusively(DateObject minInclusive, DateObject maxInclusive){
        return value -> isAfterOrAt(value, minInclusive) && isBeforeOrAt(value, maxInclusive);
    }

    private OffsetDateTime getDateTime(DateObject dateObject){
        String dateString = (String)dateObject.get("date");
        return GeneratorTestUtilities.getOffsetDateTime(dateString);
    }

    private boolean isAfterOrAt(OffsetDateTime date, DateObject minInclusiveObject){
        OffsetDateTime minInclusive = getDateTime(minInclusiveObject);
        return date.equals(minInclusive) || date.isAfter(minInclusive);
    }

    private boolean isBeforeOrAt(OffsetDateTime date, DateObject maxInclusiveObject){
        OffsetDateTime maxInclusive = getDateTime(maxInclusiveObject);
        return date.equals(maxInclusive) || date.isBefore(maxInclusive);
    }
}

