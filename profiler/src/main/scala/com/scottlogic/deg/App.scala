package com.scottlogic.deg

import java.io.File

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Guice
import com.scottlogic.deg.classifier.{ClassifiedField, DataFrameClassifier, SemanticTypeField, StringType}
import com.scottlogic.deg.io.{FileReader, FileWriter}
import com.scottlogic.deg.mappers.ProfileDTOMapper
import com.scottlogic.deg.profiler.Profiler
import javax.inject.Inject
import net.codingwell.scalaguice.InjectorExtensions._
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

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

    val df = fileReader.readCSV(inFile)
    val classification = DataFrameClassifier.analyse(df)
    printClassification(classification)

    // TODO: At this point user should be able to confirm which fields are of which type. The user input would replace classification at this stage.
    // Converting back to SQL schema in order to be able to use Spark's SQL functionality on the data.
    val detectedSchema = DataFrameClassifier.detectSchema(classification)
    val typedData = fileReader.readCSVWithSchema(inFile, detectedSchema)

    // TODO: Delete this variable after we have gotten user input with specific types.
    val userInput = classification.map(c => {
      val total = c.typeDetectionCount(StringType).toFloat
      val mostRelevantType = c.typeDetectionCount.toArray.filter(t => t._2 / total > 0.5).maxBy(t => (t._1.rank, t._2 / total))._1
      SemanticTypeField(c.name, mostRelevantType)
    })

    val profile = Profiler.profile(typedData, userInput)
    val profileDTO = ProfileDTOMapper.Map(profile)

    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)

    val marshalled: String = mapper
      .writerWithDefaultPrettyPrinter()
      .writeValueAsString(profileDTO)

    val fileWriter = new FileWriter()
    fileWriter.write(outFile, marshalled)

    spark.stop()
  }

  // Present the result to users. Displaying results with more than 50% of value matches, sorted by ranking.
  def printClassification(classification: Seq[ClassifiedField]): Unit = {
    println("Field classification results:")

    classification.foreach(field => {
      println(field.name)
      val total = field.typeDetectionCount(StringType).toFloat

      field.typeDetectionCount.toArray.filter(t => t._2 / total > 0.5).sortBy(t => (t._1.rank, t._2 / total)).reverse.foreach(t => {
        println(f"\t${t._1} ${t._2 / total * 100.00}%.2f%%")
      })

      println
    })
  }
}
