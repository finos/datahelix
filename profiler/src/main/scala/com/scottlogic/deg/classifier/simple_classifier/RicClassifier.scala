package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{RicType, SemanticType}

object RicClassifier extends Classifier {
  private val ricRegex = "^[A-Z]{1,4}\\.[A-Z]{1,2}$"
  override val semanticType: SemanticType = RicType
  override def matches(input: String): Boolean = input.toUpperCase.matches(ricRegex)
}
