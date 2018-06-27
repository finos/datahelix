package com.scottlogic.deg.io

import java.io
import java.io.{BufferedWriter, File}

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

class FileWriter(spark: SparkSession) {
    def writeJson(name: String, df: DataFrame): Boolean = {
        df.write.mode(SaveMode.Overwrite).json("output/" + name)
        return true
    }

    def writeProfile(name: String, dfs: Array[(String, DataFrame)]): Unit = {
        val columnDistributions: String = dfs.map( columnData => {
            "{ \"columnName\": \"" + columnData._1 + "\", \"distribution\": " + columnData._2.toJSON.collect.mkString(", ") + "}"
        }).mkString(", ")

        val jsonString = "{ \"profileName\": \"testProfile\", " +
            "\"columnDistributions\": [" + columnDistributions + "]" +
            "}"
        val file = new File("test-output.json")
        val bw = new BufferedWriter(new io.FileWriter(file))
        bw.write(jsonString)
        bw.close()
    }
}
