package com.scottlogic.deg.analyser.field_analyser.timestamp_analyser

import com.scottlogic.deg.models._
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.StructField

import scala.collection.mutable.ListBuffer

class NaiveTimestampAnalyser(val df: DataFrame, val field: StructField) extends TimestampAnalyser {
    override def constructField(): Rule = {

        val inputField = field.name;

        val fieldTypeConstraint = new IsOfTypeConstraint(inputField,"temporal");

        val allConstraints = ListBuffer[IConstraint]();
        allConstraints += fieldTypeConstraint;

        return new Rule(inputField, allConstraints.toList);
    }
}
