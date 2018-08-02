package com.scottlogic.deg.analyser.field_analyser.numeric_analyser

import java.util

import com.scottlogic.deg.schemas.v3.{ConstraintDTO, ConstraintDTOBuilder, RuleDTO}
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

    val fieldTypeConstraint = ConstraintDTOBuilder.instance()
      .appendField(fieldName)
      .appendIs("ofType")
      .appendValue("number")
      .Build();

    val minLengthConstraint = ConstraintDTOBuilder.instance()
      .appendField(fieldName)
      .appendIs("greaterThanOrEqual")
      .appendValue(head.getAs("min"))
      .Build();

    val maxLengthConstraint = ConstraintDTOBuilder.instance()
      .appendField(fieldName)
      .appendNot(
        ConstraintDTOBuilder.instance()
        .appendField(fieldName)
        .appendIs("greaterThan")
        .appendValue(head.getAs("max"))
        .Build()
      )
      .Build();

    val regexConstraint = ConstraintDTOBuilder.instance()
      .appendField(fieldName)
      .appendIs("matchesRegex")
      .appendValue(
        inputField.dataType match {
          case DoubleType => "%f"
          case LongType => "%l"
          case IntegerType => "%d"
        }
      )
      .Build();

    allFieldConstraints.add(fieldTypeConstraint);
    allFieldConstraints.add(minLengthConstraint);
    allFieldConstraints.add(maxLengthConstraint);
    allFieldConstraints.add(regexConstraint);

    return new RuleDTO(s"Numeric field ${fieldName} rule", allFieldConstraints);
  }
}
