package com.scottlogic.deg.io

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

class FileWriter(spark: SparkSession) {
    def writeJson(name: String, df: DataFrame): Boolean = {
        df.write.json("output/" + name)
        return true
    }
}
