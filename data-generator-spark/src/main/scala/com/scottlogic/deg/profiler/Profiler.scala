package com.scottlogic.deg.profiler

import com.scottlogic.deg.dto._
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import org.apache.spark.sql.functions.{sum,when}
import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField}

case class Location(lat: Double, lon: Double)

trait AbstractSparkToDTOFieldMapper {
    def constructDTOField():AbstractField
}

class StringSparkToDTOFieldMapper(val df: DataFrame, val field: StructField) extends AbstractSparkToDTOFieldMapper {
    override def constructDTOField():StringField = {
        // deterministic mock generator
        val specialization = field.name.charAt(3).charValue % 3 match {
            case 0 => StringFieldEnumSpecialization(
                members = List(
                    StringFieldEnumMember(
                        name = "User",
                        prevalence = 0.2d
                    ),
                    StringFieldEnumMember(
                        name = "Admin",
                        prevalence = 0.8d
                    )
                )
            )
            case 1 => StringFieldTextSpecialization(
                lenMin = 0,
                lenMax = 10,
                lenMeanAvg = 5.0d,
                lenStdDev = 1.0d
            )
            case _ => StringFieldUnknownSpecialization()
        }
        StringField(
            name = field.name,
            nullPrevalence = 0.0d,
            specialization = specialization
        )
    }
}

class UnknownSparkToDTOFieldMapper(val df: DataFrame, val field: StructField) extends AbstractSparkToDTOFieldMapper {
    override def constructDTOField() = UnknownField(name = field.name, nullPrevalence = 0.0d)
}

class NumericSparkToDTOFieldMapper(val df: DataFrame, val field: StructField) extends AbstractSparkToDTOFieldMapper {
    override def constructDTOField():NumericField = {
        val head = df.agg(
            min(field.name).as("min"),
            max(field.name).as("max"),
            mean(field.name).as("mean"),
            stddev_pop(field.name).as("stddev_pop"),
            sum(col(field.name).isNull.cast(IntegerType))
                .divide(count(col(field.name)))
                .as("nullPrevalence")
        ).head()

        NumericField(
            name = field.name,
            meanAvg = head.getAs("mean"),
            stdDev = head.getAs("stddev_pop"),
            min = head.getAs("min"),
            max = head.getAs("max"),
            nullPrevalence = head.getAs("nullPrevalence")
        )
    }
}

class Profiler(df: DataFrame) {
    def profile(): Profile = {
        Profile(
            schemaVersion = 1,
            fields = df.schema.fields.map(field => (field.dataType match {
                case DoubleType | IntegerType => new NumericSparkToDTOFieldMapper(df, field)
                case StringType => new StringSparkToDTOFieldMapper(df, field)
                case _ => new UnknownSparkToDTOFieldMapper(df, field)
            }).constructDTOField()
        ))
    }
}
