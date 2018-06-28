package com.scottlogic.deg.profiler

import com.scottlogic.deg.io.{FileReader, FileWriter}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{min, max, mean, stddev_pop, stddev_samp, variance}

class Profiler(args: Array[String], fileReader: FileReader, fileWriter: FileWriter) {
    def profile() {
        val df: DataFrame = fileReader.readCSV(args(0))
        df.printSchema()
        val dfs: Array[(String, DataFrame)] = df.columns zip
            df.columns.map(col => df.agg(min(col), max(col), mean(col), stddev_pop(col), stddev_samp(col), variance(col)))
        fileWriter.writeJson("profile", df)
        fileWriter.writeProfile("profileString", dfs)
    }
}
