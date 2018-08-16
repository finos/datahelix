package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{Classifier, RICType, SemanticType}

object RICClassifier extends Classifier {
  private val ricRegex = "^[A-Z]{1,4}\\.[A-Z]{1,2}$"

  override def classify(input: String): Set[SemanticType] = {
    // TODO: Classifier logic
    if (input.toUpperCase.matches(ricRegex)) {
      Set(RICType)
    } else {
      Set()
    }
  }
}
