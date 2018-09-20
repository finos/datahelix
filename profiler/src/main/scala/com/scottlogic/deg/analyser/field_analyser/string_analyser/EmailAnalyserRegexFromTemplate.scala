package com.scottlogic.deg.analyser.field_analyser.string_analyser

import java.util.Arrays
import com.scottlogic.deg.models._
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructField

import scala.collection.mutable.ListBuffer

/*
 * This analyser generates a regex constraint that is hard-coded to a template (based on
 * the suggestion of @SL-Mark)
 */
class EmailAnalyserRegexFromTemplate(val df: DataFrame, val field: StructField) extends EmailAnalyser {
  private val spark = SparkSession.builder.getOrCreate()
  
  import spark.implicits._
  
  override def constructField(): Rule = {
    
    val fieldName = field.name;
    val allFieldConstraints = ListBuffer[IConstraint]();
    val RegexTemplate = "(tim|frank|greg)\\.(smith|jones|sestero)@(gmail|hotmail|live)\\.com";
    
    val fieldTypeConstraint = new IsOfTypeConstraint(fieldName, "string"); // FIXME use a constant?
    val regexConstraint = new MatchesRegexConstraint(fieldName, RegexTemplate)
        
    allFieldConstraints += fieldTypeConstraint
    allFieldConstraints += regexConstraint
    
    return new Rule(s"String field ${fieldName} rule", allFieldConstraints.toList);                 
  }  
}