package com.scottlogic.deg.classifier

trait Classifier {
  val semanticType: SemanticType
  def matches(input: String): Boolean

  def classify(input: String): Option[SemanticType] = {
    if (matches(input)) Some(semanticType) else None
  }
}
