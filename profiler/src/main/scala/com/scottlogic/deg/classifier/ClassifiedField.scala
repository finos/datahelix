package com.scottlogic.deg.classifier

case class ClassifiedField(fieldName: String, typeDetectionCount: collection.Map[SemanticType,Int])
