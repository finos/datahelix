package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{Classifier, IntegerType, SemanticType}

object IntegerClassifier extends Classifier {
  private val integerRegex : String = "^[-]?\\d+$"

  override def classify(input: String): Set[SemanticType] = {
    if (input.matches(integerRegex)) {
      Set(IntegerType)
    } else {
      Set()
    }
  }
}
