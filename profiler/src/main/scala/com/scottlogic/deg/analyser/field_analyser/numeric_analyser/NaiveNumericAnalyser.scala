package com.scottlogic.deg.analyser.field_analyser.numeric_analyser

import com.scottlogic.deg.models._
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
    val allFieldConstraints = ListBuffer[IConstraint]();

    val fieldTypeConstraint = new IsOfTypeConstraint(fieldName,"numeric");

    val minLengthConstraint = new IsGreaterThanOrEqualToConstantConstraint(fieldName,head.getAs("min").toString);

    val maxLengthConstraint = new IsLowerThanConstraint(fieldName, head.getAs("max").toString);

    val regexExp = inputField.dataType match {
      case DoubleType => "^\\d+(\\.\\d+)?$"
      case LongType => "^\\d+(\\.\\d+)?$"
      case IntegerType => "^\\d$"
    };

    val regexConstraint = new MatchesRegexConstraint(fieldName, regexExp);

    allFieldConstraints += fieldTypeConstraint;
    allFieldConstraints += minLengthConstraint;
    allFieldConstraints += maxLengthConstraint;
    allFieldConstraints += regexConstraint;

    return new Rule(s"Numeric field ${fieldName} rule", allFieldConstraints.toList);
  }
}
