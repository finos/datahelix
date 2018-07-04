package com.scottlogic.deg.profiler

import com.scottlogic.deg.dto._
import com.scottlogic.deg.io.{FileReader, FileWriter}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField}

case class Location(lat: Double, lon: Double)

trait AbstractSparkToDTOFieldMapper {
    def constructDTOField():AbstractField
}

class StringSparkToDTOFieldMapper(val df: DataFrame, val field: StructField) extends AbstractSparkToDTOFieldMapper {
    override def constructDTOField() = StringField(name = field.name)
}

class UnknownSparkToDTOFieldMapper(val df: DataFrame, val field: StructField) extends AbstractSparkToDTOFieldMapper {
    override def constructDTOField() = UnknownField(name = field.name)
}

class NumericSparkToDTOFieldMapper(val df: DataFrame, val field: StructField) extends AbstractSparkToDTOFieldMapper {
    override def constructDTOField():NumericField = {
        val head = df.agg(
            min(field.name).alias("min"),
            max(field.name).alias("max"),
            mean(field.name).alias("mean"),
            stddev_pop(field.name).alias("stddev_pop")
        ).head()

        NumericField(
            name = field.name,
            meanAvg = head.getAs("mean"),
            stdDev = head.getAs("stddev_pop"),
            min = head.getAs("min"),
            max = head.getAs("max")
        )
    }
}

class Profiler(df: DataFrame) {
    def profile(): Profile = {
        Profile(fields = df.schema.fields.map(field => (field.dataType match {
                case DoubleType | IntegerType => new NumericSparkToDTOFieldMapper(df, field)
                case StringType => new StringSparkToDTOFieldMapper(df, field)
                case _ => new UnknownSparkToDTOFieldMapper(df, field)
            }).constructDTOField()
        ))
    }
}
