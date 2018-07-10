package com.scottlogic.deg.analyser.field_analyser.string_analyser

import com.scottlogic.deg.dto
import com.scottlogic.deg.dto._
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructField

class NaiveStringAnalyser(val df: DataFrame, val field: StructField) extends StringAnalyser {
    private val spark = SparkSession.builder.getOrCreate()
    import spark.implicits._

    override def constructDTOField():AbstractField = {

        val analysis: (DataFrame, DataFrame) = analyse()
        val dfCharSet: DataFrame = analysis._1
        val lengthAnalysis = analysis._2.first()

        dto.TextField(
            name = field.name,
            nullPrevalence = 0.0d,
            distribution = PerCharacterRandomDistribution(
                lengthAnalysis.getAs("len_min"),
                lengthAnalysis.getAs("len_max"),
                lengthAnalysis.getAs("len_avg"),
                alphabet = dfCharSet.select("character").collect().mkString
            )
        )
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

        val dfLengthAnalysis = df.agg(
            min(length(col)).as("len_min"),
            max(length(col)).as("len_max"),
            avg(length(col)).as("len_avg"),
            stddev(length(col)).as("len_stddev")
        ).as(columnName)

        (dfCharSet, dfLengthAnalysis)
    }
}