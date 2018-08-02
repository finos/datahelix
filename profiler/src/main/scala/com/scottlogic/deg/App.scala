package com.scottlogic.deg

import java.io.File

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Guice
import com.scottlogic.deg.io.{FileReader, FileWriter}
import com.scottlogic.deg.profiler.Profiler
import javax.inject.Inject
import net.codingwell.scalaguice.InjectorExtensions._
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SparkSession}

case class Params(inputPath: String, outputDir: String)

object App {
  private val injector = Guice.createInjector(new LiveModule, new SharedModule)

  val usage =
    """
Usage: java com.scottlogic.deg.App inputFile outputDir
    """

  def main(args: Array[String]) {
    if (args.length < 2) {
      println(usage)
      return
    }

    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)

    injector.instance[DEGApp].run(Params(
      inputPath = args(0),
      outputDir = args(1)
    ))
  }
}

class DEGApp @Inject()(
                        val spark: SparkSession,
                        val mapper: ObjectMapper,
                        val fileReader: FileReader
                      ) {
  def run(params: Params): Unit = {
    val inFile = new File(params.inputPath)
    val outFile = new File(params.outputDir, "test-output.json")

    val df: DataFrame = fileReader.readCSV(inFile)

    val profiler = new Profiler(df)
    val profile = profiler.profile()

    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    val marshalled: String = mapper
      .writerWithDefaultPrettyPrinter()
      .writeValueAsString(profile)

    val fileWriter = new FileWriter()
    fileWriter.write(outFile, marshalled)

    spark.stop()
  }
}
