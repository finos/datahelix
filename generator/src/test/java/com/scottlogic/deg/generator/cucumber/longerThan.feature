Feature: User can specify that a string length is longer than, a specified number of characters

  Background:
    Given the generation strategy is full
    And there is a field foo


Scenario: Running a 'longerThan' request that includes positive value should be successful
  Given foo is longer than 5
  And foo is in set:
  | "a" |
  | "aa" |
  | "aaa" |
  | "aaaa" |
  | "aaaaa" |
  | "aaaaaa" |
  Then the following data should be generated:
    | foo  |
    | null |
    | "aaaaaa" |

Scenario: Running a 'longerThan' request that includes the value zero should be succesful
  And foo is longer than 0
  And foo is in set:
  | "" |
  | "a" |
  Then the following data should be generated:
  | foo |
  | null |
  | "a"  |

Scenario: Running a 'longerThan' request on a negative number should fail with an error
    Given foo is longer than -5
    Then I am presented with an error message
    And no data is created

Scenario: Running a 'longerThan' request on a decimal number to specify a the length of a generated string should fail with an error message
  And foo is longer than 1.1
  Then I am presented with an error message
  And no data is created


Scenario: Running a 'longerThan' request on a negative number should fail with an error
    Given foo is longer than -5
    Then I am presented with an error message
    And no data is created

Scenario: Running a 'longerThan' request on a character string should be successful
    Given foo is longer than 2
    And foo is longer than 1
    And foo is in set:
      | "a" |
      | "aa" |
      | "aaa" |
      | "aab" |
    Then the following data should be generated:
      |  foo  |
      | null  |
      | "aaa" |
      | "aab" |


Scenario: Running a 'longerThan' request on a character string should be successful
  Given foo is longer than 1
  And foo is anything but longer than 3
  And foo is in set:
   | "a" |
   | "aa" |
   | "aaa" |
   | "aaaa" |
  Then the following data should be generated:
   | foo |
   | null |
   | "aa" |
   | "aaa" |

Scenario: Running a 'longerThan' request on a character string should be successful
  Given foo is anything but longer than 5
  And foo is anything but longer than 6
  And foo is in set:
    | "a" |
    | "aa" |
    | "aaa" |
    | "aaaa" |
    | "aaaaa" |
    | "aaaaaa" |
  Then the following data should be generated:
    | foo |
    | null |
    | "a" |
    | "aa" |
    | "aaa" |
    | "aaaa" |
    | "aaaaa" |

Scenario:  Running a 'longerThan' request against contradicting not longerThan constraint should only generate numeric,temporal and null
  Given foo is longer than 3
  And foo is anything but longer than 3
  And foo is in set:
  | 1 |
  | "abc" |
  | 2011-01-01T00:00:00.000 |
  Then the following data should be generated:
  | foo |
  | null |
  | 1    |
  | 2011-01-01T00:00:00.000 |

Scenario: Running a 'longerThan' request against non contradicting shorterThan should be succesful
  Given foo is longer than 2
  And foo is shorter than 5
  And foo is in set:
  | "a" |
  | "aa" |
  | "aaa" |
  | "aaaa" |
  | "aaaaaa" |
  Then the following data should be generated:
    | foo |
    | null |
    | "aaa" |
    | "aaaa" |

Scenario: Running a 'longerThan' request against non contradicting shorterThan should be succesful
  Given foo is longer than 2
  And foo is anything but shorter than 1
  And foo is in set:
    | "a" |
    | "aa" |
    | "aaa" |
    | "aaaa" |
  Then the following data should be generated:
    | foo |
    | null |
    | "aaa" |
    | "aaaa" |

Scenario: Running a 'longerThan' request against non contradicting shorterThan should be succesful
  Given foo is anything but longer than 5
  And foo is shorter than 3
  And foo is in set:
    | "a" |
    | "aa" |
    | "aaa" |
    | "aaaa" |
    | "aaaaa" |
  Then the following data should be generated:
    | foo |
    | null |
    | "a"  |
    |"aa" |

Scenario: Running a 'longerThan' request against non contradicting shorterThan should be successful
  Given foo is anything but longer than 3
  And foo is anything but shorter than 1
  And foo is in set:
    | "a" |
    | "aa" |
    | "aaa" |
    | "aaaa" |
  Then the following data should be generated:
    | foo |
    | null |
    | "a"  |
    | "aa" |
   | "aaa" |

Scenario: Running a 'longerThan' request against contradicting shorterThan constraint should only generate numeric,temporal and null
    Given foo is longer than 3
    And foo is shorter than 2
    And foo is in set:
      | 1 |
      | "abc" |
      | 2011-01-01T00:00:00.000 |
    Then the following data should be generated:
      | foo |
      | null |
      | 1    |
      | 2011-01-01T00:00:00.000 |
Scenario: Running a 'longerThan' request against contradicting shorterThan constraint should only generate numeric,temporal and null
  Given foo is anything but longer than 3
  And foo is anything but shorter than 4
  And foo is in set:
    | 1 |
    | "abc" |
    | 2011-01-01T00:00:00.000 |
  Then the following data should be generated:
    | foo |
    | null |
    | 1  |
    | 2011-01-01T00:00:00.000 |

@ignore
  Scenario: Running an 'longerThan' request alongside a non-contradicting aValid constraint should be successful
    Given foo is longer than 12
    And foo is a valid "ISIN"
    And foo is in set:
      | "US0000XVGZA3" |
      | "US0378331005" |
      | "22"           |
    Then the following data should be generated:
      | foo            |
      | null           |
      | "US0000XVGZA3" |
      | "US0378331005" |
    And the following data should not be included in what is generated:
      | foo  |
      | "22" |
@ignore
   Scenario: Running an 'longerThan' request alongside a non-contradicting aValid constraint should be successful
     Given foo is longer than 2
     And foo is anything but a  valid "ISIN"
     And foo is in set:
       | "US0000XVGZA3" |
       | "US0378331005" |
       | "22"           |
  Then the following data should be generated:
      | foo            |
      | null           |
      | "US0000XVGZA3" |
      | "US0378331005" |
  And the following data should not be included in what is generated:
     | foo  |
     | "22" |

@ignore
   Scenario: Running an 'longerThan' request alongside a non-contradicting aValid constraint should be successful
     Given foo is anything but  longer than 1
     And foo is a valid "ISIN"
     And foo is in set:
       | "US0000XVGZA3" |
       | "US0378331005" |
       | "2"           |
  Then the following data should be generated:
       | foo            |
       | null           |
       | "US0000XVGZA3" |
       | "US0378331005" |
      And the following data should not be included in what is generated:
       | foo  |
       | "2" |

@ignore
   Scenario: Running an 'longerThan' request alongside a non-contradicting aValid constraint should be successful
      Given foo is anything but  longer than 1
        And foo is a valid "ISIN"
        And foo is in set:
           | "US0000XVGZA3" |
           | "US0378331005" |
           | "2"           |
      Then the following data should be generated:
           | foo            |
           | null           |
           | "US0000XVGZA3" |
           | "US0378331005" |
        And the following data should not be included in what is generated:
          | foo  |
          | "2" |

@ignore
   Scenario: Running an 'longerThan' request alongside a non-contradicting aValid constraint should be successful
      Given foo is anything but longer than 1
        And foo is antyhing but valid "ISIN"
        And foo is in set:
          | "US0000XVGZA3" |
          | "US0378331005" |
          | "2"           |
      Then the following data should be generated:
          | foo            |
          | null           |
          | "US0000XVGZA3" |
          | "US0378331005" |
        And the following data should not be included in what is generated:
          | foo  |
          | "2" |

@ignore
   Scenario: Running a 'longerThan' request against contradicting aValid constraint should only generate numeric,temporal and null
      Given foo is longer than 20
         And foo is a valid "ISIN"
         And foo is in set:
           | 22 |
           | "abc" |
           | "US0000XVGZA3" |
           | 2011-01-01T00:00:00.000 |
      Then the following data should be generated:
           | foo |
           | null |
           | 22   |
           | 2011-01-01T00:00:00.000 |



  Scenario: Running a 'longerThan' request against non contradicting greaterThan should be successful
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

  Scenario: Running a 'longerThan' request against non contradicting greaterThan should be successful
    Given foo is longer than 10
    And foo is anything but greater than 2
    And foo is in set:
      | "a"     |
      | "aa"    |
      | "aaaa" |
      | "aaaaaaaaaa" |
      | "aaaaaaaaaaa" |
      | "aaaaaaaaaaaaa" |
      | 1      |
    Then the following data should be generated:
      | foo     |
      | null    |
      | "aaaaaaaaaaa" |
      | "aaaaaaaaaaaaa" |
      | 1     |

  Scenario: Running a 'longerThan' request against non contradicting greaterThanOrEqualTo should be successful
    Given foo is longer than 10
    And foo is anything but greater than or equal to 2
    And foo is in set:
      | "a"     |
      | "aa"    |
      | "aaaa" |
      | "aaaaaaaaaa" |
      | "aaaaaaaaaaa" |
      | "aaaaaaaaaaaaa" |
      | 1      |
      | 2      |
      | 5      |
    Then the following data should be generated:
      | foo     |
      | null    |
      | "aaaaaaaaaaa" |
      | "aaaaaaaaaaaaa" |
      | 1    |

  Scenario: Running a 'longerThan' request against non contradicting lessThan should be successful
    Given foo is longer than 10
    And foo is less than 5
    And foo is in set:
      | "a"     |
      | "aa"    |
      | "aaaa" |
      | "aaaaaaaaaa" |
      | "aaaaaaaaaaa" |
      | "aaaaaaaaaaaaa" |
      | 1      |
      | 2      |
      | 5      |
    Then the following data should be generated:
      | foo     |
      | null    |
      | "aaaaaaaaaaa" |
      | "aaaaaaaaaaaaa" |
      | 1    |
      | 2    |

  Scenario: Running a 'longerThan' request against non contradicting lessThan should be successful
    Given foo is longer than 1
    And foo is anything but less than 10
    And foo is in set:
      | "a"     |
      | "aa"    |
      | "aaaa" |
      | "aaaaaaaaaa" |
      | "aaaaaaaaaaa" |
      | "aaaaaaaaaaaaa" |
      | 1      |
      | 2      |
      | 12      |
    Then the following data should be generated:
      | foo     |
      | null    |
      | "aa"   |
      | "aaaa" |
      | "aaaaaaaaaa" |
      | "aaaaaaaaaaa" |
      | "aaaaaaaaaaaaa" |
      | 12    |

  Scenario: Running a 'longerThan' request against non contradicting lessThanOrEqualTo should be successful
    Given foo is longer than 10
    And foo is less than or equal to 10
    And foo is in set:
      | "a"     |
      | "aa"    |
      | "aaaa" |
      | "aaaaaaaaaa" |
      | "aaaaaaaaaaa" |
      | "aaaaaaaaaaaaa" |
      | 1      |
      | 2      |
      | 5      |
      | 10     |
      | 15     |
    Then the following data should be generated:
      | foo     |
      | null    |
      | "aaaaaaaaaaa" |
      | "aaaaaaaaaaaaa" |
      | 1      |
      | 2      |
      | 5      |
      | 10     |


  Scenario: Running a 'longerThan' request against non contradicting lessThanOrEqualTo should be successful
    Given foo is longer than 1
    And foo is anything but less than or equal to 10
    And foo is in set:
      | "a"     |
      | "aa"    |
      | "aaaa" |
      | "aaaaaaaaaa" |
      | "aaaaaaaaaaa" |
      | "aaaaaaaaaaaaa" |
      | 1      |
      | 2      |
      | 12      |
    Then the following data should be generated:
      | foo     |
      | null    |
      | "aa"   |
      | "aaaa" |
      | "aaaaaaaaaa" |
      | "aaaaaaaaaaa" |
      | "aaaaaaaaaaaaa" |
      | 12    |

  Scenario: Running a 'longerThan' request alongside a granularTo constraint should be successful
    Given foo is longer than 1
    And foo is granular to 0.1
    And foo is in set:
      | "a"     |
      | "aa"    |
      | "aaaa" |
      | "aaaaaaaaaa" |
      | "aaaaaaaaaaa" |
      | "aaaaaaaaaaaaa" |
      | 0.1      |

    Then the following data should be generated:
      | foo     |
      | null    |
      | "aa"   |
      | "aaaa" |
      | "aaaaaaaaaa" |
      | "aaaaaaaaaaa" |
      | "aaaaaaaaaaaaa" |
      | 0.1   |

  Scenario: Running a 'longerThan' request alongside a granularTo constraint should be successful
    Given foo is longer than 1
    And foo is anything but granular to 1
    And foo is in set:
      | "a"     |
      | "aa"    |
      | "aaaa" |
      | "aaaaaaaaaa" |
      | "aaaaaaaaaaa" |
      | "aaaaaaaaaaaaa" |
      | 1      |
      | 2      |

    Then the following data should be generated:
      | foo     |
      | null    |
      | "aa"   |
      | "aaaa" |
      | "aaaaaaaaaa" |
      | "aaaaaaaaaaa" |
      | "aaaaaaaaaaaaa" |
      | 1      |
      | 2      |

  Scenario: Running an 'longerThan' request alongside a after constraint should be successful
    Given foo is longer than 1
    And foo is after 2019-01-01T00:00:00.000
    And foo is in set:
      | "aa"   |
      | "aaaa" |
      | "aaaaaaaaaa" |
      | "2019-01-01T00:00:00.001" |
      |  2019-01-01T00:00:00.001   |

    Then the following data should be generated:
      | foo     |
      | null    |
      | "aa"   |
      | "aaaa" |
      | "aaaaaaaaaa" |
      | "2019-01-01T00:00:00.001" |
      |  2019-01-01T00:00:00.001   |


  Scenario: Running an 'longerThan' request alongside a after constraint should be successful
    Given foo is longer than 1
    And foo is anything but after 2019-01-01T00:00:00.000
    And foo is in set:
      | "a"    |
      | "aa"   |
      | "aaaa" |
      | "aaaaaaaaaa" |
      | "2019-01-01T00:00:00.000" |
      |  2019-01-01T00:00:00.000   |

    Then the following data should be generated:
      | foo     |
      | null    |
      | "aa"   |
      | "aaaa" |
      | "aaaaaaaaaa" |
      | "2019-01-01T00:00:00.000" |
      |  2019-01-01T00:00:00.000  |

  Scenario: Running an 'longerThan' request alongside a afterOrAt  constraint should be successful
    Given foo is longer than 1
    And foo is after or at 2019-01-01T00:00:00.000
    And foo is in set:
      | "a"    |
      | "aa"   |
      | "aaaa" |
      | "aaaaaaaaaa" |
      | "2019-01-01T00:00:00.000" |
      | "2018-09-01T00:00:00.001" |
      | 2019-01-01T00:00:00.000   |
      | 2019-01-01T00:00:00.001   |
    Then the following data should be generated:
      | foo     |
      | null    |
      | "aa"   |
      | "aaaa" |
      | "aaaaaaaaaa" |
      | "2019-01-01T00:00:00.000" |
      | "2018-09-01T00:00:00.001" |
      | 2019-01-01T00:00:00.000   |
      | 2019-01-01T00:00:00.001   |

  Scenario: Running an 'longerThan' request alongside a afterOrAt  constraint should be successful
    Given foo is longer than 1
    And foo is anything but after or at 2019-01-01T00:00:00.000
    And foo is in set:
      | "a"    |
      | "aa"   |
      | "aaaa" |
      | "aaaaaaaaaa" |
      | "2018-01-01T00:00:00.001" |
      | "2019-01-01T00:00:00.000" |
      | 2018-01-01T00:00:00.001   |
      | 2019-01-01T00:00:00.000   |
    Then the following data should be generated:
      | foo     |
      | null    |
      | "aa"   |
      | "aaaa" |
      | "aaaaaaaaaa" |
      | "2018-01-01T00:00:00.001" |
      |  2018-01-01T00:00:00.001   |

