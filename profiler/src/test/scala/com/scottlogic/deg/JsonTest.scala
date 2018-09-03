package com.scottlogic.deg

import com.fasterxml.jackson.databind.ObjectMapper
import javax.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api._

case class Person(name:String, age:Int)

@Test
@RequiresInjection
class JsonTest {

  @Inject
  var mapper:ObjectMapper = _

  @Test
  def givenPerson_whenWriteValueAsString_thenJsonIsReturned(): Unit = {
    // Arrange
    val person = Person("Fred", 65)
    val expected = "{\"name\":\"Fred\",\"age\":65}"

    // Act
    val marshalled:String = mapper.writeValueAsString(person)

    // Assert
    assertEquals(expected, marshalled)
  }
}