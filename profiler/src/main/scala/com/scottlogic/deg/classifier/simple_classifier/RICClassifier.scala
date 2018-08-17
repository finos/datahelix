package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{Classifier, RICType, SemanticType}

object RICClassifier extends Classifier {
  private val ricRegex = "^[A-Z]{1,4}\\.[A-Z]{1,2}$"
  override val semanticType: SemanticType = RICType
  override def matches(input: String): Boolean = input.toUpperCase.matches(ricRegex)
}
