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
        // deterministic mock generator
        field.name.charAt(3).charValue % 3 match {
            case 0 => EnumField(
                name = field.name,
                nullPrevalence = 0.0d,
                distribution = SetDistribution(
                    members = List(
                        SetDistributionMember(
                            name = "User",
                            prevalence = 0.2d
                        ),
                        SetDistributionMember(
                            name = "Admin",
                            prevalence = 0.8d
                        )
                    )
                )
            )
            case 1 => dto.TextField(
                name = field.name,
                nullPrevalence = 0.0d,
                distribution = PerCharacterRandomDistribution(
                    lengthMin = 0,
                    lengthMax = 10,
                    alphabet = "qwerty"
                )
            )
            case _ => UnknownField(
                name = field.name,
                nullPrevalence = 0.0d
            )
        }
    }

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