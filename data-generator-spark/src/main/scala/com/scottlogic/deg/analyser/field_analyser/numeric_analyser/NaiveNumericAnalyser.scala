package com.scottlogic.deg.analyser.field_analyser.numeric_analyser

import com.scottlogic.deg.dto.{NormalDistribution, NumericField, SprintfNumericFormat}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.{DoubleType, IntegerType, LongType, StructField}
import org.apache.spark.sql.functions.{max, min, _}

class NaiveNumericAnalyser(val df: DataFrame, val field: StructField) extends NumericAnalyser {
    override def constructDTOField():NumericField = {
        val head = df.agg(
            min(field.name).as("min"),
            max(field.name).as("max"),
            mean(field.name).as("mean"),
            stddev_pop(field.name).as("stddev_pop"),
            lit(1).minus(count(field.name)
                .divide(count(lit(1))))
                .as("nullPrevalence")
        ).head()

        NumericField(
            name = field.name,
            nullPrevalence = head.getAs("nullPrevalence"),
            distribution = NormalDistribution(
                meanAvg = head.getAs("mean"),
                stdDev = head.getAs("stddev_pop"),
                min = head.getAs("min"),
                max = head.getAs("max")
            ),
            format = SprintfNumericFormat(
                template = field.dataType match {
                    case DoubleType => "%f"
                    case LongType => "%l"
                    case IntegerType => "%d"
                }
            )
        )
    }
}
