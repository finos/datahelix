package com.scottlogic.deg

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.junit.{Before, Test}


//case class Field(name:String, nullPrevalence:Int, typedPayload:)
case class Person(name:String, age:Int)

@Test
class JsonTest {

  var mapper:ObjectMapper = _

  @Before
  def setup(): Unit = {
    // https://github.com/FasterXML/jackson-module-scala
    mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
  }

  @Test
  def marshal(): Unit = {
    val person = Person("Fred", 65)
    val marshalled:String = mapper.writeValueAsString(person)
    System.out.println(marshalled)
  }
}
