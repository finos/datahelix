package com.scottlogic.deg.analyser.field_analyser.numeric_analyser

import com.scottlogic.deg.dto.NumericField
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.{IntegerType, StructField}
import org.apache.spark.sql.functions._

class NaiveNumericAnalyser(val df: DataFrame, val field: StructField) extends NumericAnalyser {
    override def constructDTOField(): NumericField = {
        val head = df.agg(
            min(field.name).as("min"),
            max(field.name).as("max"),
            mean(field.name).as("mean"),
            stddev_pop(field.name).as("stddev_pop"),
            sum(col(field.name).isNull.cast(IntegerType))
                .divide(count(col(field.name)))
                .as("nullPrevalence")
        ).head()

        NumericField(
            name = field.name,
            meanAvg = head.getAs("mean"),
            stdDev = head.getAs("stddev_pop"),
            min = head.getAs("min"),
            max = head.getAs("max"),
            nullPrevalence = head.getAs("nullPrevalence")
        )
    }
}
