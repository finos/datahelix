package com.scottlogic.deg.profiler

import java.util

import com.scottlogic.deg.analyser.field_analyser.GenericFieldAnalyser
import com.scottlogic.deg.analyser.field_analyser.numeric_analyser.NaiveNumericAnalyser
import com.scottlogic.deg.analyser.field_analyser.string_analyser.NaiveStringAnalyser
import com.scottlogic.deg.analyser.field_analyser.timestamp_analyser.NaiveTimestampAnalyser
import com.scottlogic.deg.schemas.v3.{FieldDTO, RuleDTO, V3ProfileDTO}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types._

import collection.JavaConverters._

case class Location(lat: Double, lon: Double)

class Profiler(df: DataFrame) {
  def profile(): V3ProfileDTO = {
    val fieldArray = df.schema.fields.map(field => new FieldDTO(field.name.toString()));
    val ruleArray = df.schema.fields.map(field => (field.dataType match {
      case DoubleType | LongType | IntegerType => new NaiveNumericAnalyser(df, field)
      case TimestampType => new NaiveTimestampAnalyser(df, field)
      case StringType => new NaiveStringAnalyser(df, field)
      case _ => new GenericFieldAnalyser(df, field)
    }).constructDTOField());

    val profile = new V3ProfileDTO();
    profile.fields = asJavaCollectionConverter(fieldArray).asJavaCollection;
    profile.rules = asJavaCollectionConverter(ruleArray).asJavaCollection;

    return profile;
  }
}