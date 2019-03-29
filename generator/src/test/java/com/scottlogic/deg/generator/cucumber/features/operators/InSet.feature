Feature: User can specify that a field value belongs to a set of predetermined options.

Background:
     Given the generation strategy is full

### inSet alone ###
Scenario: Running an 'inSet' request that includes strings with roman alphabet lowercase chars (a-z) only should be successful
  Given there is a field foo
    And foo is in set:
      | "aaa" |
      | "aab" |
  Then the following data should be generated:
      |  foo  |
      | null  |
      | "aaa" |
      | "aab" |

Scenario: Running an 'inSet' request that includes strings with roman alphabet uppercase chars (A-Z) only should be successful
  Given there is a field foo
    And foo is in set:
      | "CCC" |
      | "DDD" |
  Then the following data should be generated:
      | foo   |
      | null  |
      | "CCC" |
      | "DDD" |

Scenario: Running an 'inSet' request that includes strings with roman numeric chars (0-9) only should be successful
  Given there is a field foo
    And foo is in set:
      | "012" |
      | "345" |
  Then the following data should be generated:
      | foo   |
      | null  |
      | "012" |
      | "345" |

Scenario: Running an 'inSet' request that includes roman character strings that include profanity should be successful
  Given there is a field foo
    And foo is in set:
      | "Dick Van Dyke"       |
      | "Scunthorpe Hospital" |
  Then the following data should be generated:
      |  foo                  |
      | null                  |
      | "Dick Van Dyke"       |
      | "Scunthorpe Hospital" |


Scenario: Running an 'inSet' request that includes roman character strings that include in-use values should be successful
  Given there is a field foo
    And foo is in set:
      | "true"      |
      | "false"     |
      | "null"      |
      | "undefined" |
      | "NaN"       |
      | "nil"       |
      | "infinity"  |
  Then the following data should be generated:
      |  foo        |
      | null        |
      | "true"      |
      | "false"     |
      | "null"      |
      | "undefined" |
      | "NaN"       |
      | "nil"       |
      | "infinity"  |

Scenario: Running an 'inSet' request that includes strings with special characters (standard) should be successful
  Given there is a field foo
    And foo is in set:
      | "!£$%^&*()"   |
      | "{}:@~;'#<>?" |
  Then the following data should be generated:
      |  foo          |
      | null          |
      | "!£$%^&*()"   |
      | "{}:@~;'#<>?" |

Scenario: Running an 'inSet' request that includes strings with special characters (white spaces) should be successful
  Given there is a field foo
    And foo is in set:
      | "]	[] [] [] [" |
      | "] [] [] ["     |
  Then the following data should be generated:
      |  foo            |
      | null            |
      | "]	[] [] [] [" |
      | "] [] [] ["     |

Scenario: Running an 'inSet' request that includes strings with special characters (unicode symbols) should be successful
  Given there is a field foo
    And foo is in set:
      | "†ŠŒŽ™¼ǅ©" |
      | "®…¶Σ֎"    |
  Then the following data should be generated:
      |  foo       |
      | null       |
      | "†ŠŒŽ™¼ǅ©" |
      | "®…¶Σ֎"    |

Scenario: Running an 'inSet' request that includes strings with special characters (emoji) should be successful
  Given there is a field foo
    And foo is in set:
      | "🚫⌛⚡🐢"   |
      | "👟💪😈🔬" |
  Then the following data should be generated:
      |  foo       |
      | null       |
      | "🚫⌛⚡🐢"   |
      | "👟💪😈🔬" |

Scenario: Running an 'inSet' request that includes strings with special characters (non roman character maps) should be successful
  Given there is a field foo
    And foo is in set:
      | "Ω"  |
      | "ڦ"  |
      | "আ" |
      | "⾉" |
      | "㑹" |
      | "㾹" |
  Then the following data should be generated:
      | foo  |
      | null |
      | "Ω"  |
      | "ڦ"  |
      | "আ" |
      | "⾉" |
      | "㑹" |
      | "㾹" |

Scenario: Running an 'inSet' request that includes roman numeric strings that include decimal numbers should be successful
  Given there is a field foo
    And foo is in set:
      | "0.1"         |
      | "0.00"        |
      | "12.5.99"     |
      | "0000000.345" |
  Then the following data should be generated:
      | foo           |
      | null          |
      | "0.1"         |
      | "0.00"        |
      | "12.5.99"     |
      | "0000000.345" |

Scenario: Running an 'inSet' request that includes roman numeric strings that include comma separated numbers should be successful
  Given there is a field foo
    And foo is in set:
      | "55,5"         |
      | "10,000"       |
      | "1,000,000.00" |
  Then the following data should be generated:
      | foo            |
      | null           |
      | "55,5"         |
      | "10,000"       |
      | "1,000,000.00" |

Scenario: Running an 'inSet' request that includes roman numeric strings that include numbers with Preceding zeros should be successful
  Given there is a field foo
    And foo is in set:
      | "£1.00"   |
      | "€5,99"   |
      | "¥10,000" |
  Then the following data should be generated:
      | foo       |
      | null      |
      | "£1.00"   |
      | "€5,99"   |
      | "¥10,000" |

Scenario: Running an 'inSet' request that includes roman numeric strings that include negative numbers("-1") should be successful
  Given there is a field foo
    And foo is in set:
      | "-1"      |
      | "-0.9999" |
  Then the following data should be generated:
      | foo       |
      | null      |
      | "-1"      |
      | "-0.9999" |

Scenario: Running an 'inSet' request that includes roman numeric strings that include positive numbers("+1") should be successful
  Given there is a field foo
    And foo is in set:
      | "+7864"     |
      | "+0.555555" |
  Then the following data should be generated:
      | foo         |
      | null        |
      | "+7864"     |
      | "+0.555555" |

Scenario: Running an 'inSet' request that includes roman character strings that include computer formatted numbers should be successful
  Given there is a field foo
    And foo is in set:
      | "1E+02"   |
      | "001 000" |
  Then the following data should be generated:
      | foo       |
      | null      |
      | "1E+02"   |
      | "001 000" |

Scenario: Running an 'inSet' request that includes roman character strings that include valid date values should be successful
  Given there is a field foo
    And foo is in set:
      | "2010-01-01T00:00:00.000" |
      | "2010-01-01T00:00:00.001" |
      | "2011-01-01T00:00:00.000" |
  Then the following data should be generated:
      | foo                       |
      | null                      |
      | "2010-01-01T00:00:00.000" |
      | "2010-01-01T00:00:00.001" |
      | "2011-01-01T00:00:00.000" |

Scenario: Running an 'inSet' request that includes roman character strings that include invalidly formatted date values should be successful
  Given there is a field foo
    And foo is in set:
      | "2010-01-01T00:00:00"     |
      | "01-01-2010T00:00:00.001" |
      | "1st Jan 2010"            |
      | "2011-01-88T00:00:00.000" |
      | "2011-01-01T88:00:00.000" |
  Then the following data should be generated:
      | foo                       |
      | null                      |
      | "2010-01-01T00:00:00"     |
      | "01-01-2010T00:00:00.001" |
      | "1st Jan 2010"            |
      | "2011-01-88T00:00:00.000" |
      | "2011-01-01T88:00:00.000" |

Scenario: 'InSet' value of an empty string "" is successful
  Given there is a field foo
    And foo is in set:
      | "" |
  Then the following data should be generated:
      | foo  |
      | null |
      | ""   |

Scenario: Running an 'inSet' request that includes a number value (not a string) should be successful
  Given there is a field foo
    And foo is in set:
      | 1     |
      | 54    |
      | 99999 |
  Then the following data should be generated:
      | foo   |
      | null  |
      | 1     |
      | 54    |
      | 99999 |

Scenario: Running an 'inSet' request that includes a decimal number value should be successful
  Given there is a field foo
    And foo is in set:
      | 0.1       |
      | 600.01    |
      | 9.0000009 |
  Then the following data should be generated:
      | foo       |
      | null      |
      | 0.1       |
      | 600.01    |
      | 9.0000009 |

Scenario: Running an 'inSet' request that includes a negative number value should be successful
  Given there is a field foo
    And foo is in set:
      | -10         |
      | -0.0000089  |
      | -9999999999 |
  Then the following data should be generated:
      | foo         |
      | null        |
      | -10         |
      | -0.0000089  |
      | -9999999999 |

Scenario: Running an 'inSet' request that includes the number zero should be successful
  Given there is a field foo
    And foo is in set:
      | 0   |
      | 0.0 |
  Then the following data should be generated:
      | foo  |
      | null |
      | 0    |
      | 0.0  |

Scenario: Running an 'inSet' request that includes a date value should be successful
  Given there is a field foo
    And foo is in set:
     | 2010-01-01T00:00:00.000Z |
     | 2010-01-01T00:00:00.001Z |
     | 2011-01-01T00:00:00.000Z |
  Then the following data should be generated:
     | foo                      |
     | null                     |
     | 2010-01-01T00:00:00.000Z |
     | 2010-01-01T00:00:00.001Z |
     | 2011-01-01T00:00:00.000Z |

Scenario: Running an 'inSet' request that includes a date value (leap year) should be successful
  Given there is a field foo
    And foo is in set:
      | 2020-02-29T00:00:00.000Z |
      | 2016-02-29T00:00:00.000Z |
      | 2012-02-29T00:00:00.000Z |
  Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2020-02-29T00:00:00.000Z |
      | 2016-02-29T00:00:00.000Z |
      | 2012-02-29T00:00:00.000Z |

Scenario: Running an 'inSet' request that includes a date value (system epoch dates) should be successful
  Given there is a field foo
    And foo is in set:
      | 0001-01-01T00:00:01.000Z |
      | 9999-12-31T23:59:59.999Z |
  Then the following data should be generated:
      | foo                      |
      | null                     |
      | 0001-01-01T00:00:01.000Z |
      | 9999-12-31T23:59:59.999Z |

Scenario: Running an 'inSet' request that includes an invalid date value should fail with an error message
  Given there is a field foo
    And foo is in set:
      | 2010-13-40T00:00:00.000Z |
      | 2017-12-31T23:59:59.999Z |
  Then I am presented with an error message
    And no data is created

Scenario: Running an 'inSet' request that includes an invalid time value should fail with an error message
  Given there is a field foo
    And foo is in set:
      | 2017-12-31T40:59:59.999Z |
      | 2017-12-31T23:59:59.999Z |
  Then I am presented with an error message
    And no data is created

Scenario: Running an 'inSet' request that includes a null entry ("") characters should be successful
  Given there is a field foo
    And foo is in set:
      | "" |
      | 1  |
  Then the following data should be generated:
      | foo  |
      | null |
      | ""   |
      | 1    |

Scenario: Running an 'inSet' request that includes a null entry (null) characters should throw an error
  Given there is a field foo
    And foo is in set:
    | null |
    | 1    |
  Then the profile is invalid because "Cannot create an IsInSetConstraint for field 'foo' with a set containing null."
    And no data is created

Scenario: Running an 'inSet' request that includes strings, numeric and datetime fields should be successful.
  Given there is a field foo
    And foo is in set:
    | 1                        |
    | 2010-01-01T00:00:00.000Z |
    | "String!"                |
  Then the following data should be generated:
    | foo                      |
    | 1                        |
    | 2010-01-01T00:00:00.000Z |
    | "String!"                |
    | null                     |

Scenario: Running an 'inSet' request that includes multiples of the same entry should be successful.
  Given there is a field foo
    And foo is in set:
      | 1 |
      | 1 |
      | 2 |
  Then the following data should be generated:
      | foo  |
      | null |
      | 1    |
      | 2    |

### inSet ###

Scenario: Running a 'inSet' request alongside a non-contradicting inSet constraint should be successful
  Given there is a field foo
    And foo is in set:
      | "Test 1" |
      | "Test 2" |
      | "Test 3" |
    And foo is in set:
      | "Test 3" |
      | "Test 4" |
      | "Test 5" |
  Then the following data should be generated:
      | foo      |
      | null     |
      | "Test 3" |

Scenario: 'InSet' with a non-contradictory not 'inSet' is successful
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is anything but in set:
      | "A" |
  Then the following data should be generated:
      | foo  |
      | null |
      | "a"  |

Scenario: Running a 'inSet' request alongside a contradicting inSet constraint should produce null
  Given there is a field foo
    And foo is in set:
      | "Test 1" |
      | "Test 2" |
      | "Test 3" |
    And foo is in set:
      | "Test 4" |
      | "Test 5" |
      | "Test 6" |
  Then the following data should be generated:
      | foo  |
      | null |

Scenario: 'InSet' with a contradicting not 'inSet' emits null
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is anything but in set:
      | "a" |
  Then the following data should be generated:
      | foo  |
      | null |

### null ###

Scenario: 'InSet' with not null is successful
  Given there is a field foo
    And foo is in set:
      | 1 |
    And foo is anything but null
  Then the following data should be generated:
      | foo |
      | 1   |

Scenario: Not 'inSet' with null emits null
  Given there is a field foo
    And foo is anything but in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is null
  Then the following data should be generated:
      | foo  |
      | null |

Scenario: Running a 'inSet' request alongside a null constraint should produce null
  Given there is a field foo
    And foo is in set:
      | "Test 1" |
      | "Test 2" |
      | "Test 3" |
    And foo is null
  Then the following data should be generated:
      | foo  |
      | null |

### ofType ###

Scenario: 'InSet' with non-contradicting 'ofType' string should be successful
  Given there is a field foo
    And foo is in set:
      | "Test 1" |
      | "Test 2" |
      | "Test 3" |
    And foo is of type "string"
  Then the following data should be generated:
      | foo      |
      | null     |
      | "Test 1" |
      | "Test 2" |
      | "Test 3" |

Scenario: 'InSet' with non-contradicting 'ofType' integer should be successful
  Given there is a field foo
    And foo is in set:
      | 1 |
    And foo is of type "integer"
  Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

Scenario: 'InSet' with non-contradicting 'ofType' decimal should be successful
  Given there is a field foo
    And foo is in set:
      | 1 |
    And foo is of type "decimal"
  Then the following data should be generated:
    | foo  |
    | null |
    | 1    |

Scenario: 'InSet' with non-contradicting 'ofType' datetime should be successful
  Given there is a field foo
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is of type "datetime"
  Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.000Z |

Scenario Outline: 'InSet' type values with non-contradicting not 'ofType' are successful
  Given there is a field foo
    And foo is in set:
      | <setValue> |
    And foo is anything but of type <type>
  Then the following data should be generated:
      | foo        |
      | null       |
      | <setValue> |
  Examples:
      | setValue                 | type       |
      | 1                        | "string"   |
      | 2019-01-01T00:00:00.000Z | "string"   |
      | "a"                      | "decimal"  |
      | 2019-01-01T00:00:00.000Z | "decimal"  |
      | 1                        | "datetime" |
      | "a"                      | "datetime" |

Scenario Outline: Not 'inSet' type values with non-contradicting 'ofType' are successful
  Given there is a field foo
    And foo is anything but in set:
      | <setValue> |
    And foo is of type <type>
    And foo is in set:
      | 1                        |
      | "a"                      |
      | 2019-01-01T00:00:00.000Z |
  Then the following data should be generated:
      | foo             |
      | null            |
      | <expectedValue> |
  Examples:
      | setValue                 | type       | expectedValue            |
      | 1                        | "string"   | "a"                      |
      | 2019-01-01T00:00:00.000Z | "string"   | "a"                      |
      | "a"                      | "decimal"  | 1                        |
      | 2019-01-01T00:00:00.000Z | "decimal"  | 1                        |
      | 1                        | "datetime" | 2019-01-01T00:00:00.000Z |
      | "a"                      | "datetime" | 2019-01-01T00:00:00.000Z |


Scenario Outline: Running a 'inSet' of string values with a contradicting ofType emits null
  Given there is a field foo
    And foo is in set:
      | "1" |
      | "2" |
      | "3" |
    And foo is of type <type>
  Then the following data should be generated:
      | foo  |
      | null |
  Examples:
      | type       |
      | "integer"  |
      | "decimal"  |
      | "datetime" |

Scenario Outline: Running a 'inSet' of numeric values with a contradicting ofType emits null
  Given there is a field foo
    And foo is in set:
      | 1 |
    And foo is of type <type>
  Then the following data should be generated:
      | foo  |
      | null |
  Examples:
      | type       |
      | "string"   |
      | "datetime" |

Scenario Outline: Running a 'inSet' of date values with a contradicting ofType emits null
  Given there is a field foo
    And foo is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-01T00:00:00.001Z |
      | 2011-01-01T00:00:00.000Z |
    And foo is of type <type>
  Then the following data should be generated:
      | foo  |
      | null |
  Examples:
      | type      |
      | "integer" |
      | "decimal"  |
      | "string"  |

Scenario Outline: : 'InSet' equal to a type value and with not 'ofType' for the same type should emit null
  Given there is a field foo
    And foo is in set:
      | <typeValue> |
    And foo is anything but of type <type>
  Then the following data should be generated:
      | foo  |
      | null |
  Examples:
      | typeValue                | type       |
      | "a"                      | "string"   |
      | 1                        | "decimal"  |
      | 2010-01-01T00:00:00.000Z | "datetime" |

### matchingRegex ###

Scenario: Running a 'inSet' request alongside a non-contradicting 'matchingRegex' constraint should be successful
  Given there is a field foo
    And foo is in set:
      | "Test"  |
      | "test"  |
      | "Testt" |
      | "Tes7"  |
    And foo is matching regex /[a-z]{4}/
  Then the following data should be generated:
      | foo    |
      | null   |
      | "test" |

Scenario: 'InSet' string value with a not 'matchingRegex' of contradictory value is successful
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is anything but matching regex /[b]{1}/
  Then the following data should be generated:
      | foo  |
      | null |
      | "a"  |

@ignore #out of memory
Scenario: Not 'inSet' string value with a 'matchingRegex' of contradictory value is successful
  Given there is a field foo
    And foo is anything but in set:
      | "a" |
    And foo is matching regex /[b]{1}/
  Then the following data should be generated:
      | foo  |
      | null |
      | "b"  |

Scenario Outline: 'InSet' value of non-string type with 'matchingRegex' is successful
  Given there is a field foo
    And foo is in set:
      | <typeValue> |
    And foo is matching regex /[a]{1}/
  Then the following data should be generated:
      | foo         |
      | null        |
      | <typeValue> |
  Examples:
      | typeValue                |
      | 1                        |
      | 2018-01-01T00:00:00.000Z |
      | true                     |

Scenario: 'InSet' alongside a contradicting 'matchingRegex' constraint should produce null
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is matching regex /[b]{1}/
  Then the following data should be generated:
      | foo  |
      | null |

Scenario: 'InSet' alongside a 'matchingRegex' constraint of contradictory length should produce null
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is matching regex /[a]{2}/
  Then the following data should be generated:
      | foo  |
      | null |

Scenario: 'InSet' alongside a contradicting not 'matchingRegex' emits null
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is anything but matching regex /[a]{1}/
  Then the following data should be generated:
      | foo  |
      | null |

@ignore #out of memory
Scenario: Not 'inSet' alongside a contradicting 'matchingRegex' emits null
  Given there is a field foo
    And foo is anything but in set:
      | "a" |
    And foo is matching regex /[a]{1}/
  Then the following data should be generated:
    | foo  |
    | null |

### containingRegex ###

Scenario: Running a 'inSet' request alongside a non-contradicting 'containingRegex' constraint should be successful
  Given there is a field foo
    And foo is in set:
      | "aa" |
      | "b"  |
    And foo is containing regex /[a]{1}/
  Then the following data should be generated:
      | foo  |
      | null |
      | "aa" |

Scenario: 'InSet' string value with a not 'containingRegex' of contradictory value is successful
  Given there is a field foo
    And foo is in set:
      | "a"  |
      | "ab" |
      | "b"  |
    And foo is anything but containing regex /[b]{1}/
  Then the following data should be generated:
      | foo  |
      | null |
      | "a"  |

Scenario: Not 'inSet' string value with a 'containingRegex' of contradictory value is successful
  Given there is a field foo
    And foo is anything but in set:
      | "a" |
    And foo is containing regex /[b]{1}/
    And foo is in set:
      | "a"  |
      | "b"  |
      | "ab" |
      | "aa" |
  Then the following data should be generated:
      | foo  |
      | null |
      | "b"  |
      | "ab" |

Scenario Outline: 'InSet' value of non-string type with 'containingRegex' is successful
  Given there is a field foo
    And foo is in set:
      | <typeValue> |
    And foo is containing regex /[a]{1}/
  Then the following data should be generated:
      | foo         |
      | null        |
      | <typeValue> |
  Examples:
      | typeValue                |
      | 1                        |
      | 2018-01-01T00:00:00.000Z |
      | true                     |

Scenario: 'InSet' alongside a contradicting 'containingRegex' constraint should produce null
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is containing regex /[b]{1}/
  Then the following data should be generated:
      | foo  |
      | null |

Scenario: 'InSet' alongside a 'containingRegex' constraint of contradictory length should produce null
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is containing regex /[a]{2}/
  Then the following data should be generated:
      | foo  |
      | null |

Scenario: 'InSet' alongside a contradicting not 'containingRegex' emits null
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is anything but containing regex /[a]{1}/
  Then the following data should be generated:
      | foo  |
      | null |

### ofLength ###

Scenario: 'InSet' with a non contradicting 'ofLength' is successful
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is of length 1
  Then the following data should be generated:
      | foo  |
      | null |
      | "a"  |

Scenario: 'InSet' with a non contradicting not 'ofLength' is successful
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is anything but of length 2
  Then the following data should be generated:
      | foo  |
      | null |
      | "a"  |

Scenario: Not 'inSet' with a non contradicting 'ofLength' is successful
  Given there is a field foo
    And foo is anything but in set:
      | "a" |
    And foo is of length 1
    And foo is in set:
      | "a"  |
      | "b"  |
  Then the following data should be generated:
      | foo  |
      | null |
      | "b"  |

Scenario Outline: 'InSet' of a non-string type value with a non contradicting 'ofLength' is successful
  Given there is a field foo
    And foo is in set:
      | <typeValue> |
    And foo is of length 0
  Then the following data should be generated:
      | foo         |
      | null        |
      | <typeValue> |
  Examples:
      | typeValue                |
      | 1                        |
      | 2018-01-01T00:00:00.000Z |

Scenario: 'InSet' with a contradicting 'ofLength' emits null
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is of length 2
  Then the following data should be generated:
      | foo  |
      | null |

Scenario: 'InSet' with a contradicting not 'ofLength' emits null
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is anything but of length 1
  Then the following data should be generated:
      | foo  |
      | null |

### longerThan ###

Scenario: 'InSet' with a non contradicting 'longerThan' is successful
  Given there is a field foo
    And foo is in set:
      | "aa" |
    And foo is longer than 1
  Then the following data should be generated:
      | foo  |
      | null |
      | "aa" |

Scenario: 'InSet' with a non contradicting not 'longerThan' is successful
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is anything but longer than 1
  Then the following data should be generated:
      | foo  |
      | null |
      | "a"  |

Scenario: Not 'inSet' with a non contradicting 'longerThan' is successful
  Given there is a field foo
    And foo is anything but in set:
      | "aa" |
    And foo is longer than 1
    And foo is in set:
      | "aa" |
      | "ba" |
    Then the following data should be generated:
      | foo  |
      | null |
      | "ba" |

Scenario Outline: 'InSet' of a non-string type value with a non contradicting 'longerThan' is successful
  Given there is a field foo
    And foo is in set:
      | <typeValue> |
    And foo is longer than 1
  Then the following data should be generated:
      | foo         |
      | null        |
      | <typeValue> |
  Examples:
      | typeValue                |
      | 1                        |
      | 2018-01-01T00:00:00.000Z |

Scenario: 'InSet' with a contradicting 'longerThan' emits null
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is longer than 1
  Then the following data should be generated:
      | foo  |
      | null |

Scenario: 'InSet' with a contradicting not 'longerThan' emits null
  Given there is a field foo
    And foo is in set:
      | "aa" |
    And foo is anything but longer than 1
  Then the following data should be generated:
      | foo  |
      | null |

### shorterThan ###

Scenario: 'InSet' with a non contradicting 'shorterThan' is successful
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is shorter than 2
  Then the following data should be generated:
      | foo  |
      | null |
      | "a"  |

Scenario: 'InSet' with a non contradicting not 'shorterThan' is successful
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is anything but shorter than 1
  Then the following data should be generated:
      | foo  |
      | null |
      | "a"  |

Scenario: Not 'inSet' with a non contradicting 'shorterThan' is successful
  Given there is a field foo
    And foo is anything but in set:
      | "a" |
    And foo is shorter than 2
    And foo is in set:
      | "a" |
      | "b" |
  Then the following data should be generated:
      | foo  |
      | null |
      | "b" |

Scenario Outline: 'InSet' of a non-string type value with a non contradicting 'shorterThan' is successful
  Given there is a field foo
    And foo is in set:
      | <typeValue> |
    And foo is shorter than 1
  Then the following data should be generated:
      | foo         |
      | null        |
      | <typeValue> |
  Examples:
      | typeValue                |
      | 1                        |
      | 2018-01-01T00:00:00.000Z |

Scenario: 'InSet' with a contradicting 'shorterThan' emits null
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is shorter than 1
  Then the following data should be generated:
      | foo  |
      | null |

Scenario: 'InSet' with a contradicting not 'shorterThan' emits null
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is anything but shorter than 2
  Then the following data should be generated:
      | foo  |
      | null |

### aValid ###

@ignore
Scenario: 'InSet' with a non contradicting 'aValid' ISIN is successful
  Given there is a field foo
    And foo is in set:
      | "GB0002634947" |
    And foo is a valid "ISIN"
  Then the following data should be generated:
    | foo            |
    | null           |
    | "GB0002634947" |

Scenario: 'InSet' with a non contradicting not 'aValid' ISIN is successful
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is anything but a valid "ISIN"
  Then the following data should be generated:
      | foo  |
      | null |
      | "a"  |

@ignore
Scenario: Not 'inSet' with a non contradicting 'aValid' ISIN is successful
  Given there is a field foo
    And foo is anything but in set:
      | "a" |
    And foo is a valid "ISIN"
    And foo is in set:
      | "a"            |
      | "GB0002634947" |
  Then the following data should be generated:
      | foo            |
      | null           |
      | "GB0002634947" |

Scenario Outline: 'InSet' of a non-string type with 'aValid' is successful
  Given there is a field foo
    And foo is in set:
      | <typeValue> |
    And foo is a valid "ISIN"
  Then the following data should be generated:
      | foo         |
      | null        |
      | <typeValue> |
  Examples:
      | typeValue                |
      | 1                        |
      | 2018-01-01T00:00:00.000Z |

Scenario: 'InSet' run against a contradicting 'aValid' ISIN emits null
  Given there is a field foo
    And foo is in set:
      | "a" |
    And foo is a valid "ISIN"
  Then the following data should be generated:
      | foo  |
      | null |

@ignore
Scenario: 'InSet' run against a contradicting not 'aValid' ISIN emits null
  Given there is a field foo
    And foo is in set:
      | "GB0002634947" |
    And foo is anything but a valid "ISIN"
  Then the following data should be generated:
      | foo  |
      | null |

### greaterThan ###

Scenario: 'InSet' with a non contradicting 'greaterThan' is successful
  Given there is a field foo
    And foo is in set:
      | 2 |
    And foo is greater than 1
  Then the following data should be generated:
      | foo  |
      | null |
      | 2    |

Scenario: 'InSet' with a non contradicting not 'greaterThan' is successful
  Given there is a field foo
    And foo is in set:
      | 1 |
    And foo is anything but greater than 1
  Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

Scenario: Not 'inSet' with a non contradicting 'greaterThan' is successful
  Given there is a field foo
    And foo is anything but in set:
      | 1 |
    And foo is greater than 0
    And foo is in set:
      | 1 |
      | 2 |
  Then the following data should be generated:
      | foo  |
      | null |
      | 2    |

Scenario Outline: 'InSet' of a non-numeric type value with a non contradicting 'greaterThan' is successful
  Given there is a field foo
    And foo is in set:
      | <typeValue> |
    And foo is greater than 1
  Then the following data should be generated:
      | foo         |
      | null        |
      | <typeValue> |
  Examples:
      | typeValue                |
      | "a"                      |
      | 2018-01-01T00:00:00.000Z |

Scenario: 'InSet' with a contradicting 'greaterThan' emits null
  Given there is a field foo
    And foo is in set:
      | 1 |
    And foo is greater than 1
  Then the following data should be generated:
      | foo  |
      | null |

Scenario: 'InSet' with a contradicting not 'greaterThan' emits null
  Given there is a field foo
    And foo is in set:
      | 1.1 |
    And foo is anything but greater than 1
  Then the following data should be generated:
      | foo  |
      | null |

### greaterThanOrEqualTo ###

Scenario: 'InSet' with a non contradicting 'greaterThanOrEqualTo' is successful
  Given there is a field foo
    And foo is in set:
      | 1 |
    And foo is greater than or equal to 1
  Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

Scenario: 'InSet' with a non contradicting not 'greaterThanOrEqualTo' is successful
  Given there is a field foo
    And foo is in set:
      | 1 |
    And foo is anything but greater than or equal to 2
  Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

Scenario: Not 'inSet' with a non contradicting 'greaterThanOrEqualTo' is successful
  Given there is a field foo
    And foo is anything but in set:
      | 1 |
    And foo is greater than or equal to 1
    And foo is in set:
      | 1 |
      | 2 |
  Then the following data should be generated:
      | foo  |
      | null |
      | 2    |

Scenario Outline: 'InSet' of a non-numeric type value with a non contradicting 'greaterThanOrEqualTo' is successful
  Given there is a field foo
    And foo is in set:
      | <typeValue> |
    And foo is greater than or equal to 1
  Then the following data should be generated:
      | foo         |
      | null        |
      | <typeValue> |
  Examples:
      | typeValue                |
      | "a"                      |
      | 2018-01-01T00:00:00.000Z |

Scenario: 'InSet' with a contradicting 'greaterThanOrEqualTo' emits null
  Given there is a field foo
    And foo is in set:
      | 1 |
    And foo is greater than or equal to 2
  Then the following data should be generated:
      | foo  |
      | null |

Scenario: 'InSet' with a contradicting not 'greaterThanOrEqualTo' emits null
  Given there is a field foo
    And foo is in set:
      | 1 |
    And foo is anything but greater than or equal to 1
  Then the following data should be generated:
      | foo  |
      | null |

### lessThan ###

Scenario: 'InSet' with a non contradicting 'lessThan' is successful
  Given there is a field foo
    And foo is in set:
      | 1 |
    And foo is less than 2
  Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

Scenario: 'InSet' with a non contradicting not 'lessThan' is successful
  Given there is a field foo
    And foo is in set:
      | 1 |
    And foo is anything but less than 1
  Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

Scenario: Not 'inSet' with a non contradicting 'lessThan' is successful
  Given there is a field foo
    And foo is anything but in set:
      | 1 |
    And foo is less than 3
    And foo is in set:
      | 1 |
      | 2 |
      | 3 |
  Then the following data should be generated:
      | foo  |
      | null |
      | 2    |

Scenario Outline: 'InSet' of a non-numeric type value with a non contradicting 'lessThan' is successful
  Given there is a field foo
    And foo is in set:
      | <typeValue> |
    And foo is less than 1
  Then the following data should be generated:
      | foo         |
      | null        |
      | <typeValue> |
  Examples:
      | typeValue                |
      | "a"                      |
      | 2018-01-01T00:00:00.000Z |

Scenario: 'InSet' with a contradicting 'lessThan' emits null
  Given there is a field foo
    And foo is in set:
      | 1 |
  And foo is less than 1
  Then the following data should be generated:
    | foo  |
    | null |

  Scenario: 'InSet' with a contradicting not 'lessThan' emits null
    Given there is a field foo
    And foo is in set:
      | 1 |
    And foo is anything but less than 2
    Then the following data should be generated:
      | foo  |
      | null |

### lessThanOrEqualTo ###

Scenario: 'InSet' with a non contradicting 'lessThanOrEqualTo' is successful
  Given there is a field foo
    And foo is in set:
      | 1 |
    And foo is less than or equal to 1
  Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

Scenario: 'InSet' with a non contradicting not 'lessThanOrEqualTo' is successful
  Given there is a field foo
    And foo is in set:
      | 2 |
    And foo is anything but less than or equal to 1
  Then the following data should be generated:
      | foo  |
      | null |
      | 2    |

Scenario: Not 'inSet' with a non contradicting 'lessThanOrEqualTo' is successful
  Given there is a field foo
    And foo is anything but in set:
      | 1 |
    And foo is less than or equal to 2
    And foo is in set:
      | 1 |
      | 2 |
  Then the following data should be generated:
      | foo  |
      | null |
      | 2    |

Scenario Outline: 'InSet' of a non-numeric type value with a non contradicting 'lessThanOrEqualTo' is successful
  Given there is a field foo
    And foo is in set:
      | <typeValue> |
    And foo is less than or equal to 1
  Then the following data should be generated:
      | foo         |
      | null        |
      | <typeValue> |
  Examples:
      | typeValue                |
      | "a"                      |
      | 2018-01-01T00:00:00.000Z |

Scenario: 'InSet' with a contradicting 'lessThanOrEqualTo' emits null
  Given there is a field foo
    And foo is in set:
      | 2 |
    And foo is less than or equal to 1
  Then the following data should be generated:
      | foo  |
      | null |

Scenario: 'InSet' with a contradicting not 'lessThanOrEqualTo' emits null
  Given there is a field foo
    And foo is in set:
      | 1 |
    And foo is anything but less than or equal to 1
  Then the following data should be generated:
      | foo  |
      | null |

### granularTo ###

Scenario: 'InSet' with a non contradicting 'granularTo' is successful
  Given there is a field foo
    And foo is in set:
      | 10 |
    And foo is granular to 1
  Then the following data should be generated:
      | foo  |
      | null |
      | 10   |

Scenario: 'InSet' with a non contradicting not 'granularTo' is successful
  Given there is a field foo
    And foo is in set:
      | 1.1 |
    And foo is anything but granular to 1
  Then the following data should be generated:
      | foo  |
      | null |
      | 1.1  |

@ignore #issue #824 - 2.0 should be generated with a granularity of 1
Scenario: Integer within an inSet and a non contradicting 'granularTo' is successful
  Given there is a field foo
    And foo is granular to 1
    And foo is in set:
      | 1.1 |
      | 1   |
      | 2.0 |
  Then the following data should be generated:
      | foo  |
      | null |
      | 1    |
      | 2.0  |

Scenario: Not 'inSet' with a non contradicting 'granularTo' is successful
  Given there is a field foo
    And foo is anything but in set:
      | 1.1 |
    And foo is granular to 1
    And foo is in set:
      | 1.1 |
      | 1   |
  Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

Scenario Outline: 'InSet' of a non-numeric type value with a non contradicting 'granularTo' is successful
  Given there is a field foo
    And foo is in set:
      | <typeValue> |
    And foo is granular to 1
  Then the following data should be generated:
      | foo         |
      | null        |
      | <typeValue> |
  Examples:
      | typeValue                |
      | "a"                      |
      | 2018-01-01T00:00:00.000Z |

@ignore
Scenario: 'InSet' with a contradicting 'granularTo' emits null
  Given there is a field foo
    And foo is in set:
      | 1.1 |
    And foo is granular to 1
  Then the following data should be generated:
      | foo  |
      | null |

@ignore
Scenario: 'InSet' with a contradicting not 'granularTo' emits null
  Given there is a field foo
    And foo is in set:
      | 1 |
    And foo is anything but granular to 1
  Then the following data should be generated:
      | foo  |
      | null |

### after ###

Scenario: 'InSet' with a non contradicting 'after' is successful
  Given there is a field foo
    And foo is in set:
      | 2019-01-01T00:00:00.001Z |
    And foo is after 2019-01-01T00:00:00.000Z
  Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.001Z |

Scenario: 'InSet' with a non contradicting not 'after' is successful
  Given there is a field foo
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is anything but after 2019-01-01T00:00:00.000Z
  Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.000Z |

Scenario: Not 'inSet' with a non contradicting 'after' is successful
  Given there is a field foo
    And foo is anything but in set:
      | 2019-01-01T00:00:00.001Z |
    And foo is after 2019-01-01T00:00:00.000Z
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.001Z |
      | 2019-01-01T00:00:00.002Z |
  Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.002Z |

Scenario Outline: 'InSet' of a non-datetime type value with a non contradicting 'after' is successful
  Given there is a field foo
    And foo is in set:
      | <typeValue> |
    And foo is after 2019-01-01T00:00:00.000Z
  Then the following data should be generated:
      | foo         |
      | null        |
      | <typeValue> |
  Examples:
      | typeValue |
      | "a"       |
      | 1         |

Scenario: 'InSet' with a contradicting 'after' emits null
  Given there is a field foo
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is after 2019-01-01T00:00:00.000Z
  Then the following data should be generated:
      | foo  |
      | null |

Scenario: 'InSet' with a contradicting not 'after' emits null
  Given there is a field foo
    And foo is in set:
      | 2019-01-01T00:00:00.001Z |
    And foo is anything but after 2019-01-01T00:00:00.000Z
  Then the following data should be generated:
      | foo  |
      | null |

### afterOrAt ###

Scenario: 'InSet' with a non contradicting 'afterOrAt' is successful
  Given there is a field foo
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is after or at 2019-01-01T00:00:00.000Z
  Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.000Z |

Scenario: 'InSet' with a non contradicting not 'afterOrAt' is successful
  Given there is a field foo
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is anything but after or at 2019-01-01T00:00:00.001Z
  Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.000Z |

Scenario: Not 'inSet' with a non contradicting 'afterOrAt' is successful
  Given there is a field foo
    And foo is anything but in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is after or at 2019-01-01T00:00:00.000Z
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.001Z |
  Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.001Z |

Scenario Outline: 'InSet' of a non-datetime type value with a non contradicting 'afterOrAt' is successful
  Given there is a field foo
    And foo is in set:
      | <typeValue> |
    And foo is after or at 2019-01-01T00:00:00.000Z
  Then the following data should be generated:
      | foo         |
      | null        |
      | <typeValue> |
  Examples:
      | typeValue |
      | "a"       |
      | 1         |

Scenario: 'InSet' with a contradicting 'afterOrAt' emits null
  Given there is a field foo
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is after or at 2019-01-01T00:00:00.001Z
  Then the following data should be generated:
      | foo  |
      | null |

Scenario: 'InSet' with a contradicting not 'afterOrAt' emits null
  Given there is a field foo
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is anything but after or at 2019-01-01T00:00:00.000Z
  Then the following data should be generated:
      | foo  |
      | null |

### before ###

Scenario: 'InSet' with a non contradicting 'before' is successful
  Given there is a field foo
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is before 2019-01-01T00:00:00.001Z
  Then the following data should be generated:
      | foo                     |
      | null                    |
      | 2019-01-01T00:00:00.000Z |

Scenario: 'InSet' with a non contradicting not 'before' is successful
  Given there is a field foo
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is anything but before 2019-01-01T00:00:00.000Z
  Then the following data should be generated:
      | foo                     |
      | null                    |
      | 2019-01-01T00:00:00.000Z |

Scenario: Not 'inSet' with a non contradicting 'before' is successful
  Given there is a field foo
    And foo is anything but in set:
      | 2019-01-01T00:00:00.001Z |
    And foo is before 2019-01-01T00:00:00.002Z
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.001Z |
      | 2019-01-01T00:00:00.002Z |
  Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.000Z |

Scenario Outline: 'InSet' of a non-datetime type value with a non contradicting 'before' is successful
  Given there is a field foo
    And foo is in set:
      | <typeValue> |
    And foo is before 2019-01-01T00:00:00.000Z
  Then the following data should be generated:
      | foo         |
      | null        |
      | <typeValue> |
  Examples:
      | typeValue |
      | "a"       |
      | 1         |

Scenario: 'InSet' with a contradicting 'before' emits null
  Given there is a field foo
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is before 2019-01-01T00:00:00.000Z
  Then the following data should be generated:
      | foo  |
      | null |

Scenario: 'InSet' with a contradicting not 'before' emits null
  Given there is a field foo
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is anything but before 2019-01-01T00:00:00.001Z
  Then the following data should be generated:
      | foo  |
      | null |

### beforeOrAt ###

Scenario: 'InSet' with a non contradicting 'beforeOrAt' is successful
  Given there is a field foo
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is before or at 2019-01-01T00:00:00.000Z
  Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.000Z |

Scenario: 'InSet' with a non contradicting not 'beforeOrAt' is successful
  Given there is a field foo
    And foo is in set:
      | 2019-01-01T00:00:00.001Z |
    And foo is anything but before or at 2019-01-01T00:00:00.000Z
  Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.001Z |

Scenario: Not 'inSet' with a non contradicting 'beforeOrAt' is successful
  Given there is a field foo
    And foo is anything but in set:
      | 2019-01-01T00:00:00.001Z |
    And foo is before or at 2019-01-01T00:00:00.002Z
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.001Z |
      | 2019-01-01T00:00:00.002Z |
  Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.002Z |

Scenario Outline: 'InSet' of a non-datetime type value with a non contradicting 'beforeOrAt' is successful
  Given there is a field foo
    And foo is in set:
      | <typeValue> |
    And foo is before or at 2019-01-01T00:00:00.000Z
  Then the following data should be generated:
      | foo         |
      | null        |
      | <typeValue> |
  Examples:
      | typeValue |
      | "a"       |
      | 1         |

Scenario: 'InSet' with a contradicting 'beforeOrAt' emits null
  Given there is a field foo
    And foo is in set:
      | 2019-01-01T00:00:00.001Z |
    And foo is before or at 2019-01-01T00:00:00.000Z
  Then the following data should be generated:
      | foo  |
      | null |

Scenario: 'InSet' with a contradicting not 'beforeOrAt' emits null
  Given there is a field foo
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is anything but before or at 2019-01-01T00:00:00.000Z
  Then the following data should be generated:
      | foo  |
      | null |
Scenario: Running a 'inSet' request alongside an ofType = string should be successful
     Given there is a field foo
       And foo is in set:
       | "Test 1" |
       | "Test 2" |
       | "Test 3" |
       And foo is of type "string"
     Then the following data should be generated:
       | foo      |
       | null     |
       | "Test 1" |
       | "Test 2" |
       | "Test 3" |

Scenario: Running a 'inSet' request alongside a contradicting ofType = string should produce null
     Given there is a field foo
       And foo is in set:
       | 1 |
       | 2 |
       | 3 |
       And foo is of type "string"
     Then the following data should be generated:
       | foo  |
       | null |

Scenario: Running a 'inSet' request alongside an ofType = integer should be successful
  Given there is a field foo
    And foo is in set:
      | 1 |
      | 2 |
      | 3 |
    And foo is of type "integer"
  Then the following data should be generated:
    | foo  |
    | null |
    | 1    |
    | 2    |
    | 3    |

Scenario: Running a 'inSet' request alongside an ofType = decimal should be successful
  Given there is a field foo
    And foo is in set:
      | 1 |
      | 2 |
      | 3 |
    And foo is of type "decimal"
  Then the following data should be generated:
    | foo  |
    | null |
    | 1    |
    | 2    |
    | 3    |

Scenario: Running a 'inSet' request alongside a contradicting ofType = integer should produce null
  Given there is a field foo
    And foo is in set:
      | "1" |
      | "2" |
      | "3" |
    And foo is of type "integer"
  Then the following data should be generated:
    | foo  |
    | null |

Scenario: Running a 'inSet' request alongside a contradicting ofType = decimal should produce null
  Given there is a field foo
    And foo is in set:
      | "1" |
      | "2" |
      | "3" |
    And foo is of type "decimal"
  Then the following data should be generated:
    | foo  |
    | null |

Scenario: Running a 'inSet' request alongside an ofType = datetime should be successful
     Given there is a field foo
       And foo is in set:
       | 2010-01-01T00:00:00.000Z |
       | 2010-01-01T00:00:00.001Z |
       | 2011-01-01T00:00:00.000Z |
       And foo is of type "datetime"
     Then the following data should be generated:
       | foo                      |
       | null                     |
       | 2010-01-01T00:00:00.000Z |
       | 2010-01-01T00:00:00.001Z |
       | 2011-01-01T00:00:00.000Z |

Scenario: Running a 'inSet' request alongside a contradicting ofType = datetime should produce null
     Given there is a field foo
       And foo is in set:
       | "2010-01-01T00:00:00.000" |
       | "2010-01-01T00:00:00.001" |
       | "2011-01-01T00:00:00.000" |
       And foo is of type "datetime"
     Then the following data should be generated:
       | foo  |
       | null |

Scenario: Running a 'inSet' request alongside a non-contradicting matchingRegex constraint should be successful
     Given there is a field foo
       And foo is in set:
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Tes7"  |
     And foo is matching regex /[a-z]{4}/
       Then the following data should be generated:
       | foo    |
       | null   |
       | "test" |

Scenario: Running a 'inSet' request alongside a contradicting matchingRegex constraint should produce null
     Given there is a field foo
       And foo is in set:
       | "Test"  |
       | "Testt" |
       | "Tes7"  |
       And foo is matching regex /[a-z]{4}/
     Then the following data should be generated:
       | foo  |
       | null |


Scenario: Running a 'inSet' request alongside a non-contradicting containingRegex constraint should be successful
     Given there is a field foo
       And foo is in set:
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Tes7"  |
       And foo is containing regex /[a-z]{4}/
     Then the following data should be generated:
       | foo     |
       | null    |
       | "test"  |
       | "Testt" |

Scenario: Running a 'inSet' request alongside a contradicting containingRegex constraint should generate null
  Given there is a field foo
  And foo is in set:
    | "Test"  |
    | "test"  |
    | "Testt" |
    | "Tes7"  |
  And foo is containing regex /[A-Z]{4}/
  Then the following data should be generated:
    | foo     |
    | null    |

Scenario: Running a 'inSet' request alongside a non-contradicting ofLength constraint should be successful
     Given there is a field foo
       And foo is in set:
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7"  |
     And foo is of length 4
       Then the following data should be generated:
       | foo     |
       | null    |
       | "Test"  |
       | "test"  |

Scenario: Running a 'inSet' request alongside a contradicting ofLength (too short) constraint should produce null
     Given there is a field foo
       And foo is in set:
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |
       And foo is of length 3
     Then the following data should be generated:
       | foo  |
       | null |

Scenario: Running a 'inSet' request alongside a contradicting ofLength (too long) constraint should produce null
     Given there is a field foo
       And foo is in set:
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |
       And foo is of length 10
     Then the following data should be generated:
       | foo  |
       | null |

Scenario: Running a 'inSet' request alongside a non-contradicting longerThan constraint should be successful
     Given there is a field foo
       And foo is in set:
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |
       And foo is longer than 4
     Then the following data should be generated:
       |   foo   |
       | "Testt" |
       | "Test7" |
       |   null  |

Scenario: Running a 'inSet' request alongside a contradicting longerThan (equal) constraint should produce null
     Given there is a field foo
       And foo is in set:
       | "Test1" |
       | "Test2" |
       | "Test3" |
       | "Test4" |
       And foo is longer than 5
     Then the following data should be generated:
       | foo  |
       | null |

Scenario: Running a 'inSet' request alongside a contradicting longerThan (too long) constraint should produce null
     Given there is a field foo
       And foo is in set:
       | "Test1" |
       | "Test2" |
       | "Test3" |
       | "Test4" |
       And foo is longer than 10
     Then the following data should be generated:
       | foo  |
       | null |

Scenario: Running a 'inSet' request alongside a non-contradicting shorterThan constraint should be successful
     Given there is a field foo
       And foo is in set:
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |
       And foo is shorter than 5
     Then the following data should be generated:
       |  foo   |
       | "Test" |
       | "test" |
       |  null  |

Scenario: Running a 'inSet' request alongside a contradicting shorterThan (too short) constraint should produce null
     Given there is a field foo
       And foo is in set:
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |
       And foo is shorter than 3
     Then the following data should be generated:
       | foo  |
       | null |

Scenario: Running a 'inSet' request alongside a contradicting shorterThan (equal) constraint should produce null
     Given there is a field foo
       And foo is in set:
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |
       And foo is shorter than 4
     Then the following data should be generated:
       | foo  |
       | null |

Scenario: Running a 'inSet' request alongside a greaterThan constraint should be successful
     Given there is a field foo
       And foo is in set:
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |
       And foo is greater than 1
     Then the following data should be generated:
       | foo     |
       | null    |
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |

Scenario: Running a 'inSet' request alongside a greaterThanOrEqualTo constraint should be successful
     Given there is a field foo
       And foo is in set:
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |
       And foo is greater than or equal to 1
     Then the following data should be generated:
       | foo     |
       | null    |
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |

Scenario: Running a 'inSet' request alongside a lessThan constraint should be successful
     Given there is a field foo
       And foo is in set:
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |
       And foo is less than 1
     Then the following data should be generated:
       | foo     |
       | null    |
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |

Scenario: Running a 'inSet' request alongside a lessThanOrEqualTo constraint should be successful
     Given there is a field foo
       And foo is in set:
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |
       And foo is less than or equal to 1
     Then the following data should be generated:
       | foo     |
       | null    |
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |

Scenario: Running a 'inSet' request alongside a granularTo constraint should be successful
     Given there is a field foo
       And foo is in set:
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |
       And foo is granular to 0.1
     Then the following data should be generated:
       | foo     |
       | null    |
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |

Scenario: Running a 'inSet' request alongside a after constraint should be successful
     Given there is a field foo
       And foo is in set:
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |
       And foo is after 2010-01-01T00:00:00.000Z
     Then the following data should be generated:
       | foo     |
       | null    |
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |

Scenario: Running a 'inSet' request alongside a afterOrAt constraint should be successful
     Given there is a field foo
       And foo is in set:
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |
       And foo is after or at 2010-01-01T00:00:00.000Z
     Then the following data should be generated:
       | foo     |
       | null    |
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |

Scenario: Running a 'inSet' request alongside a before constraint should be successful
     Given there is a field foo
       And foo is in set:
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |
       And foo is before 2010-01-01T00:00:00.000Z
     Then the following data should be generated:
       | foo     |
       | null    |
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |

Scenario: Running a 'inSet' request alongside a beforeOrAt constraint should be successful
     Given there is a field foo
       And foo is in set:
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |
       And foo is before or at 2010-01-01T00:00:00.000Z
     Then the following data should be generated:
       | foo     |
       | null    |
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |

Scenario: Running a 'inSet' request as part of a non-contradicting anyOf constraint should be successful
     Given there is a field foo
       And there is a constraint:
       """
       { "anyOf": [
         { "field": "foo", "is": "inSet", "values": [ "Test 1", "Test 2" ] },
         { "field": "foo", "is": "inSet", "values": [ "Test 3", "Test 4" ] }
       ]}
       """
     Then the following data should be generated:
       | foo      |
       | null     |
       | "Test 1" |
       | "Test 2" |
       | "Test 3" |
       | "Test 4" |

Scenario: Running a 'inSet' request as part of a non-contradicting allOf constraint should be successful
     Given there is a field foo
       And there is a constraint:
       """
       { "allOf": [
         { "field": "foo", "is": "inSet", "values": [ "Test1", "Test2" ] },
         { "field": "foo", "is": "inSet", "values": [ "Test1", "Test2" ] }
       ]}
       """
     Then the following data should be generated:
       | foo      |
       | null     |
       | "Test1" |
       | "Test2" |

Scenario: Running a 'inSet' request as part of a contradicting allOf constraint should produce null
     Given there is a field foo
       And there is a constraint:
       """
       { "allOf": [
         { "field": "foo", "is": "inSet", "values": [ "Test1", "Test2" ] },
         { "field": "foo", "is": "inSet", "values": [ "Test3", "Test4" ] }
       ]}
       """
     Then the following data should be generated:
       | foo  |
       | null |


  Scenario: Running a 'inSet' request as part of an if constraint should be successful
     Given the following fields exist:
       | foo   |
       | price |
       And foo is in set:
       | "Test1" |
       | "Test2" |
       | "Test3" |
       | "Test4" |
       And there is a constraint:
       """
       {
         "if": { "field": "foo", "is": "inSet", "values": [ "Test1", "Test2" ] },
         "then": { "field": "price", "is": "equalTo", "value": 1 },
         "else": { "field": "price", "is": "equalTo", "value": 2 }
         }
       """
       And foo is anything but null
       And price is anything but null
     Then the following data should be generated:
       | foo     | price |
       | "Test1" | 1     |
       | "Test2" | 1     |
       | "Test3" | 2     |
       | "Test4" | 2     |
