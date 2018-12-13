package com.scottlogic.deg

import java.io.File

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.google.inject.Guice
import org.junit.jupiter.api.Test

@Test
class IntegrationTest {

  private val actualOutputDirectory = getClass.getResource("").getPath+"/actual_output"
  private val expectedOutputDirectory = getClass.getResource("/expected_output/").getPath

  @Test
  def givenEmailSampleData_whenProfileGenerated_matchExpectedProfile(): Unit = {

    val sampleDataFileName = "email.csv"
    val profileFileName = "email.json"

    assertProfileMatch(sampleDataFileName, profileFileName)
  }

  @Test
  def givenISINSampleData_whenProfileGenerated_matchExpectedProfile(): Unit = {

    val sampleDataFileName = "isin_example.csv"
    val profileFileName = "isin_example.json"

    assertProfileMatch(sampleDataFileName, profileFileName)
  }

  @Test
  def givenBasicClassifierSampleData_whenProfileGenerated_matchExpectedProfile(): Unit = {

    val sampleDataFileName = "basic_classifier.csv"
    val profileFileName = "basic_classifier.json"

    assertProfileMatch(sampleDataFileName, profileFileName)
  }

  @Test
  def givenGfxCleanedSampleData_whenProfileGenerated_matchExpectedProfile(): Unit = {

    val sampleDataFileName = "gfx_cleaned.csv"
    val profileFileName = "gfx_cleaned.json"

    assertProfileMatch(sampleDataFileName, profileFileName)
  }


  private def assertProfileMatch(sampleDataFileName: String, profileFileName: String) = {
    // Arrange
    val expected = getProfile(s"$expectedOutputDirectory/$profileFileName")

    // Act
    generateProfile(sampleDataFileName)
    val actual = getProfile(s"$actualOutputDirectory/$profileFileName")

    // Assert
    assert(expected == actual)
  }


  private def getProfile(path: String) : JsonNode = {
    val mapper = new ObjectMapper()
    mapper.readTree(new File(path))
  }

  private def generateProfile(dataFileName: String) : Unit = {
    val inputPath = getClass.getResource(s"/$dataFileName").getPath
    val injector = Guice.createInjector(new TestModule, new SharedModule)
    injector.getInstance(classOf[DEGApp]).run(Params(inputPath = inputPath, outputDir = actualOutputDirectory))

  }
}
