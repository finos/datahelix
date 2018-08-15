package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{Classifier, SEDOLType, SemanticType}

object SEDOLClassifier extends Classifier {
  private val sedolRegex = "[B-Db-dF-Hf-hJ-Nj-nP-Tp-tV-Xv-xYyZz\\d]{6}\\d";

  override def classify(input: String): Seq[SemanticType] = {
    if(input.length != 7){
      return List[SemanticType]()
    }

    if(input.matches(sedolRegex)){
      return List[SemanticType](SEDOLType)
    }

    return Seq[SemanticType]()
  }
}
