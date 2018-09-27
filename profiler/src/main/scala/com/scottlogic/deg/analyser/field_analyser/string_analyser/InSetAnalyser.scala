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

    val members: List[Object] = analyse()

    val fieldName = field.name;

    val allFieldConstraints: List[IConstraint] = List(
          new IsOfTypeConstraint(fieldName, "string"),
          new InSetConstraint(fieldName, members)
        )

    return new Rule(s"String field ${fieldName} rule", allFieldConstraints.toList);
  }

  def analyse(): List[Object] = {
    val columnName = field.name
    val col = df(columnName)

    if (col == null) {
      throw new NullPointerException("Column does not exist")
    }

    val members = df.select(columnName)
                        .map(r => r.getString(0))
                        .distinct
                        .collect()
                        .toList
      
    members
  }
}