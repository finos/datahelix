package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.SemanticType

trait Classifier {
  val semanticType: SemanticType
  def matches(input: String): Boolean

  def classify(input: String): Option[SemanticType] = {
    if (matches(input)) Some(semanticType) else None
  }
}
