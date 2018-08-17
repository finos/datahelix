package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{SemanticType, StringType}

object StringClassifier extends Classifier {
  override val semanticType: SemanticType = StringType
  override def matches(input: String): Boolean = true
}
