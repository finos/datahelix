package com.scottlogic.deg.analyser.field_analyser.timestamp_analyser

import java.text.SimpleDateFormat

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
        var dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'SSS");

        val higherThanOrEqualConstraint = new IsAfterOrEqualToConstantDateTimeConstraint(inputField, Map("date" -> dateFormat.format(dateAnalysis.getAs("min"))))
        val lowerThanConstraint = new IsBeforeConstantDateTimeConstraint(inputField, Map("date" -> dateFormat.format(dateAnalysis.getAs("max"))))

        val allConstraints = ListBuffer[Constraint]()
        allConstraints += fieldTypeConstraint
        allConstraints += higherThanOrEqualConstraint
        allConstraints += lowerThanConstraint

        new Rule(inputField, allConstraints.toList)
    }
}