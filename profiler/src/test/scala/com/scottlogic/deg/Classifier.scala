package com.scottlogic.deg

import com.scottlogic.deg.classifier.simple_classifier._
import com.scottlogic.deg.classifier._
import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api._

@Test
class Classifier {
  @Test
  def givenNullValue_whenClassify_thenNullTypeIsReturned(): Unit = {
    // Act
    val types = Classifiers.classify(null)
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

  @Test
  def givenValidIso3CurrencyCode_whenClassify_thenCurrencyCodeIsReturned(): Unit = {
    // Arrange
    val values = List[String]("AFA","ALL","DZD","USD","ESP", "FRF", "ADP","AOA","XCD","XCD","ARS","AMD","AWG","AUD","ATS","AZM","BSD","BHD","BDT","BBD","BYB", "RYR","BEF","BZD","XOF","BMD","INR", "BTN","BOB", "BOV","BAM","BWP","NOK","BRL","USD","BND","BGL", "BGN","XOF","BIF","KHR","XAF","CAD","CVE","KYD","XAF","XAF","CLP", "CLF","CNY","HKD","MOP","AUD","AUD","COP","KMF","XAF","CDF","NZD","CRC","XOF","HRK","CUP","CYP","CZK","DKK","DJF","XCD","DOP","TPE", "IDE","ECS", "ECV","EGP","SVC","XAF","ERN","EEK","ETB","DKK","XEU","EUR","FKP","FJD","FIM","FRF","FRF","XPF","XPF","XAF","GMD","GEL","DEM","GHC","GIP","GRD","DKK","XCD","FRF","USD","GTQ","GNF","GWP", "XOF","GYD","HTG", "USD","AUD","ITL","HNL","HUF","ISK","INR","IDR","XDR","IRR","IQD","IEP","ILS","ITL","JMD","JPY","JOD","KZT","KES","AUD","KPW","KRW","KWD","KGS","LAK","LVL","LBP","ZAR", "LSL","LRD","LYD","CHF","LTL","LUF","MKD","MGF","MWK","MYR","MVR","XOF","MTL","USD","FRF","MRO","MUR","MXN", "MXV","USD","MDL","FRF","MNT","XCD","MAD","MZM","MMK","ZAR", "NAD","AUD","NPR","ANG","NLG","XPF","NZD","NIO","XOF","NGN","NZD","AUD","USD","NOK","OMR","PKR","USD","PAB", "USD","PGK","PYG","PEN","PHP","NZD","PLN","PTE","USD","QAR","FRF","ROL","RUR", "RUB","RWF","XCD","FRF","XCD","XCD","SHP","WST","ITL","STD","SAR","XOF","SCR","SLL","SGD","SKK","SIT","SBD","SOS","ZAR","ESP","LKR","SDP","SRG","NOK","SZL","SEK","CHF","SYP","TWD","TJR","TZS","THB","XOF","NZD","TOP","TTD","TND","TRL","TMM","USD","AUD","UGX","UAH","AED","GBP","USD", "USS", "USN","USD","UYU","UZS","VUV","VEB","VND","USD","USD","XPF","MAD","YER","YUN","ZRN","ZMK","ZWD")

    values.foreach(v => {
      // Act
      val types = CurrencyClassifier.classify(v)
      // Assert
      assertEquals(1, types.size)
      assertTrue(types.contains(CurrencyType))
    })
  }

  @Test
  def givenInvalidIso3CurrencyCode_whenClassify_thenCurrencyCodeIsNotReturned(): Unit = {
    // Arrange
    val values = List("FFF", "1")

    values.foreach(v => {
      // Act
      val types = CurrencyClassifier.classify(v)
      // Assert
      assertEquals(0, types.size)
    })
  }

  @Test
  def givenValidEmail_whenClassify_thenEmailTypeIsReturned(): Unit = {
    // Arrange
    val values = List("email@domain.com",
      "firstname.lastname@domain.com",
      "email@subdomain.domain.com",
      "firstname+lastname@domain.com",
      "email@123.123.123.123",
      "email@[123.123.123.123]",
      "\"email\"@domain.com",
      "1234567890@domain.com",
      "email@domain-one.com",
      "_______@domain.com",
      "email@domain.name",
      "email@domain.co.jp",
      "firstname-lastname@domain.com")

    values.foreach(v => {
      // Act
      val types = EmailClassifier.classify(v)
      // Assert
      assertEquals(1, types.size)
      assertTrue(types.contains(EmailType))
    })
  }

  @Test
  def givenInvalidEmail_whenClassify_thenEmailTypeIsNotReturned(): Unit = {
    // Arrange
    val values = List("plainaddress",
      "#@%^%#$@#$@#.com",
      "@domain.com",
      "Joe Smith <email@domain.com>",
      "email.domain.com",
      "email@domain@domain.com",
      ".email@domain.com",
      "email.@domain.com",
      "email..email@domain.com",
      "あいうえお@domain.com",
      "email@domain",
      "email@-domain.com")

    values.foreach(v => {
      // Act
      val types = EmailClassifier.classify(v)
      // Assert
      assertEquals(0, types.size)
    })
  }

  @Test
  def givenValidFloatValue_whenClassify_thenFloatTypeIsReturned(): Unit = {
    // Arrange
    val values = List("1234", "12.345", "12,345", "01234", "-1234", "-12.345")

    values.foreach(v => {
      // Act
      val types = FloatClassifier.classify(v)
      // Assert
      assertEquals(1, types.size)
      assertTrue(types.contains(FloatType))
    })
  }

  @Test
  def givenInvalidFloatValue_whenClassify_thenFloatTypeIsNotReturned(): Unit = {
    // Arrange
    val values = List("aaa", "45.45.345.5334")

    values.foreach(v => {
      // Act
      val types = FloatClassifier.classify(v)
      // Assert
      assertEquals(0, types.size)
    })
  }

  @Test
  def givenValidISIN_whenClassify_thenISINTypeIsReturned(): Unit = {
    // Arrange
    val values = List("CA1234567891", "CCABCDEFGHI4", "CA12345ABCD4")

    values.foreach(v => {
      // Act
      val types = ISINClassifier.classify(v)
      // Assert
      assertEquals(1,types.size)
      assertTrue(types.contains(ISINType))
    })
  }

  @Test
  def givenInvalidISIN_whenClassify_thenISINTypeIsNotReturned(): Unit = {
    // Arrange
    val values = List("CA12345678", "ZZ1234567891", "CA123456789F")

    values.foreach(v => {
      // Act
      val types = ISINClassifier.classify(v)
      // Assert
      assertEquals(0,types.size)
    })
  }

  @Test
  def ValidName_whenClassify_thenNameTypeReturned(): Unit = {
    //TODO: Write name logic first
    assertTrue(true)
  }

  @Test
  def givenInvalidName_whenClassify_thenNameTypeIsNotReturned(): Unit = {
    //TODO: Write name logic first
    assertTrue(true)
  }

  @Test
  def givenValidRICC_whenClassify_thenRICCTypeIsReturned(): Unit = {
    // Arrange
    val values = List("ABCD.AB", "DEFZ.AZ")

    values.foreach(v => {
      // Act
      val types = RICClassifier.classify(v)
      // Assert
      assertEquals(1, types.size)
      assertTrue(types.contains(RICType))
    })
  }

  @Test
  def givenInvalidRICC_whenClassify_thenRICCTypeIsNotReturned(): Unit = {
    // Arrange
    val values = List("AB.ABCD", "12345.AZ")

    values.foreach(v => {
      // Act
      val types = RICClassifier.classify(v)
      // Assert
      assertEquals(0, types.size)
    })
  }

  @Test
  def givenValidSEDOL_whenClassify_thenSEDOLTypeIsReturned(): Unit = {
    // Arrange
    val values = List("7980591", "BcBcB67")

    values.foreach(v => {
      // Act
      val types = SEDOLClassifier.classify(v)
      // Assert
      assertEquals(1, types.size)
      assertTrue(types.contains(SEDOLType))
    })
  }

  @Test
  def givenInvalidSEDOL_whenClassify_thenSEDOLTypeIsNotReturned(): Unit = {
    // Arrange
    val values = List("1234", "123456A")

    values.foreach(v => {
      // Act
      val types = SEDOLClassifier.classify(v)
      // Assert
      assertEquals(0, types.size)
    })
  }

  @Test
  def givenValidTimeStamp_whenClassify_thenTimeStampTypeIsReturned(): Unit = {
    // Arrange
    val values = List("10/10/2018", "2018/10/10", "10-10-2018", "2018/10/10", "10-10-2018 10:10:00.000")

    values.foreach(v => {
      // Act
     val types =  TimeStampClassifier.classify(v)
      // Assert
      assertEquals(1, types.size)
      assertTrue(types.contains(TimeStampType))
    })
  }

  @Test
  def givenInvalidTimeStamp_whenClassify_thenTimeStampTypeIsNotReturned(): Unit = {
    // Arrange
    val values = List("123/10/2018","10/123/2018","10/2018","1234", "123-10-2018","10-123-2018","10-2018")

    values.foreach(v => {
      // Act
      val types = TimeStampClassifier.classify(v)
      // Assert
      assertEquals(0, types.size)
    })
  }
}
