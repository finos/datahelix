Feature: User can specify that a value is equalTo a required value

  Background:
    Given the generation strategy is full

### alone ###

  Scenario: Running an 'equalTo' request that includes strings with roman numeric chars (0-9) only should be successful
    Given there is a field foo
    And foo is equal to "0123456789"
    Then the following data should be generated:
      | foo          |
      | null         |
      | "0123456789" |

  Scenario: Running an 'equalTo' that includes strings with both roman alphabet lowercase (a-z) and uppercase (A-Z) is successful
    Given there is a field foo
    And foo is equal to "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    Then the following data should be generated:
      | foo                                                    |
      | null                                                   |
      | "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" |

  Scenario: Running an 'equalTo' request that includes strings with both roman alphabet (a-z, A-Z)and numeric chars (0-9) should be successful
    Given there is a field foo
    And foo is equal to "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    Then the following data should be generated:
      | foo                                                              |
      | null                                                             |
      | "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789" |

  Scenario: Running an 'equalTo' request that includes roman character strings that include profanity is successful
    Given there is a field foo
    And foo is equal to "Dick van Dyke"
    Then the following data should be generated:
      | foo             |
      | null            |
      | "Dick van Dyke" |

  Scenario: Running an 'equalTo' request that includes roman character strings that include in-use values ("true") should be successful
    Given there is a field foo
    And foo is equal to "true"
    Then the following data should be generated:
      | foo    |
      | null   |
      | "true" |

  Scenario: Running an 'equalTo' request that includes roman character strings that include in-use values ("false") should be successful
    Given there is a field foo
    And foo is equal to "false"
    Then the following data should be generated:
      | foo     |
      | null    |
      | "false" |

  Scenario: Running an 'equalTo' request that includes roman character strings that include in-use values ("null") should be successful
    Given there is a field foo
    And foo is equal to "null"
    Then the following data should be generated:
      | foo    |
      | null   |
      | "null" |

  Scenario: Running an 'equalTo' request that includes roman character strings that include in-use values ("undefined") should be successful
    Given there is a field foo
    And foo is equal to "undefined"
    Then the following data should be generated:
      | foo         |
      | null        |
      | "undefined" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (standard) should be successful
    Given there is a field foo
    And foo is equal to ".,;:/()-+£$%€!?=&#@<>[]{}^*"
    Then the following data should be generated:
      | foo                           |
      | null                          |
      | ".,;:/()-+£$%€!?=&#@<>[]{}^*" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (white spaces) should be successful
    Given there is a field foo
    And foo is equal to "]	[] [] [] [] [] [] ["
    Then the following data should be generated:
      | foo                     |
      | null                    |
      | "]	[] [] [] [] [] [] [" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (unicode symbols) should be successful
    Given there is a field foo
    And foo is equal to "†ŠŒŽ™¼ǅ©®…¶Σ֎"
    Then the following data should be generated:
      | foo             |
      | null            |
      | "†ŠŒŽ™¼ǅ©®…¶Σ֎" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (emoji) should be successful
    Given there is a field foo
    And foo is equal to "☺☹☻😀😁😂😃😄😅😆😇😈😉😊😋😌🚩🚪🚫🚬🚭🚮🚯🚰"
    Then the following data should be generated:
      | foo                                             |
      | null                                            |
      | "☺☹☻😀😁😂😃😄😅😆😇😈😉😊😋😌🚩🚪🚫🚬🚭🚮🚯🚰" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (non roman character maps: Chinese / Arabic / Russian) should be successful
    Given there is a field foo
    And foo is equal to "传/傳象形字ФХѰѾЦИتشرقصف"
    Then the following data should be generated:
      | foo                  |
      | null                 |
      | "传/傳象形字ФХѰѾЦИتشرقصف" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (non roman character maps: Chinese / Arabic / Russian) should be successful
    Given there is a field foo
    And foo is equal to "בְּרֵאשִׁית, בָּרָא אֱלֹהִים, אֵת הַשָּׁמַיִם, וְאֵת הָאָרֶץ"
    Then the following data should be generated:
      | foo                                                            |
      | null                                                           |
      | "בְּרֵאשִׁית, בָּרָא אֱלֹהִים, אֵת הַשָּׁמַיִם, וְאֵת הָאָרֶץ" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (standard) alongside roman alphanumeric characters should be successful
    Given there is a field foo
    And foo is equal to "abcdefghijk.,;:/()-+£$%€!?=&#@<>[]{}^*"
    Then the following data should be generated:
      | foo                                      |
      | null                                     |
      | "abcdefghijk.,;:/()-+£$%€!?=&#@<>[]{}^*" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (white spaces) alongside roman alphanumeric characters should be successful
    Given there is a field foo
    And foo is equal to "abcdefghijk]	[] [] [] [] [] [] ["
    Then the following data should be generated:
      | foo                                |
      | null                               |
      | "abcdefghijk]	[] [] [] [] [] [] [" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (unicode symbols) alongside roman alphanumeric characters should be successful
    Given there is a field foo
    And foo is equal to "abcdefghijk†ŠŒŽ™¼ǅ©®…¶Σ֎"
    Then the following data should be generated:
      | foo                        |
      | null                       |
      | "abcdefghijk†ŠŒŽ™¼ǅ©®…¶Σ֎" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (emoji) alongside roman alphanumeric characters should be successful
    Given there is a field foo
    And foo is equal to "abcdefghijk☺☹☻😀😁😂😃😄😅😆😇😈😉😊😋😌🚩🚪🚫🚬🚭🚮🚯🚰"
    Then the following data should be generated:
      | foo                                                        |
      | null                                                       |
      | "abcdefghijk☺☹☻😀😁😂😃😄😅😆😇😈😉😊😋😌🚩🚪🚫🚬🚭🚮🚯🚰" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (non roman character maps: Chinese / Arabic / Russian) alongside roman alphanumeric characters should be successful
    Given there is a field foo
    And foo is equal to "abcdefghijk传/傳象形字ФХѰѾЦИتشرقصف"
    Then the following data should be generated:
      | foo                             |
      | null                            |
      | "abcdefghijk传/傳象形字ФХѰѾЦИتشرقصف" |

  Scenario: Running an 'equalTo' request that includes roman numeric strings that include decimal numbers should be successful
    Given there is a field foo
    And foo is equal to "0.00"
    Then the following data should be generated:
      | foo    |
      | null   |
      | "0.00" |

  Scenario: Running an 'equalTo' request that includes roman numeric strings that include comma separated numbers should be successful
    Given there is a field foo
    And foo is equal to "1,000"
    Then the following data should be generated:
      | foo     |
      | null    |
      | "1,000" |

  Scenario: Running an 'equalTo' request that includes roman numeric strings that include numbers with Preceding zeros should be successful
    Given there is a field foo
    And foo is equal to "010"
    Then the following data should be generated:
      | foo   |
      | null  |
      | "010" |

  Scenario: Running an 'equalTo' request that includes roman numeric strings that include numbers in a currency style should be successful
    Given there is a field foo
    And foo is equal to "£1.00"
    Then the following data should be generated:
      | foo     |
      | null    |
      | "£1.00" |

  Scenario: Running an 'equalTo' request that includes roman numeric strings that include negative numbers should be successful
    Given there is a field foo
    And foo is equal to "-1"
    Then the following data should be generated:
      | foo  |
      | null |
      | "-1" |

  Scenario: Running an 'equalTo' request that includes roman numeric strings that include positive numbers should be successful
    Given there is a field foo
    And foo is equal to "+1"
    Then the following data should be generated:
      | foo  |
      | null |
      | "+1" |

  Scenario: Running an 'equalTo' request that includes roman character strings that include in-use numeric values ("Infinity") should be successful
    Given there is a field foo
    And foo is equal to "Infinity"
    Then the following data should be generated:
      | foo        |
      | null       |
      | "Infinity" |

  Scenario: Running an 'equalTo' request that includes roman character strings that include in-use numeric values ("NaN") should be successful
    Given there is a field foo
    And foo is equal to "NaN"
    Then the following data should be generated:
      | foo   |
      | null  |
      | "NaN" |

  Scenario: Running an 'equalTo' request that includes roman character strings that include in-use numeric values ("nil") should be successful
    Given there is a field foo
    And foo is equal to "nil"
    Then the following data should be generated:
      | foo   |
      | null  |
      | "nil" |

  Scenario: Running an 'equalTo' request that includes roman character strings that include computer formatted numbers (hexidecimal) should be successful
    Given there is a field foo
    And foo is equal to "1E+02"
    Then the following data should be generated:
      | foo     |
      | null    |
      | "1E+02" |

  Scenario: Running an 'equalTo' request that includes roman character strings that include computer formatted numbers (binary) should be successful
    Given there is a field foo
    And foo is equal to "001 000"
    Then the following data should be generated:
      | foo       |
      | null      |
      | "001 000" |

  Scenario: Running an 'equalTo' request that includes roman character strings that include valid date values should be successful
    Given there is a field foo
    And foo is equal to "2010-01-01T00:00:00.000"
    Then the following data should be generated:
      | foo                       |
      | null                      |
      | "2010-01-01T00:00:00.000" |

  Scenario: Running an 'equalTo' request that includes roman character strings that include invalidly formatted date values should be successful
    Given there is a field foo
    And foo is equal to "2010-01-01T00:00"
    Then the following data should be generated:
      | foo                |
      | null               |
      | "2010-01-01T00:00" |

  Scenario: Running an 'equalTo' request that includes a number value (not a string) should be successful
    Given there is a field foo
    And foo is equal to 100
    Then the following data should be generated:
      | foo  |
      | null |
      | 100  |

  Scenario: Running an 'equalTo' request that includes a decimal number value should be successful
    Given there is a field foo
    And foo is equal to 0.14
    Then the following data should be generated:
      | foo  |
      | null |
      | 0.14 |

  Scenario: Running an 'equalTo' request that includes a negative number value should be successful
    Given there is a field foo
    And foo is equal to -1
    Then the following data should be generated:
      | foo  |
      | null |
      | -1   |

  Scenario: Running an 'equalTo' request that includes a decimal number with trailing zeros should be successful
    Given there is a field foo
    And foo is equal to 1.001000
    Then the following data should be generated:
      | foo      |
      | null     |
      | 1.001000 |

  Scenario: Running an 'equalTo' request that includes the number zero should be successful
    Given there is a field foo
    And foo is equal to 0
    Then the following data should be generated:
      | foo  |
      | null |
      | 0    |

  Scenario: Running an 'equalTo' request that includes a date value (not a string) should be successful
    Given there is a field foo
    And foo is equal to 2010-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2010-01-01T00:00:00.000Z |

  Scenario: Running an 'equalTo' request that includes a date value (leap year) should be successful
    Given there is a field foo
    And foo is equal to 2020-02-29T00:00:00.000Z
    And foo is of type "datetime"
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2020-02-29T00:00:00.000Z |

  Scenario: Running an 'equalTo' request that includes a date value (earliest date) should be successful
    Given there is a field foo
    And foo is equal to 0001-01-01T00:00:01.000Z
    And foo is of type "datetime"
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 0001-01-01T00:00:01.000Z |

  Scenario: Running an 'equalTo' request that includes a date value (system max future dates) should be successful
    Given there is a field foo
    And foo is equal to 9999-12-31T23:59:59.999Z
    And foo is of type "datetime"
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 9999-12-31T23:59:59.999Z |

  Scenario: Running an 'equalTo' request that includes an empty string("") characters should be successful
    Given there is a field foo
    And foo is equal to ""
    Then the following data should be generated:
      | foo  |
      | null |
      | ""   |

  Scenario: Running an 'equalTo' request that includes a null entry (null) characters should fail with an error message
    Given there is a field foo
    And foo is equal to null
    Then the profile is invalid because "Field \[foo\]: Couldn't recognise 'value' property, it must be set to a value"

  Scenario: Running an 'equalTo' request that includes an invalid date value should fail with an error message
    Given there is a field foo
    And foo is equal to 2010-13-40T00:00:00.000Z
    Then the profile is invalid because "Field \[foo\]: Date string '2010-13-40T00:00:00.000Z' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS\[Z\] between \(inclusive\) 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z"
    And no data is created

  Scenario: Running an 'equalTo' request that includes an invalid time value  should fail with an error message
    Given there is a field foo
    And foo is equal to 2010-01-01T55:00:00.000Z
    Then the profile is invalid because "Field \[foo\]: Date string '2010-01-01T55:00:00.000Z' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS\[Z\] between \(inclusive\) 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z"
    And no data is created

  Scenario: Running an 'equalTo' request that includes a boolean value e.g. true should be successful
    Given there is a field foo
    And foo is equal to true
    And foo is anything but null
    Then the following data should be generated:
      | foo  |
      | true |

  Scenario: Running an 'equalTo' request that includes a boolean value e.g. false should be successful
    Given there is a field foo
    And foo is equal to false
    And foo is anything but null
    Then the following data should be generated:
      | foo   |
      | false |

### EqualTo ###

  Scenario: Two non-contradictory 'equalTo' statements should be successful
    Given there is a field foo
    And foo is equal to "a"
    And foo is equal to "a"
    Then the following data should be generated:
      | foo  |
      | "a"  |
      | null |

  Scenario: A not 'equalTo' statement should have no impact on an 'equalTo' statement
    Given there is a field foo
    And foo is equal to "a"
    And foo is anything but equal to "A"
    Then the following data should be generated:
      | foo  |
      | "a"  |
      | null |

  Scenario: Contradictory 'equalTo' statements should emit null
    Given there is a field foo
    And foo is equal to "a"
    And foo is equal to "b"
    Then the following data should be generated:
      | foo  |
      | null |

### InSet ###

  Scenario: Running an 'inSet' request alongside a non-contradicting 'equalTo' constraint should be successful
    Given there is a field foo
    And foo is in set:
      | "Test 1" |
      | "Test 2" |
      | "Test 3" |
    And foo is equal to "Test 1"
    Then the following data should be generated:
      | foo      |
      | null     |
      | "Test 1" |

  Scenario: Running an 'inSet' request alongside a contradicting 'equalTo' constraint should produce null
    Given there is a field foo
    And foo is in set:
      | "Test 1" |
      | "Test 2" |
      | "Test 3" |
    And foo is equal to "Test 4"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Running an 'inSet' request with a not constraint should be successful
    Given there is a field foo
    And foo is anything but in set:
      | "Test"  |
      | "test"  |
      | "Testt" |
      | "Test7" |
    And foo is equal to "Test 01 is not in set"
    Then the following data should be generated:
      | foo                     |
      | null                    |
      | "Test 01 is not in set" |

  Scenario: Non-contradictory not 'equalTo' and 'inSet' should be successful
    Given there is a field foo
    And foo is in set:
      | "Test1" |
      | "Test2" |
      | 1       |
    And foo is anything but equal to "Test1"
    Then the following data should be generated:
      | foo     |
      | "Test2" |
      | 1       |
      | null    |

  Scenario: Not 'equalTo' and in set for same value should emit null
    Given there is a field foo
    And foo is in set:
      | "Test1" |
    And foo is anything but equal to "Test1"
    Then the following data should be generated:
      | foo  |
      | null |

### null ###

  Scenario: 'EqualTo' and not null should be successful
    Given there is a field foo
    And foo is equal to 15
    And foo is anything but null
    Then the following data should be generated:
      | foo |
      | 15  |

  Scenario: 'EqualTo' a value and must be null should emit null
    Given there is a field foo
    And foo is equal to "a"
    And foo is null
    Then the following data should be generated:
      | foo  |
      | null |

### ofType ###

  Scenario: 'OfType' string 'equalTo' a string value should be successful
    Given there is a field foo
    And foo is equal to "0123456789"
    And foo is of type "string"
    Then the following data should be generated:
      | foo          |
      | null         |
      | "0123456789" |


  Scenario: 'EqualTo' an integer with 'ofType' integer is successful
    Given there is a field foo
    And foo is equal to 1
    And foo is of type "integer"
    Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

  Scenario: 'EqualTo' a decimal with 'ofType' decimal is successful
    Given there is a field foo
    And foo is equal to 1
    And foo is of type "decimal"
    Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

  Scenario: 'EqualTo' a date value with 'ofType' datetime is successful
    Given there is a field foo
    And foo is equal to 2010-01-01T00:00:00.000Z
    And foo is of type "datetime"
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2010-01-01T00:00:00.000Z |

  Scenario: 'EqualTo' a number and not 'ofType' string is successful
    Given there is a field foo
    And foo is equal to 1
    And foo is anything but of type "string"
    Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

  Scenario: 'EqualTo' a date and not 'ofType' string is successful
    Given there is a field foo
    And foo is equal to 9999-12-31T23:59:59.999Z
    And foo is anything but of type "string"
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 9999-12-31T23:59:59.999Z |

  Scenario: 'EqualTo' a string and not 'ofType' decimal is successful
    Given there is a field foo
    And foo is equal to "a"
    And foo is anything but of type "decimal"
    Then the following data should be generated:
      | foo  |
      | null |
      | "a"  |

  Scenario: 'EqualTo' a date and not 'ofType' decimal is successful
    Given there is a field foo
    And foo is equal to 2019-01-01T00:00:00.000Z
    And foo is anything but of type "decimal"
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.000Z |

  Scenario: 'EqualTo' a string and not 'ofType' datetime is successful
    Given there is a field foo
    And foo is equal to "a"
    And foo is anything but of type "datetime"
    Then the following data should be generated:
      | foo  |
      | null |
      | "a"  |

  Scenario: 'EqualTo' a number and not 'ofType' datetime is successful
    Given there is a field foo
    And foo is equal to 1
    And foo is anything but of type "datetime"
    Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

  Scenario: Not 'equalTo' a number and 'ofType' string is successful
    Given there is a field foo
    And foo is anything but equal to 1
    And foo is of type "string"
    And foo is in set:
      | 1         |
      | "1"       |
      | "STr!ng5" |
    Then the following data should be generated:
      | foo       |
      | null      |
      | "1"       |
      | "STr!ng5" |

  Scenario: Not 'equalTo' a date and 'ofType' string is successful
    Given there is a field foo
    And foo is anything but equal to 2019-01-01T00:00:00.000Z
    And foo is of type "string"
    And foo is in set:
      | 1                         |
      | "2019-01-01T00:00:00.000" |
      | "STr!ng5"                 |
    Then the following data should be generated:
      | foo                       |
      | null                      |
      | "2019-01-01T00:00:00.000" |
      | "STr!ng5"                 |

  Scenario: Not 'equalTo' a string value and 'ofType' integer is successful
    Given there is a field foo
    And foo is anything but equal to "a"
    And foo is of type "integer"
    And foo is in set:
      | 1   |
      | "a" |
    Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

  Scenario: Not 'equalTo' a string value and 'ofType' decimal is successful
    Given there is a field foo
    And foo is anything but equal to "a"
    And foo is of type "decimal"
    And foo is in set:
      | 1.1 |
      | "a" |
    Then the following data should be generated:
      | foo  |
      | null |
      | 1.1  |

  Scenario: Not 'equalTo' a date value and 'ofType' integer is successful
    Given there is a field foo
    And foo is anything but equal to 2019-01-01T00:00:00.000Z
    And foo is of type "integer"
    And foo is in set:
      | 1                        |
      | 2019-01-01T00:00:00.000Z |
      | "1"                      |
    Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

  Scenario: Not 'equalTo' a date value and 'ofType' decimal is successful
    Given there is a field foo
    And foo is anything but equal to 2019-01-01T00:00:00.000Z
    And foo is of type "decimal"
    And foo is in set:
      | 1.1                      |
      | 2019-01-01T00:00:00.000Z |
      | "1"                      |
    Then the following data should be generated:
      | foo  |
      | null |
      | 1.1  |

  Scenario: Not 'equalTo' a string value and 'ofType' datetime is successful
    Given there is a field foo
    And foo is anything but equal to "a"
    And foo is of type "datetime"
    And foo is in set:
      | 2019-01-01T00:00:00.000Z  |
      | "2019-01-01T00:00:00.000" |
      | "a"                       |
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.000Z |

  Scenario: Not 'equalTo' an integer value and 'ofType' datetime is successful
    Given there is a field foo
    And foo is anything but equal to 1
    And foo is of type "datetime"
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
      | 100                      |
      | 1                        |
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.000Z |

  Scenario: 'EqualTo' an empty string and 'ofType' integer emits null
    Given there is a field foo
    And foo is equal to ""
    And foo is of type "integer"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' an empty string and 'ofType' decimal emits null
    Given there is a field foo
    And foo is equal to ""
    And foo is of type "decimal"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' an empty string and 'ofType' datetime emits null
    Given there is a field foo
    And foo is equal to ""
    And foo is of type "datetime"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' an integer value and 'ofType' string emits null
    Given there is a field foo
    And foo is equal to 2
    And foo is of type "string"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' a date value and 'ofType' string emits null
    Given there is a field foo
    And foo is equal to 2010-01-01T00:00:00.000Z
    And foo is of type "string"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' a string value and 'ofType' integer emits null
    Given there is a field foo
    And foo is equal to "2"
    And foo is of type "integer"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' a string value and 'ofType' decimal emits null
    Given there is a field foo
    And foo is equal to "2"
    And foo is of type "decimal"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' a date value and 'ofType' integer emits null
    Given there is a field foo
    And foo is equal to 2010-01-01T00:00:00.000Z
    And foo is of type "integer"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' a date value and 'ofType' decimal emits null
    Given there is a field foo
    And foo is equal to 2010-01-01T00:00:00.000Z
    And foo is of type "decimal"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' a string value and 'ofType' datetime emits null
    Given there is a field foo
    And foo is equal to "2010-01-01T00:00:00.000"
    And foo is of type "datetime"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' an integer value and 'ofType' datetime emits null
    Given there is a field foo
    And foo is equal to 2
    And foo is of type "datetime"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' string and not 'ofType' string emits null
    Given there is a field foo
    And foo is equal to "this is a string"
    And foo is anything but of type "string"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' decimal and not 'ofType' decimal emits null
    Given there is a field foo
    And foo is equal to 1.12
    And foo is anything but of type "decimal"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' date and not 'ofType' datetime emits null
    Given there is a field foo
    And foo is equal to 2019-02-12T09:11:53.000Z
    And foo is anything but of type "datetime"
    Then the following data should be generated:
      | foo  |
      | null |

### matchingRegex ###

  Scenario: 'EqualTo' with a non-contradictory 'matchingRegex' should be successful
    Given there is a field foo
    And foo is equal to "aaa"
    And foo is matching regex /[a]{3}/
    Then the following data should be generated:
      | foo   |
      | null  |
      | "aaa" |

  Scenario: 'EqualTo' a string and not 'matchingRegex' of contradictory string should be successful
    Given there is a field foo
    And foo is equal to "a"
    And foo is anything but matching regex /[a]{3}/
    Then the following data should be generated:
      | foo  |
      | null |
      | "a"  |

  Scenario: Not 'equalTo' a value and a non-contradicting 'matchingRegex' should be successful
    Given there is a field foo
    And foo is of type "string"
    And foo is anything but equal to "aa"
    And foo is matching regex /[a]{1}/
    Then the following data should be generated:
      | foo  |
      | "a"  |
      | null |

  Scenario: 'EqualTo' number and 'matchingReqex' should be successful
    Given there is a field foo
    And foo is equal to 1
    And foo is matching regex /[a]{1}/
    Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

  Scenario: 'EqualTo' date and 'matchingReqex' should be successful
    Given there is a field foo
    And foo is equal to 2018-01-01T00:00:00.000Z
    And foo is matching regex /[a]{1}/
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2018-01-01T00:00:00.000Z |

  Scenario: 'EqualTo' string value with contradictory 'matchingRegex' emits null
    Given there is a field foo
    And foo is matching regex /[a]{3}/
    And foo is equal to "bbb"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' staring with 'matchingRegex' of contradicting length emits null
    Given there is a field foo
    And foo is equal to "a"
    And foo is matching regex /[a]{2}/
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' and not 'matchingRegex' for identical strings emits null
    Given there is a field foo
    And foo is equal to "a"
    And foo is anything but matching regex /[a]{1}/
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Not 'equalTo' and 'matchingRegex' for identical strings emits null
    Given there is a field foo
    And foo is of type "string"
    And foo is anything but equal to "a"
    And foo is matching regex /[a]{1}/
    Then the following data should be generated:
      | foo  |
      | null |

### containingReqex ###

  Scenario: 'EqualTo' with a 'containingRegex' should be successful
    Given there is a field foo
    And foo is equal to "aaa"
    And foo is containing regex /[a]{1}/
    Then the following data should be generated:
      | foo   |
      | null  |
      | "aaa" |

  Scenario: 'EqualTo' with a non-contradictory not 'containingRegex' is successful
    Given there is a field foo
    And foo is equal to "a"
    And foo is anything but containing regex /[b]{1}/
    Then the following data should be generated:
      | foo  |
      | null |
      | "a"  |

  Scenario: Not 'equalTo' with a non-contradictory 'containingRegex' is successful
    Given there is a field foo
    And foo is anything but equal to /[a]{1}/
    And foo is containing regex /[b]{1}/
    And foo is in set:
      | "a"   |
      | "ab"  |
      | "bbb" |
    Then the following data should be generated:
      | foo   |
      | null  |
      | "ab"  |
      | "bbb" |

  Scenario: 'EqualTo' a number with a non-contradictory 'containingRegex' is successful
    Given there is a field foo
    And foo is equal to 1
    And foo is containing regex /[a]{1}/
    And foo is in set:
      | 1    |
      | "1"  |
      | "a"  |
      | "1a" |
    Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

  Scenario: 'EqualTo' a date with a non-contradictory 'containingRegex' is successful
    Given there is a field foo
    And foo is equal to 2018-01-01T00:00:00.000Z
    And foo is containing regex /[a]{1}/
    And foo is in set:
      | 2018-01-01T00:00:00.000Z   |
      | "2018-01-01T00:00:00.000"  |
      | "2018-01-01T00:00:00.000a" |
      | "a"                        |
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2018-01-01T00:00:00.000Z |

  Scenario: 'EqualTo' with a contradictory 'containingRegex' emits null
    Given there is a field foo
    And foo is equal to "b"
    And foo is containing regex /[a]{1}/
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo" with a 'containingReqex of contradictory length emits null
    Given there is a field foo
    And foo is equal to "a"
    And foo is containing regex /[a]{2}/
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' with a contradictory not 'containingRegex' emits null
    Given there is a field foo
    And foo is equal to "a"
    And foo is anything but containing regex /[a]{1}/
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Not 'equalTo' with a contradictory 'containingRegex' emits null
    Given there is a field foo
    And foo is anything but equal to "a"
    And foo is containing regex /[a]{1}/
    And foo is in set:
      | "Aa" |
      | "a"  |
      | "aa" |
    Then the following data should be generated:
      | foo  |
      | null |
      | "Aa" |
      | "aa" |

### ofLength ###

  Scenario: 'EqualTo' alongside a non-contradicting 'ofLength' constraint should be successful
    Given there is a field foo
    And foo is equal to "1"
    And foo is of length 1
    Then the following data should be generated:
      | foo  |
      | null |
      | "1"  |

  Scenario: 'EqualTo' with a non-contradictory not 'ofLength' is successful
    Given there is a field foo
    And foo is equal to "a"
    And foo is anything but of length 2
    Then the following data should be generated:
      | foo  |
      | null |
      | "a"  |

  Scenario: Not 'equalTo' with a non-contradictory 'ofLength' is successful
    Given there is a field foo
    And foo is anything but equal to "a"
    And foo is of length 2
    And foo is in set:
      | "a"  |
      | "b"  |
      | "ab" |
    Then the following data should be generated:
      | foo  |
      | null |
      | "ab" |

  Scenario Outline: 'EqualTo' a non-string type with an 'ofLength' zero is successful
    Given there is a field foo
    And foo is equal to <value>
    And foo is of length 0
    Then the following data should be generated:
      | foo     |
      | null    |
      | <value> |
    Examples:
      | value                    |
      | 1                        |
      | 2018-01-01T00:00:00.000Z |

  Scenario: 'EqualTo' request alongside a contradicting 'ofLength' constraint emits null
    Given there is a field foo
    And foo is equal to "22"
    And foo is of length 1
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' with a contradictory not 'ofLength' constraint emits null
    Given there is a field foo
    And foo is equal to "a"
    And foo is anything but of length 1
    Then the following data should be generated:
      | foo  |
      | null |

### longerThan ###

  Scenario: 'EqualTo' run against a non contradicting 'longerThan' should be successful
    Given there is a field foo
    And foo is equal to "aa"
    And foo is longer than 1
    Then the following data should be generated:
      | foo  |
      | null |
      | "aa" |

  Scenario: 'EqualTo' run against a non contradicting not 'longerThan' should be successful
    Given there is a field foo
    And foo is equal to "a"
    And foo is anything but longer than 2
    Then the following data should be generated:
      | foo  |
      | null |
      | "a"  |

  Scenario: Not 'EqualTo' run against a non contradicting 'longerThan' should be successful
    Given there is a field foo
    And foo is anything but equal to "a"
    And foo is longer than 2
    And foo is in set:
      | "aaa" |
      | "aA"  |
      | "a"   |
      | 222   |
    Then the following data should be generated:
      | foo   |
      | null  |
      | "aaa" |
      | 222   |

  Scenario Outline: 'EqualTo' non string value run against a non contradicting 'longerThan' should be successful
    Given there is a field foo
    And foo is equal to <value>
    And foo is longer than 100
    Then the following data should be generated:
      | foo     |
      | null    |
      | <value> |
    Examples:
      | value                    |
      | 1                        |
      | 2018-01-01T00:00:00.000Z |

  Scenario: 'EqualTo' run against a contradicting 'longerThan' should only generate null
    Given there is a field foo
    And foo is equal to "a"
    And foo is longer than 2
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' run against a contradicting not 'longerThan' should only generate null
    Given there is a field foo
    And foo is equal to "aa"
    And foo is anything but longer than 1
    Then the following data should be generated:
      | foo  |
      | null |

### shorterThan ###

  Scenario: 'EqualTo' run against a non contradicting 'shorterThan' should be successful
    Given there is a field foo
    And foo is equal to "1234"
    And foo is shorter than 5
    Then the following data should be generated:
      | foo    |
      | null   |
      | "1234" |

  Scenario: 'EqualTo' run against a non contradicting not 'shorterThan' should be successful
    Given there is a field foo
    And foo is equal to "aa"
    And foo is anything but shorter than 1
    Then the following data should be generated:
      | foo  |
      | null |
      | "aa" |

  Scenario: Not 'equalTo' run against a non contradicting 'shorterThan' should be successful
    Given there is a field foo
    And foo is anything but equal to "a"
    And foo is shorter than 2
    And foo is in set:
      | 1    |
      | "a"  |
      | "ab" |
      | "b"  |
    Then the following data should be generated:
      | foo  |
      | null |
      | 1    |
      | "b"  |

  Scenario Outline: 'EqualTo' a value of a different type with a non contradicting 'shorterThan' should be successful
    Given there is a field foo
    And foo is equal to <value>
    And foo is shorter than 2
    Then the following data should be generated:
      | foo     |
      | null    |
      | <value> |
    Examples:
      | value                    |
      | 1                        |
      | 2018-01-01T00:00:00.000Z |

  Scenario: 'EqualTo' run against a contradicting 'shorterThan' should only generate null
    Given there is a field foo
    And foo is equal to "aa"
    And foo is shorter than 2
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' run against a contradicting not 'shorterThan' should only generate null
    Given there is a field foo
    And foo is equal to "a"
    And foo is anything but shorter than 2
    Then the following data should be generated:
      | foo  |
      | null |

### aValid ###

  Scenario: 'equalTo' with a non-contradicting 'aValid' constraint should be successful
    Given there is a field foo
    And foo is equal to "GB0002634946"
    And foo is of type "ISIN"
    Then the following data should be generated:
      | foo            |
      | null           |
      | "GB0002634946" |

  Scenario: 'EqualTo' run against a non contradicting not 'aValid' ISIN should be successful
    Given there is a field foo
    And foo is equal to "a"
    And foo is anything but of type "ISIN"
    Then the following data should be generated:
      | foo  |
      | null |
      | "a"  |

  Scenario: Not 'equalTo' run against a non contradicting 'aValid' ISIN should be successful
    Given there is a field foo
    And foo is anything but equal to "a"
    And foo is of type "ISIN"
    And foo is in set:
      | "a"            |
      | "GB0002634946" |
      | "G40002634946" |
    Then the following data should be generated:
      | foo            |
      | null           |
      | "GB0002634946" |

  Scenario: 'EqualTo' an invalid ISIN with 'aValid' should emit null
    Given there is a field foo
    And foo is equal to "GB00026349"
    And foo is of type "ISIN"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' run against a contradicting 'aValid' ISIN should only generate null
    Given there is a field foo
    And foo is equal to "aa"
    And foo is of type "ISIN"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' a valid ISIN with a contradicting not 'aValid' ISIN emits null
    Given there is a field foo
    And foo is equal to "GB00YG2XYC52"
    And foo is anything but of type "ISIN"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' run against a non contradicting not 'aValid' SEDOL should be successful
    Given there is a field foo
    And foo is equal to "a"
    And foo is anything but of type "SEDOL"
    Then the following data should be generated:
      | foo  |
      | null |
      | "a"  |

  Scenario: Not 'equalTo' run against a non contradicting 'aValid' SEDOL should be successful
    Given there is a field foo
    And foo is anything but equal to "a"
    And foo is of type "SEDOL"
    And foo is in set:
      | "a"       |
      | "0263494" |
      | "0263497" |
    Then the following data should be generated:
      | foo       |
      | null      |
      | "0263494" |

  Scenario: 'EqualTo' an invalid SEDOL with 'aValid' should emit null
    Given there is a field foo
    And foo is equal to "0263497"
    And foo is of type "SEDOL"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' run against a contradicting 'aValid' SEDOL should only generate null
    Given there is a field foo
    And foo is equal to "aa"
    And foo is of type "SEDOL"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' a valid SEDOL with a contradicting not 'aValid' SEDOL emits null
    Given there is a field foo
    And foo is equal to "0263494"
    And foo is anything but of type "SEDOL"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' run against a non contradicting not 'aValid' CUSIP should be successful
    Given there is a field foo
    And foo is equal to "a"
    And foo is anything but of type "CUSIP"
    Then the following data should be generated:
      | foo  |
      | null |
      | "a"  |

  Scenario: Not 'equalTo' run against a non contradicting 'aValid' CUSIP should be successful
    Given there is a field foo
    And foo is anything but equal to "a"
    And foo is of type "CUSIP"
    And foo is in set:
      | "a"         |
      | "38259P508" |
      | "38259P502" |
    Then the following data should be generated:
      | foo         |
      | null        |
      | "38259P508" |

  Scenario: 'EqualTo' an invalid CUSIP with 'aValid' should emit null
    Given there is a field foo
    And foo is equal to "38259P502"
    And foo is of type "CUSIP"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' run against a contradicting 'aValid' CUSIP should only generate null
    Given there is a field foo
    And foo is equal to "aa"
    And foo is of type "CUSIP"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' a valid CUSIP with a contradicting not 'aValid' CUSIP emits null
    Given there is a field foo
    And foo is equal to "38259P508"
    And foo is anything but of type "CUSIP"
    Then the following data should be generated:
      | foo  |
      | null |

### greaterThan ###

  Scenario: 'EqualTo' run against a non contradicting 'greaterThan' should be successful
    Given there is a field foo
    And foo is equal to 2
    And foo is greater than 1
    Then the following data should be generated:
      | foo  |
      | null |
      | 2    |

  Scenario: 'EqualTo' run against a non contradicting not 'greaterThan' should be successful
    Given there is a field foo
    And foo is equal to 1
    And foo is anything but greater than 1
    Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

  Scenario: Not 'equalTo' run against a non contradicting 'greaterThan' should be successful
    Given there is a field foo
    And foo is anything but equal to 1
    And foo is greater than 1
    And foo is in set:
      | 1   |
      | "1" |
      | 1.1 |
      | -1  |
      | 2   |
    Then the following data should be generated:
      | foo  |
      | null |
      | "1"  |
      | 1.1  |
      | 2    |

  Scenario Outline: 'EqualTo' value of a different type with a 'shorterThan' is successful
    Given there is a field foo
    And foo is equal to <value>
    And foo is shorter than 100
    Then the following data should be generated:
      | foo     |
      | null    |
      | <value> |
    Examples:
      | value                    |
      | "a"                      |
      | 2018-01-01T00:00:00.000Z |

  Scenario: 'EqualTo' run against a contradicting 'greaterThan' should only generate null
    Given there is a field foo
    And foo is equal to 1
    And foo is greater than 2
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' run against a contradicting not 'greaterThan' should generate null
    Given there is a field foo
    And foo is equal to 2
    And foo is anything but greater than 1
    Then the following data should be generated:
      | foo  |
      | null |

### greaterThanOrEqualTo ###

  Scenario: 'EqualTo' run against a non contradicting 'greaterThanOrEqualToOrEqualTo' should be successful
    Given there is a field foo
    And foo is equal to 1
    And foo is greater than or equal to 1
    Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

  Scenario: 'EqualTo' run against a non contradicting not 'greaterThanOrEqualToOrEqualTo' should be successful
    Given there is a field foo
    And foo is equal to 1
    And foo is anything but greater than or equal to 2
    Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

  Scenario: Not 'equalTo' run against a non contradicting 'greaterThanOrEqualToOrEqualTo' should be successful
    Given there is a field foo
    And foo is anything but equal to 1
    And foo is greater than or equal to 2
    And foo is in set:
      | 1         |
      | 2         |
      | "1"       |
      | 1.9999999 |
    Then the following data should be generated:
      | foo  |
      | null |
      | 2    |
      | "1"  |

  Scenario Outline: 'EqualTo' a value of a non-numeric type with 'shorterThan' should be successful
    Given there is a field foo
    And foo is equal to <value>
    And foo is greater than or equal to 10
    Then the following data should be generated:
      | foo     |
      | null    |
      | <value> |
    Examples:
      | value                    |
      | "abcdefghijklm"          |
      | 2018-01-01T00:00:00.000Z |

  Scenario: 'EqualTo' run against a contradicting 'greaterThanOrEqualToOrEqualTo' should only generate null
    Given there is a field foo
    And foo is equal to 1
    And foo is greater than or equal to 2
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' run against a contradicting not 'greaterThanOrEqualToOrEqualTo' should only generate null
    Given there is a field foo
    And foo is equal to 2
    And foo is anything but greater than or equal to 2
    Then the following data should be generated:
      | foo  |
      | null |

### lessThan ###

  Scenario: 'EqualTo' run against a non contradicting 'lessThan' should be successful
    Given there is a field foo
    And foo is equal to 1
    And foo is less than 2
    Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

  Scenario: 'EqualTo' run against a non contradicting not 'lessThan' should be successful
    Given there is a field foo
    And foo is equal to 2
    And foo is anything but less than 2
    Then the following data should be generated:
      | foo  |
      | null |
      | 2    |

  Scenario: Not 'equalTo' run against a non contradicting 'lessThan' should be successful
    Given there is a field foo
    And foo is anything but equal to 2
    And foo is less than 2
    And foo is in set:
      | 2      |
      | 1.9999 |
      | 0      |
      | -1     |
    Then the following data should be generated:
      | foo    |
      | null   |
      | 1.9999 |
      | 0      |
      | -1     |

  Scenario Outline: 'EqualTo' a non-numeric value with 'lessThan' should be successful
    Given there is a field foo
    And foo is equal to <value>
    And foo is less than 1
    Then the following data should be generated:
      | foo     |
      | null    |
      | <value> |
    Examples:
      | value                    |
      | "a"                      |
      | 2018-01-01T00:00:00.000Z |

  Scenario: 'EqualTo' run against a contradicting 'lessThan' should only generate null
    Given there is a field foo
    And foo is equal to 3
    And foo is less than 3
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' run against a contradicting not 'lessThan' should only generate null
    Given there is a field foo
    And foo is equal to 1
    And foo is anything but less than 2
    Then the following data should be generated:
      | foo  |
      | null |

### lessThanOrEqualTo ###

  Scenario: 'EqualTo' run against a non contradicting 'lessThanOrEqualTo' is successful
    Given there is a field foo
    And foo is equal to 1
    And foo is less than or equal to 2
    Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

  Scenario: 'EqualTo' run against a non contradicting not 'lessThanOrEqualTo' is successful
    Given there is a field foo
    And foo is equal to 2
    And foo is anything but less than or equal to 1
    Then the following data should be generated:
      | foo  |
      | null |
      | 2    |

  Scenario: Not 'equalTo' run against a non contradicting 'lessThanOrEqualTo' is successful
    Given there is a field foo
    And foo is anything but equal to 3
    And foo is less than or equal to 2
    And foo is in set:
      | -1       |
      | 0        |
      | 2        |
      | 2.000001 |
      | 3        |
    Then the following data should be generated:
      | foo  |
      | null |
      | -1   |
      | 0    |
      | 2    |

  Scenario Outline: 'EqualTo' a non-numeric value with 'lessThanOrEqualTo' should be successful
    Given there is a field foo
    And foo is equal to <value>
    And foo is less than or equal to 1
    Then the following data should be generated:
      | foo     |
      | null    |
      | <value> |
    Examples:
      | value                    |
      | "a"                      |
      | 2018-01-01T00:00:00.000Z |

  Scenario: 'EqualTo' run against a contradicting 'lessThanOrEqualTo' should only generate null
    Given there is a field foo
    And foo is equal to 3
    And foo is less than or equal to 2
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' run against a contradicting not 'lessThanOrEqualTo' should only generate null
    Given there is a field foo
    And foo is equal to 1
    And foo is anything but less than or equal to 1
    Then the following data should be generated:
      | foo  |
      | null |

### granularTo ###


  Scenario: 'EqualTo' run against a non contradicting 'granularTo' should be successful
    Given there is a field foo
    And foo is equal to 1
    And foo is granular to 1
    Then the following data should be generated:
      | foo  |
      | null |
      | 1    |

  Scenario: 'EqualTo' run against a non contradicting not 'granularTo' should be successful
    Given there is a field foo
    And foo is equal to 1.1
    And foo is anything but granular to 1
    Then the following data should be generated:
      | foo  |
      | null |
      | 1.1  |

  Scenario: Not 'equalTo' run against a non contradicting 'granularTo' should' should be successful
    Given there is a field foo
    And foo is anything but equal to 1.1
    And foo is granular to 1
    And foo is in set:
      | 1.1 |
      | 1.2 |
      | 1.0 |
      | 2   |
      | 0   |
    Then the following data should be generated:
      | foo  |
      | null |
      | 1.0  |
      | 2    |
      | 0    |

  Scenario Outline: 'EqualTo' a non-numeric value with 'granularTo' should be successful
    Given there is a field foo
    And foo is equal to <value>
    And foo is granular to 1
    Then the following data should be generated:
      | foo     |
      | null    |
      | <value> |
    Examples:
      | value                    |
      | "a"                      |
      | 2018-01-01T00:00:00.000Z |

  Scenario: 'EqualTo' run against a contradicting 'granularTo' should only generate null
    Given there is a field foo
    And foo is equal to 1.1
    And foo is granular to 1
    Then the following data should be generated:
      | foo  |
      | null |

  @ignore #769 Violation of numeric and temporal granularity
  Scenario: 'EqualTo' run against a contradicting not 'granularTo' should only generate null
    Given there is a field foo
    And foo is equal to 1
    And foo is anything but granular to 1
    Then the following data should be generated:
      | foo  |
      | null |

### after ###

  Scenario: 'EqualTo' run against a non contradicting 'after' should be successful
    Given there is a field foo
    And foo is equal to 2019-01-01T00:00:00.000Z
    And foo is after 2018-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.000Z |

  Scenario: 'EqualTo' run against a non contradicting not 'after' should be successful
    Given there is a field foo
    And foo is equal to 2019-01-01T00:00:00.000Z
    And foo is anything but after 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.000Z |

  Scenario: Not 'equalTo' run against a non contradicting 'after' should be successful
    Given there is a field foo
    And foo is of type "datetime"
    And foo is anything but equal to 2019-01-01T00:00:00.002Z
    And foo is after 2019-01-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.001Z |
      | 2019-01-01T00:00:00.003Z |
      | 2019-01-01T00:00:00.004Z |
      | 2019-01-01T00:00:00.005Z |
      | 2019-01-01T00:00:00.006Z |

  Scenario Outline: 'EqualTo' a non-datetime value with 'after' should be successful
    Given there is a field foo
    And foo is equal to <value>
    And foo is after 2018-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo     |
      | null    |
      | <value> |
    Examples:
      | value |
      | "a"   |
      | 1     |

  Scenario: 'EqualTo' run against a contradicting 'after' should only generate null
    Given there is a field foo
    And foo is equal to 2019-01-01T00:00:00.000Z
    And foo is after 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' run against a contradicting not 'after' should only generate null
    Given there is a field foo
    And foo is equal to 2019-01-01T00:00:00.001Z
    And foo is anything but after 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

### afterOrAt ###
  Scenario: 'EqualTo' run against a non contradicting 'afterOrAt' should be successful
    Given there is a field foo
    And foo is equal to 2019-01-01T00:00:00.000Z
    And foo is after or at 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.000Z |

  Scenario: 'EqualTo' run against a non contradicting not 'afterOrAt' should be successful
    Given there is a field foo
    And foo is equal to 2019-01-01T00:00:00.000Z
    And foo is anything but after or at 2019-01-01T00:00:00.001Z
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.000Z |

  Scenario: Not 'equalTo' run against a non contradicting 'afterOrAt' should only be successful
    Given there is a field foo
    And foo is of type "datetime"
    And foo is anything but equal to 2019-01-01T00:00:00.000Z
    And foo is after or at 2019-01-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.001Z |
      | 2019-01-01T00:00:00.002Z |
      | 2019-01-01T00:00:00.003Z |
      | 2019-01-01T00:00:00.004Z |
      | 2019-01-01T00:00:00.005Z |

  Scenario Outline: 'EqualTo' to a non-datetime value with 'afterOrAt' should be successful
    Given there is a field foo
    And foo is equal to <value>
    And foo is after or at 2018-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo     |
      | null    |
      | <value> |
    Examples:
      | value |
      | "a"   |
      | 1     |

  Scenario: 'EqualTo' run against a contradicting 'afterOrAt' should only generate null
    Given there is a field foo
    And foo is equal to 2019-01-01T00:00:00.000Z
    And foo is after or at 2019-01-01T00:00:00.001Z
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' run against a contradicting not 'afterOrAt' should only generate null
    Given there is a field foo
    And foo is equal to 2019-01-01T00:00:00.000Z
    And foo is anything but after or at 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

### before ###

  Scenario: 'EqualTo' run against a non contradicting 'before' should be successful
    Given there is a field foo
    And foo is equal to 2019-01-01T00:00:00.000Z
    And foo is before 2019-01-01T00:00:00.001Z
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.000Z |

  Scenario: 'EqualTo' run against a non contradicting not 'before' should be successful
    Given there is a field foo
    And foo is equal to 2019-01-01T00:00:00.000Z
    And foo is anything but before 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.000Z |

  @ignore #594 As a user I expect values to be emitted in descending order
  Scenario: Not 'equalTo' run against a non contradicting 'before' should be successful
    Given there is a field foo
    And foo is of type "datetime"
    And foo is anything but equal to 2018-12-31T23:59:59.998Z
    And foo is before 2019-01-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 2018-12-31T23:59:59.999Z |
      | 2018-12-31T23:59:59.997Z |
      | 2018-12-31T23:59:59.996Z |
      | 2018-12-31T23:59:59.995Z |
      | 2018-12-31T23:59:59.994Z |

  Scenario Outline: 'EqualTo' a non-datetime value with 'before' should be successful
    Given there is a field foo
    And foo is equal to <value>
    And foo is before 2018-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo     |
      | null    |
      | <value> |
    Examples:
      | value |
      | "a"   |
      | 1     |

  Scenario: 'EqualTo' run against a contradicting 'before' should only generate null
    Given there is a field foo
    And foo is equal to 2019-01-01T00:00:00.000Z
    And foo is before 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' run against a contradicting not 'before' should only generate null
    Given there is a field foo
    And foo is equal to 2019-01-01T00:00:00.000Z
    And foo is anything but before 2019-01-01T00:00:00.001Z
    Then the following data should be generated:
      | foo  |
      | null |

### beforeOrAt ###

  Scenario: 'EqualTo' run against a non contradicting 'beforeOrAt' should be successful
    Given there is a field foo
    And foo is equal to 2019-01-01T00:00:00.000Z
    And foo is before or at 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.000Z |

  Scenario: 'EqualTo' run against a non contradicting not 'beforeOrAt' should be successful
    Given there is a field foo
    And foo is equal to 2019-01-01T00:00:00.001Z
    And foo is anything but before or at 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.001Z |

  @ignore #594 As a user I expect values to be emitted in descending order
  Scenario: Not 'equalTo' run against a non contradicting 'beforeOrAt' should be successful
    Given there is a field foo
    And foo is of type "datetime"
    And foo is anything but equal to 2019-01-01T00:00:00.000Z
    And foo is before or at 2019-01-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 2018-12-31T23:59:59.999Z |
      | 2018-12-31T23:59:59.997Z |
      | 2018-12-31T23:59:59.996Z |
      | 2018-12-31T23:59:59.995Z |
      | 2018-12-31T23:59:59.995Z |

  Scenario Outline: 'EqualTo' a non-datetime value with 'beforeOrAt' should be successful
    Given there is a field foo
    And foo is equal to <value>
    And foo is before 2018-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo     |
      | null    |
      | <value> |
    Examples:
      | value |
      | "a"   |
      | 1     |

  Scenario: 'EqualTo' run against a contradicting 'beforeOrAt' should only generate null
    Given there is a field foo
    And foo is equal to 2019-01-01T00:00:00.001Z
    And foo is before or at 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' run against a contradicting not 'beforeOrAt' should only generate null
    Given there is a field foo
    And foo is equal to 2019-01-01T00:00:00.000Z
    And foo is anything but before or at 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'EqualTo' request including a string of the maximum length should be successful
    Given there is a field foo
    And the maximum string length is 1000
    And foo is equal to "I am 1000 chars long   jdny97XhjJE0ywt6mRMfYj1ECoNufcF3Dy2DStFmnLVHH5GcfLtLTXEG34LNgTxPvmAqYL6UCWiia23IqmzrooICtND1UtSbrsDOhQeVjNUjTNMsin6AO5oSOiLkpU0h4hctiKKg8IoZ05TrRyl8ZBg99S986vM737sSUxUv3yKj8lPOMH5ZjrgAn52D2LerAlBRvcQMoYP5mnuPidtCHT6RrHMJX44nHFeMJS6371dHMC9bDqjJRrMsnu1DWc7kUkttSPioKZbR1BDUn5s1WTM5brzWv9bgWvtFhjzHYdhMY0bxq1qXksGzAqaOkcbbUh6bCirz6N4nAt4I2aQccMQqCp5TjXAFGMLxbRO7uttWZI8GRWiXP2joA9aTw7K8Fk5rllWbGfgFHSlMHYmeGGRF8ig10LgkeVDdP7tVHyGr4O6nKV3TB61UJaHCRZUIoyPuce3SWeckv835iwVrKy9PIC5D42HBd3431GIyMy7sxpR4pWs7djW6UxhdnTC3q2MlX0aMXjDrLCAjybo89q7qJw4eEPfR2cwuc1xvSiC2RoVVlBprmLkKiDeCZPRZxxVn9QwzvPNnRsjx9nFenwfPIDf1C6MbQ22aYmxqcnxQky1gLLdPRWVYpgqzeztnBziahVuZZLob5EvFjgv5HmKnfg3DUrU2Em61l9nE0L6IYiz9xrZ0kmiDSB44cEOoubhJUwihD7PrM92pmCKXoWouigS6LSlCIX8OkQxaHRA0m2FYgtYV0H9rkK0kQfflvlF3zd7TvSjW1NGRxzjh5jGNfvkl9M9O5tpvieoM55uPi2fY9f8ZD2Eq0KjEHEcKtLNWnxdpuIVa7mzByWqkawwrhdjH0qF4RwXsGbTHhrNT7SFyBs4h1MdKEkUlrXgGlXXtSo104KsMv5qWIXRI221jjfwZZ7nl1XLSSOqLhDoWdvgiR0XPPwvLtPMBWiwqW86upHDMMcPAYKCnP"
    Then the following data should be generated:
      | foo                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
      | null                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
      | "I am 1000 chars long   jdny97XhjJE0ywt6mRMfYj1ECoNufcF3Dy2DStFmnLVHH5GcfLtLTXEG34LNgTxPvmAqYL6UCWiia23IqmzrooICtND1UtSbrsDOhQeVjNUjTNMsin6AO5oSOiLkpU0h4hctiKKg8IoZ05TrRyl8ZBg99S986vM737sSUxUv3yKj8lPOMH5ZjrgAn52D2LerAlBRvcQMoYP5mnuPidtCHT6RrHMJX44nHFeMJS6371dHMC9bDqjJRrMsnu1DWc7kUkttSPioKZbR1BDUn5s1WTM5brzWv9bgWvtFhjzHYdhMY0bxq1qXksGzAqaOkcbbUh6bCirz6N4nAt4I2aQccMQqCp5TjXAFGMLxbRO7uttWZI8GRWiXP2joA9aTw7K8Fk5rllWbGfgFHSlMHYmeGGRF8ig10LgkeVDdP7tVHyGr4O6nKV3TB61UJaHCRZUIoyPuce3SWeckv835iwVrKy9PIC5D42HBd3431GIyMy7sxpR4pWs7djW6UxhdnTC3q2MlX0aMXjDrLCAjybo89q7qJw4eEPfR2cwuc1xvSiC2RoVVlBprmLkKiDeCZPRZxxVn9QwzvPNnRsjx9nFenwfPIDf1C6MbQ22aYmxqcnxQky1gLLdPRWVYpgqzeztnBziahVuZZLob5EvFjgv5HmKnfg3DUrU2Em61l9nE0L6IYiz9xrZ0kmiDSB44cEOoubhJUwihD7PrM92pmCKXoWouigS6LSlCIX8OkQxaHRA0m2FYgtYV0H9rkK0kQfflvlF3zd7TvSjW1NGRxzjh5jGNfvkl9M9O5tpvieoM55uPi2fY9f8ZD2Eq0KjEHEcKtLNWnxdpuIVa7mzByWqkawwrhdjH0qF4RwXsGbTHhrNT7SFyBs4h1MdKEkUlrXgGlXXtSo104KsMv5qWIXRI221jjfwZZ7nl1XLSSOqLhDoWdvgiR0XPPwvLtPMBWiwqW86upHDMMcPAYKCnP" |

  Scenario: 'EqualTo' request including a string over the maximum length should throw an error
    Given there is a field foo
    And the maximum string length is 1000
    And foo is equal to "I am 1001 chars long    dny97XhjJE0ywt6mRMfYj1ECoNufcF3Dy2DStFmnLVHH5GcfLtLTXEG34LNgTxPvmAqYL6UCWiia23IqmzrooICtND1UtSbrsDOhQeVjNUjTNMsin6AO5oSOiLkpU0h4hctiKKg8IoZ05TrRyl8ZBg99S986vM737sSUxUv3yKj8lPOMH5ZjrgAn52D2LerAlBRvcQMoYP5mnuPidtCHT6RrHMJX44nHFeMJS6371dHMC9bDqjJRrMsnu1DWc7kUkttSPioKZbR1BDUn5s1WTM5brzWv9bgWvtFhjzHYdhMY0bxq1qXksGzAqaOkcbbUh6bCirz6N4nAt4I2aQccMQqCp5TjXAFGMLxbRO7uttWZI8GRWiXP2joA9aTw7K8Fk5rllWbGfgFHSlMHYmeGGRF8ig10LgkeVDdP7tVHyGr4O6nKV3TB61UJaHCRZUIoyPuce3SWeckv835iwVrKy9PIC5D42HBd3431GIyMy7sxpR4pWs7djW6UxhdnTC3q2MlX0aMXjDrLCAjybo89q7qJw4eEPfR2cwuc1xvSiC2RoVVlBprmLkKiDeCZPRZxxVn9QwzvPNnRsjx9nFenwfPIDf1C6MbQ22aYmxqcnxQky1gLLdPRWVYpgqzeztnBziahVuZZLob5EvFjgv5HmKnfg3DUrU2Em61l9nE0L6IYiz9xrZ0kmiDSB44cEOoubhJUwihD7PrM92pmCKXoWouigS6LSlCIX8OkQxaHRA0m2FYgtYV0H9rkK0kQfflvlF3zd7TvSjW1NGRxzjh5jGNfvkl9M9O5tpvieoM55uPi2fY9f8ZD2Eq0KjEHEcKtLNWnxdpuIVa7mzByWqkawwrhdjH0qF4RwXsGbTHhrNT7SFyBs4h1MdKEkUlrXgGlXXtSo104KsMv5qWIXRI221jjfwZZ7nl1XLSSOqLhDoWdvgiR0XPPwvLtPMBWiwqW86upHDMMcPAYKCnPe"
    Then the profile is invalid because "Field \[foo\]: set contains a string longer than maximum permitted length, was: 1001, max-length: 1000"
