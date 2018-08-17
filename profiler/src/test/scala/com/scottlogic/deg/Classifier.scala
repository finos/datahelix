package com.scottlogic.deg

import com.scottlogic.deg.classifier.simple_classifier.{CountryCodeClassifier, IntegerClassifier, StringClassifier}
import com.scottlogic.deg.classifier._
import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api._
import org.junit.runners.Parameterized.Parameters

@Test
class Classifier {
  @Test
  def givenNullValue_whenClassify_thenNullTypeIsReturned(): Unit = {
    // Act
    val types = MainClassifier.classify(null)
    // Assert
    assertEquals(1, types.size)
    assertEquals(Set(NullType), types)
  }

  @Test
  def givenValidStringValue_whenClassify_thenStringTypeIsReturned(): Unit = {
    // Arrange
    val values = List("", "foo", "bar", "Hello, World!", "\n\n\ttext_goes_here\t\t", "1234", "25/02/2018")

    values.foreach(value => {
      // Act
      val types = StringClassifier.classify(value)
      // Assert
      assertEquals(1, types.size)
      assertTrue(types.contains(StringType))
    })
  }

  @Test
  def givenValidIntegerValue_whenClassify_thenIntegerTypeIsReturned(): Unit = {
    // Arrange
    val values = List("0", "1", "2", "10", "9268342", "-0", "-8762", "42")

    values.foreach(value => {
      // Act
      val types = IntegerClassifier.classify(value)
      // Assert
      assertEquals(1, types.size)
      assertTrue(types.contains(IntegerType))
    })
  }

  @Test
  def givenInvalidIntegerValue_whenClassify_thenIntegerTypeIsNotReturned(): Unit = {
    // Arrange
    val values = List("aaa", "1.2568", "-12.2", "+", "-")

    values.foreach(value => {
      // Act
      val types = IntegerClassifier.classify(value)
      // Assert
      assertEquals(0, types.size)
    })
  }

  @Test
  def givenValidCountryCode_whenClassify_thenCountryCodeTypeIsReturned(): Unit = {
    // Arrange
    val values = List("AF","AX","AL","DZ","AS","AD","AO","AI","AQ","AG","AR","AM","AW","AU","AT","AZ","BS","BH","BD","BB","BY","BE","BZ","BJ","BM","BT","BO","BA","BW","BV","BR","VG","IO","BN","BG","BF","BI","KH","CM","CA","CV","KY","CF","TD","CL","CN","HK","MO","CX","CC","CO","KM","CG","CD","CK","CR","CI","HR","CU","CY","CZ","DK","DJ","DM","DO","EC","EG","SV","GQ","ER","EE","ET","FK","FO","FJ","FI","FR","GF","PF","TF","GA","GM","GE","DE","GH","GI","GR","GL","GD","GP","GU","GT","GG","GN","GW","GY","HT","HM","VA","HN","HU","IS","IN","ID","IR","IQ","IE","IM","IL","IT","JM","JP","JE","JO","KZ","KE","KI","KP","KR","KW","KG","LA","LV","LB","LS","LR","LY","LI","LT","LU","MK","MG","MW","MY","MV","ML","MT","MH","MQ","MR","MU","YT","MX","FM","MD","MC","MN","ME","MS","MA","MZ","MM","NA","NR","NP","NL","AN","NC","NZ","NI","NE","NG","NU","NF","MP","NO","OM","PK","PW","PS","PA","PG","PY","PE","PH","PN","PL","PT","PR","QA","RE","RO","RU","RW","BL","SH","KN","LC","MF","PM","VC","WS","SM","ST","SA","SN","RS","SC","SL","SG","SK","SI","SB","SO","ZA","GS","SS","ES","LK","SD","SR","SJ","SZ","SE","CH","SY","TW","TJ","TZ","TH","TL","TG","TK","TO","TT","TN","TR","TM","TC","TV","UG","UA","AE","GB","US","UM","UY","UZ","VU","VE","VN","VI","WF","EH","YE","ZM","ZW")

    values.foreach(value => {
      // Act
      val types = CountryCodeClassifier.classify(value)
      // Assert
      assertEquals(1, types.size)
      assertTrue(types.contains(CountryCodeType))
    })
  }

  @Test
  def givenInvalidCountryCode_whenClassify_thenCountryCodeIsNotReturned(): Unit = {
    // Arrange
    val values = List("1", "Australia", "22/02/2018")

    values.foreach(value => {
      // Act
      val types = CountryCodeClassifier.classify(value)
      // Assert
      assertEquals(0, types.size)
    })
  }

}
