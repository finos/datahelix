package com.scottlogic.deg.analyser.StringAnalyser

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

class NaiveStringAnalyser extends StringAnalyser {
    def analyse(df: DataFrame, columnName: String): Unit = {
        val col = df(columnName)
        if (col != null) {
            val dfString = df.agg(min(col), max(col))
            dfString.show()
        }
    }
}
