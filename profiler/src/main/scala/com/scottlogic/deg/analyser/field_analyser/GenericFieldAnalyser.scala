package com.scottlogic.deg.analyser.field_analyser

import com.scottlogic.deg.models.{Constraint, Rule}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.StructField

class GenericFieldAnalyser(val df: DataFrame, val field: StructField) extends FieldAnalyser {
  override def constructField() = {
    new Rule(s"Generic rule for field ${field.name}", List[Constraint]())
  }
}
