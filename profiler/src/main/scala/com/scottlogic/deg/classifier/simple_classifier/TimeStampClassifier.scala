package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{Classifier, SemanticType, TimeStampType}

object TimeStampClassifier extends Classifier {
  private val dateFormats : List[String] = List[String]("^\\d{4}[-/]\\d{2}[-/]\\d{2}.*$", "^\\d{2}[-/]\\d{2}[-/]\\d{4}.*$")

  override def classify(input: String): Seq[SemanticType] = {
    for (dateFormat <- dateFormats){
      if(input.matches(dateFormat)){
        return Seq[SemanticType](TimeStampType)
      }
    }

    Seq[SemanticType]()
  }
}
