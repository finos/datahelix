package com.scottlogic.deg.analyser.field_analyser.string_analyser

import java.util

import com.scottlogic.deg.schemas.v3.{ConstraintDTO, ConstraintDTOBuilder, RuleDTO}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructField

class NaiveStringAnalyser(val df: DataFrame, val field: StructField) extends StringAnalyser {
  private val spark = SparkSession.builder.getOrCreate()

  import spark.implicits._

  override def constructDTOField(): RuleDTO = {

    val analysis: (DataFrame, DataFrame) = analyse()
    val dfCharSet: DataFrame = analysis._1
    val stringAnalysis = analysis._2.first()

    val fieldName = field.name;
    val allFieldConstraints = new util.ArrayList[ConstraintDTO]();

    val fieldTypeConstraint = ConstraintDTOBuilder.instance()
      .appendField(fieldName)
      .appendIs("ofType")
      .appendValue("string")
    .Build();

    val regexConstraint = ConstraintDTOBuilder.instance()
      .appendField(fieldName)
      .appendIs("matchesRegex")
      .appendValue(s".{${stringAnalysis.getAs("len_min")},${stringAnalysis.getAs("len_max")}}")
      .Build();

    allFieldConstraints.add(fieldTypeConstraint);
    allFieldConstraints.add(regexConstraint);

    // stringAnalysis.getAs("len_avg") not used anymore

    // alphabet = dfCharSet.select("character").collect().mkString not used for now

    return new RuleDTO(s"String field ${fieldName} rule", allFieldConstraints);
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