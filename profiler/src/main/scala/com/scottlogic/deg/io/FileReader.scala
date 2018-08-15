package com.scottlogic.deg.io

import java.io.File

import javax.inject.Inject
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}

class FileReader @Inject()(
  val spark: SparkSession
) {
    def readCSV(file: File): DataFrame = {
        spark.read.format("csv")
          // Do not infer Schema
          // .option("inferSchema", "true")
            .option("header", "true")
            .load(file.getPath)
    }

    def readCSVWithSchema(file: File, schema : StructType): DataFrame = {
        spark.read.format("csv")
          .option("header", "true")
          .schema(schema)
          .load(file.getPath)
    }

    def readJson(file: File): DataFrame = {
        spark.read.json(file.getPath)
    }
}
