package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{Classifier, SemanticType, StringType}

object StringClassifier extends Classifier {
  override def classify(input: String): Seq[SemanticType] = {
    // Fields are always strings when null or empty
    if(input == null || input == ""){
      return List[SemanticType](StringType)
    }

    // TODO: Classifier logic

    return Seq[SemanticType](StringType)
  }
}
