package com.scottlogic.deg.dto

case class StringField(
  name:String,
  nullPrevalence:Number,
  specialization: StringFieldAbstractSpecialization
) extends AbstractField
