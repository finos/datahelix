package com.scottlogic.deg

import com.scottlogic.deg.spark.SparkWrapper

object App {
    def main(args : Array[String]) {
        lazy val spark = new SparkWrapper()

        spark.stop()
    }
}
