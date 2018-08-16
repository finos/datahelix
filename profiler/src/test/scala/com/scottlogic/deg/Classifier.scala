package com.scottlogic.deg

import com.scottlogic.deg.classifier.{IntegerType, MainClassifier, NullType, StringType}
import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api._

@Test
class Classifier {
  @Test
  def classifyNullTest(): Unit = {
    val types = MainClassifier.classify(null)
    assertEquals(Set(NullType), types)
  }

  @Test
  def classifyStringsTest(): Unit = {
    val values = List("", "foo", "bar", "Hello, World!", "\n\n\ttext_goes_here\t\t")

    values.foreach(value => {
      val types = MainClassifier.classify(value)
      assertTrue(types.contains(StringType))
    })
  }

  @Test
  def classifyIntegersTest(): Unit = {
    val values = List("0", "1", "2", "10", "9268342", "-0", "-8762", "42")

    values.foreach(value => {
      val types = MainClassifier.classify(value)
      assertTrue(types.contains(StringType))
      assertTrue(types.contains(IntegerType))
    })
  }
}
