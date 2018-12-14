package com.scottlogic.deg.analyser.field_analyser.string_analyser

import com.scottlogic.deg.models._
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructField

import scala.collection.mutable.ListBuffer

class InSetAnalyser(val df: DataFrame, val field: StructField) extends StringAnalyser {
  private val spark = SparkSession.builder.getOrCreate()

  import spark.implicits._

  override def constructField(): Rule = {

    val fieldName = field.name;

    val members: List[Object] = df.select(field.name)
                                  .map(r => r.getString(0))
                                  .distinct
                                  .collect()
                                  .toList


    val allFieldConstraints: List[Constraint] = List(
          new IsOfTypeConstraint(fieldName, "string"),
          new InSetConstraint(fieldName, members)
        )

    return new Rule(s"String field ${fieldName} rule", allFieldConstraints.toList);
  }
}