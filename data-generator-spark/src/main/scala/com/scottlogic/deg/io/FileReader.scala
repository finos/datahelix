package com.scottlogic.deg.io

import java.io.File

import org.apache.spark.sql.{DataFrame, SparkSession}

class FileReader(spark: SparkSession) {
    def readCSV(file: File): DataFrame = {
        spark.read.format("csv")
            .option("inferSchema", "true")
            .option("header", "true")
            .load(file.getPath)
    }

    def readJson(file: File): DataFrame = {
        spark.read.json(file.getPath)
    }
}
