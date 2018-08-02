package com.scottlogic.deg.analyser.field_analyser.numeric_analyser

import java.util

import com.scottlogic.deg.schemas.v3.{ConstraintDTO, RuleDTO}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.{DoubleType, IntegerType, LongType, StructField}
import org.apache.spark.sql.functions.{max, min, _}

class NaiveNumericAnalyser(val df: DataFrame, val field: StructField) extends NumericAnalyser {
  override def constructDTOField(): RuleDTO = {
    val head = df.agg(
      min(field.name).as("min"),
      max(field.name).as("max"),
      mean(field.name).as("mean"),
      stddev_pop(field.name).as("stddev_pop"),
      count(field.name).divide(count(lit(1))).as("nullPrevalence")
    ).head()

    val inputField = field;
    val fieldName = inputField.name;
    val allFieldConstraints = new util.ArrayList[ConstraintDTO]();

    val fieldTypeConstraint = new ConstraintDTO {
      def field = fieldName;
      def is = "ofType";
      def value = "number";
    };

    val minLengthConstraint = new ConstraintDTO {
      def field = fieldName;
      def is = "greaterThanOrEqual";
      def value = head.getAs("min");
    };

    val maxLengthConstraint = new ConstraintDTO {
      def field = fieldName;
      def not = new ConstraintDTO {
        def field = fieldName;
        def is = "greaterThan";
        def value = head.getAs("max");
      };
    };

    val regexConstraint = new ConstraintDTO {
      def field = fieldName;
      def is = "matchesRegex";
      def value = inputField.dataType match {
        case DoubleType => "%f"
        case LongType => "%l"
        case IntegerType => "%d"
      };
    };

    allFieldConstraints.add(fieldTypeConstraint);
    allFieldConstraints.add(minLengthConstraint);
    allFieldConstraints.add(maxLengthConstraint);
    allFieldConstraints.add(regexConstraint);

    val ruleDTO = new RuleDTO {
      def constraints = allFieldConstraints;
      def description = s"Numeric field ${fieldName} rule";
    };

    return ruleDTO;
  }
}
