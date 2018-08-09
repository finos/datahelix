package com.scottlogic.deg.classifier

import org.apache.spark.sql.DataFrame

class DataFrameClassifier(df: DataFrame) {

  def getAnalysis(): Seq[ClassifiedField] = {
    df.schema.fields.map(field => {
      val typeList = df.rdd.flatMap(row => {
        val fieldValue = row.getAs[String](field.name)
        val fieldValueCleansed = if (fieldValue == null) "" else fieldValue
        MainClassifier.classify(fieldValueCleansed)
      }).groupBy(identity)
        .mapValues(_.size)
        .collectAsMap()

      ClassifiedField(field.name, typeList)
    })
  }
}
