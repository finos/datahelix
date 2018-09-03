package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{IntegerType, SemanticType}

object IntegerClassifier extends Classifier {
  private val integerRegex : String = "^[-]?\\d+$"
  override val semanticType: SemanticType = IntegerType
  override def matches(input: String): Boolean = input.matches(integerRegex)
}
