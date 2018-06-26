package com.scottlogic.deg.spark.reader

import org.apache.spark.sql.{DataFrame, SparkSession}

class FileReader(spark: SparkSession) {
    def readCSV(filepath: String): DataFrame = {
        val csv = spark.read.format("csv")
            .option("inferSchema", "true")
            .option("header", "true")
            .load(filepath)
        return csv
    }

    def readJson(filepath: String): DataFrame = {
        return spark.read.json(filepath)
    }
}
