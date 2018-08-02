package com.scottlogic.deg.analyser.field_analyser

import com.scottlogic.deg.schemas.v3.RuleDTO
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.StructField

class GenericFieldAnalyser(val df: DataFrame, val field: StructField) extends FieldAnalyser {
  override def constructDTOField() = {
    val inputField = field.name;
    new RuleDTO {
      def field = inputField
    };
  }
}
