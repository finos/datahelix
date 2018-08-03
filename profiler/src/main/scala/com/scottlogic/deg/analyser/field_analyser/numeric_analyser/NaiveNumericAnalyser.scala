package com.scottlogic.deg.analyser.field_analyser.numeric_analyser

import com.scottlogic.deg.models.{Constraint, ConstraintBuilder, Rule}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.{DoubleType, IntegerType, LongType, StructField}
import org.apache.spark.sql.functions.{max, min, _}

import scala.collection.mutable.ListBuffer

class NaiveNumericAnalyser(val df: DataFrame, val field: StructField) extends NumericAnalyser {
  override def constructField(): Rule = {
    val head = df.agg(
      min(field.name).as("min"),
      max(field.name).as("max"),
      mean(field.name).as("mean"),
      stddev_pop(field.name).as("stddev_pop"),
      count(field.name).divide(count(lit(1))).as("nullPrevalence")
    ).head()

    val inputField = field;
    val fieldName = inputField.name;
    val allFieldConstraints = ListBuffer[Constraint]();

    val fieldTypeConstraint = ConstraintBuilder.instance
      .appendField(fieldName)
      .appendIs("ofType")
      .appendValue("number")
      .Build;

    val minLengthConstraint = ConstraintBuilder.instance
      .appendField(fieldName)
      .appendIs("greaterThanOrEqual")
      .appendValue(head.getAs("min"))
      .Build;

    val maxLengthConstraint = ConstraintBuilder.instance
      .appendField(fieldName)
      .appendNot(
        ConstraintBuilder.instance
        .appendField(fieldName)
        .appendIs("greaterThan")
        .appendValue(head.getAs("max"))
        .Build
      )
      .Build;

    val regexConstraint = ConstraintBuilder.instance
      .appendField(fieldName)
      .appendIs("matchesRegex")
      .appendValue(
        inputField.dataType match {
          case DoubleType => "%f"
          case LongType => "%l"
          case IntegerType => "%d"
        }
      )
      .Build;

    allFieldConstraints += fieldTypeConstraint;
    allFieldConstraints += minLengthConstraint;
    allFieldConstraints += maxLengthConstraint;
    allFieldConstraints += regexConstraint;

    return new Rule(s"Numeric field ${fieldName} rule", allFieldConstraints.toList);
  }
}
