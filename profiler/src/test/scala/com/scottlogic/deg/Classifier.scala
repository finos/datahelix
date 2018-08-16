package com.scottlogic.deg

import com.scottlogic.deg.classifier.{MainClassifier, NullType}
import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api._

@Test
class Classifier {
  @Test
  def classifyNullTest(): Unit = {
    val types = MainClassifier.classify(null)
    assertEquals(1, types.size)
    assertTrue(types.contains(NullType))
  }
}
