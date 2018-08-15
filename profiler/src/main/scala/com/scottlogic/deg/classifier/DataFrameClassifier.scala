package com.scottlogic.deg.classifier

import com.scottlogic.deg.mappers.SqlTypeMapper
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.{StructField, StructType}

class DataFrameClassifier(df: DataFrame) {

  def getAnalysis(): Seq[ClassifiedField] = {
    df.schema.fields.map(field => {
      var typeList = df.rdd.flatMap(row => {
        val fieldValue = row.getAs[String](field.name)
        MainClassifier.classify(fieldValue)
      }).groupBy(identity)
        .mapValues(_.size)
        .collectAsMap()

      // TODO: Check if there is a better way to trigger Enum identification
      if(typeList.keys.size == 1 && (typeList.contains(StringType) || typeList.contains(IntegerType))){
        val multiValueTypes = MainClassifier.classifyMany(df.rdd.map(row => row.getAs[String](field.name)))
        multiValueTypes.foreach(semanticType => {
          typeList = typeList + (semanticType -> df.rdd.count().toInt)
        });
      }

      ClassifiedField(field.name, typeList)
    })
  }

  def generateNewSchema(analysis : Seq[ClassifiedField]) : StructType = {
    val fields = analysis.map(f => {
      // TODO: Logic for deciding which of all the types is best
      val detectionCount = f.typeDetectionCount
      val detectedTypes = detectionCount.keys.toList
      val isNull = detectedTypes.count(e => e == NullType) > 0
      if(isNull){
        detectionCount-NullType
      }
      StructField(f.fieldName, SqlTypeMapper.Map(detectionCount.last._1), true); //TODO: Replace true with "isNull"
    })
    StructType(fields)
  }

}
