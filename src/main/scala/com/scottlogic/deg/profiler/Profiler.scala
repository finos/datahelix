package com.scottlogic.deg.profiler

import com.scottlogic.deg.io.{FileReader, FileWriter}
import org.apache.spark.sql.DataFrame

class Profiler(args: Array[String], fileReader: FileReader, fileWriter: FileWriter) {
    def profile() {
        val df: DataFrame = fileReader.readCSV(args(0))
        df.show()
        fileWriter.writeJson("profile", df)
    }
}
