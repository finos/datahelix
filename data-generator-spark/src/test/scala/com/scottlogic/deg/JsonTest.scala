package com.scottlogic.deg

import com.fasterxml.jackson.databind.ObjectMapper
import javax.inject.Inject
import org.junit.jupiter.api._

case class Person(name:String, age:Int)

@Test
@RequiresInjection
class JsonTest {

  @Inject
  var mapper:ObjectMapper = _

  @Test
  def marshal(): Unit = {
    val person = Person("Fred", 65)
    val marshalled:String = mapper.writeValueAsString(person)
    System.out.println(marshalled)
  }
}
