Feature: User can specify the length of generated string data using 'ofLength'

  Background:
    Given the generation strategy is full
    And there is a non nullable field foo
    And foo has type "string"

  Scenario Outline: Running an 'ofLength' request on a roman alphabet character string should be successful
    Given foo is of length <length>
    And foo is in set:
      | "a"  |
      | "aa" |
    Then the following data should be generated:
      | foo        |
      | null       |
      | <expected> |
    Examples:
      | length | expected |
      | 1      | "a"      |
      | 2      | "aa"     |

  Scenario Outline: Running an 'ofLength' request that includes a negation of a valid numeric length should be successful
    Given foo is anything but of length <length>
    And foo is in set:
      | "a"  |
      | "aa" |
    Then the following data should be generated:
      | foo        |
      | null       |
      | <expected> |
    Examples:
      | length | expected |
      | 1      | "aa"     |
      | 2      | "a"      |

  Scenario: Running an 'ofLength' request on a roman alphabet character string value and white space should be successful
    Given foo is of length 10
    And foo is in set:
      | "a"          |
      | "  10  10  " |
    Then the following data should be generated:
      | foo          |
      | null         |
      | "  10  10  " |

  Scenario: Running an 'ofLength' for a length of zero should be successful
    Given foo is of length 0
    And foo is in set:
      | ""  |
      | "a" |
    Then the following data should be generated:
      | foo  |
      | null |
      | ""   |

  Scenario Outline: Running an 'ofLength' request that includes a invalid value should fail with an error
    Given foo is of length <length>
    And foo is in set:
      | "a" |
    Then the profile is invalid because "(Couldn't recognise 'value' property, it must be an Integer but was a String with value `.*`)|(Cannot create an StringHasLengthConstraint for field 'foo' with a a negative length.)|(String length must have a value >= 0, currently is -?\d+)"
    And no data is created
    Examples:
      | length                    |
      | -1                        |

# COMBINATION OF CONSTRAINTS #

  Scenario: ofLength run against a non contradicting ofLength should be successful
    Given foo is of length 1
    And foo is of length 1
    And foo is in set:
      | "1"  |
      | "a"  |
      | "22" |
    Then the following data should be generated:
      | foo  |
      | null |
      | "1"  |
      | "a"  |

  Scenario: ofLength run against a non contradicting not ofLength should be successful
    Given foo is of length 1
    And foo is anything but of length 2
    And foo is in set:
      | "1"  |
      | "a"  |
      | "22" |
    Then the following data should be generated:
      | foo  |
      | null |
      | "1"  |
      | "a"  |

  Scenario: ofLength run against a contradicting ofLength should only generate null
    Given foo is of length 1
    And foo is of length 2
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: ofLength run against a contradicting not ofLength should only generate null
    Given foo is of length 1
    And foo is anything but of length 1
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: ofLength run against a non contradicting longerThan should be successful
    Given foo is of length 2
    And foo is longer than 1
    And foo is in set:
      | "1"  |
      | "a"  |
      | "22" |
    Then the following data should be generated:
      | foo  |
      | null |
      | "22" |

  Scenario: ofLength run against a non contradicting not longerThan should be successful
    Given foo is of length 2
    And foo is anything but longer than 5
    And foo is in set:
      | "1"  |
      | "a"  |
      | "22" |
    Then the following data should be generated:
      | foo  |
      | null |
      | "22" |

  Scenario: not ofLength run against a non contradicting longerThan should be successful
    Given foo is anything but of length 1
    And foo is longer than 2
    And foo is in set:
      | "1"   |
      | "a"   |
      | "222" |
    Then the following data should be generated:
      | foo   |
      | null  |
      | "222" |

  Scenario: not ofLength run against a non contradicting not longerThan should be successful
    Given foo is anything but of length 2
    And foo is anything but longer than 2
    And foo is in set:
      | "1"   |
      | "aa"  |
      | "222" |
    Then the following data should be generated:
      | foo  |
      | null |
      | "1"  |

  Scenario: ofLength run against a contradicting longerThan should only generate null
    Given foo is of length 1
    And foo is longer than 1
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: ofLength run against a contradicting not longerThan should only generate null
    Given foo is of length 4
    And foo is anything but longer than 1
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: ofLength run against a non contradicting shorterThan should be successful
    Given foo is of length 1
    And foo is shorter than 2
    And foo is in set:
      | "1"  |
      | "a"  |
      | "22" |
    Then the following data should be generated:
      | foo  |
      | null |
      | "1"  |
      | "a"  |

  Scenario: ofLength run against a non contradicting not shorterThan should be successful
    Given foo is of length 1
    And foo is anything but shorter than 1
    And foo is in set:
      | "1"  |
      | "a"  |
      | "22" |
    Then the following data should be generated:
      | foo  |
      | null |
      | "1"  |
      | "a"  |

  Scenario: not ofLength run against a non contradicting shorterThan should be successful
    Given foo is anything but of length 1
    And foo is shorter than 3
    And foo is in set:
      | "1"   |
      | "22"  |
      | "333" |
    Then the following data should be generated:
      | foo  |
      | null |
      | "22" |

  Scenario: not ofLength run against a non contradicting not shorterThan should be successful
    Given foo is anything but of length 2
    And foo is anything but shorter than 2
    And foo is in set:
      | "1"   |
      | "22"  |
      | "333" |
    Then the following data should be generated:
      | foo   |
      | null  |
      | "333" |

  Scenario: ofLength run against a contradicting shorterThan should only generate null
    Given foo is of length 2
    And foo is shorter than 2
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: ofLength run against a contradicting not shorterThan should only generate null
    Given foo is of length 2
    And foo is anything but shorter than 3
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: ofLength with maximum permitted value should be successful
    Given foo is of length 1000
    And the generator can generate at most 1 rows
    And the generation strategy is random
    And foo is anything but null
    Then foo contains strings of length between 1000 and 1000 inclusively

  Scenario: ofLength with value larger than maximum permitted should fail with an error message
    Given foo is of length 1001
    Then the profile is invalid because "String length must have a value <= 1000, currently is 1001"
