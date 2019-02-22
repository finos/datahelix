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

Scenario: Running an 'inSet' request that includes strings with both roman alphabet lowercase (a-z) and uppercase (A-Z) should be successful
     Given there is a field foo
       And foo is in set:
       | "aAbB" |
       | "AaBb" |
     Then the following data should be generated:
       | foo    |
       | null   |
       | "aAbB" |
       | "AaBb" |

Scenario: Running an 'inSet' request that includes strings with both roman alphabet (a-z, A-Z)and numeric chars (0-9) should be successful
     Given there is a field foo
       And foo is in set:
       | "Testing01" |
       | "Testing02" |
     Then the following data should be generated:
       |  foo        |
       | null        |
       | "Testing01" |
       | "Testing02" |

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
     Then the following data should be generated:
       |  foo        |
       | null        |
       | "true"      |
       | "false"     |
       | "null"      |
       | "undefined" |

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
       | "] [] [] ["    |
     Then the following data should be generated:
       |  foo           |
       | null           |
       | "]	[] [] [] [" |
       | "] [] [] ["    |

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

Scenario: Running an 'inSet' request that includes strings with special characters (standard) alongside roman alphanumeric characters should be successful
     Given there is a field foo
       And foo is in set:
       | "£1.00"        |
       | "$5.00"        |
       | "Testing (01)" |
     Then the following data should be generated:
       | foo            |
       | null           |
       | "£1.00"        |
       | "$5.00"        |
       | "Testing (01)" |

Scenario: Running an 'inSet' request that includes strings with special characters (white spaces) alongside roman alphanumeric characters should be successful
     Given there is a field foo
       And foo is in set:
         | "Test	One" |
         | "Test 02"   |
         | "Test Three" |
     Then the following data should be generated:
       | foo         |
       | null        |
       | "Test	One" |
       | "Test 02"   |
       | "Test Three" |

Scenario: Running an 'inSet' request that includes strings with special characters (unicode symbols) alongside roman alphanumeric characters should be successful
     Given there is a field foo
       And foo is in set:
       | "I will display ♠"           |
       | "★ I will display ★"        |
       | "♞♟♜⚣ Displaying symbols" |
     Then the following data should be generated:
       | foo                          |
       | null                         |
       | "I will display ♠"           |
       | "★ I will display ★"        |
       | "♞♟♜⚣ Displaying symbols" |

Scenario: Running an 'inSet' request that includes strings with special characters (emoji) alongside roman alphanumeric characters should be successful
     Given there is a field foo
       And foo is in set:
       | "Test 01 has passed 😃"  |
       | "❤ Test 02 ❤"          |
       | "Passing Tests ☑"       |
     Then the following data should be generated:
       | foo                      |
       | null                     |
       | "Test 01 has passed 😃"  |
       | "❤ Test 02 ❤"          |
       | "Passing Tests ☑"       |

Scenario: Running an 'inSet' request that includes strings with special characters (non roman character maps) alongside roman alphanumeric characters should be successful
     Given there is a field foo
       And foo is in set:
       | "Cyrillic text: Тхис ис Тест Нумбер 01" |
       | "Japanese text: これはテスト番号2です"   |
       | "Korean text: 이것은 시험 번호 3입니다"  |
     Then the following data should be generated:
       | foo                                     |
       | null                                    |
       | "Cyrillic text: Тхис ис Тест Нумбер 01" |
       | "Japanese text: これはテスト番号2です"   |
       | "Korean text: 이것은 시험 번호 3입니다"   |

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
       | "001"                  |
       | "010"                  |
       | "01.00"                |
       | "000000000.0000000001" |
     Then the following data should be generated:
       | foo                    |
       | null                   |
       | "001"                  |
       | "010"                  |
       | "01.00"                |
       | "000000000.0000000001" |

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

Scenario: Running an 'inSet' request that includes roman character strings that include in-use numeric values should be successful
     Given there is a field foo
       And foo is in set:
       | "Infinity" |
       | "NaN"      |
       | "nil"      |
     Then the following data should be generated:
       | foo        |
       | null       |
       | "Infinity" |
       | "NaN"      |
       | "nil"      |

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

Scenario: Running an 'inSet' request that includes a date value (not a string) should be successful
     Given there is a field foo
       And foo is in set:
       | 2010-01-01T00:00:00.000 |
       | 2010-01-01T00:00:00.001 |
       | 2011-01-01T00:00:00.000 |
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 2010-01-01T00:00:00.000 |
       | 2010-01-01T00:00:00.001 |
       | 2011-01-01T00:00:00.000 |

Scenario: Running an 'inSet' request that includes a date value (leap year) should be successful
     Given there is a field foo
       And foo is in set:
       | 2020-02-29T00:00:00.000 |
       | 2016-02-29T00:00:00.000 |
       | 2012-02-29T00:00:00.000 |
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 2020-02-29T00:00:00.000 |
       | 2016-02-29T00:00:00.000 |
       | 2012-02-29T00:00:00.000 |

Scenario: Running an 'inSet' request that includes a date value (system epoch dates) should be successful
     Given there is a field foo
       And foo is in set:
       | 0001-01-01T00:00:01.000 |
       | 9999-12-31T23:59:59.999 |
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 0001-01-01T00:00:01.000 |
       | 9999-12-31T23:59:59.999 |

Scenario: Running an 'inSet' request that includes an invalid date value should fail with an error message
     Given there is a field foo
       And foo is in set:
       | 2010-13-40T00:00:00.000 |
       | 2017-12-31T23:59:59.999 |
     Then I am presented with an error message
       And no data is created

Scenario: Running an 'inSet' request that includes an invalid time value should fail with an error message
     Given there is a field foo
       And foo is in set:
       | 2017-12-31T40:59:59.999 |
       | 2017-12-31T23:59:59.999 |
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
     Then I am presented with an error message
       And no data is created

Scenario: Running an 'inSet' request that includes strings, numeric and temporal fields should be successful.
     Given there is a field foo
       And foo is in set:
       | 1                       |
       | 2010-01-01T00:00:00.000 |
       | "String!"               |
     Then the following data should be generated:
       | foo                     |
       | 1                       |
       | 2010-01-01T00:00:00.000 |
       | "String!"               |
       | null                    |

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

Scenario: Running a 'inSet' request alongside a non-contradicting equalTo constraint should be successful
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

Scenario: Running a 'inSet' request alongside a contradicting equalTo constraint should produce null
     Given there is a field foo
       And foo is in set:
       | "Test 1" |
       | "Test 2" |
       | "Test 3" |
       And foo is equal to "Test 4"
     Then the following data should be generated:
       | foo  |
       | null |

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

### null ###

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

Scenario: Running a 'inSet' request alongside an ofType = numeric should be successful
     Given there is a field foo
       And foo is in set:
       | 1 |
       | 2 |
       | 3 |
       And foo is of type "numeric"
     Then the following data should be generated:
       | foo  |
       | null |
       | 1    |
       | 2    |
       | 3    |

Scenario: Running a 'inSet' request alongside a contradicting ofType = numeric should produce null
     Given there is a field foo
       And foo is in set:
       | "1" |
       | "2" |
       | "3" |
       And foo is of type "numeric"
     Then the following data should be generated:
       | foo  |
       | null |

Scenario: Running a 'inSet' request alongside an ofType = temporal should be successful
     Given there is a field foo
       And foo is in set:
       | 2010-01-01T00:00:00.000 |
       | 2010-01-01T00:00:00.001 |
       | 2011-01-01T00:00:00.000 |
       And foo is of type "temporal"
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 2010-01-01T00:00:00.000 |
       | 2010-01-01T00:00:00.001 |
       | 2011-01-01T00:00:00.000 |

Scenario: Running a 'inSet' request alongside a contradicting ofType = temporal should produce null
     Given there is a field foo
       And foo is in set:
       | "2010-01-01T00:00:00.000" |
       | "2010-01-01T00:00:00.001" |
       | "2011-01-01T00:00:00.000" |
       And foo is of type "temporal"
     Then the following data should be generated:
       | foo  |
       | null |

### matchingRegex ###

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

### containingRegex ###

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
       And foo is after 2010-01-01T00:00:00.000
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
       And foo is after or at 2010-01-01T00:00:00.000
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
       And foo is before 2010-01-01T00:00:00.000
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
       And foo is before or at 2010-01-01T00:00:00.000
     Then the following data should be generated:
       | foo     |
       | null    |
       | "Test"  |
       | "test"  |
       | "Testt" |
       | "Test7" |

Scenario: Running a 'inSet' request with a not constraint should be successful
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
