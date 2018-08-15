package com.scottlogic.deg.classifier

sealed trait SemanticType
case object StringType extends SemanticType
case object EmailType extends SemanticType
case object NameType extends SemanticType
case object CountryCodeType extends SemanticType
case object CurrencyType extends SemanticType
case object IntegerType extends SemanticType
case object DoubleType extends SemanticType
case object FloatType extends SemanticType
case object TimeStampType extends SemanticType
case object RICType extends SemanticType
case object SEDOLType extends SemanticType
case object ISINType extends SemanticType
case object EnumType extends SemanticType
case object NullType extends SemanticType