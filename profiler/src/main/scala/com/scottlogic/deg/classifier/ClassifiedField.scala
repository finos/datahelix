package com.scottlogic.deg.classifier

case class ClassifiedField(name: String, typeDetectionCount: collection.Map[SemanticType, Int])
