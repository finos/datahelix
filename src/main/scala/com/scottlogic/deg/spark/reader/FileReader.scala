package com.scottlogic.deg.spark.reader

import org.apache.spark.sql.{DataFrame, SparkSession}

class FileReader(spark: SparkSession) {
    def readCSV(localPath: String): DataFrame = {
        val fullPath = getClass.getResource(localPath).getPath
        val csv = spark.read.format("csv")
            .option("inferSchema", "true")
            .option("header", "true")
            .load(fullPath)
        return csv
    }

    def readJson(resourceFileName: String): DataFrame = {
        val resourcesPath = getClass.getResource(resourceFileName)
        return spark.read.json(resourcesPath.getPath)
    }
}
