package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.cucumber.utils.CucumberTestHelper;
import com.scottlogic.deg.generator.cucumber.utils.CucumberTestState;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.time.LocalDateTime;
import java.util.function.Function;

public class DateValueStep {

    public static final String DATE_REGEX = "(-?(\\d{4,19})-(\\d{2})-(\\d{2}T(\\d{2}:\\d{2}:\\d{2}\\.\\d{3})))";
    private final CucumberTestState state;
    private final CucumberTestHelper helper;

    public DateValueStep(CucumberTestState state, CucumberTestHelper helper){
        this.state = state;
        this.helper = helper;
    }

    @When("{fieldVar} is {operator} {date}")
    public void whenFieldIsConstrainedByDateValue(String fieldName, String constraintName, DateObject value) throws Exception {
        state.addConstraint(fieldName, constraintName, value);
    }

    @When("{fieldVar} is anything but {operator} {date}")
    public void whenFieldIsNotConstrainedByDateValue(String fieldName, String constraintName, DateObject value) throws Exception {
        state.addNotConstraint(fieldName, constraintName, value);
    }

    @Then("{fieldVar} contains datetime data")
    public void producedDataShouldContainDateTimeValuesForField(String fieldName){
        helper.assertFieldContainsNullOrMatching(fieldName, LocalDateTime.class);
    }

    @Then("{fieldVar} contains anything but datetime data")
    public void producedDataShouldContainAnythingButStringValuesForField(String fieldName){
        helper.assertFieldContainsNullOrNotMatching(fieldName, LocalDateTime.class);
    }

    @Then("{fieldVar} contains datetimes between {date} and {date} inclusively")
    public void producedDataShouldContainDateTimeValuesInRangeForField(String fieldName, DateObject minInclusive, DateObject maxInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            LocalDateTime.class,
            isBetweenInclusively(minInclusive, maxInclusive));
    }

    @Then("{fieldVar} contains datetimes outside {date} and {date}")
    public void producedDataShouldContainDateTimeValuesOutOfRangeForField(String fieldName, DateObject min, DateObject max){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            LocalDateTime.class,
            value -> !isBetweenInclusively(min, max).apply(value));
    }

    @Then("{fieldVar} contains datetimes before or at {date}")
    public void producedDataShouldContainDateTimeValuesBeforeForField(String fieldName, DateObject beforeInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            LocalDateTime.class,
            value -> isBeforeOrAt(value, beforeInclusive));
    }

    @Then("{fieldVar} contains datetimes after or at {date}")
    public void producedDataShouldContainDateTimeValuesAfterForField(String fieldName, DateObject afterInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            LocalDateTime.class,
            value -> isAfterOrAt(value, afterInclusive));
    }

    private Function<LocalDateTime, Boolean> isBetweenInclusively(DateObject minInclusive, DateObject maxInclusive){
        return value -> isAfterOrAt(value, minInclusive) && isBeforeOrAt(value, maxInclusive);
    }

    private LocalDateTime getDateTime(DateObject dateObject){
        String dateString = (String)dateObject.get("date");
        return LocalDateTime.parse(dateString);
    }

    private boolean isAfterOrAt(LocalDateTime date, DateObject minInclusiveObject){
        LocalDateTime minInclusive = getDateTime(minInclusiveObject);
        return date.equals(minInclusive) || date.isAfter(minInclusive);
    }

    private boolean isBeforeOrAt(LocalDateTime date, DateObject maxInclusiveObject){
        LocalDateTime maxInclusive = getDateTime(maxInclusiveObject);
        return date.equals(maxInclusive) || date.isBefore(maxInclusive);
    }
}

