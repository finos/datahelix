package com.scottlogic.deg.analyser.field_analyser.string_analyser

import com.scottlogic.deg.models.{Constraint, ConstraintBuilder, Rule}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructField

import scala.collection.mutable.ListBuffer

class NaiveStringAnalyser(val df: DataFrame, val field: StructField) extends StringAnalyser {
  private val spark = SparkSession.builder.getOrCreate()

  import spark.implicits._

  override def constructField(): Rule = {

    val analysis: (DataFrame, DataFrame) = analyse()
    val dfCharSet: DataFrame = analysis._1
    val stringAnalysis = analysis._2.first()

    val fieldName = field.name;
    val allFieldConstraints = ListBuffer[Constraint]();

    val fieldTypeConstraint = ConstraintBuilder.instance
      .appendField(fieldName)
      .appendIs("ofType")
      .appendValue("string")
    .Build;

    val regexConstraint = ConstraintBuilder.instance
      .appendField(fieldName)
      .appendIs("matchesRegex")
      .appendValue(s".{${stringAnalysis.getAs("len_min")},${stringAnalysis.getAs("len_max")}}")
      .Build;

    allFieldConstraints += fieldTypeConstraint;
    allFieldConstraints += regexConstraint;

    // stringAnalysis.getAs("len_avg") not used anymore

    // alphabet = dfCharSet.select("character").collect().mkString not used for now

    return new Rule(s"String field ${fieldName} rule", allFieldConstraints.toList);
  }

  def analyse(): (DataFrame, DataFrame) = {
    val columnName = field.name
    val col = df(columnName)

    if (col == null) {
      throw new NullPointerException("Column does not exist")
    }

    // distribute map transformation load among workers rather collecting to driver
    val dfCharSet = df.select(columnName)
      .map(r => r.getString(0)).collect
      .mkString.toLowerCase
      .toSeq.map(char => char.toString)
      .toDF("character")
      .distinct()

    val dfStringAnalysis = df.agg(
      min(length(col)).as("len_min"),
      max(length(col)).as("len_max"),
      avg(length(col)).as("len_avg"),
      stddev(length(col)).as("len_stddev"),
      count(field.name).divide(count(lit(1))).as("nullPrevalence")
    ).as(columnName)

    (dfCharSet, dfStringAnalysis)
  }
}