package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{Classifier, SemanticType}

object RICClassifier extends Classifier {
  override def classify(input: String): Seq[SemanticType] = {
    // TODO: Classifier logic
    if(input == null || input == ""){
      return List[SemanticType]()
    }
    return Seq[SemanticType]()
  }
}
