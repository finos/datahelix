package com.scottlogic.deg.classifier

sealed trait SemanticType {def rank: Int}
case object NullType extends SemanticType {val rank = 0}
case object StringType extends SemanticType {val rank = 1}
case object IntegerType extends SemanticType {val rank = 2}
case object DoubleType extends SemanticType {val rank = 3}
case object FloatType extends SemanticType {val rank = 4}
case object EmailType extends SemanticType {val rank = 5}
case object NameType extends SemanticType {val rank = 6}
case object CountryCodeType extends SemanticType {val rank = 7}
case object CurrencyType extends SemanticType {val rank = 8}
case object TimeStampType extends SemanticType {val rank = 9}
case object RICType extends SemanticType {val rank = 10}
case object SEDOLType extends SemanticType {val rank = 11}
case object ISINType extends SemanticType {val rank = 12}
case object EnumType extends SemanticType {val rank = 13}
