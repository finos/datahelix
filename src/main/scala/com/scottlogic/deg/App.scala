package com.scottlogic.deg

import org.apache.spark.sql.SparkSession
import org.apache.log4j.Logger
import org.apache.log4j.Level


object App {
    def main(args : Array[String]) {
        Logger.getLogger("org").setLevel(Level.ERROR)
        Logger.getLogger("akka").setLevel(Level.ERROR)
        val spark = SparkSession.builder
            .appName("Data Engineering Generator")
            .config("spark.master", "local")
            .getOrCreate()
        spark.stop()
    }
}