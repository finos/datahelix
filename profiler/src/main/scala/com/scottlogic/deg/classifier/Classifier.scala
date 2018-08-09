package com.scottlogic.deg.classifier

trait Classifier {
  def classify ( input : String ) : Seq[SemanticType]
}