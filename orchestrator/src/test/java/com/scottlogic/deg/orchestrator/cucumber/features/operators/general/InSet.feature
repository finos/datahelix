# Copyright 2019 Scott Logic Ltd
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
Feature: User can specify that a field value belongs to a set of predetermined options.

  Background:
    Given the generation strategy is full

### inSet alone ###
  Scenario: Running an 'inSet' request that includes strings with roman alphabet lowercase chars (a-z) only should be successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "aaa" |
      | "aab" |
    Then the following data should be generated:
      | foo   |
      | "aaa" |
      | "aab" |

  Scenario: Running an 'inSet' request that includes strings with roman alphabet uppercase chars (A-Z) only should be successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "CCC" |
      | "DDD" |
    Then the following data should be generated:
      | foo   |
      | "CCC" |
      | "DDD" |

  Scenario: Running an 'inSet' request that includes strings with roman numeric chars (0-9) only should be successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "012" |
      | "345" |
    Then the following data should be generated:
      | foo   |
      | "012" |
      | "345" |

  Scenario: Running an 'inSet' request that includes roman character strings that include profanity should be successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "Dick Van Dyke"       |
      | "Scunthorpe Hospital" |
    Then the following data should be generated:
      | foo                   |
      | "Dick Van Dyke"       |
      | "Scunthorpe Hospital" |

  Scenario: Running an 'inSet' request that includes roman character strings that include in-use values should be successful
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "true"      |
      | "false"     |
      | "null"      |
      | "undefined" |
      | "NaN"       |
      | "nil"       |
      | "infinity"  |
    Then the following data should be generated:
      | foo         |
      | null        |
      | "true"      |
      | "false"     |
      | "null"      |
      | "undefined" |
      | "NaN"       |
      | "nil"       |
      | "infinity"  |

  Scenario: Running an 'inSet' request that includes roman numeric strings that include decimal numbers should be successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "0.1"         |
      | "0.00"        |
      | "12.5.99"     |
      | "0000000.345" |
    Then the following data should be generated:
      | foo           |
      | "0.1"         |
      | "0.00"        |
      | "12.5.99"     |
      | "0000000.345" |

  Scenario: Running an 'inSet' request that includes roman numeric strings that include comma separated numbers should be successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "55,5"         |
      | "10,000"       |
      | "1,000,000.00" |
    Then the following data should be generated:
      | foo            |
      | "55,5"         |
      | "10,000"       |
      | "1,000,000.00" |

  Scenario: Running an 'inSet' request that includes roman character strings that include valid date values should be successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "2010-01-01T00:00:00.000" |
      | "2010-01-01T00:00:00.001" |
      | "2011-01-01T00:00:00.000" |
    Then the following data should be generated:
      | foo                       |
      | "2010-01-01T00:00:00.000" |
      | "2010-01-01T00:00:00.001" |
      | "2011-01-01T00:00:00.000" |

  Scenario: 'InSet' value of an empty string "" is successful
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "" |
    Then the following data should be generated:
      | foo  |
      | null |
      | ""   |

  Scenario: Running an 'inSet' request that includes a number value (not a string) should be successful
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 1     |
      | 54    |
      | 99999 |
    Then the following data should be generated:
      | foo   |
      | 1     |
      | 54    |
      | 99999 |

  Scenario: Running an 'inSet' request that includes a decimal number value should be successful
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 0.1       |
      | 600.01    |
      | 9.0000009 |
    Then the following data should be generated:
      | foo       |
      | 0.1       |
      | 600.01    |
      | 9.0000009 |

  Scenario: Running an 'inSet' request that includes a negative number value should be successful
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | -10         |
      | -0.0000089  |
      | -9999999999 |
    Then the following data should be generated:
      | foo         |
      | -10         |
      | -0.0000089  |
      | -9999999999 |

  Scenario: Running an 'inSet' request that includes the number zero should be successful
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 0   |
      | 0.0 |
    Then the following data should be generated:
      | foo |
      | 0   |

  Scenario: Running an 'inSet' request that includes a date value should be successful
    Given there is a non nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-01T00:00:00.001Z |
      | 2011-01-01T00:00:00.000Z |
    Then the following data should be generated:
      | foo                      |
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-01T00:00:00.001Z |
      | 2011-01-01T00:00:00.000Z |

  Scenario: Running an 'inSet' request that includes a date value (leap year) should be successful
    Given there is a non nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2020-02-29T00:00:00.000Z |
      | 2016-02-29T00:00:00.000Z |
      | 2012-02-29T00:00:00.000Z |
    Then the following data should be generated:
      | foo                      |
      | 2020-02-29T00:00:00.000Z |
      | 2016-02-29T00:00:00.000Z |
      | 2012-02-29T00:00:00.000Z |

  Scenario: Running an 'inSet' request that includes a date value (system epoch dates) should be successful
    Given there is a non nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 0001-01-01T00:00:01.000Z |
      | 9999-12-31T23:59:59.999Z |
    Then the following data should be generated:
      | foo                      |
      | 0001-01-01T00:00:01.000Z |
      | 9999-12-31T23:59:59.999Z |

  Scenario: Running an 'inSet' request that includes an invalid date value should fail with an error message
    Given there is a non nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2010-13-40T00:00:00.000Z |
      | 2017-12-31T23:59:59.999Z |
    Then the profile is invalid because "Date string '2010-13-40T00:00:00.000Z' must be in ISO-8601 format: Either yyyy-MM-ddTHH:mm:ss.SSS\[Z\] between 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z or yyyy-mm-dd between 0001-01-01 and 9999-12-31"
    And no data is created

  Scenario: Running an 'inSet' request that includes an invalid time value should fail with an error message
    Given there is a non nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2017-12-31T40:59:59.999Z |
      | 2017-12-31T23:59:59.999Z |
    Then the profile is invalid because "Date string '2017-12-31T40:59:59.999Z' must be in ISO-8601 format: Either yyyy-MM-ddTHH:mm:ss.SSS\[Z\] between 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z or yyyy-mm-dd between 0001-01-01 and 9999-12-31"
    And no data is created

  Scenario: Running an 'inSet' request that includes an empty string ("") characters should be successful
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "" |
    Then the following data should be generated:
      | foo  |
      | null |
      | ""   |

  Scenario: Running an 'inSet' request that includes a null entry (null) characters should throw an error
    Given there is a nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | null |
      | 1    |
    Then the profile is invalid with error "Values must be specified | Field: foo | Constraint: inSet | Rule: Unnamed rule"
    And no data is created

  Scenario: Running an 'inSet' request that includes multiples of the same entry should be successful.
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 1 |
      | 1 |
      | 2 |
    Then the following data should be generated:
      | foo |
      | 1   |
      | 2   |

### inSet ###
  Scenario: Running a 'inSet' request alongside a non-contradicting inSet constraint should be successful
    Given there is a non nullable field foo
    And foo has type "string"
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
      | "Test 3" |

  Scenario: 'InSet' with a non-contradictory not 'inSet' is successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo is anything but in set:
      | "A" |
    Then the following data should be generated:
      | foo |
      | "a" |

  Scenario: Running a 'inSet' request alongside a contradicting inSet constraint should produce null
    Given there is a nullable field foo
    And foo has type "string"
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
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo is anything but in set:
      | "a" |
    Then the following data should be generated:
      | foo  |
      | null |

### null ###
  Scenario: 'InSet' with not null is successful
    Given there is a nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 1 |
    And foo is anything but null
    Then the following data should be generated:
      | foo |
      | 1   |

  Scenario: Running a 'inSet' request alongside a null constraint should produce null
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "Test 1" |
      | "Test 2" |
      | "Test 3" |
    And foo is null
    Then the following data should be generated:
      | foo  |
      | null |
    
### matchingRegex ###
  Scenario: Running a 'inSet' request alongside a non-contradicting 'matchingRegex' constraint should be successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "Test"  |
      | "test"  |
      | "Testt" |
      | "Tes7"  |
    And foo is matching regex /[a-z]{4}/
    Then the following data should be generated:
      | foo    |
      | "test" |

  Scenario: 'InSet' string value with a not 'matchingRegex' of contradictory value is successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo is anything but matching regex /[b]{1}/
    Then the following data should be generated:
      | foo |
      | "a" |

  Scenario: Not 'inSet' string value with a 'matchingRegex' of contradictory value is successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is anything but in set:
      | "a" |
    And foo is matching regex /[b]{1}/
    Then the following data should be generated:
      | foo |
      | "b" |

  Scenario: 'InSet' alongside a contradicting 'matchingRegex' constraint should produce null
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo is matching regex /[b]{1}/
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'InSet' alongside a 'matchingRegex' constraint of contradictory length should produce null
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo is matching regex /[a]{2}/
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Not 'inSet' alongside a matching 'matchingRegex' emits null
    Given there is a nullable field foo
    And foo has type "string"
    And foo is anything but in set:
      | "a" |
    And foo is matching regex /[a]{1}/
    Then the following data should be generated:
      | foo  |
      | null |

### containingRegex ###
  Scenario: Running a 'inSet' request alongside a non-contradicting 'containingRegex' constraint should be successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "aa" |
      | "b"  |
    And foo is containing regex /[a]{1}/
    Then the following data should be generated:
      | foo  |
      | "aa" |

  Scenario: 'InSet' string value with a not 'containingRegex' of contradictory value is successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a"  |
      | "ab" |
      | "b"  |
    And foo is anything but containing regex /[b]{1}/
    Then the following data should be generated:
      | foo |
      | "a" |

  Scenario: 'InSet' alongside a contradicting 'containingRegex' constraint should produce null
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo is containing regex /[b]{1}/
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'InSet' alongside a 'containingRegex' constraint of contradictory length should produce null
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo is containing regex /[a]{2}/
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'InSet' alongside a contradicting not 'containingRegex' emits null
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo is anything but containing regex /[a]{1}/
    Then the following data should be generated:
      | foo  |
      | null |

### ofLength ###
  Scenario: 'InSet' with a non contradicting 'ofLength' is successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo is of length 1
    Then the following data should be generated:
      | foo |
      | "a" |

  Scenario: 'InSet' with a non contradicting not 'ofLength' is successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo is anything but of length 2
    Then the following data should be generated:
      | foo |
      | "a" |

  Scenario: Not 'inSet' with a non contradicting 'ofLength' is successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is anything but in set:
      | "a" |
    And foo is of length 1
    And foo is in set:
      | "a" |
      | "b" |
    Then the following data should be generated:
      | foo |
      | "b" |

  Scenario: 'InSet' with a contradicting 'ofLength' emits null
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo is of length 2
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'InSet' with a contradicting not 'ofLength' emits null
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo is anything but of length 1
    Then the following data should be generated:
      | foo  |
      | null |

### longerThan ###
  Scenario: 'InSet' with a non contradicting 'longerThan' is successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "aa" |
    And foo is longer than 1
    Then the following data should be generated:
      | foo  |
      | "aa" |

  Scenario: 'InSet' with a non contradicting not 'longerThan' is successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo is anything but longer than 1
    Then the following data should be generated:
      | foo |
      | "a" |

  Scenario: Not 'inSet' with a non contradicting 'longerThan' is successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is anything but in set:
      | "aa" |
    And foo is longer than 1
    And foo is in set:
      | "aa" |
      | "ba" |
    Then the following data should be generated:
      | foo  |
      | "ba" |

  Scenario: 'InSet' with a contradicting 'longerThan' emits null
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo is longer than 1
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'InSet' with a contradicting not 'longerThan' emits null
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "aa" |
    And foo is anything but longer than 1
    Then the following data should be generated:
      | foo  |
      | null |

### shorterThan ###
  Scenario: 'InSet' with a non contradicting 'shorterThan' is successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo is shorter than 2
    Then the following data should be generated:
      | foo |
      | "a" |

  Scenario: 'InSet' with a non contradicting not 'shorterThan' is successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo is anything but shorter than 1
    Then the following data should be generated:
      | foo |
      | "a" |

  Scenario: Not 'inSet' with a non contradicting 'shorterThan' is successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is anything but in set:
      | "a" |
    And foo is shorter than 2
    And foo is in set:
      | "a" |
      | "b" |
    Then the following data should be generated:
      | foo |
      | "b" |

  Scenario: 'InSet' with a contradicting 'shorterThan' emits null
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo is shorter than 1
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'InSet' with a contradicting not 'shorterThan' emits null
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo is anything but shorter than 2
    Then the following data should be generated:
      | foo  |
      | null |

### Financial data types ###
  Scenario: Not in set of things that are not valid ISINs combined with an ISIN constraint generates valid ISINs
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is anything but in set:
      | "a" |
    And foo has type "ISIN"
    And foo is in set:
      | "a"            |
      | "GB00YG2XYC52" |
    Then the following data should be generated:
      | foo            |
      | "GB00YG2XYC52" |

  Scenario: In set of things that are not valid ISINs combined with an ISIN constraint only generates nulls
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo has type "ISIN"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Not in set of things that are not valid SEDOLs combined with a SEDOL constraint generates valid SEDOLs
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is anything but in set:
      | "a" |
    And foo has type "SEDOL"
    And foo is in set:
      | "a"       |
      | "0263494" |
    Then the following data should be generated:
      | foo       |
      | "0263494" |

  Scenario: In set of things that are not valid SEDOLs combined with a SEDOL constraint only generates null
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo has type "SEDOL"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Not in set of things that are not valid CUSIPs combined with a CUSIP constraint generates valid CUSIPs
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is anything but in set:
      | "a" |
    And foo has type "CUSIP"
    And foo is in set:
      | "a"         |
      | "38259P508" |
    Then the following data should be generated:
      | foo         |
      | "38259P508" |

  Scenario: In set of things that are not valid CUSIPs combined with a CUSIP constraint only generates null
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "a" |
    And foo has type "CUSIP"
    Then the following data should be generated:
      | foo  |
      | null |

### greaterThan ###
  Scenario: 'InSet' with a non contradicting 'greaterThan' is successful
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 2 |
    And foo is greater than 1
    Then the following data should be generated:
      | foo |
      | 2   |

  Scenario: 'InSet' with a non contradicting not 'greaterThan' is successful
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 1 |
    And foo is anything but greater than 1
    Then the following data should be generated:
      | foo |
      | 1   |

  Scenario: Not 'inSet' with a non contradicting 'greaterThan' is successful
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is anything but in set:
      | 1 |
    And foo is greater than 0
    And foo is in set:
      | 1 |
      | 2 |
    Then the following data should be generated:
      | foo |
      | 2   |

  Scenario: 'InSet' with a contradicting 'greaterThan' emits null
    Given there is a nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 1 |
    And foo is greater than 1
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'InSet' with a contradicting not 'greaterThan' emits null
    Given there is a nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 1.1 |
    And foo is anything but greater than 1
    Then the following data should be generated:
      | foo  |
      | null |

### greaterThanOrEqualTo ###
  Scenario: 'InSet' with a non contradicting 'greaterThanOrEqualTo' is successful
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 1 |
    And foo is greater than or equal to 1
    Then the following data should be generated:
      | foo |
      | 1   |

  Scenario: 'InSet' with a non contradicting not 'greaterThanOrEqualTo' is successful
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 1 |
    And foo is anything but greater than or equal to 2
    Then the following data should be generated:
      | foo |
      | 1   |

  Scenario: Not 'inSet' with a non contradicting 'greaterThanOrEqualTo' is successful
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is anything but in set:
      | 1 |
    And foo is greater than or equal to 1
    And foo is in set:
      | 1 |
      | 2 |
    Then the following data should be generated:
      | foo |
      | 2   |

  Scenario: 'InSet' with a contradicting 'greaterThanOrEqualTo' emits null
    Given there is a nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 1 |
    And foo is greater than or equal to 2
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'InSet' with a contradicting not 'greaterThanOrEqualTo' emits null
    Given there is a nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 1 |
    And foo is anything but greater than or equal to 1
    Then the following data should be generated:
      | foo  |
      | null |

### lessThan ###
  Scenario: 'InSet' with a non contradicting 'lessThan' is successful
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 1 |
    And foo is less than 2
    Then the following data should be generated:
      | foo |
      | 1   |

  Scenario: 'InSet' with a non contradicting not 'lessThan' is successful
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 1 |
    And foo is anything but less than 1
    Then the following data should be generated:
      | foo |
      | 1   |

  Scenario: Not 'inSet' with a non contradicting 'lessThan' is successful
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is anything but in set:
      | 1 |
    And foo is less than 3
    And foo is in set:
      | 1 |
      | 2 |
      | 3 |
    Then the following data should be generated:
      | foo |
      | 2   |

  Scenario: 'InSet' with a contradicting 'lessThan' emits null
    Given there is a nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 1 |
    And foo is less than 1
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'InSet' with a contradicting not 'lessThan' emits null
    Given there is a nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 1 |
    And foo is anything but less than 2
    Then the following data should be generated:
      | foo  |
      | null |

### lessThanOrEqualTo ###
  Scenario: 'InSet' with a non contradicting 'lessThanOrEqualTo' is successful
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 1 |
    And foo is less than or equal to 1
    Then the following data should be generated:
      | foo |
      | 1   |

  Scenario: 'InSet' with a non contradicting not 'lessThanOrEqualTo' is successful
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 2 |
    And foo is anything but less than or equal to 1
    Then the following data should be generated:
      | foo |
      | 2   |

  Scenario: Not 'inSet' with a non contradicting 'lessThanOrEqualTo' is successful
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is anything but in set:
      | 1 |
    And foo is less than or equal to 2
    And foo is in set:
      | 1 |
      | 2 |
    Then the following data should be generated:
      | foo |
      | 2   |

  Scenario: 'InSet' with a contradicting 'lessThanOrEqualTo' emits null
    Given there is a nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 2 |
    And foo is less than or equal to 1
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'InSet' with a contradicting not 'lessThanOrEqualTo' emits null
    Given there is a nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 1 |
    And foo is anything but less than or equal to 1
    Then the following data should be generated:
      | foo  |
      | null |

### granularTo ###
  Scenario: 'InSet' with a non contradicting 'granularTo' is successful
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 10 |
    And foo is granular to 1
    Then the following data should be generated:
      | foo |
      | 10  |

  Scenario: Integer within an inSet and a non contradicting 'granularTo' is successful
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is granular to 1
    And foo is in set:
      | 1.1 |
      | 1   |
      | 2.0 |
    Then the following data should be generated:
      | foo |
      | 1   |
      | 2   |

  Scenario: Not 'inSet' with a non contradicting 'granularTo' is successful
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is anything but in set:
      | 1.1 |
    And foo is granular to 1
    And foo is in set:
      | 1.1 |
      | 1   |
    Then the following data should be generated:
      | foo |
      | 1   |

  Scenario: 'InSet' with a contradicting 'granularTo' emits null
    Given there is a nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 1.1 |
    And foo is granular to 1
    Then the following data should be generated:
      | foo  |
      | null |

### after ###
  Scenario: 'InSet' with a non contradicting 'after' is successful
    Given there is a non nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2019-01-01T00:00:00.001Z |
    And foo is after 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.001Z |

  Scenario: 'InSet' with a non contradicting not 'after' is successful
    Given there is a non nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is anything but after 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.000Z |

  Scenario: Not 'inSet' with a non contradicting 'after' is successful
    Given there is a non nullable field foo
    And foo has type "datetime"
    And foo is anything but in set:
      | 2019-01-01T00:00:00.001Z |
    And foo is after 2019-01-01T00:00:00.000Z
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.001Z |
      | 2019-01-01T00:00:00.002Z |
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.002Z |

  Scenario: 'InSet' with a contradicting 'after' emits null
    Given there is a nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is after 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'InSet' with a contradicting not 'after' emits null
    Given there is a nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2019-01-01T00:00:00.001Z |
    And foo is anything but after 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

### afterOrAt ###
  Scenario: 'InSet' with a non contradicting 'afterOrAt' is successful
    Given there is a non nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is after or at 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.000Z |

  Scenario: 'InSet' with a non contradicting not 'afterOrAt' is successful
    Given there is a non nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is anything but after or at 2019-01-01T00:00:00.001Z
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.000Z |

  Scenario: Not 'inSet' with a non contradicting 'afterOrAt' is successful
    Given there is a non nullable field foo
    And foo has type "datetime"
    And foo is anything but in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is after or at 2019-01-01T00:00:00.000Z
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.001Z |
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.001Z |

  Scenario: 'InSet' with a contradicting 'afterOrAt' emits null
    Given there is a nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is after or at 2019-01-01T00:00:00.001Z
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'InSet' with a contradicting not 'afterOrAt' emits null
    Given there is a nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is anything but after or at 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

### before ###
  Scenario: 'InSet' with a non contradicting 'before' is successful
    Given there is a non nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is before 2019-01-01T00:00:00.001Z
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.000Z |

  Scenario: 'InSet' with a non contradicting not 'before' is successful
    Given there is a non nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is anything but before 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.000Z |

  Scenario: Not 'inSet' with a non contradicting 'before' is successful
    Given there is a non nullable field foo
    And foo has type "datetime"
    And foo is anything but in set:
      | 2019-01-01T00:00:00.001Z |
    And foo is before 2019-01-01T00:00:00.002Z
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.001Z |
      | 2019-01-01T00:00:00.002Z |
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.000Z |

  Scenario: 'InSet' with a contradicting 'before' emits null
    Given there is a nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is before 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'InSet' with a contradicting not 'before' emits null
    Given there is a nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is anything but before 2019-01-01T00:00:00.001Z
    Then the following data should be generated:
      | foo  |
      | null |

### beforeOrAt ###
  Scenario: 'InSet' with a non contradicting 'beforeOrAt' is successful
    Given there is a non nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is before or at 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.000Z |

  Scenario: 'InSet' with a non contradicting not 'beforeOrAt' is successful
    Given there is a non nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2019-01-01T00:00:00.001Z |
    And foo is anything but before or at 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.001Z |

  Scenario: Not 'inSet' with a non contradicting 'beforeOrAt' is successful
    Given there is a non nullable field foo
    And foo has type "datetime"
    And foo is anything but in set:
      | 2019-01-01T00:00:00.001Z |
    And foo is before or at 2019-01-01T00:00:00.002Z
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.001Z |
      | 2019-01-01T00:00:00.002Z |
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.002Z |

  Scenario: 'InSet' with a contradicting 'beforeOrAt' emits null
    Given there is a nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2019-01-01T00:00:00.001Z |
    And foo is before or at 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'InSet' with a contradicting not 'beforeOrAt' emits null
    Given there is a nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2019-01-01T00:00:00.000Z |
    And foo is anything but before or at 2019-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Running a 'inSet' request alongside an ofType = string should be successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "Test 1" |
      | "Test 2" |
      | "Test 3" |
    And foo has type "string"
    Then the following data should be generated:
      | foo      |
      | "Test 1" |
      | "Test 2" |
      | "Test 3" |

  Scenario: Running a 'inSet' request alongside an ofType = integer should be successful
    Given there is a non nullable field foo
    And foo has type "integer"
    And foo is in set:
      | 1 |
      | 2 |
      | 3 |
    And foo has type "integer"
    Then the following data should be generated:
      | foo |
      | 1   |
      | 2   |
      | 3   |

  Scenario: Running a 'inSet' request alongside an ofType = decimal should be successful
    Given there is a non nullable field foo
    And foo has type "decimal"
    And foo is in set:
      | 1 |
      | 2 |
      | 3 |
    And foo has type "decimal"
    Then the following data should be generated:
      | foo |
      | 1   |
      | 2   |
      | 3   |

  Scenario: Running a 'inSet' request alongside an ofType = datetime should be successful
    Given there is a non nullable field foo
    And foo has type "datetime"
    And foo is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-01T00:00:00.001Z |
      | 2011-01-01T00:00:00.000Z |
    And foo has type "datetime"
    Then the following data should be generated:
      | foo                      |
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-01T00:00:00.001Z |
      | 2011-01-01T00:00:00.000Z |

  Scenario: Running a 'inSet' request alongside a non-contradicting matchingRegex constraint should be successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "Test"  |
      | "test"  |
      | "Testt" |
      | "Tes7"  |
    And foo is matching regex /[a-z]{4}/
    Then the following data should be generated:
      | foo    |
      | "test" |

  Scenario: Running a 'inSet' request alongside a contradicting matchingRegex constraint should produce null
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "Test"  |
      | "Testt" |
      | "Tes7"  |
    And foo is matching regex /[a-z]{4}/
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Running a 'inSet' request alongside a non-contradicting containingRegex constraint should be successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "Test"  |
      | "test"  |
      | "Testt" |
      | "Tes7"  |
    And foo is containing regex /[a-z]{4}/
    Then the following data should be generated:
      | foo     |
      | "test"  |
      | "Testt" |

  Scenario: Running a 'inSet' request alongside a contradicting containingRegex constraint should generate null
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "Test"  |
      | "test"  |
      | "Testt" |
      | "Tes7"  |
    And foo is containing regex /[A-Z]{4}/
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Running a 'inSet' request alongside a non-contradicting ofLength constraint should be successful
    Given there is a non nullable field foo
    And foo has type "string"
    And foo is in set:
      | "Test"  |
      | "test"  |
      | "Testt" |
      | "Test7" |
    And foo is of length 4
    Then the following data should be generated:
      | foo    |
      | "Test" |
      | "test" |

  Scenario: Running a 'inSet' request alongside a contradicting ofLength (too short) constraint should produce null
    Given there is a nullable field foo
    And foo has type "string"
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
    Given there is a nullable field foo
    And foo has type "string"
    And foo is in set:
      | "Test"  |
      | "test"  |
      | "Testt" |
      | "Test7" |
    And foo is of length 10
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Running a 'inSet' request as part of a non-contradicting anyOf constraint should be successful
    Given there is a nullable field foo
    And foo has type "string"
    And foo is anything but null
    And Any Of the next 2 constraints
    And foo is in set:
      | "Test 1" |
      | "Test 2" |
    And foo is in set:
      | "Test 3" |
      | "Test 4" |
    Then the following data should be generated:
      | foo      |
      | "Test 1" |
      | "Test 2" |
      | "Test 3" |
      | "Test 4" |

  Scenario: Running a 'inSet' request as part of a non-contradicting allOf constraint should be successful
    Given there is a non nullable field foo
    And foo has type "string"
    And All Of the next 2 constraints
    And foo is in set:
      | "Test1" |
      | "Test2" |
    And foo is in set:
      | "Test1" |
      | "Test2" |
    Then the following data should be generated:
      | foo     |
      | "Test1" |
      | "Test2" |

  Scenario: Running a 'inSet' request as part of a contradicting allOf constraint should produce null
    Given there is a nullable field foo
    And foo has type "string"
    And All Of the next 2 constraints
    And foo is in set:
      | "Test 1" |
      | "Test 2" |
    And foo is in set:
      | "Test 3" |
      | "Test 4" |
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Running a 'inSet' request as part of an if constraint should be successful
    Given the following non nullable fields exist:
      | foo   |
      | price |
    And foo has type "string"
    And price has type "decimal"
    And foo is in set:
      | "Test1" |
      | "Test2" |
      | "Test3" |
      | "Test4" |
    When If Then and Else are described below
    And foo is in set:
      | "Test1" |
      | "Test2" |
    And price is equal to 1
    And price is equal to 2
    Then the following data should be generated:
      | foo     | price |
      | "Test1" | 1     |
      | "Test2" | 1     |
      | "Test3" | 2     |
      | "Test4" | 2     |
#TODO  Scenario: Running a 'inSet' request alongside a contradicting ofType = string should produce null
#    Given there is a non nullable field foo
#    And foo has type "string"
#    And foo is in set:
#      | 1 |
#      | 2 |
#      | 3 |
#    Then the profile is invalid because "Field \[foo\]: is type STRING , but you are trying to apply a inSet constraint which requires NUMERIC"
