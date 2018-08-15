package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{Classifier, SemanticType}

object NameClassifier extends Classifier {
  override def classify(input: String): Seq[SemanticType] = {
    // TODO: Classifier logic, we would need a name dictionary
    return Seq[SemanticType]()
  }
}
