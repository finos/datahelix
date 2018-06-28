package com.scottlogic.deg

import com.scottlogic.deg.io.{FileReader, FileWriter}
import com.scottlogic.deg.profiler.Profiler
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object App {
    def main(args : Array[String]) {
        new DEGApp().run(args)
    }
}

trait SparkSessionBuilder {
    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)
    lazy val spark = SparkSession.builder
        .appName("Data Engineering Generator")
        .config("spark.master", "local")
        .getOrCreate()
}

class DEGApp extends SparkSessionBuilder {
    def run(args: Array[String]): Unit = {
        lazy val fileReader = new FileReader(spark)
        lazy val fileWriter = new FileWriter(spark)
        lazy val profiler = new Profiler(args, fileReader, fileWriter)
        profiler.profile()
        spark.stop()
    }
}
