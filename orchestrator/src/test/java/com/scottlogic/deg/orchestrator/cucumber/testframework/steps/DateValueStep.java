/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.orchestrator.cucumber.testframework.steps;

import com.scottlogic.deg.orchestrator.cucumber.testframework.utils.CucumberTestHelper;
import com.scottlogic.deg.orchestrator.cucumber.testframework.utils.CucumberTestState;
import com.scottlogic.deg.orchestrator.cucumber.testframework.utils.GeneratorTestUtilities;
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
    public void whenFieldIsConstrainedByDateValue(String fieldName, String constraintName, String value) {
        state.addConstraint(fieldName, constraintName, value);
    }

    @When("{fieldVar} is anything but {operator} {date}")
    public void whenFieldIsNotConstrainedByDateValue(String fieldName, String constraintName, String value) {
        state.addNotConstraint(fieldName, constraintName, value);
    }

    @Then("{fieldVar} contains only datetime data")
    public void producedDataShouldContainOnlyDateTimeValuesForField(String fieldName){
        helper.assertFieldContainsNullOrMatching(fieldName, OffsetDateTime.class);
    }

    @Then("{fieldVar} contains datetime data")
    public void producedDataShouldContainStringValuesForField(String fieldName){
        helper.assertFieldContainsSomeOf(fieldName, OffsetDateTime.class);
    }

    @Then("{fieldVar} contains anything but datetime data")
    public void producedDataShouldContainAnythingButStringValuesForField(String fieldName){
        helper.assertFieldContainsNullOrNotMatching(fieldName, OffsetDateTime.class);
    }

    @Then("{fieldVar} contains datetimes between {date} and {date} inclusively")
    public void producedDataShouldContainDateTimeValuesInRangeForField(String fieldName,  String minInclusive, String maxInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            OffsetDateTime.class,
            isBetweenInclusively(minInclusive, maxInclusive));
    }

    @Then("{fieldVar} contains datetimes outside {date} and {date}")
    public void producedDataShouldContainDateTimeValuesOutOfRangeForField(String fieldName, String min, String max){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            OffsetDateTime.class,
            value -> !isBetweenInclusively(min, max).apply(value));
    }

    @Then("{fieldVar} contains datetimes before or at {date}")
    public void producedDataShouldContainDateTimeValuesBeforeForField(String fieldName, String beforeInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            OffsetDateTime.class,
            value -> isBeforeOrAt(value, beforeInclusive));
    }

    @Then("{fieldVar} contains datetimes after or at {date}")
    public void producedDataShouldContainDateTimeValuesAfterForField(String fieldName, String afterInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            OffsetDateTime.class,
            value -> isAfterOrAt(value, afterInclusive));
    }

    private Function<OffsetDateTime, Boolean> isBetweenInclusively(String minInclusive, String maxInclusive){
        return value -> isAfterOrAt(value, minInclusive) && isBeforeOrAt(value, maxInclusive);
    }

    private boolean isAfterOrAt(OffsetDateTime date, String minInclusiveString){
        OffsetDateTime minInclusive = getDateTime(minInclusiveString);
        return date.equals(minInclusive) || date.isAfter(minInclusive);
    }

    private boolean isBeforeOrAt(OffsetDateTime date, String maxInclusiveString){
        OffsetDateTime maxInclusive = getDateTime(maxInclusiveString);
        return date.equals(maxInclusive) || date.isBefore(maxInclusive);
    }
    private OffsetDateTime getDateTime(String date){
        return GeneratorTestUtilities.getOffsetDateTime(date);
    }
}

