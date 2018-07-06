package com.scottlogic.deg

import com.google.inject.{AbstractModule, Provides}
import net.codingwell.scalaguice.ScalaModule
import org.apache.spark.sql.SparkSession

class LiveModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
  }

  @Provides
  def provideSparkSession():SparkSession = SparkSession.builder
    .appName("Data Engineering Generator")
    .config("spark.master", "local")
    .getOrCreate()
}
