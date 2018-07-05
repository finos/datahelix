package com.scottlogic.deg.dto

case class Profile(
  schemaVersion: Integer,
  fields: Iterable[AbstractField]
)