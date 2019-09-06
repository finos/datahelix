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
    Then the profile is invalid because "Field \[foo\]: longerThan constraint must have an operand/value >= 0, currently is -5"
    And no data is created

  Scenario: 'longerThan' a decimal number with an non-zero mantissa should fail with an error message
    Given foo is longer than 1.1
    Then the profile is invalid because "Field \[foo\]: Couldn't recognise 'value' property, it must be an integer but was a decimal with value `1.1`"
    And no data is created

  Scenario: 'longerThan' a string should fail with an error message
    Given foo is longer than "Test"
    Then the profile is invalid because "Field \[foo\]: Couldn't recognise 'value' property, it must be a Integer but was a String with value `Test`"
    And no data is created

  Scenario: 'longerThan' an empty string should fail with an error message
    Given foo is longer than ""
    Then the profile is invalid because "Field \[foo\]: Couldn't recognise 'value' property, it must be a Integer but was a String with value ``"
    And no data is created

  Scenario: 'longerThan' whitespace should fail with an error message
    Given foo is longer than " "
    Then the profile is invalid because "Field \[foo\]: Couldn't recognise 'value' property, it must be a Integer but was a String with value ` `"
    And no data is created

  Scenario: 'longerThan' null should fail with an error message
    Given foo is longer than null
    Then the profile is invalid because "Field \[foo\]: Couldn't recognise 'value' property, it must be set to a value"
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
      | foo   |
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

  Scenario: A longer than constraint combined with an ISIN constraint generates valid ISINs if the operand of the longer than constraint is less than the length of a valid ISIN
    Given foo is longer than 11
    And foo is of type "ISIN"
    And foo is in set:
      | "GB00YG2XYC52" |
      | "US0378331005" |
      | "twelvedigits" |
    Then the following data should be generated:
      | foo            |
      | null           |
      | "GB00YG2XYC52" |
      | "US0378331005" |

  @ignore "Standard constraints e.g. ISINs cannot yet be negated."
  Scenario: A longer than constraint combined with a non-ISIN constraint generates data that matches the longer than constraint and contains no valid ISINs
    Given foo is longer than 2
    And foo is anything but null
    And foo is anything but of type "ISIN"
    And foo is in set:
      | "US0000XVGZA3" |
      | "U10378331005" |
      | "twelvedigits" |
    Then the following data should be generated:
      | foo            |
      | "US0000XVGZA3" |
      | "U10378331005" |
      | "twelvedigits" |

  Scenario: A not longer than constraint combined with an ISIN constraint generates valid ISINs if the operand of the not longer than constraint is at least as large as the length of a valid ISIN
    Given foo is anything but longer than 12
    And foo is of type "ISIN"
    And foo is in set:
      | "GB00YG2XYC52" |
      | "US0378331005" |
      | "2"            |
    Then the following data should be generated:
      | foo            |
      | null           |
      | "GB00YG2XYC52" |
      | "US0378331005" |

  Scenario: A longer than constraint combined with an ISIN constraint only generates null if the operand of the longer than constraint is greater than the length of a valid ISIN
    Given foo is longer than 20
    And foo is of type "ISIN"
    Then the following data should be generated:
      | foo  |
      | null |

  @ignore "Standard constraints e.g. ISINs cannot yet be negated."
  Scenario: A not longer than constraint combined with a non-ISIN constraint generates data that contains no valid ISINs
    Given foo is anything but longer than 12
    And foo is anything but null
    And foo is anything but of type "ISIN"
    And foo is in set:
      | "US0000XVGZA3" |
      | "U10000XVGZA3" |
      | "twelvedigits" |
    Then the following data should be generated:
      | foo            |
      | "US0000XVGZA3" |
      | "U10000XVGZA3" |
      | "twelvedigits" |

  Scenario: A longer than constraint combined with a SEDOL constraint generates valid SEDOLs if the operand of the longer than constraint is less than the length of a valid SEDOL
    Given foo is longer than 6
    And foo is of type "SEDOL"
    And foo is in set:
      | "0263494" |
      | "3091357" |
      | "string7" |
    Then the following data should be generated:
      | foo       |
      | null      |
      | "0263494" |
      | "3091357" |

  @ignore "Standard constraints e.g. ISINs cannot yet be negated."
  Scenario: A longer than constraint combined with a non-SEDOL constraint generates data that matches the longer than constraint and contains no valid SEDOLs
    Given foo is longer than 2
    And foo is anything but null
    And foo is anything but of type "SEDOL"
    And foo is in set:
      | "0263499" |
      | "3091352" |
      | "string7" |
    Then the following data should be generated:
      | foo       |
      | "0263499" |
      | "3091352" |
      | "string7" |

  Scenario: A not longer than constraint combined with a SEDOL constraint generates valid SEDOLs if the operand of the not longer than constraint is at least as large as the length of a valid SEDOL
    Given foo is anything but longer than 7
    And foo is of type "SEDOL"
    And foo is in set:
      | "0263494" |
      | "3091357" |
      | "2"       |
    Then the following data should be generated:
      | foo       |
      | null      |
      | "0263494" |
      | "3091357" |

  Scenario: A longer than constraint combined with a SEDOL constraint only generates null if the operand of the longer than constraint is larger than the length of a valid SEDOL
    Given foo is longer than 20
    And foo is of type "SEDOL"
    Then the following data should be generated:
      | foo  |
      | null |

  @ignore "Standard constraints e.g. ISINs cannot yet be negated."
  Scenario: A not longer than constraint combined with a non-SEDOL constraint generates data that matches the longer than constraint and contains no valid SEDOLs
    Given foo is anything but longer than 7
    And foo is anything but null
    And foo is anything but of type "SEDOL"
    And foo is in set:
      | "0263497" |
      | "3091354" |
      | "string7" |
    Then the following data should be generated:
      | foo       |
      | "0263497" |
      | "3091354" |
      | "string7" |

  Scenario: A longer than constraint combined with a CUSIP constraint generates valid CUSIPs if the operand of the longer than constraint is less than the length of a valid CUSIP
    Given foo is longer than 8
    And foo is of type "CUSIP"
    And foo is in set:
      | "38259P508" |
      | "594918104" |
      | "strngnine" |
    Then the following data should be generated:
      | foo         |
      | null        |
      | "38259P508" |
      | "594918104" |

  @ignore "Standard constraints e.g. ISINs cannot yet be negated."
  Scenario: A longer than constraint combined with a non-CUSIP constraint generates data that matches the longer than constraint and contains no valid CUSIPs
    Given foo is longer than 2
    And foo is anything but null
    And foo is anything but of type "CUSIP"
    And foo is in set:
      | "38259W508" |
      | "5F4918104" |
      | "strngnine" |
    Then the following data should be generated:
      | foo         |
      | "38259W508" |
      | "5F4918104" |
      | "strngnine" |

  Scenario: A not longer than constraint combined with a CUSIP constraint generates valid CUSIPs if the operand of the longer than constraint is at least as large as the length of a valid CUSIP
    Given foo is anything but longer than 9
    And foo is of type "CUSIP"
    And foo is in set:
      | "38259P508" |
      | "594918104" |
      | "2"         |
    Then the following data should be generated:
      | foo         |
      | null        |
      | "38259P508" |
      | "594918104" |

  Scenario: A longer than constraint combined with a CUSIP constraint only generates null if the operand of the longer than constraint is at least as large as the length of a valid CUSIP
    Given foo is longer than 10
    And foo is of type "CUSIP"
    Then the following data should be generated:
      | foo  |
      | null |

  @ignore "Standard constraints e.g. ISINs cannot yet be negated."
  Scenario: A not longer than constraint combined with a non-CUSIP constraint generates data that matches the not longer than constraint and contains no valid CUSIPs
    Given foo is anything but longer than 9
    And foo is anything but null
    And foo is anything but of type "CUSIP"
    And foo is in set:
      | "38259W508" |
      | "5F4918104" |
      | "strngnine" |
    Then the following data should be generated:
      | foo         |
      | "38259W508" |
      | "5F4918104" |
      | "strngnine" |

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
      | "a"           |
      | "aa"          |
      | "aaaaaaaaa"   |
      | "aaaaaaaaaa"  |
      | "aaaaaaaaaaa" |
      | 9             |
      | 10            |
      | 11            |
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

  @ignore #769 Violation of numeric and temporal granularity
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
      | 2019-01-01T00:00:00.001Z  |
      | 2019-01-01T00:00:00.000Z  |
    Then the following data should be generated:
      | foo                       |
      | null                      |
      | "aa"                      |
      | "aaaa"                    |
      | "aaaaaaaaaa"              |
      | "2019-01-01T00:00:00.001" |
      | 2019-01-01T00:00:00.001Z  |

  Scenario: Running an 'longerThan' with a not 'after' constraint should be successful
    Given foo is longer than 1
    And foo is anything but after 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "a"                       |
      | "aa"                      |
      | "aaaa"                    |
      | "2019-01-01T00:00:00.000" |
      | 2019-01-01T00:00:00.000Z  |
      | 2019-01-01T00:00:00.001Z  |
    Then the following data should be generated:
      | foo                       |
      | null                      |
      | "aa"                      |
      | "aaaa"                    |
      | "2019-01-01T00:00:00.000" |
      | 2019-01-01T00:00:00.000Z  |

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

  Scenario: longerThan with maximum permitted value should be successful
    Given foo is longer than 999
    And the generation strategy is random
    And the generator can generate at most 1 rows
    And foo is anything but null
    Then foo contains strings of length between 1000 and 1000 inclusively

  Scenario: longerThan with value larger than maximum permitted should fail with an error message
    Given foo is longer than 1000
    Then the profile is invalid because "Field \[foo\]: longerThan constraint must have an operand/value <= 999, currently is 1000"

  Scenario: Running a 'longerThan' request with a value less than default max (1000) should generate data of length between value and 1000
    Given foo is of type "string"
    And foo is longer than 999
    And the generator can generate at most 20 rows
    Then foo contains strings of length between 1000 and 1000 inclusively
