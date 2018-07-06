package com.scottlogic.deg

import java.io.File

import com.scottlogic.deg.io.FileReader
import com.scottlogic.deg.profiler.Profiler
import org.apache.spark.sql.SparkSession
import org.junit.{Before, Test}

import org.junit.Assert._


@Test
class SemanticInferenceTest {
  var spark : SparkSession = _ // var to get spark session before each
  var fileReader : FileReader = _

  @Before
  def before(): Unit = {
    spark = SparkSession.builder
      .appName("Data Engineering Generator")
      .config("spark.master", "local")
      .getOrCreate()

    fileReader = new FileReader(spark)
  }

  @Test
  def inferTypesTest() {
    val path = getClass.getClassLoader.getResource("semantic.csv").getPath
    val df = fileReader.readCSV(new File(path))
    val profiler = new Profiler(df)
    val profile = profiler.profile()
    assertEquals(profile.fields.size, 6)
  }
}
