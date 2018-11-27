package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{LongType, SemanticType}

object LongClassifier extends Classifier {
  private val integerRegex : String = "^[-]?\\d+$"
  override val semanticType: SemanticType = LongType
  override def matches(input: String): Boolean = input.matches(integerRegex)
}
