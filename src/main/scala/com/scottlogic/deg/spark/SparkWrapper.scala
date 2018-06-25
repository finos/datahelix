package com.scottlogic.deg.spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

class SparkWrapper {
    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)
    lazy val spark = SparkSession.builder
        .appName("Data Engineering Generator")
        .config("spark.master", "local")
        .getOrCreate()

    def stop(): Unit = {
        spark.stop()
    }
}
