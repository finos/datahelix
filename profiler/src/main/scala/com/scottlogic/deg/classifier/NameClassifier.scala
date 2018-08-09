package com.scottlogic.deg.classifier

object NameClassifier extends Classifier {
  override def classify(input: String): Seq[SemanticType] = {
    // TODO: Classifier logic
    return Seq[SemanticType](NameType)
  }
}