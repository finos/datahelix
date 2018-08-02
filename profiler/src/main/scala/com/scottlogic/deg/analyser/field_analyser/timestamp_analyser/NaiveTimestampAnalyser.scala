package com.scottlogic.deg.analyser.field_analyser.timestamp_analyser

import java.util

import com.scottlogic.deg.schemas.v3.{ConstraintDTO, ConstraintDTOBuilder, RuleDTO}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.StructField

class NaiveTimestampAnalyser(val df: DataFrame, val field: StructField) extends TimestampAnalyser {
    override def constructDTOField(): RuleDTO = {

        val inputField = field.name;

        val fieldTypeConstraint = ConstraintDTOBuilder.instance()
          .appendField(inputField)
          .appendIs("ofType")
          .appendValue("temporal")
          .Build();

        val allConstraints = new util.ArrayList[ConstraintDTO]();
        allConstraints.add(fieldTypeConstraint);

        return new RuleDTO(inputField, allConstraints);
    }
}
