package com.scottlogic.deg

import com.google.inject.{AbstractModule, Provides}
import net.codingwell.scalaguice.ScalaModule
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

class TestModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
  }

  @Provides
  def provideSparkSession():SparkSession = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)
    SparkSession.builder
      .appName("Data Engineering Generator")
      .config("spark.master", "local")
      .getOrCreate()
  }
}
