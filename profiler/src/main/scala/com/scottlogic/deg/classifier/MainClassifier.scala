package com.scottlogic.deg.classifier

import com.scottlogic.deg.classifier.simple_classifier._
import org.apache.spark.rdd.RDD

import scala.collection.mutable.ListBuffer

object MainClassifier extends Classifier {
  private val classifiers : List[Classifier] = List[Classifier](CountryCodeClassifier, CurrencyClassifier,EmailClassifier, FloatClassifier, IntegerClassifier, NameClassifier, RICClassifier, ISINClassifier, SEDOLClassifier, StringClassifier, TimeStampClassifier)

  override def classify(input: String): Seq[SemanticType] = {
    if(input == null || input == ""){
      return List[SemanticType]()
    }
    val results : ListBuffer[SemanticType] = ListBuffer[SemanticType]()
    classifiers.foreach(classifier => results.appendAll(classifier.classify(input)))
    return results.toList
  }

  // TODO: Move this somewhere else, it is useful only for Enums
  def classifyMany(input: RDD[String]) : Seq[SemanticType] = {
    val minimumValuesToEvaluate = 20
    if(input.count() < minimumValuesToEvaluate){
      return List[SemanticType]()
    }
    val groupedValues = input.groupBy(identity).mapValues(_.size).collectAsMap()
    val enumMaxSize = 10
    val enumMinSize = 1
    if(groupedValues.keys.size < enumMaxSize && groupedValues.keys.size > enumMinSize){
      return List[SemanticType](EnumType)
    }
    return List[SemanticType]()
  }
}