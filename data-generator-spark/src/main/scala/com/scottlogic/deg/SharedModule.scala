package com.scottlogic.deg

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.google.inject.{AbstractModule, Provides}
import net.codingwell.scalaguice.ScalaModule

class SharedModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
  }

  @Provides
  def provideObjectMapper():ObjectMapper = {
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
  }
}
