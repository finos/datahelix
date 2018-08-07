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

    allFieldConstraints += fieldTypeConstraint;
    allFieldConstraints += minLengthConstraint;
    allFieldConstraints += maxLengthConstraint;
    if (inputField.dataType == IntegerType)
      allFieldConstraints += new HasDecimalPlacesConstraint(fieldName,0);

    return new Rule(s"Numeric field ${fieldName} rule", allFieldConstraints.toList);
  }
}
