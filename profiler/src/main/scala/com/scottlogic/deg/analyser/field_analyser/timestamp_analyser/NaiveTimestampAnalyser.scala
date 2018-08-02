package com.scottlogic.deg.analyser.field_analyser.timestamp_analyser

import java.util

import com.scottlogic.deg.schemas.v3.{ConstraintDTO, RuleDTO}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.StructField

class NaiveTimestampAnalyser(val df: DataFrame, val field: StructField) extends TimestampAnalyser {
    override def constructDTOField(): RuleDTO = {

        val inputField = field.name;

        val fieldTypeConstraint = new ConstraintDTO {
            def field = inputField;
            def is = "ofType";
            def value = "temporal";
        };

        val allConstraints = new util.ArrayList[ConstraintDTO]();
        allConstraints.add(fieldTypeConstraint);

        return new RuleDTO {
            def field = inputField;
            def constraints = allConstraints;
        };
    }
}
