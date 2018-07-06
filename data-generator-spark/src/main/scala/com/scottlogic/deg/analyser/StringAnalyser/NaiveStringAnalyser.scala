package com.scottlogic.deg.analyser.StringAnalyser

import com.scottlogic.deg.SparkSessionBuilder
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

class NaiveStringAnalyser(spark: SparkSession) extends StringAnalyser {
    def analyse(df: DataFrame, columnName: String): Unit = {
        val col = df(columnName)
        if (col != null) {
            val dfString = df.agg(
                min(length(col)).as("len_min"),
                max(length(col)).as("len_max"),
                avg(length(col)).as("len_avg"),
                stddev(length(col)).as("len_stddev")
            ).as(columnName)
            dfString.show()
        }
    }
}
