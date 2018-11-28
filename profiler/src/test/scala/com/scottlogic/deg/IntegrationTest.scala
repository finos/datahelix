package com.scottlogic.deg

import java.io.File
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Guice
import org.junit.jupiter.api.Test

@Test
class IntegrationTest {

  @Test
  def givenEmailSampleData_whenProfileGenerated_matchExpectedProfile(): Unit = {

    val mapper = new ObjectMapper()

    // Arrange
    val expected = mapper.readTree(new File(getClass.getResource("/expected_output/email.json").getPath))

    // Act
    val inputPath = getClass.getResource("/email.csv").getPath;
    val outputDir = getClass.getResource("/actual_output").getPath;
    val injector = Guice.createInjector(new TestModule, new SharedModule)
    injector.getInstance(classOf[DEGApp]).run(Params(inputPath = inputPath, outputDir = outputDir))

    val actual = mapper.readTree(new File(getClass.getResource("/actual_output/email.json").getPath))

    // Assert
    assert(expected == actual)
  }
}
