package com.scottlogic.deg.analyser.field_analyser

import com.scottlogic.deg.dto.UnknownField
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.StructField

class UnknownSparkToDTOFieldMapper(val df: DataFrame, val field: StructField) extends FieldAnalyser {
    override def constructDTOField() = UnknownField(name = field.name, nullPrevalence = 0.0d)
}
