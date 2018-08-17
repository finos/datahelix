package com.scottlogic.deg.classifier

import com.scottlogic.deg.classifier.simple_classifier._
import org.apache.spark.rdd.RDD

object Classifiers {
  val classifiers: List[Classifier] = List(
    CountryCodeClassifier, CurrencyClassifier,EmailClassifier, FloatClassifier, IntegerClassifier, NameClassifier,
    RICClassifier, ISINClassifier, SEDOLClassifier, StringClassifier, TimeStampClassifier
  )

  def classify(input: String): Set[SemanticType] = {
    if (input == null) {
      Set(NullType)
    } else {
      classifiers.flatMap(_.classify(input)).toSet
    }
  }

  // TODO: Move this somewhere else, it is useful only for Enums
  def classifyMany(input: RDD[String]) : Set[SemanticType] = {
    val minimumValuesToEvaluate = 20
    if(input.count() < minimumValuesToEvaluate){
      return Set()
    }
    val groupedValues = input.groupBy(identity).mapValues(_.size).collectAsMap()
    val enumMaxSize = 10
    val enumMinSize = 1
    if(groupedValues.keys.size < enumMaxSize && groupedValues.keys.size > enumMinSize){
      return Set(EnumType)
    }
    return Set()
  }
}
