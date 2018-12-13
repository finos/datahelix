package com.scottlogic.deg.mappers

import com.scottlogic.deg.classifier._
import org.apache.spark.sql.types.{DataType, TimestampType => SQLTimeStampType, DoubleType => SQLDoubleType, FloatType => SQLFloatType, LongType => SQLLongType, StringType => SQLStringType}

object SqlTypeMapper extends Mapper[SemanticType, DataType] {
  override def Map(original: SemanticType): DataType = {
    original match {
      case StringType => SQLStringType;
      case EmailType => SQLStringType;
      case NameType => SQLStringType;
      case CountryCodeType => SQLStringType;
      case CurrencyType => SQLStringType;
      case LongType => SQLLongType;
      case DoubleType => SQLDoubleType;
      case FloatType => SQLFloatType;
      case TimeStampType => SQLTimeStampType;
      case RicType => SQLStringType;
      case SedolType => SQLStringType;
      case IsinType => SQLStringType;
      case EnumType => SQLStringType;
      case NullType => SQLStringType;
    }
  }
}
