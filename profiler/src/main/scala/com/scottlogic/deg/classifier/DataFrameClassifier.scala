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

  def detectSchema(analysis: Seq[ClassifiedField]): StructType = {
    StructType(
      analysis.map(f => {
        val mostPreciseType = f.typeDetectionCount.toArray.maxBy(t => t._1.rank)._1
        val nullable = f.typeDetectionCount.keySet.contains(NullType)
        StructField(f.name, SqlTypeMapper.Map(mostPreciseType), nullable)
      })
    )
  }

}
