package com.scottlogic.deg.profiler

import com.scottlogic.deg.analyser.field_analyser.GenericFieldAnalyser
import com.scottlogic.deg.analyser.field_analyser.numeric_analyser.NaiveNumericAnalyser
import com.scottlogic.deg.analyser.field_analyser.string_analyser.NaiveStringAnalyser
import com.scottlogic.deg.analyser.field_analyser.timestamp_analyser.NaiveTimestampAnalyser
import com.scottlogic.deg.classifier._
import com.scottlogic.deg.models.{Field, Profile}
import org.apache.spark.sql.DataFrame

object Profiler {
  def profile(df: DataFrame, fieldClassification: Seq[SemanticTypeField]): Profile = {
    val fields = df.schema.fields.map(field => new Field(field.name))

    val rules = df.schema.fields.map(field => {
      val semanticType = if (fieldClassification == null || fieldClassification.isEmpty) {
        StringType
      } else {
        fieldClassification.filter(f => f.fieldName == field.name).last.semanticType
      }

      (semanticType match {
        case DoubleType | FloatType | IntegerType => new NaiveNumericAnalyser(df, field)
        case TimeStampType => new NaiveTimestampAnalyser(df, field)
        case StringType | CountryCodeType | CurrencyType | EnumType | EmailType => new NaiveStringAnalyser(df, field)
        case _ => new GenericFieldAnalyser(df, field)
      }).constructField()
    })

    new Profile(fields, rules)
  }
}
