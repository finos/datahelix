package com.scottlogic.deg.profiler

import com.scottlogic.deg.analyser.field_analyser.GenericFieldAnalyser
import com.scottlogic.deg.analyser.field_analyser.numeric_analyser.NaiveNumericAnalyser
import com.scottlogic.deg.analyser.field_analyser.string_analyser.NaiveStringAnalyser
import com.scottlogic.deg.dto._
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType}

case class Location(lat: Double, lon: Double)


class Profiler(df: DataFrame) {
    def profile(): Profile = {
        Profile(fields = df.schema.fields.map(field => (field.dataType match {
                case DoubleType | IntegerType => new NaiveNumericAnalyser(df, field)
                case StringType => new NaiveStringAnalyser(df, field)
                case _ => new GenericFieldAnalyser(df, field)
            }).constructDTOField()
        ))
    }
}