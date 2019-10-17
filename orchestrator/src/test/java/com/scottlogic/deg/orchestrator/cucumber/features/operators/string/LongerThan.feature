Feature: User can specify that a string length is longer than, a specified number of characters

  Background:
    Given the generation strategy is full
    And there is a field foo
    And foo has type "string"

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
    Then the profile is invalid because "String length must have a value >= 0, currently is -5"
    And no data is created

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

  Scenario: A longer than constraint combined with an ISIN constraint generates valid ISINs if the operand of the longer than constraint is less than the length of a valid ISIN
    Given foo is longer than 11
    And foo has type "ISIN"
    And foo is in set:
      | "GB00YG2XYC52" |
      | "US0378331005" |
      | "twelvedigits" |
    Then the following data should be generated:
      | foo            |
      | null           |
      | "GB00YG2XYC52" |
      | "US0378331005" |

  Scenario: A not longer than constraint combined with an ISIN constraint generates valid ISINs if the operand of the not longer than constraint is at least as large as the length of a valid ISIN
    Given foo is anything but longer than 12
    And foo has type "ISIN"
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
    And foo has type "ISIN"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: A longer than constraint combined with a SEDOL constraint generates valid SEDOLs if the operand of the longer than constraint is less than the length of a valid SEDOL
    Given foo is longer than 6
    And foo has type "SEDOL"
    And foo is in set:
      | "0263494" |
      | "3091357" |
      | "string7" |
    Then the following data should be generated:
      | foo       |
      | null      |
      | "0263494" |
      | "3091357" |

  Scenario: A not longer than constraint combined with a SEDOL constraint generates valid SEDOLs if the operand of the not longer than constraint is at least as large as the length of a valid SEDOL
    Given foo is anything but longer than 7
    And foo has type "SEDOL"
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
    And foo has type "SEDOL"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: A longer than constraint combined with a CUSIP constraint generates valid CUSIPs if the operand of the longer than constraint is less than the length of a valid CUSIP
    Given foo is longer than 8
    And foo has type "CUSIP"
    And foo is in set:
      | "38259P508" |
      | "594918104" |
      | "strngnine" |
    Then the following data should be generated:
      | foo         |
      | null        |
      | "38259P508" |
      | "594918104" |

  Scenario: A not longer than constraint combined with a CUSIP constraint generates valid CUSIPs if the operand of the longer than constraint is at least as large as the length of a valid CUSIP
    Given foo is anything but longer than 9
    And foo has type "CUSIP"
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
    And foo has type "CUSIP"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: longerThan with maximum permitted value should be successful
    Given foo is longer than 999
    And the generation strategy is random
    And the generator can generate at most 1 rows
    And foo is anything but null
    Then foo contains strings of length between 1000 and 1000 inclusively

  Scenario: longerThan with value larger than maximum permitted should fail with an error message
    Given foo is longer than 1001
    And foo has type "string"
    Then the profile is invalid because "String length must have a value <= 1000, currently is 1001"

  Scenario: Running a 'longerThan' request with a value less than default max (1000) should generate data of length between value and 1000
    Given foo has type "string"
    And foo is longer than 999
    And the generator can generate at most 20 rows
    Then foo contains strings of length between 1000 and 1000 inclusively
