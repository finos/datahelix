package com.scottlogic.deg.analyser.field_analyser.timestamp_analyser

import com.scottlogic.deg.dto
import com.scottlogic.deg.dto.{NormalDistribution, TemporalField}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.StructField

class NaiveTimestampAnalyser(val df: DataFrame, val field: StructField) extends TimestampAnalyser {
    override def constructDTOField(): TemporalField = {
        dto.TemporalField(
            name = field.name,
            nullPrevalence = 0.0d,
            distribution = NormalDistribution(
                meanAvg = 1.0d,
                stdDev = 1.0d,
                min = 1.0d,
                max = 1.0d
            )
        )
    }
}
