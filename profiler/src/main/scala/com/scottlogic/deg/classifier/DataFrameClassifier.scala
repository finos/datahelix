package com.scottlogic.deg.classifier

import com.scottlogic.deg.mappers.SqlTypeMapper
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.{StructField, StructType}

object DataFrameClassifier {

  def analyse(df: DataFrame): Seq[ClassifiedField] = {
    df.schema.fields.map(field => {
      val fieldValues = df.rdd.map(_.getAs[String](field.name)).collect()
      val extraTypes: Set[SemanticType] = if (suggestEnum(fieldValues)) Set(EnumType) else Set()

      val types = df.rdd.flatMap(row => {
        val fieldValue = row.getAs[String](field.name)
        Classifiers.classify(fieldValue).union(extraTypes)
      }).groupBy(identity)
        .mapValues(_.size)
        .collectAsMap()

      ClassifiedField(field.name, types)
    })
  }

  /*
   * Suggest that a field is an enumerated type if there are less than the threshold ratio of unique values
   */
  def suggestEnum(field: Array[String]): Boolean = {
    val ratioThreshold = 0.02
    (field.distinct.length.toDouble / field.length.toDouble) <= ratioThreshold
  }

  def generateNewSchema(analysis : Seq[ClassifiedField]) : StructType = {
    val fields = analysis.map(f => {
      val detectionCount = f.typeDetectionCount
      val detectedTypes = detectionCount.keys.toList
      val isNull = detectedTypes.count(e => e == NullType) >= 0
      val totalCount = detectionCount.toArray.maxBy(t => t._2)._2
      val sortedDetectionCount = f.typeDetectionCount.toArray.sortBy(t => t._1.rank)
      StructField(f.name, SqlTypeMapper.Map(sortedDetectionCount.last._1), isNull)
    })
    StructType(fields)
  }

}
