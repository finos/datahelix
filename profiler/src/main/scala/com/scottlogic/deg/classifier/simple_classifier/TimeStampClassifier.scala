package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{Classifier, SemanticType, TimeStampType}

object TimeStampClassifier extends Classifier {
  private val dateFormats : List[String] = List[String]("^\\d{4}[-/]\\d{2}[-/]\\d{2}.*$", "^\\d{2}[-/]\\d{2}[-/]\\d{4}.*$")
  override val semanticType: SemanticType = TimeStampType

  override def matches(input: String): Boolean = {
    for (dateFormat <- dateFormats){
      if(input.matches(dateFormat)){
        return true
      }
    }

    false
  }
}
