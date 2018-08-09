package com.scottlogic.deg.classifier

object SEDOLClassifier extends Classifier {
  override def classify(input: String): Seq[SemanticType] = {
    // TODO: Classifier logic
    return Seq[SemanticType](SEDOLType)
  }
}
