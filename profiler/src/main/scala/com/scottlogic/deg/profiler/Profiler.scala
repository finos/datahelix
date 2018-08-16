package com.scottlogic.deg.profiler

import com.scottlogic.deg.analyser.field_analyser.GenericFieldAnalyser
import com.scottlogic.deg.analyser.field_analyser.numeric_analyser.NaiveNumericAnalyser
import com.scottlogic.deg.analyser.field_analyser.string_analyser.NaiveStringAnalyser
import com.scottlogic.deg.analyser.field_analyser.timestamp_analyser.NaiveTimestampAnalyser
import com.scottlogic.deg.models.{Field, Profile}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types._

case class Location(lat: Double, lon: Double)

class Profiler(df: DataFrame) {
  def profile(): Profile = {
    val fieldArray = df.schema.fields.map(field => new Field(field.name));
    // TODO: Use our SemanticTypes
    val ruleArray = df.schema.fields.map(field => (field.dataType match {
      case DoubleType | FloatType | LongType | IntegerType => new NaiveNumericAnalyser(df, field)
      case TimestampType => new NaiveTimestampAnalyser(df, field)
      case StringType => new NaiveStringAnalyser(df, field)
      case _ => new GenericFieldAnalyser(df, field)
    }).constructField());

    val profile = new Profile();
    profile.Fields = fieldArray.toList;
    profile.Rules = ruleArray.toList;

    return profile;
  }
}