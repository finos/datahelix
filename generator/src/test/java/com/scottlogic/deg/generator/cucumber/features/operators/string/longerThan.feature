Feature: User can specify that a string length is longer than, a specified number of characters

Background:
  Given the generation strategy is full
    And there is a field foo

Scenario: Running a 'longerThan' request that includes positive value should be successful
  Given foo is longer than 5
    And foo is in set:
      | "aaaa"   |
      | "aaaaa"  |
      | "aaaaaa" |
  Then the following data should be generated:
      | foo      |
      | null     |
      | "aaaaaa" |

Scenario: Running a 'longerThan' request that includes the value zero should be successful
  Given foo is longer than 0
    And foo is in set:
      | ""  |
      | "a" |
  Then the following data should be generated:
      | foo  |
      | null |
      | "a"  |

Scenario: 'longerThan' a negative number should fail with an error
  Given foo is longer than -5
  Then I am presented with an error message
    And no data is created

Scenario: 'longerThan' a decimal number with an non-zero mantissa should fail with an error message
  Given foo is longer than 1.1
  Then I am presented with an error message
    And no data is created

Scenario: 'longerThan' a string should fail with an error message
  Given foo is longer than "Test"
  Then I am presented with an error message
    And no data is created

Scenario: 'longerThan' an empty string should fail with an error message
  Given foo is longer than ""
  Then I am presented with an error message
    And no data is created

Scenario: 'longerThan' whitespace should fail with an error message
  Given foo is longer than " "
  Then I am presented with an error message
    And no data is created

Scenario: 'longerThan' null should fail with an error message
  Given foo is longer than null
  Then the profile is invalid because "Couldn't recognise 'value' property, it must be set to a value"
    And no data is created

Scenario: 'longerThan' a decimal number with a zero mantissa should be successful
  Given foo is longer than 2.0
    And foo is in set:
      | 1.0   |
      | 2.0   |
      | "a"   |
      | "aaa" |
  Then the following data should be generated:
      | foo   |
      | null  |
      | 1.0   |
      | 2.0   |
      | "aaa" |

Scenario: Multiple non-contradictory 'longerThan' requests should be successful
  Given foo is longer than 2
    And foo is longer than 1
    And foo is in set:
      | "a"   |
      | "aa"  |
      | "aaa" |
      | "aab" |
  Then the following data should be generated:
      |  foo  |
      | null  |
      | "aaa" |
      | "aab" |


Scenario: Valid 'longerThan' and not 'longerThan' requests should be successful
  Given foo is longer than 1
    And foo is anything but longer than 3
    And foo is in set:
      | "a"    |
      | "aa"   |
      | "aaa"  |
      | "aaaa" |
  Then the following data should be generated:
      | foo   |
      | null  |
      | "aa"  |
      | "aaa" |

Scenario: Multiple not 'longerThan' requests should be successful
  Given foo is anything but longer than 5
    And foo is anything but longer than 6
    And foo is in set:
      | "a"      |
      | "aa"     |
      | "aaa"    |
      | "aaaa"   |
      | "aaaaa"  |
      | "aaaaaa" |
  Then the following data should be generated:
       | foo     |
       | null    |
       | "a"     |
       | "aa"    |
       | "aaa"   |
       | "aaaa"  |
       | "aaaaa" |

Scenario: 'longerThan' with contradicting not 'longerThan' should emit numeric,datetime and null
  Given foo is longer than 3
    And foo is anything but longer than 3
    And foo is in set:
       | 1                        |
       | "abc"                    |
       | 2011-01-01T00:00:00.000Z |
  Then the following data should be generated:
       | foo                      |
       | null                     |
       | 1                        |
       | 2011-01-01T00:00:00.000Z |

Scenario: Running a 'longerThan' request against non contradicting 'shorterThan' should be successful
  Given foo is longer than 2
    And foo is shorter than 5
    And foo is in set:
      | "a"      |
      | "aa"     |
      | "aaa"    |
      | "aaaa"   |
      | "aaaaaa" |
  Then the following data should be generated:
      | foo    |
      | null   |
      | "aaa"  |
      | "aaaa" |

Scenario: Running a 'longerThan' against non contradicting not 'shorterThan' should be successful
  Given foo is longer than 2
    And foo is anything but shorter than 1
    And foo is in set:
      | "a"    |
      | "aa"   |
      | "aaa"  |
      | "aaaa" |
  Then the following data should be generated:
      | foo    |
      | null   |
      | "aaa"  |
      | "aaaa" |

Scenario: Not 'longerThan' with non contradicting 'shorterThan' should be successful
  Given foo is anything but longer than 5
    And foo is shorter than 3
    And foo is in set:
      | "a"     |
      | "aa"    |
      | "aaa"   |
      | "aaaa"  |
      | "aaaaa" |
  Then the following data should be generated:
      | foo  |
      | null |
      | "a"  |
      | "aa" |

Scenario: Not 'longerThan' with non contradicting not 'shorterThan' should be successful
  Given foo is anything but longer than 3
    And foo is anything but shorter than 1
    And foo is in set:
      | "a"    |
      | "aa"   |
      | "aaa"  |
      | "aaaa" |
  Then the following data should be generated:
      | foo   |
      | null  |
      | "a"   |
      | "aa"  |
      | "aaa" |

Scenario: 'longerThan' with contradicting 'shorterThan' should emit numeric,datetime and null
  Given foo is longer than 3
    And foo is shorter than 2
    And foo is in set:
      | 1                        |
      | "abc"                    |
      | 2011-01-01T00:00:00.000Z |
  Then the following data should be generated:
      | foo                      |
      | null                     |
      | 1                        |
      | 2011-01-01T00:00:00.000Z |

Scenario: Not 'longerThan' with contradicting not 'shorterThan' should emit numeric,datetime and null
  Given foo is anything but longer than 3
    And foo is anything but shorter than 4
    And foo is in set:
      | 1                        |
      | "abc"                    |
      | "abcd"                   |
      | 2011-01-01T00:00:00.000Z |
  Then the following data should be generated:
       | foo                      |
       | null                     |
       | 1                        |
       | 2011-01-01T00:00:00.000Z |

@ignore #issue 487
Scenario: 'longerThan' alongside a non-contradicting 'aValid' constraint should be successful
  Given foo is longer than 11
    And foo is a valid "ISIN"
    And foo is in set:
      | "US0000XVGZA3" |
      | "US0378331005" |
      | "twelvedigits" |
  Then the following data should be generated:
      | foo            |
      | null           |
      | "US0000XVGZA3" |
      | "US0378331005" |

@ignore #issue 487
Scenario: 'longerThan' alongside a non-contradicting not 'aValid' constraint should be successful
  Given foo is longer than 2
    And foo is anything but a valid "ISIN"
    And foo is in set:
      | "US0000XVGZA3" |
      | "U10378331005" |
      | "twelvedigits" |
  Then the following data should be generated:
      | foo            |
      | null           |
      | "U10378331005" |
      | "twelvedigits" |

@ignore #issue 487
Scenario: Not 'longerThan' alongside a non-contradicting 'aValid' constraint should be successful
  Given foo is anything but longer than 1
    And foo is a valid "ISIN"
    And foo is in set:
      | "US0000XVGZA3" |
      | "US0378331005" |
      | "2"            |
  Then the following data should be generated:
      | foo  |
      | null |
      | "2"  |

Scenario: 'longerThan' against contradicting 'aValid' emits numeric,datetime and null
  Given foo is longer than 20
    And foo is a valid "ISIN"
    And foo is in set:
      | 22                       |
      | "abc"                    |
      | "US0000XVGZA3"           |
      | 2011-01-01T00:00:00.000Z |
  Then the following data should be generated:
       | foo                      |
       | null                     |
       | 22                       |
       | 2011-01-01T00:00:00.000Z |

@ignore #issue 487
Scenario: Not 'longerThan' with a non-contradicting not 'aValid' is successful
  Given foo is anything but longer than 12
    And foo is anything but a valid "ISIN"
    And foo is in set:
      | "US0000XVGZA3" |
      | "U10000XVGZA3" |
      | "twelvedigits" |
  Then the following data should be generated:
      | foo            |
      | null           |
      | "U10000XVGZA3" |
      | "twelvedigits" |

  Scenario: Running a 'longerThan' request against non contradicting 'greaterThan' should be successful
  Given foo is longer than 1
    And foo is greater than 10
    And foo is in set:
      | "a"     |
      | "aa"    |
      | "aaaa " |
      | 15      |
  Then the following data should be generated:
      | foo     |
      | null    |
      | "aa"    |
      | "aaaa " |
      | 15      |

Scenario: 'longerThan' against non contradicting not 'greaterThan' should be successful
  Given foo is longer than 10
    And foo is anything but greater than 2
    And foo is in set:
      | "aaaaaaaaaa"    |
      | "aaaaaaaaaaa"   |
      | "aaaaaaaaaaaaa" |
      | 2               |
      | 3               |
  Then the following data should be generated:
      | foo             |
      | null            |
      | "aaaaaaaaaaa"   |
      | "aaaaaaaaaaaaa" |
      | 2               |

Scenario: 'longerThan' against non contradicting 'greaterThanOrEqualTo' should be successful
  Given foo is longer than 2
    And foo is greater than or equal to 2
    And foo is in set:
      | "aa"  |
      | "aaa" |
      | 2     |
      | 3     |
  Then the following data should be generated:
      | foo   |
      | null  |
      | "aaa" |
      | 2     |
      | 3     |

Scenario: 'longerThan' against non contradicting not 'greaterThanOrEqualTo' should be successful
  Given foo is longer than 10
    And foo is anything but greater than or equal to 2
    And foo is in set:
      | "aaaaaaaaaa"    |
      | "aaaaaaaaaaa"   |
      | "aaaaaaaaaaaaa" |
      | 1               |
      | 2               |
      | 5               |
  Then the following data should be generated:
      | foo             |
      | null            |
      | "aaaaaaaaaaa"   |
      | "aaaaaaaaaaaaa" |
      | 1               |

Scenario: Running a 'longerThan' request against non contradicting 'lessThan' should be successful
  Given foo is longer than 10
    And foo is less than 5
    And foo is in set:
      | "aaaaaaaaaa"    |
      | "aaaaaaaaaaa"   |
      | "aaaaaaaaaaaaa" |
      | 4               |
      | 5               |
  Then the following data should be generated:
      | foo             |
      | null            |
      | "aaaaaaaaaaa"   |
      | "aaaaaaaaaaaaa" |
      | 4               |

Scenario: Running a 'longerThan' request against non contradicting not 'lessThan' should be successful
  Given foo is longer than 1
    And foo is anything but less than 10
    And foo is in set:
      | "a"             |
      | "aa"            |
      | "aaaaaaaaa"     |
      | "aaaaaaaaaa"    |
      | "aaaaaaaaaaa"   |
      | 9               |
      | 10              |
      | 11              |
  Then the following data should be generated:
      | foo           |
      | null          |
      | "aa"          |
      | "aaaaaaaaa"   |
      | "aaaaaaaaaa"  |
      | "aaaaaaaaaaa" |
      | 10            |
      | 11            |

Scenario: Running a 'longerThan' request against non contradicting 'lessThanOrEqualTo' should be successful
  Given foo is longer than 10
    And foo is less than or equal to 10
    And foo is in set:
       | "a"           |
       | "aaaaaaaaa"   |
       | "aaaaaaaaaa"  |
       | "aaaaaaaaaaa" |
       | 1             |
       | 10            |
       | 15            |
  Then the following data should be generated:
      | foo           |
      | null          |
      | "aaaaaaaaaaa" |
      | 1             |
      | 10            |

Scenario: Running a 'longerThan' request against non contradicting not 'lessThanOrEqualTo' should be successful
  Given foo is longer than 1
    And foo is anything but less than or equal to 10
    And foo is in set:
      | "a"           |
      | "aa"          |
      | "aaaaaaaaaa"  |
      | "aaaaaaaaaaa" |
      | 1             |
      | 12            |
  Then the following data should be generated:
      | foo           |
      | null          |
      | "aa"          |
      | "aaaaaaaaaa"  |
      | "aaaaaaaaaaa" |
      | 12            |

@ignore #GranularTo design WIP
Scenario: Running a 'longerThan' request alongside a 'granularTo' decimal constraint should be successful
  Given foo is longer than 1
    And foo is granular to 0.1
    And foo is in set:
      | "a"  |
      | "aa" |
      | 0.1  |
      | 1    |
      | 1.11 |
  Then the following data should be generated:
      | foo  |
      | null |
      | "aa" |
      | 0.1  |
      | 1    |

@ignore #GranularTo design WIP
Scenario: Running a 'longerThan' request alongside a 'granularTo' constraint  should be successful
  Given foo is longer than 1
    And foo is anything but granular to 1
    And foo is in set:
      | "a"    |
      | "aa"   |
      | "aaaa" |
      | 1      |
      | 2      |
  Then the following data should be generated:
      | foo    |
      | null   |
      | "aa"   |
      | "aaaa" |

Scenario: Running an 'longerThan' request alongside a 'after' constraint should be successful
  Given foo is longer than 1
    And foo is after 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "aa"                      |
      | "aaaa"                    |
      | "aaaaaaaaaa"              |
      | "2019-01-01T00:00:00.001" |
      |  2019-01-01T00:00:00.001Z |
      |  2019-01-01T00:00:00.000Z |
  Then the following data should be generated:
       | foo                       |
       | null                      |
       | "aa"                      |
       | "aaaa"                    |
       | "aaaaaaaaaa"              |
       | "2019-01-01T00:00:00.001" |
       |  2019-01-01T00:00:00.001Z |

Scenario: Running an 'longerThan' with a not 'after' constraint should be successful
  Given foo is longer than 1
    And foo is anything but after 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "a"                       |
      | "aa"                      |
      | "aaaa"                    |
      | "2019-01-01T00:00:00.000" |
      |  2019-01-01T00:00:00.000Z |
      |  2019-01-01T00:00:00.001Z |
  Then the following data should be generated:
      | foo                       |
      | null                      |
      | "aa"                      |
      | "aaaa"                    |
      | "2019-01-01T00:00:00.000" |
      |  2019-01-01T00:00:00.000Z |

Scenario: Running an 'longerThan' request alongside a 'afterOrAt'  constraint should be successful
  Given foo is longer than 1
    And foo is after or at 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "a"                       |
      | "aa"                      |
      | "aaaa"                    |
      | "aaaaaaaaaa"              |
      | "2019-01-01T00:00:00.000" |
      | "2018-09-01T00:00:00.001" |
      | 2018-12-31T23:59:59.999Z  |
      | 2019-01-01T00:00:00.000Z  |
      | 2019-01-01T00:00:00.001Z  |
  Then the following data should be generated:
      | foo                       |
      | null                      |
      | "aa"                      |
      | "aaaa"                    |
      | "aaaaaaaaaa"              |
      | "2019-01-01T00:00:00.000" |
      | "2018-09-01T00:00:00.001" |
      | 2019-01-01T00:00:00.000Z  |
      | 2019-01-01T00:00:00.001Z  |

Scenario: Running an 'longerThan' request alongside a not 'afterOrAt' should be successful
  Given foo is longer than 1
    And foo is anything but after or at 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "a"                       |
      | "aa"                      |
      | "aaaa"                    |
      | "aaaaaaaaaa"              |
      | "2018-02-01T00:00:00.001" |
      | "2019-01-01T00:00:00.000" |
      | 2018-12-31T23:59:59.999Z  |
      | 2018-01-01T00:00:00.001Z  |
      | 2019-01-01T00:00:00.000Z  |
  Then the following data should be generated:
      | foo                       |
      | null                      |
      | "aa"                      |
      | "aaaa"                    |
      | "aaaaaaaaaa"              |
      | "2018-02-01T00:00:00.001" |
      | "2019-01-01T00:00:00.000" |
      | 2018-12-31T23:59:59.999Z  |
      | 2018-01-01T00:00:00.001Z  |

Scenario: Running a 'longerThan' request alongside a 'before' constraint should be successful
  Given foo is longer than 1
    And foo is before 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "a"                       |
      | "aa"                      |
      | "aaaaa"                   |
      | "2019-01-01T00:00:00.000" |
      | 2018-01-01T00:00:00.000Z  |
      | 2019-01-01T00:00:00.000Z  |
  Then the following data should be generated:
      | foo                       |
      | null                      |
      | "aa"                      |
      | "aaaaa"                   |
      | "2019-01-01T00:00:00.000" |
      | 2018-01-01T00:00:00.000Z  |

Scenario: Running a 'longerThan' request alongside a not 'before' constraint should be successful
  Given foo is longer than 1
    And foo is anything but before 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "a"                       |
      | "aa"                      |
      | "aaaaa"                   |
      | "2019-01-01T00:00:00.000" |
      | 2018-01-01T00:00:00.000Z  |
      | 2019-02-01T00:00:00.000Z  |
      | 2020-02-01T00:00:00.000Z  |
  Then the following data should be generated:
      | foo                       |
      | null                      |
      | "aa"                      |
      | "aaaaa"                   |
      | "2019-01-01T00:00:00.000" |
      | 2019-02-01T00:00:00.000Z  |
      | 2020-02-01T00:00:00.000Z  |

Scenario: Running a 'longerThan' request alongside a non contradicting 'beforeOrAt' constraint should be successful
  Given foo is longer than 1
    And foo is before or at 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "a"                       |
      | "aa"                      |
      | "aaaa"                    |
      | "2019-01-01T00:00:00.000" |
      | 2019-01-01T00:00:00.000Z  |
      | 2018-01-01T00:00:00.000Z  |
      | 2020-01-01T00:00:00.000Z  |
  Then the following data should be generated:
      | foo                       |
      | null                      |
      | "aa"                      |
      | "aaaa"                    |
      | "2019-01-01T00:00:00.000" |
      | 2019-01-01T00:00:00.000Z  |
      | 2018-01-01T00:00:00.000Z  |

Scenario: Running a 'longerThan' request alongside a non contradicting not 'beforeOrAt' should be successful
  Given foo is longer than 1
    And foo is anything but before or at 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "a"                       |
      | "aa"                      |
      | "aaaa"                    |
      | "2019-01-01T00:00:00.000" |
      | 2019-01-01T00:00:00.000Z  |
      | 2018-01-01T00:00:00.000Z  |
      | 2020-01-01T00:00:00.000Z  |
  Then the following data should be generated:
     | foo                       |
     | null                      |
     | "aa"                      |
     | "aaaa"                    |
     | "2019-01-01T00:00:00.000" |
     | 2020-01-01T00:00:00.000Z  |

