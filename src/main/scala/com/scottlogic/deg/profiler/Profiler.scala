package com.scottlogic.deg.profiler

import com.scottlogic.deg.spark.reader.FileReader
import org.apache.spark.sql.DataFrame

class Profiler(args: Array[String], fileReader: FileReader) {
    def profile() {
        val df: DataFrame = fileReader.readCSV(args(0))
        df.show()
    }
}
