package com.scottlogic.deg.analyser.field_analyser.timestamp_analyser

import com.scottlogic.deg.models._
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.functions._

import scala.collection.mutable.ListBuffer

class NaiveTimestampAnalyser(val df: DataFrame, val field: StructField) extends TimestampAnalyser {
    override def constructField(): Rule = {
        val inputField = field.name
        val column = df.col(inputField)

        val dateAnalysis = df.agg(
            min(column).as("min"),
            max(column).as("max")
        ).as(inputField).head()

        val fieldTypeConstraint = new IsOfTypeConstraint(inputField,"temporal")
        val higherThanOrEqualConstraint = new IsGreaterThanOrEqualToConstantConstraint(inputField, dateAnalysis.getAs[Int]("min").toString)
        val lowerThanConstraint = new IsLowerThanConstraint(inputField, dateAnalysis.getAs[Int]("max").toString)

        val allConstraints = ListBuffer[IConstraint]()
        allConstraints += fieldTypeConstraint
        allConstraints += higherThanOrEqualConstraint
        allConstraints += lowerThanConstraint

        new Rule(inputField, allConstraints.toList)
    }
}