package com.scottlogic.deg.dto

case class UnknownField(
                         name:String,
                         nullPrevalence:Number
                       ) extends AbstractField