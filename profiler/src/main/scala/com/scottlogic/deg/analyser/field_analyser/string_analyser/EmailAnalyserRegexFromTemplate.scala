package com.scottlogic.deg.analyser.field_analyser.string_analyser

import java.util.Arrays
import com.scottlogic.deg.models._
import org.apache.spark.sql.types.StructField

import scala.collection.mutable.ListBuffer

/*
 * This analyser generates a regex constraint that is hard-coded to a template (based on
 * the suggestion of @SL-Mark)
 */
class EmailAnalyserRegexFromTemplate(val field: StructField) extends EmailAnalyser {
  override def constructField(): Rule = {
    
    val RegexTemplate = "(tim|frank|greg)\\.(smith|jones|sestero)@(gmail|hotmail|live)\\.com";
    val fieldName = field.name;
    
    val allFieldConstraints: List[IConstraint] = List(
            new IsOfTypeConstraint(fieldName, "string"),         // FIXME use a constant?
            new MatchesRegexConstraint(fieldName, RegexTemplate)
        )
        
    new Rule(s"String field ${fieldName} rule", allFieldConstraints.toList);                 
  }  
}