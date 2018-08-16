package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{Classifier, FloatType, SemanticType}

object FloatClassifier extends Classifier {
  private val containsCharacterRegex = "[a-zA-Z]+"
  private val containsNumbersWithSeparator = "^[-]?\\d*[\\.,]\\d+$"

  override def classify(input: String): Set[SemanticType] = {
    if (input.matches(containsCharacterRegex)) {
      Set()
    } else if (input.matches(containsNumbersWithSeparator)) {
      //TODO: Improve logic ( thousand separator, etc )
      Set(FloatType)
    } else {
      Set()
    }
  }
}
