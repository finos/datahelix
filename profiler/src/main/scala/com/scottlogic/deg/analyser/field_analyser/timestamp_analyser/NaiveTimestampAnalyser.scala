package com.scottlogic.deg.analyser.field_analyser.timestamp_analyser

import com.scottlogic.deg.models.{Constraint, ConstraintBuilder, Rule}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.StructField

import scala.collection.mutable.ListBuffer

class NaiveTimestampAnalyser(val df: DataFrame, val field: StructField) extends TimestampAnalyser {
    override def constructField(): Rule = {

        val inputField = field.name;

        val fieldTypeConstraint = ConstraintBuilder.instance
          .appendField(inputField)
          .appendIs("ofType")
          .appendValue("temporal")
          .Build;

        val allConstraints = ListBuffer[Constraint]();
        allConstraints += fieldTypeConstraint;

        return new Rule(inputField, allConstraints.toList);
    }
}
