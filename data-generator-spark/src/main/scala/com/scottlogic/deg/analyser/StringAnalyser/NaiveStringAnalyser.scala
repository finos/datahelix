package com.scottlogic.deg.analyser.StringAnalyser

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

class NaiveStringAnalyser(spark: SparkSession) extends StringAnalyser {
    import spark.implicits._
    def analyse(df: DataFrame, columnName: String): (DataFrame, DataFrame) = {
        val col = df(columnName)

        if (col == null) {
            throw new NullPointerException("Column does not exist")
        }

        // distribute map transformation load among workers rather collecting to driver
        val dfCharSet = df.select(columnName)
            .map(r => r.getString(0)).collect.toList
            .mkString.toLowerCase
            .toSeq.map(char => char.toString)
            .toDF("character")
            .distinct()

        val dfLengthAnalysis = df.agg(
            min(length(col)).as("len_min"),
            max(length(col)).as("len_max"),
            avg(length(col)).as("len_avg"),
            stddev(length(col)).as("len_stddev")
        ).as(columnName)

        (dfCharSet, dfLengthAnalysis)
    }
}