package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.cucumber.utils.DegTestState;
import cucumber.api.java.en.When;

import java.util.HashMap;
import java.util.Map;

public class DateValueStep {

    public static String DATE_REGEX = "((\\d{4})-(\\d{2})-(\\d{2}T(\\d{2}:\\d{2}:\\d{2}\\.\\d{3})))$";
    private DegTestState state;

    public DateValueStep(DegTestState state){
        this.state = state;
    }

    @When("{fieldVar} is {operator} {dateString}")
    public void whenFieldIsConstrainedByDateValue(String fieldName, String constraintName, String value) throws Exception {
        this.state.addConstraint(fieldName, constraintName, dateObject(value));
    }

    @When("{fieldVar} is anything but {operator} {dateString}")
    public void whenFieldIsNotConstrainedByDateValue(String fieldName, String constraintName, String value) throws Exception {
        this.state.addNotConstraint(fieldName, constraintName, dateObject(value));
    }

    private static Map dateObject(String dateValue) {
        HashMap map = new HashMap();
        map.put("date", dateValue);
        return map;
    }
}
