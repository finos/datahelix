package com.scottlogic.deg.analyser.field_analyser.string_analyser

import java.util.Arrays
import com.scottlogic.deg.models._
import org.apache.spark.sql.types.StructField

import scala.collection.mutable.ListBuffer

class IsinAnalyser(val field: StructField) extends StringAnalyser {
  override def constructField(): Rule = {
    
    val fieldName = field.name;
    val allFieldConstraints = ListBuffer[IConstraint]();
    
    val fieldTypeConstraint = new IsOfTypeConstraint(fieldName, "string"); // FIXME use a constant?
    val aValidConstraint = new IsAValidConstraint(fieldName, "ISIN")       // FIXME use a constant?
        
    allFieldConstraints += fieldTypeConstraint
    allFieldConstraints += aValidConstraint
    
    return new Rule(s"String field ${fieldName} rule", allFieldConstraints.toList);                 
  }  
}