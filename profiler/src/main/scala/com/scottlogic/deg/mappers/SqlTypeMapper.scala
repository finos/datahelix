package com.scottlogic.deg.mappers

import com.scottlogic.deg.classifier._
import org.apache.spark.sql.types.{DataType, DateType, DoubleType => SQLDoubleType, FloatType => SQLFloatType, IntegerType => SQLIntegerType, StringType => SQLStringType}

object SqlTypeMapper extends IMapper[SemanticType, DataType] {
  override def Map(original: SemanticType): DataType = {
    original match {
      case StringType => SQLStringType;
      case EmailType => SQLStringType;
      case NameType => SQLStringType;
      case CountryCodeType => SQLStringType;
      case CurrencyType => SQLStringType;
      case IntegerType => SQLIntegerType;
      case DoubleType => SQLDoubleType;
      case FloatType => SQLFloatType;
      case TimeStampType => DateType;
      case RICType => SQLStringType;
      case SEDOLType => SQLStringType;
      case ISINType => SQLStringType;
      case EnumType => SQLStringType;
    }
  }
}