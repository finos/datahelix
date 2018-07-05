package com.scottlogic.deg.dto

case class SetDistribution (
  members: Iterable[SetDistributionMember]
) extends AbstractDistribution