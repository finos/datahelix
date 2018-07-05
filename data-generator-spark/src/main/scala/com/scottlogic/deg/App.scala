package com.scottlogic.deg

import java.io.File

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.scottlogic.deg.io.{FileReader, FileWriter}
import com.scottlogic.deg.profiler.Profiler
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SparkSession}

case class Params(inputPath: String, outputDir: String)

object App {
    val usage = """
Usage: java com.scottlogic.deg.App inputFile outputDir
    """
    def main(args : Array[String]) {
        if (args.length < 2) {
            println(usage)
            return
        }

        new DEGApp().run(Params(
            inputPath = args(0),
            outputDir = args(1)
        ))
    }
}

trait SparkSessionBuilder {
    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)
    protected val spark = SparkSession.builder
        .appName("Data Engineering Generator")
        .config("spark.master", "local")
        .getOrCreate()
}

class DEGApp extends SparkSessionBuilder {
    var mapper:ObjectMapper = _

    def run(params: Params): Unit = {
        mapper = new ObjectMapper()
        mapper.registerModule(DefaultScalaModule)

        val inFile = new File(params.inputPath)
        val outFile = new File(params.outputDir, "test-output.json")

        val fileReader = new FileReader(spark)
        val df: DataFrame = fileReader.readCSV(inFile)

        val profiler = new Profiler(df)
        val profile = profiler.profile()

        val marshalled:String = mapper
          .writerWithDefaultPrettyPrinter()
          .writeValueAsString(profile)
        val fileWriter = new FileWriter()
        fileWriter.write(outFile, marshalled)

        spark.stop()
    }
}
