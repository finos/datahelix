package com.scottlogic.deg.analyser.field_analyser.string_analyser

import java.util.Arrays
import com.scottlogic.deg.models._
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructField

import scala.collection.mutable.ListBuffer

class EmailAnalyser(val df: DataFrame, val field: StructField) extends StringAnalyser {
  private val spark = SparkSession.builder.getOrCreate()
  
  private val UnameLenMin = "UnameLenMin"
  private val UnameLenMax = "UnameLenMax"
  private val DnameLenMin = "DnameLenMin"
  private val DnameLenMax = "DnameLenMax"

  import spark.implicits._
  
  override def constructField(): Rule = {
    
    val fieldName = field.name;
    val allFieldConstraints = ListBuffer[IConstraint]();
    val emailAnalysis = analyse().first();
    
    val fieldTypeConstraint = new IsOfTypeConstraint(fieldName, "string"); // FIXME use a constant?
    val sb = StringBuilder.newBuilder
    sb.append(s".{${emailAnalysis.getAs(UnameLenMin)},${emailAnalysis.getAs(UnameLenMax)}}")
    sb.append("@")
    sb.append(s".{${emailAnalysis.getAs(DnameLenMin)},${emailAnalysis.getAs(DnameLenMax)}}")
    val regexConstraint = new MatchesRegexConstraint(fieldName, sb.toString)
        
    allFieldConstraints += fieldTypeConstraint
    allFieldConstraints += regexConstraint
    
    return new Rule(s"String field ${fieldName} rule", allFieldConstraints.toList);                 
  }
  
  def analyse(): DataFrame = {
    val columnName = field.name
    val col = df(columnName)
    
    if (col==null) {
      throw new NullPointerException("No such column: " + columnName)
    }
    
    val UserName = "UserName"
    val DomainName = "DomainName"
    
    val dfSplit = df.select(columnName)
                    .withColumn(UserName, split(col, "@").getItem(0))
                    .withColumn(DomainName, split(col, "@").getItem(1))
    
    val colUsername = dfSplit(UserName)
    val colDomain   = dfSplit(DomainName)
    val dfStringAnalysis = dfSplit
                             .agg(min(length(colUsername)).as(UnameLenMin),
                                  max(length(colUsername)).as(UnameLenMax),
                                  min(length(colDomain)).as(DnameLenMin),
                                  max(length(colDomain)).as(DnameLenMax)
                                 )
    return dfStringAnalysis
  }
}