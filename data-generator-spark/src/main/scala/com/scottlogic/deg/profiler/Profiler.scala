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
    override def constructDTOField(): StringField = StringField()
}

class UnknownSparkToDTOFieldMapper(val df: DataFrame, val field: StructField) extends AbstractSparkToDTOFieldMapper {
    override def constructDTOField(): StringField = StringField()
}

class NumericSparkToDTOFieldMapper(val df: DataFrame, val field: StructField) extends AbstractSparkToDTOFieldMapper {
    val head = df.agg(
        min(field.name).alias("min"),
        max(field.name).alias("max"),
        mean(field.name).alias("mean"),
        stddev_pop(field.name).alias("stddev_pop")
    ).head()

    override def constructDTOField(): NumericField = NumericField(
        meanAvg = head.getAs("mean"),
        stdDev = head.getAs("stddev_pop"),
        min = head.getAs("min"),
        max = head.getAs("max")
    )
}

class Profiler(args: Array[String], fileReader: FileReader, fileWriter: FileWriter) {
    def profile() {
        val df: DataFrame = fileReader.readCSV(args(0))
        df.printSchema()

        val profile: Profile = Profile(fields = df.schema.fields.map(field => (field.dataType match {
                case DoubleType | IntegerType => new NumericSparkToDTOFieldMapper(df, field)
                case StringType => new StringSparkToDTOFieldMapper(df, field)
                case _ => new UnknownSparkToDTOFieldMapper(df, field)
            }).constructDTOField()
        ))
        val dfs: Array[(String, DataFrame)] = df.columns zip
            df.columns.map(col => df.agg(min(col), max(col), mean(col), stddev_pop(col), stddev_samp(col), variance(col)))
        fileWriter.writeJson("profile", df)
        fileWriter.writeProfile("profileString", dfs)
    }
}
