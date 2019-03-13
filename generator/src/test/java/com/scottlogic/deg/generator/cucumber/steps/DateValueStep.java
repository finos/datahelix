package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.cucumber.utils.CucumberTestHelper;
import com.scottlogic.deg.generator.cucumber.utils.CucumberTestState;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.time.LocalDateTime;
import java.util.function.Function;

public class DateValueStep {

    public static final String DATE_REGEX = "(-?(\\d{4,19})-(\\d{2})-(\\d{2}T(\\d{2}:\\d{2}:\\d{2}\\.\\d{3})))$";
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

    @Then("{fieldVar} contains temporal data")
    public void producedDataShouldContainTemporalValuesForField(String fieldName){
        helper.assertFieldContainsNullOrMatching(fieldName, LocalDateTime.class);
    }

    @Then("{fieldVar} contains anything but temporal data")
    public void producedDataShouldContainAnythingButStringValuesForField(String fieldName){
        helper.assertFieldContainsNullOrNotMatching(fieldName, LocalDateTime.class);
    }

    @Then("{fieldVar} contains temporal values between {date} and {date} inclusively")
    public void producedDataShouldContainTemporalValuesInRangeForField(String fieldName, DateObject minInclusive, DateObject maxInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            LocalDateTime.class,
            isBetweenInclusively(minInclusive, maxInclusive));
    }

    @Then("{fieldVar} contains temporal values outside {date} and {date}")
    public void producedDataShouldContainTemporalValuesOutOfRangeForField(String fieldName, DateObject minInclusive, DateObject maxInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            LocalDateTime.class,
            value -> !isBetweenInclusively(minInclusive, maxInclusive).apply(value));
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

