Feature: Values can be specified by using if, then and else constraints

  Background:
    Given the generation strategy is full
    And the combination strategy is exhaustive
    And the following fields exist:
      | foo |
      | bar |

  Scenario: Running an 'if' constraint for then condition only should be successful
    Given foo is in set:
      | "a" |
      | "b" |
    And foo has type "string"
    And foo is anything but null
    And bar is in set:
      | 10 |
      | 20 |
    And bar is anything but null
    And bar has type "decimal"
    And If and Then are described below
    And foo is equal to "a"
    And bar is equal to 10
    Then the following data should be generated:
      | foo | bar |
      | "a" | 10  |
      | "b" | 10  |
      | "b" | 20  |

  Scenario: Running an 'if' constraint for then and else conditions should be successful
    Given foo is in set:
      | "a" |
      | "b" |
    And foo is anything but null
    And foo has type "string"
    And bar is anything but null
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to "a"
    And bar is equal to 3
    And bar is equal to 5
    Then the following data should be generated:
      | foo | bar |
      | "a" | 3   |
      | "b" | 5   |

  Scenario: Running an 'if' request that contains a valid anyOf statement should be successful
    Given foo is in set:
      | 1 |
      | 2 |
      | 3 |
      | 4 |
    And foo is anything but null
    And bar is in set:
      | "a" |
      | "b" |
    And foo has type "decimal"
    And bar has type "string"
    And bar is anything but null
    When If Then and Else are described below
    And Any Of the next 2 constraints
      And foo is equal to 1
      And foo is equal to 2
    And bar is equal to "a"
    And bar is equal to "b"
    Then the following data should be generated:
      | foo | bar |
      | 1   | "a" |
      | 2   | "a" |
      | 3   | "b" |
      | 4   | "b" |

  Scenario: Running an 'if' request that contains a valid allOf statement should be successful
    Given foo is in set:
      | 1 |
      | 2 |
      | 3 |
      | 4 |
    And foo is anything but null
    And bar is in set:
      | "a" |
      | "b" |
    And foo has type "decimal"
    And bar has type "string"
    And bar is anything but null
    When If Then and Else are described below
    And All Of the next 2 constraints
      And foo is greater than 1
      And foo is less than 4
    And bar is equal to "a"
    And bar is equal to "b"
    Then the following data should be generated:
      | foo | bar |
      | 1   | "b" |
      | 2   | "a" |
      | 3   | "a" |
      | 4   | "b" |

  Scenario: Running an 'if' request that contains a valid not statement should be successful
    Given foo is in set:
      | 1 |
      | 2 |
      | 3 |
      | 4 |
    And foo is anything but null
    And bar is in set:
      | "a" |
      | "b" |
    And foo has type "decimal"
    And bar has type "string"
    And bar is anything but null
    When If Then and Else are described below
    And foo is anything but equal to 1
    And bar is equal to "a"
    And bar is equal to "b"
    Then the following data should be generated:
      | foo | bar |
      | 1   | "b" |
      | 2   | "a" |
      | 3   | "a" |
      | 4   | "a" |

  Scenario: Running an 'if' request that contains a valid nested if statement should be successful
    Given foo is in set:
      | 1 |
      | 2 |
      | 3 |
      | 4 |
    And foo is anything but null
    And bar is in set:
      | "a" |
      | "b" |
      | "c" |
    And foo has type "decimal"
    And bar has type "string"
    And bar is anything but null
    When If Then and Else are described below
    And foo is greater than 1
      When If Then and Else are described below
      And foo is greater than 3
      And bar is equal to "a"
      And bar is equal to "b"
    And bar is equal to "c"
    Then the following data should be generated:
      | foo | bar |
      | 1   | "c" |
      | 2   | "b" |
      | 3   | "b" |
      | 4   | "a" |

  Scenario: Running an 'if' request that is invalidly formatted (missing a then statement) should be successful
    Given foo is in set:
      | 1 |
      | 2 |
      | 3 |
      | 4 |
    And foo is anything but null
    And bar is in set:
      | "a" |
      | "b" |
      | "c" |
    And foo has type "decimal"
    And bar has type "string"
    And bar is anything but null
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 1 },
        "else": { "field": "bar", "is": "equalTo", "value": "a" }
      }
      """
    Then the profile is invalid because "Constraint is null"
    And no data is created

  Scenario: Running a 'if' request that includes an invalid if value (not in field set) should be successful
    Given foo is in set:
      | 1 |
      | 2 |
      | 3 |
      | 4 |
    And foo is anything but null
    And bar is in set:
      | "a" |
      | "b" |
      | "c" |
    And foo has type "decimal"
    And bar has type "string"
    And bar is anything but null
    When If Then and Else are described below
    And foo is equal to 5
    And bar is equal to "a"
    And bar is equal to "b"
    Then the following data should be generated:
      | foo | bar |
      | 1   | "b" |
      | 2   | "b" |
      | 3   | "b" |
      | 4   | "b" |

  Scenario: Running a 'if' request that includes an invalid then value (not in field set) should be successful
    Given foo is in set:
      | 1 |
      | 2 |
      | 3 |
      | 4 |
    And foo is anything but null
    And bar is in set:
      | "a" |
      | "b" |
      | "c" |
    And foo has type "decimal"
    And bar has type "string"
    And bar is anything but null
    When If Then and Else are described below
    And foo is equal to 1
    And bar is equal to "X"
    And bar is equal to "b"
    Then the following data should be generated:
      | foo | bar |
      | 2   | "b" |
      | 3   | "b" |
      | 4   | "b" |

  Scenario: Running a 'if' request that includes an invalid else value (not in field set) should be successful
    Given foo is in set:
      | 1 |
      | 2 |
      | 3 |
      | 4 |
    And foo is anything but null
    And bar is in set:
      | "a" |
      | "b" |
      | "c" |
    And foo has type "decimal"
    And bar has type "string"
    And bar is anything but null
    When If Then and Else are described below
    And foo is equal to 1
    And bar is equal to "a"
    And bar is equal to "X"
    Then the following data should be generated:
      | foo | bar |
      | 1   | "a" |

  Scenario: Running an if request that contains a non contradictory inSet constraint within its if statement should be successful
    Given foo is in set:
      | 1 |
      | 2 |
      | 3 |
      | 4 |
    And foo is anything but null
    And bar is in set:
      | "a" |
      | "b" |
      | "c" |
    And foo has type "decimal"
    And bar has type "string"
    And bar is anything but null
    When If Then and Else are described below
    And foo is in set:
      | 1 |
      | 2 |
    And bar is equal to "a"
    And bar is equal to "b"
    Then the following data should be generated:
      | foo | bar |
      | 1   | "a" |
      | 2   | "a" |
      | 3   | "b" |
      | 4   | "b" |

  Scenario: Running an if request that contains a non contradictory inSet constraint within its then statement should be successful
    Given foo is in set:
      | 1 |
      | 2 |
      | 3 |
      | 4 |
    And foo is anything but null
    And bar is in set:
      | "a" |
      | "b" |
      | "c" |
    And foo has type "decimal"
    And bar has type "string"
    And bar is anything but null
    When If Then and Else are described below
    And foo is equal to 1
    And bar is in set:
      | "a" |
      | "b" |
    And bar is equal to "c"
    Then the following data should be generated:
      | foo | bar |
      | 1   | "a" |
      | 1   | "b" |
      | 2   | "c" |
      | 3   | "c" |
      | 4   | "c" |

  Scenario: Running an if request that contains a non contradictory inSet constraint within its else statement should be successful
    Given foo is in set:
      | 1 |
      | 2 |
      | 3 |
      | 4 |
    And foo is anything but null
    And bar is in set:
      | "a" |
      | "b" |
      | "c" |
    And foo has type "decimal"
    And bar has type "string"
    And bar is anything but null
    When If Then and Else are described below
    And foo is equal to 1
    And bar is equal to "b"
    And bar is in set:
      | "a" |
      | "c" |
    Then the following data should be generated:
      | foo | bar |
      | 1   | "b" |
      | 2   | "a" |
      | 2   | "c" |
      | 3   | "a" |
      | 3   | "c" |
      | 4   | "a" |
      | 4   | "c" |

  Scenario: Running an if request that contains a contradictory inSet constraint within its if statement should be successful
    Given foo is in set:
      | 1 |
      | 2 |
      | 3 |
      | 4 |
    And foo is anything but null
    And bar is in set:
      | "a" |
      | "b" |
      | "c" |
    And foo has type "decimal"
    And bar has type "string"
    And bar is anything but null
    When If Then and Else are described below
    And foo is in set:
      | 8 |
      | 9 |
    And bar is equal to "a"
    And bar is equal to "b"
    Then the following data should be generated:
      | foo | bar |
      | 1   | "b" |
      | 2   | "b" |
      | 3   | "b" |
      | 4   | "b" |

  Scenario: Running an if request that contains a contradictory inSet constraint within its else statement should be successful
    Given foo is in set:
      | 1 |
      | 2 |
      | 3 |
      | 4 |
    And foo is anything but null
    And bar is in set:
      | "a" |
      | "b" |
      | "c" |
    And foo has type "decimal"
    And bar has type "string"
    And bar is anything but null
    When If Then and Else are described below
    And foo is equal to 1
    And bar is equal to "a"
    And bar is in set:
      | "X" |
      | "Y" |
    Then the following data should be generated:
      | foo | bar |
      | 1   | "a" |

  Scenario: Running an if request that contains a non contradictory null constraint within its if statement should be successful
    Given foo is in set:
      | 2 |
      | 3 |
      | 4 |
    And bar is in set:
      | "b" |
      | "c" |
    And foo has type "decimal"
    And bar has type "string"
    When If Then and Else are described below
    And foo is null
    And bar is equal to "b"
    And bar is equal to "c"
    Then the following data should be generated:
      | foo  | bar  |
      | null | "b"  |
      | 2    | "c"  |
      | 3    | "c"  |
      | 4    | "c"  |

  Scenario: Running an if request that contains a non contradictory null constraint within its then statement should be successful
    Given foo is in set:
      | 2 |
      | 3 |
      | 4 |
    And bar is in set:
      | "b" |
      | "c" |
    And foo has type "decimal"
    And bar has type "string"
    When If Then and Else are described below
    And foo is equal to 2
    And bar is null
    And bar is equal to "c"
    Then the following data should be generated:
      | foo  | bar  |
      | null | "c"  |
      | 2    | null |
      | 3    | "c"  |
      | 4    | "c"  |

  Scenario: Running an if request that contains a non contradictory null constraint within its else statement should be successful
    Given foo is in set:
      | 2 |
      | 3 |
      | 4 |
    And bar is in set:
      | "b" |
      | "c" |
    And foo has type "decimal"
    And bar has type "string"
    When If Then and Else are described below
    And foo is equal to 2
    And bar is equal to "b"
    And bar is null
    Then the following data should be generated:
      | foo  | bar  |
      | null | null |
      | 2    | "b"  |
      | 3    | null |
      | 4    | null |

  Scenario: Running an if request that contains a contradictory null constraint within its if statement should be successful
    Given foo is in set:
      | 1 |
      | 2 |
      | 3 |
      | 4 |
    And foo is anything but null
    And bar is in set:
      | "a" |
      | "b" |
      | "c" |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "string"
    When If Then and Else are described below
    And foo is null
    And bar is equal to "a"
    And bar is equal to "b"
    Then the following data should be generated:
      | foo | bar |
      | 1   | "b" |
      | 2   | "b" |
      | 3   | "b" |
      | 4   | "b" |

  Scenario: Running an if request that contains a contradictory null constraint within its then statement should be successful
    Given foo is in set:
      | 1 |
      | 2 |
      | 3 |
      | 4 |
    And foo is anything but null
    And bar is in set:
      | "a" |
      | "b" |
      | "c" |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "string"
    When If Then and Else are described below
    And foo is equal to 1
    And bar is null
    And bar is equal to "a"
    Then the following data should be generated:
      | foo | bar |
      | 2   | "a" |
      | 3   | "a" |
      | 4   | "a" |

  Scenario: Running an if request that contains a contradictory null constraint within its else statement should be successful
    Given foo is in set:
      | 1 |
      | 2 |
      | 3 |
      | 4 |
    And foo is anything but null
    And bar is in set:
      | "a" |
      | "b" |
      | "c" |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "string"
    When If Then and Else are described below
    And foo is equal to 1
    And bar is equal to "a"
    And bar is null
    Then the following data should be generated:
      | foo | bar |
      | 1   | "a" |

  Scenario: Running an if request that contains a non contradictory matchingRegex constraint within its if statement should be successful
    Given foo is in set:
      | "1" |
      | "2" |
      | "a" |
      | "b" |
    And foo is anything but null
    And bar is in set:
      | "10" |
      | "20" |
      | "AA" |
      | "BB" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is matching regex /[a-z]{1}/
    And bar is equal to "AA"
    And bar is equal to "10"
    Then the following data should be generated:
      | foo | bar  |
      | "1" | "10" |
      | "2" | "10" |
      | "a" | "AA" |
      | "b" | "AA" |

  Scenario: Running an if request that contains a non contradictory matchingRegex constraint within its then statement should be successful
    Given foo is in set:
      | "1" |
      | "2" |
      | "a" |
      | "b" |
    And foo is anything but null
    And bar is in set:
      | "10" |
      | "20" |
      | "AA" |
      | "BB" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is equal to "1"
    And bar is matching regex /[A-Z]{2}/
    And bar is equal to "10"
    Then the following data should be generated:
      | foo | bar  |
      | "1" | "AA" |
      | "1" | "BB" |
      | "2" | "10" |
      | "a" | "10" |
      | "b" | "10" |

  Scenario: Running an if request that contains a non contradictory matchingRegex constraint within its else statement should be successful
    Given foo is in set:
      | "1" |
      | "2" |
      | "a" |
      | "b" |
    And foo is anything but null
    And foo has type "string"
    And bar has type "string"
    And bar is in set:
      | "10" |
      | "20" |
      | "AA" |
      | "BB" |
    And bar is anything but null
    When If Then and Else are described below
    And foo is equal to "1"
    And bar is equal to "AA"
    And bar is matching regex /[0-9]{2}/
    Then the following data should be generated:
      | foo | bar  |
      | "1" | "AA" |
      | "2" | "10" |
      | "2" | "20" |
      | "a" | "10" |
      | "a" | "20" |
      | "b" | "10" |
      | "b" | "20" |

  Scenario: Running an if request that contains a contradictory matchingRegex constraint within its if statement should be successful
    Given foo is in set:
      | "1" |
      | "2" |
      | "a" |
      | "b" |
    And foo is anything but null
    And foo has type "string"
    And bar has type "string"
    And bar is in set:
      | "10" |
      | "20" |
      | "AA" |
      | "BB" |
    And bar is anything but null
    When If Then and Else are described below
    And foo is matching regex /[0-9]{2}/
    And bar is equal to "AA"
    And bar is equal to "10"
    Then the following data should be generated:
      | foo | bar  |
      | "1" | "10" |
      | "2" | "10" |
      | "a" | "10" |
      | "b" | "10" |

  Scenario: Running an if request that contains a contradictory matchingRegex constraint within its then statement should be successful
    Given foo is in set:
      | "1" |
      | "2" |
      | "a" |
      | "b" |
    And foo is anything but null
    And bar is in set:
      | "10" |
      | "20" |
      | "AA" |
      | "BB" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is equal to "a"
    And bar is matching regex /C/
    And bar is equal to "10"
    Then the following data should be generated:
      | foo | bar  |
      | "1" | "10" |
      | "2" | "10" |
      | "b" | "10" |

  Scenario: Running an if request that contains a contradictory matchingRegex constraint within its else statement should be successful
    Given foo is in set:
      | "1" |
      | "2" |
      | "a" |
      | "b" |
    And foo is anything but null
    And bar is in set:
      | "10" |
      | "20" |
      | "AA" |
      | "BB" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is equal to "a"
    And bar is equal to "BB"
    And bar is matching regex /WRONG/
    Then the following data should be generated:
      | foo | bar  |
      | "a" | "BB" |

  Scenario: Running an if request that contains a non contradictory containingRegex constraint within its if statement should be successful
    Given foo is in set:
      | "1"  |
      | "2"  |
      | "a1" |
      | "b2" |
    And foo is anything but null
    And bar is in set:
      | "10" |
      | "20" |
      | "AA" |
      | "BB" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is containing regex /[1]{1}/
    And bar is equal to "AA"
    And bar is equal to "10"
    Then the following data should be generated:
      | foo  | bar  |
      | "1"  | "AA" |
      | "2"  | "10" |
      | "a1" | "AA" |
      | "b2" | "10" |

  Scenario: Running an if request that contains a non contradictory containingRegex constraint within its then statement should be successful
    Given foo is in set:
      | "1"  |
      | "2"  |
      | "a1" |
      | "b2" |
    And foo is anything but null
    And foo has type "string"
    And bar has type "string"
    And bar is in set:
      | "10" |
      | "20" |
      | "AA" |
      | "BB" |
    And bar is anything but null
    When If Then and Else are described below
    And foo is equal to "1"
    And bar is containing regex /[0]{1}/
    And bar is equal to "AA"
    Then the following data should be generated:
      | foo  | bar  |
      | "1"  | "10" |
      | "1"  | "20" |
      | "2"  | "AA" |
      | "a1" | "AA" |
      | "b2" | "AA" |

  Scenario: Running an if request that contains a non contradictory containingRegex constraint within its else statement should be successful
    Given foo is in set:
      | "1"  |
      | "2"  |
      | "a1" |
      | "b2" |
    And foo is anything but null
    And bar is in set:
      | "10" |
      | "20" |
      | "AA" |
      | "BB" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is equal to "1"
    And bar is equal to "BB"
    And bar is containing regex /[0]{1}/
    Then the following data should be generated:
      | foo  | bar  |
      | "1"  | "BB" |
      | "2"  | "10" |
      | "2"  | "20" |
      | "a1" | "10" |
      | "a1" | "20" |
      | "b2" | "10" |
      | "b2" | "20" |

  Scenario: Running an if request that contains a contradictory containingRegex constraint within its if statement should be successful
    Given foo is in set:
      | "1"  |
      | "2"  |
      | "a1" |
      | "b2" |
    And foo is anything but null
    And bar is in set:
      | "10" |
      | "20" |
      | "AA" |
      | "BB" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is containing regex /[🚫]{1}/
    And bar is equal to "AA"
    And bar is equal to "10"
    Then the following data should be generated:
      | foo  | bar  |
      | "1"  | "10" |
      | "2"  | "10" |
      | "a1" | "10" |
      | "b2" | "10" |

  Scenario: Running an if request that contains a contradictory containingRegex constraint within its then statement should be successful
    Given foo is in set:
      | "1"  |
      | "2"  |
      | "a1" |
      | "b2" |
    And foo is anything but null
    And bar is in set:
      | "10" |
      | "20" |
      | "AA" |
      | "BB" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is equal to "1"
    And bar is containing regex /[🚫]{1}/
    And bar is equal to "10"
    Then the following data should be generated:
      | foo  | bar  |
      | "2"  | "10" |
      | "a1" | "10" |
      | "b2" | "10" |

  Scenario: Running an if request that contains a contradictory containingRegex constraint within its then statement should be successful
    Given foo is in set:
      | "1"  |
      | "2"  |
      | "a1" |
      | "b2" |
    And foo is anything but null
    And bar is in set:
      | "10" |
      | "20" |
      | "AA" |
      | "BB" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is equal to "1"
    And bar is equal to "10"
    And bar is containing regex /[🚫]{1}/
    Then the following data should be generated:
      | foo | bar  |
      | "1" | "10" |

  Scenario: Running an if request that contains a non contradictory ofLength constraint within its if statement should be successful
    Given foo is in set:
      | "a"    |
      | "bb"   |
      | "ccc"  |
      | "dddd" |
    And foo is anything but null
    And bar is in set:
      | "1"    |
      | "22"   |
      | "333"  |
      | "4444" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is of length 2
    And bar is equal to "1"
    And bar is equal to "4444"
    Then the following data should be generated:
      | foo    | bar    |
      | "a"    | "4444" |
      | "bb"   | "1"    |
      | "ccc"  | "4444" |
      | "dddd" | "4444" |

  Scenario: Running an if request that contains a non contradictory ofLength constraint within its then statement should be successful
    Given foo is in set:
      | "a"    |
      | "bb"   |
      | "ccc"  |
      | "dddd" |
    And foo is anything but null
    And bar is in set:
      | "1"    |
      | "22"   |
      | "333"  |
      | "4444" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is equal to "bb"
    And bar is of length 3
    And bar is equal to "1"
    Then the following data should be generated:
      | foo    | bar   |
      | "a"    | "1"   |
      | "bb"   | "333" |
      | "ccc"  | "1"   |
      | "dddd" | "1"   |

  Scenario: Running an if request that contains a non contradictory ofLength constraint within its else statement should be successful
    Given foo is in set:
      | "a"    |
      | "bb"   |
      | "ccc"  |
      | "dddd" |
    And foo is anything but null
    And bar is in set:
      | "1"    |
      | "22"   |
      | "333"  |
      | "4444" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is equal to "a"
    And bar is equal to "1"
    And bar is of length 4
    Then the following data should be generated:
      | foo    | bar    |
      | "a"    | "1"    |
      | "bb"   | "4444" |
      | "ccc"  | "4444" |
      | "dddd" | "4444" |

  Scenario: Running an if request that contains a contradictory ofLength constraint within its if statement should be successful
    Given foo is in set:
      | "a"    |
      | "bb"   |
      | "ccc"  |
      | "dddd" |
    And foo is anything but null
    And bar is in set:
      | "1"    |
      | "22"   |
      | "333"  |
      | "4444" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is of length 7
    And bar is equal to "1"
    And bar is equal to "4444"
    Then the following data should be generated:
      | foo    | bar    |
      | "a"    | "4444" |
      | "bb"   | "4444" |
      | "ccc"  | "4444" |
      | "dddd" | "4444" |

  Scenario: Running an if request that contains a contradictory ofLength constraint within its then statement should be successful
    Given foo is in set:
      | "a"    |
      | "bb"   |
      | "ccc"  |
      | "dddd" |
    And foo is anything but null
    And bar is in set:
      | "1"    |
      | "22"   |
      | "333"  |
      | "4444" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is equal to "a"
    And bar is of length 10
    And bar is equal to "4444"
    Then the following data should be generated:
      | foo    | bar    |
      | "bb"   | "4444" |
      | "ccc"  | "4444" |
      | "dddd" | "4444" |

  Scenario: Running an if request that contains a contradictory ofLength constraint within its else statement should be successful
    Given foo is in set:
      | "a"    |
      | "bb"   |
      | "ccc"  |
      | "dddd" |
    And foo is anything but null
    And bar is in set:
      | "1"    |
      | "22"   |
      | "333"  |
      | "4444" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is equal to "a"
    And bar is equal to "1"
    And bar is of length 10
    Then the following data should be generated:
      | foo | bar |
      | "a" | "1" |

  Scenario: Running an if request that contains a non contradictory longerThan constraint within its if statement should be successful
    Given foo is in set:
      | "a"    |
      | "bb"   |
      | "ccc"  |
      | "dddd" |
    And foo is anything but null
    And bar is in set:
      | "1"    |
      | "22"   |
      | "333"  |
      | "4444" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is longer than 2
    And bar is equal to "1"
    And bar is equal to "4444"
    Then the following data should be generated:
      | foo    | bar    |
      | "a"    | "4444" |
      | "bb"   | "4444" |
      | "ccc"  | "1"    |
      | "dddd" | "1"    |

  Scenario: Running an if request that contains a non contradictory longerThan constraint within its then statement should be successful
    Given foo is in set:
      | "a"    |
      | "bb"   |
      | "ccc"  |
      | "dddd" |
    And foo is anything but null
    And bar is in set:
      | "1"    |
      | "22"   |
      | "333"  |
      | "4444" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is equal to "bb"
    And bar is longer than 2
    And bar is equal to "1"
    Then the following data should be generated:
      | foo    | bar    |
      | "a"    | "1"    |
      | "bb"   | "333"  |
      | "bb"   | "4444" |
      | "ccc"  | "1"    |
      | "dddd" | "1"    |

  Scenario: Running an if request that contains a non contradictory longerThan constraint within its else statement should be successful
    Given foo is in set:
      | "a"    |
      | "bb"   |
      | "ccc"  |
      | "dddd" |
    And foo is anything but null
    And bar is in set:
      | "1"    |
      | "22"   |
      | "333"  |
      | "4444" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is equal to "dddd"
    And bar is equal to "1"
    And bar is longer than 2
    Then the following data should be generated:
      | foo    | bar    |
      | "a"    | "333"  |
      | "a"    | "4444" |
      | "bb"   | "333"  |
      | "bb"   | "4444" |
      | "ccc"  | "333"  |
      | "ccc"  | "4444" |
      | "dddd" | "1"    |

  Scenario: Running an if request that contains a non contradictory shorterThan constraint within its if statement should be successful
    Given foo is in set:
      | "a"    |
      | "bb"   |
      | "ccc"  |
      | "dddd" |
    And foo is anything but null
    And bar is in set:
      | "1"    |
      | "22"   |
      | "333"  |
      | "4444" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is shorter than 3
    And bar is equal to "1"
    And bar is equal to "4444"
    Then the following data should be generated:
      | foo    | bar    |
      | "a"    | "1"    |
      | "bb"   | "1"    |
      | "ccc"  | "4444" |
      | "dddd" | "4444" |

  Scenario: Running an if request that contains a non contradictory shorterThan constraint within its then statement should be successful
    Given foo is in set:
      | "a"    |
      | "bb"   |
      | "ccc"  |
      | "dddd" |
    And foo is anything but null
    And bar is in set:
      | "1"    |
      | "22"   |
      | "333"  |
      | "4444" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is equal to "ccc"
    And bar is shorter than 3
    And bar is equal to "333"
    Then the following data should be generated:
      | foo    | bar   |
      | "a"    | "333" |
      | "bb"   | "333" |
      | "ccc"  | "1"   |
      | "ccc"  | "22"  |
      | "dddd" | "333" |

  Scenario: Running an if request that contains a non contradictory shorterThan constraint within its else statement should be successful
    Given foo is in set:
      | "a"    |
      | "bb"   |
      | "ccc"  |
      | "dddd" |
    And foo is anything but null
    And bar is in set:
      | "1"    |
      | "22"   |
      | "333"  |
      | "4444" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is equal to "dddd"
    And bar is equal to "4444"
    And bar is shorter than 4
    Then the following data should be generated:
      | foo    | bar    |
      | "a"    | "1"    |
      | "a"    | "22"   |
      | "a"    | "333"  |
      | "bb"   | "1"    |
      | "bb"   | "22"   |
      | "bb"   | "333"  |
      | "ccc"  | "1"    |
      | "ccc"  | "22"   |
      | "ccc"  | "333"  |
      | "dddd" | "4444" |

  Scenario: Running an if request that contains a contradictory shorterThan constraint within its then statement should be successful
    Given foo is in set:
      | "a"    |
      | "bb"   |
      | "ccc"  |
      | "dddd" |
    And foo is anything but null
    And bar is in set:
      | "1"    |
      | "22"   |
      | "333"  |
      | "4444" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is equal to "dddd"
    And bar is shorter than 1
    And bar is equal to "4444"
    Then the following data should be generated:
      | foo   | bar    |
      | "a"   | "4444" |
      | "bb"  | "4444" |
      | "ccc" | "4444" |

  Scenario: Running an if request that contains a contradictory shorterThan constraint within its else statement should be successful
    Given foo is in set:
      | "a"    |
      | "bb"   |
      | "ccc"  |
      | "dddd" |
    And foo is anything but null
    And bar is in set:
      | "1"    |
      | "22"   |
      | "333"  |
      | "4444" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    When If Then and Else are described below
    And foo is equal to "dddd"
    And bar is equal to "4444"
    And bar is shorter than 1
    Then the following data should be generated:
      | foo    | bar    |
      | "dddd" | "4444" |

  Scenario: Running an if request that contains a non contradictory greaterThan constraint within its if statement should be successful
    Given foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    And foo is anything but null
    And bar is in set:
      | 1    |
      | 22   |
      | 333  |
      | 4444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is greater than 29
    And bar is equal to 22
    And bar is equal to 1
    Then the following data should be generated:
      | foo | bar |
      | 10  | 1   |
      | 20  | 1   |
      | 30  | 22  |
      | 40  | 22  |

  Scenario: Running an if request that contains a non contradictory greaterThan constraint within its then statement should be successful
    Given foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    And foo is anything but null
    And bar is in set:
      | 1    |
      | 22   |
      | 333  |
      | 4444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to 40
    And bar is greater than 20
    And bar is equal to 1
    Then the following data should be generated:
      | foo | bar  |
      | 10  | 1    |
      | 20  | 1    |
      | 30  | 1    |
      | 40  | 22   |
      | 40  | 333  |
      | 40  | 4444 |

  Scenario: Running an if request that contains a non contradictory greaterThan constraint within its else statement should be successful
    Given foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    And foo is anything but null
    And bar is in set:
      | 1    |
      | 22   |
      | 333  |
      | 4444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to 40
    And bar is equal to 1
    And bar is greater than 300
    Then the following data should be generated:
      | foo | bar  |
      | 10  | 333  |
      | 10  | 4444 |
      | 20  | 333  |
      | 20  | 4444 |
      | 30  | 333  |
      | 30  | 4444 |
      | 40  | 1    |

  Scenario: Running an if request that contains a contradictory greaterThan constraint within its then statement should be successful
    Given foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    And foo is anything but null
    And bar is in set:
      | 1    |
      | 22   |
      | 333  |
      | 4444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to 40
    And bar is greater than 8000
    And bar is equal to 1
    Then the following data should be generated:
      | foo | bar |
      | 10  | 1   |
      | 20  | 1   |
      | 30  | 1   |

  Scenario: Running an if request that contains a contradictory greaterThan constraint within its else statement should be successful
    Given foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    And foo is anything but null
    And bar is in set:
      | 1    |
      | 22   |
      | 333  |
      | 4444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to 40
    And bar is equal to 1
    And bar is greater than 8000
    Then the following data should be generated:
      | foo | bar |
      | 40  | 1   |

  Scenario: Running an if request that contains a non contradictory greaterThanOrEqualTo constraint within its if statement should be successful
    Given foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    And foo is anything but null
    And bar is in set:
      | 1    |
      | 22   |
      | 333  |
      | 4444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is greater than or equal to 20
    And bar is equal to 22
    And bar is equal to 1
    Then the following data should be generated:
      | foo | bar |
      | 10  | 1   |
      | 20  | 22  |
      | 30  | 22  |
      | 40  | 22  |

  Scenario: Running an if request that contains a non contradictory greaterThanOrEqualTo constraint within its then statement should be successful
    Given foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    And foo is anything but null
    And bar is in set:
      | 1    |
      | 22   |
      | 333  |
      | 4444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to 20
    And bar is greater than or equal to 22
    And bar is equal to 1
    Then the following data should be generated:
      | foo | bar  |
      | 10  | 1    |
      | 20  | 22   |
      | 20  | 333  |
      | 20  | 4444 |
      | 30  | 1    |
      | 40  | 1    |

  Scenario: Running an if request that contains a non contradictory greaterThanOrEqualTo constraint within its else statement should be successful
    Given foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    And foo is anything but null
    And bar is in set:
      | 1    |
      | 22   |
      | 333  |
      | 4444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to 20
    And bar is equal to 1
    And bar is greater than or equal to 22
    Then the following data should be generated:
      | foo | bar  |
      | 10  | 22   |
      | 10  | 333  |
      | 10  | 4444 |
      | 20  | 1    |
      | 30  | 22   |
      | 30  | 333  |
      | 30  | 4444 |
      | 40  | 22   |
      | 40  | 333  |
      | 40  | 4444 |

  Scenario: Running an if request that contains a contradictory greaterThanOrEqualTo constraint within its then statement should be successful
    Given foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    And foo is anything but null
    And bar is in set:
      | 1    |
      | 22   |
      | 333  |
      | 4444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to 10
    And bar is greater than or equal to 8000
    And bar is equal to 333
    Then the following data should be generated:
      | foo | bar |
      | 20  | 333 |
      | 30  | 333 |
      | 40  | 333 |

  Scenario: Running an if request that contains a contradictory greaterThanOrEqualTo constraint within its else statement should be successful
    Given foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    And foo is anything but null
    And bar is in set:
      | 1    |
      | 22   |
      | 333  |
      | 4444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to 10
    And bar is equal to 4444
    And bar is greater than or equal to 8000
    Then the following data should be generated:
      | foo | bar  |
      | 10  | 4444 |

  Scenario: Running an if request that contains a non contradictory lessThan constraint within its if statement should be successful
    Given foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    And foo is anything but null
    And bar is in set:
      | 1    |
      | 22   |
      | 333  |
      | 4444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is less than 20
    And bar is equal to 4444
    And bar is equal to 1
    Then the following data should be generated:
      | foo | bar  |
      | 10  | 4444 |
      | 20  | 1    |
      | 30  | 1    |
      | 40  | 1    |

  Scenario: Running an if request that contains a non contradictory lessThan constraint within its then statement should be successful
    Given foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    And foo is anything but null
    And bar is in set:
      | 1    |
      | 22   |
      | 333  |
      | 4444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to 40
    And bar is less than 4400
    And bar is equal to 1
    Then the following data should be generated:
      | foo | bar |
      | 10  | 1   |
      | 20  | 1   |
      | 30  | 1   |
      | 40  | 1   |
      | 40  | 22  |
      | 40  | 333 |

  Scenario: Running an if request that contains a non contradictory lessThan constraint within its else statement should be successful
    Given foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    And foo is anything but null
    And bar is in set:
      | 1    |
      | 22   |
      | 333  |
      | 4444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to 40
    And bar is equal to 333
    And bar is less than 300
    Then the following data should be generated:
      | foo | bar |
      | 10  | 1   |
      | 10  | 22  |
      | 20  | 1   |
      | 20  | 22  |
      | 30  | 1   |
      | 30  | 22  |
      | 40  | 333 |

  Scenario: Running an if request that contains a contradictory lessThan constraint within its then statement should be successful
    Given foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    And foo is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    And bar is in set:
      | 1    |
      | 22   |
      | 333  |
      | 4444 |
    And bar is anything but null
    When If Then and Else are described below
    And foo is equal to 10
    And bar is less than 1
    And bar is equal to 333
    Then the following data should be generated:
      | foo | bar |
      | 20  | 333 |
      | 30  | 333 |
      | 40  | 333 |

  Scenario: Running an if request that contains a contradictory lessThan constraint within its else statement should be successful
    Given foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    And foo is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    And bar is in set:
      | 1    |
      | 22   |
      | 333  |
      | 4444 |
    And bar is anything but null
    When If Then and Else are described below
    And foo is equal to 30
    And bar is equal to 1
    And bar is less than 1
    Then the following data should be generated:
      | foo | bar |
      | 30  | 1   |

  Scenario: Running an if request that contains a non contradictory lessThanOrEqualTo constraint within its if statement should be successful
    Given foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    And foo is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    And bar is in set:
      | 1    |
      | 22   |
      | 333  |
      | 4444 |
    And bar is anything but null
    When If Then and Else are described below
    And foo is less than or equal to 20
    And bar is equal to 1
    And bar is equal to 4444
    Then the following data should be generated:
      | foo | bar  |
      | 10  | 1    |
      | 20  | 1    |
      | 30  | 4444 |
      | 40  | 4444 |

  Scenario: Running an if request that contains a non contradictory lessThanOrEqualTo constraint within its then statement should be successful
    Given foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    And foo is anything but null
    And bar is in set:
      | 1    |
      | 22   |
      | 333  |
      | 4444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to 20
    And bar is less than or equal to 333
    And bar is equal to 4444
    Then the following data should be generated:
      | foo | bar  |
      | 10  | 4444 |
      | 20  | 1    |
      | 20  | 22   |
      | 20  | 333  |
      | 30  | 4444 |
      | 40  | 4444 |

  Scenario: Running an if request that contains a non contradictory lessThanOrEqualTo constraint within its else statement should be successful
    Given foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    And foo is anything but null
    And bar is in set:
      | 1    |
      | 22   |
      | 333  |
      | 4444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to 20
    And bar is equal to 333
    And bar is less than or equal to 4444
    Then the following data should be generated:
      | foo | bar  |
      | 10  | 1    |
      | 10  | 22   |
      | 10  | 333  |
      | 10  | 4444 |
      | 20  | 333  |
      | 30  | 1    |
      | 30  | 22   |
      | 30  | 333  |
      | 30  | 4444 |
      | 40  | 1    |
      | 40  | 22   |
      | 40  | 333  |
      | 40  | 4444 |

  Scenario: Running an if request that contains a contradictory lessThanOrEqualTo constraint within its then statement should be successful
    Given foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    And foo is anything but null
    And bar is in set:
      | 1    |
      | 22   |
      | 333  |
      | 4444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to 10
    And bar is less than or equal to 0
    And bar is equal to 4444
    Then the following data should be generated:
      | foo | bar  |
      | 20  | 4444 |
      | 30  | 4444 |
      | 40  | 4444 |

  Scenario: Running an if request that contains a contradictory lessThanOrEqualTo constraint within its else statement should be successful
    Given foo is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
    And foo is anything but null
    And bar is in set:
      | 1    |
      | 22   |
      | 333  |
      | 4444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to 10
    And bar is equal to 1
    And bar is less than or equal to 0
    Then the following data should be generated:
      | foo | bar |
      | 10  | 1   |

  Scenario: Running an if request that contains a non contradictory granularTo constraint within its else statement should be successful
    Given foo is in set:
      | 1     |
      | 1.1   |
      | 1.11  |
      | 1.111 |
    And foo is anything but null
    And bar is in set:
      | 2.2   |
      | 3.33  |
      | 4.444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to 1
    And bar is equal to 3.33
    And bar is granular to 0.1
    Then the following data should be generated:
      | foo   | bar  |
      | 1     | 3.33 |
      | 1.1   | 2.2  |
      | 1.11  | 2.2  |
      | 1.111 | 2.2  |

  Scenario: Running an if request that contains a contradictory granularTo constraint within its then statement should be successful
    Given foo is in set:
      | 1     |
      | 1.1   |
      | 1.11  |
      | 1.111 |
    And foo is anything but null
    And bar is in set:
      | 2.2   |
      | 3.33  |
      | 4.444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to 1
    And bar is granular to 1
    And bar is equal to 2.2
    Then the following data should be generated:
      | foo   | bar |
      | 1.1   | 2.2 |
      | 1.11  | 2.2 |
      | 1.111 | 2.2 |

  Scenario: Running an if request that contains a contradictory granularTo constraint within its else statement should be successful
    Given foo is in set:
      | 1     |
      | 1.1   |
      | 1.11  |
      | 1.111 |
    And foo is anything but null
    And bar is in set:
      | 2.2   |
      | 3.33  |
      | 4.444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to 1
    And bar is equal to 3.33
    And bar is granular to 1
    Then the following data should be generated:
      | foo | bar  |
      | 1   | 3.33 |

  Scenario: Running an if request that contains a non contradictory after constraint within its if statement should be successful
    Given foo is in set:
      | 2018-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z |
    And foo is anything but null
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-02T00:00:00.000Z |
      | 2010-01-03T00:00:00.000Z |
      | 2010-01-04T00:00:00.000Z |
      | 2010-01-05T00:00:00.000Z |
    And bar is anything but null
    And foo has type "datetime"
    And bar has type "datetime"
    When If Then and Else are described below
    And foo is after 2018-01-02T00:00:00.000Z
    And bar is equal to 2010-01-01T00:00:00.000Z
    And bar is equal to 2010-01-05T00:00:00.000Z
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |

  Scenario: Running an if request that contains a non contradictory after constraint within its then statement should be successful
    Given foo is in set:
      | 2018-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z |
    And foo is anything but null
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-02T00:00:00.000Z |
      | 2010-01-03T00:00:00.000Z |
      | 2010-01-04T00:00:00.000Z |
      | 2010-01-05T00:00:00.000Z |
    And bar is anything but null
    And foo has type "datetime"
    And bar has type "datetime"
    When If Then and Else are described below
    And foo is equal to 2018-02-01T00:00:00.000Z
    And bar is after 2010-01-04T00:00:00.000Z
    And bar is equal to 2010-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |

  Scenario: Running an if request that contains a non contradictory after constraint within its else statement should be successful
    Given foo is in set:
      | 2018-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z |
    And foo is anything but null
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-02T00:00:00.000Z |
      | 2010-01-03T00:00:00.000Z |
      | 2010-01-04T00:00:00.000Z |
      | 2010-01-05T00:00:00.000Z |
    And bar is anything but null
    And foo has type "datetime"
    And bar has type "datetime"
    When If Then and Else are described below
    And foo is equal to 2018-01-01T00:00:00.000Z
    And bar is equal to 2010-01-01T00:00:00.000Z
    And bar is after 2010-01-04T00:00:00.000Z
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |

  Scenario: Running an if request that contains a contradictory after constraint within its then statement should be successful
    Given foo is in set:
      | 2018-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z |
    And foo is anything but null
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-02T00:00:00.000Z |
      | 2010-01-03T00:00:00.000Z |
      | 2010-01-04T00:00:00.000Z |
      | 2010-01-05T00:00:00.000Z |
    And bar is anything but null
    And foo has type "datetime"
    And bar has type "datetime"
    When If Then and Else are described below
    And foo is equal to 2018-01-01T00:00:00.000Z
    And bar is after 2020-01-01T00:00:00.000Z
    And bar is equal to 2010-01-04T00:00:00.000Z
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-02-01T00:00:00.000Z | 2010-01-04T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-04T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-04T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-04T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-04T00:00:00.000Z |

  Scenario: Running an if request that contains a contradictory after constraint within its else statement should be successful
    Given foo is in set:
      | 2018-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z |
    And foo is anything but null
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-02T00:00:00.000Z |
      | 2010-01-03T00:00:00.000Z |
      | 2010-01-04T00:00:00.000Z |
      | 2010-01-05T00:00:00.000Z |
    And bar is anything but null
    And foo has type "datetime"
    And bar has type "datetime"
    When If Then and Else are described below
    And foo is equal to 2018-01-01T00:00:00.000Z
    And bar is equal to 2010-01-01T00:00:00.000Z
    And bar is after 2020-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |

  Scenario: Running an if request that contains a non contradictory afterOrAt constraint within its if statement should be successful
    Given foo is in set:
      | 2018-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z |
    And foo is anything but null
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-02T00:00:00.000Z |
      | 2010-01-03T00:00:00.000Z |
      | 2010-01-04T00:00:00.000Z |
      | 2010-01-05T00:00:00.000Z |
    And bar is anything but null
    And foo has type "datetime"
    And bar has type "datetime"
    When If Then and Else are described below
    And foo is after or at 2018-05-01T00:00:00.000Z
    And bar is equal to 2010-01-05T00:00:00.000Z
    And bar is equal to 2010-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |

  Scenario: Running an if request that contains a non contradictory afterOrAt constraint within its then statement should be successful
    Given foo is in set:
      | 2018-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z |
    And foo is anything but null
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-02T00:00:00.000Z |
      | 2010-01-03T00:00:00.000Z |
      | 2010-01-04T00:00:00.000Z |
      | 2010-01-05T00:00:00.000Z |
    And bar is anything but null
    And foo has type "datetime"
    And bar has type "datetime"
    When If Then and Else are described below
    And foo is equal to 2018-05-01T00:00:00.000Z
    And bar is after or at 2010-01-04T00:00:00.000Z
    And bar is equal to 2010-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-04T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |

  Scenario: Running an if request that contains a non contradictory afterOrAt constraint within its else statement should be successful
    Given foo is in set:
      | 2018-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z |
    And foo is anything but null
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-02T00:00:00.000Z |
      | 2010-01-03T00:00:00.000Z |
      | 2010-01-04T00:00:00.000Z |
      | 2010-01-05T00:00:00.000Z |
    And bar is anything but null
    And foo has type "datetime"
    And bar has type "datetime"
    When If Then and Else are described below
    And foo is equal to 2018-06-01T00:00:00.000Z
    And bar is equal to 2010-01-01T00:00:00.000Z
    And bar is after or at 2010-01-04T00:00:00.000Z
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-04T00:00:00.000Z |
      | 2018-01-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-04T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-04T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-04T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-04T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |

  Scenario: Running an if request that contains a contradictory afterOrAt constraint within its then statement should be successful
    Given foo is in set:
      | 2018-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z |
    And foo is anything but null
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-02T00:00:00.000Z |
      | 2010-01-03T00:00:00.000Z |
      | 2010-01-04T00:00:00.000Z |
      | 2010-01-05T00:00:00.000Z |
    And bar is anything but null
    And foo has type "datetime"
    And bar has type "datetime"
    When If Then and Else are described below
    And foo is equal to 2018-01-01T00:00:00.000Z
    And bar is after or at 2020-01-01T00:00:00.000Z
    And bar is equal to 2010-01-05T00:00:00.000Z
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-02-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |

  Scenario: Running an if request that contains a contradictory afterOrAt constraint within its else statement should be successful
    Given foo is in set:
      | 2018-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z |
    And foo is anything but null
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-02T00:00:00.000Z |
      | 2010-01-03T00:00:00.000Z |
      | 2010-01-04T00:00:00.000Z |
      | 2010-01-05T00:00:00.000Z |
    And bar is anything but null
    And foo has type "datetime"
    And bar has type "datetime"
    When If Then and Else are described below
    And foo is equal to 2018-01-01T00:00:00.000Z
    And bar is equal to 2010-01-01T00:00:00.000Z
    And bar is after or at 2020-01-05T00:00:00.000Z
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |

  Scenario: Running an if request that contains a non contradictory before constraint within its if statement should be successful
    Given foo is in set:
      | 2018-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z |
    And foo is anything but null
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-02T00:00:00.000Z |
      | 2010-01-03T00:00:00.000Z |
      | 2010-01-04T00:00:00.000Z |
      | 2010-01-05T00:00:00.000Z |
    And bar is anything but null
    And foo has type "datetime"
    And bar has type "datetime"
    When If Then and Else are described below
    And foo is before 2018-02-01T00:00:00.000Z
    And bar is equal to 2010-01-01T00:00:00.000Z
    And bar is equal to 2010-01-05T00:00:00.000Z
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |

  Scenario: Running an if request that contains a non contradictory before constraint within its then statement should be successful
    Given foo is in set:
      | 2018-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z |
    And foo is anything but null
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-02T00:00:00.000Z |
      | 2010-01-03T00:00:00.000Z |
      | 2010-01-04T00:00:00.000Z |
      | 2010-01-05T00:00:00.000Z |
    And bar is anything but null
    And foo has type "datetime"
    And bar has type "datetime"
    When If Then and Else are described below
    And foo is equal to 2018-01-01T00:00:00.000Z
    And bar is before 2010-01-03T00:00:00.000Z
    And bar is equal to 2010-01-05T00:00:00.000Z
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-01-01T00:00:00.000Z | 2010-01-02T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |

  Scenario: Running an if request that contains a non contradictory before constraint within its else statement should be successful
    Given foo is in set:
      | 2018-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z |
    And foo is anything but null
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-02T00:00:00.000Z |
      | 2010-01-03T00:00:00.000Z |
      | 2010-01-04T00:00:00.000Z |
      | 2010-01-05T00:00:00.000Z |
    And bar is anything but null
    And foo has type "datetime"
    And bar has type "datetime"
    When If Then and Else are described below
    And foo is equal to 2018-01-01T00:00:00.000Z
    And bar is equal to 2010-01-05T00:00:00.000Z
    And bar is before 2010-01-03T00:00:00.000Z
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-02T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-02T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-02T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-02T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-02T00:00:00.000Z |

  Scenario: Running an if request that contains a contradictory before constraint within its then statement should be successful
    Given foo is in set:
      | 2018-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z |
    And foo is anything but null
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-02T00:00:00.000Z |
      | 2010-01-03T00:00:00.000Z |
      | 2010-01-04T00:00:00.000Z |
      | 2010-01-05T00:00:00.000Z |
    And bar is anything but null
    And foo has type "datetime"
    And bar has type "datetime"
    When If Then and Else are described below
    And foo is equal to 2018-01-01T00:00:00.000Z
    And bar is before 2010-01-01T00:00:00.000Z
    And bar is equal to 2010-01-03T00:00:00.000Z
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-02-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |

  Scenario: Running an if request that contains a contradictory before constraint within its else statement should be successful
    Given foo is in set:
      | 2018-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z |
    And foo is anything but null
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-02T00:00:00.000Z |
      | 2010-01-03T00:00:00.000Z |
      | 2010-01-04T00:00:00.000Z |
      | 2010-01-05T00:00:00.000Z |
    And bar is anything but null
    And foo has type "datetime"
    And bar has type "datetime"
    When If Then and Else are described below
    And foo is equal to 2018-01-01T00:00:00.000Z
    And bar is equal to 2010-01-01T00:00:00.000Z
    And bar is before 2010-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |

  Scenario: Running an if request that contains a non contradictory beforeOrAt constraint within its if statement should be successful
    Given foo is in set:
      | 2018-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z |
    And foo is anything but null
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-02T00:00:00.000Z |
      | 2010-01-03T00:00:00.000Z |
      | 2010-01-04T00:00:00.000Z |
      | 2010-01-05T00:00:00.000Z |
    And bar is anything but null
    And foo has type "datetime"
    And bar has type "datetime"
    When If Then and Else are described below
    And foo is before or at 2018-02-01T00:00:00.000Z
    And bar is equal to 2010-01-01T00:00:00.000Z
    And bar is equal to 2010-01-05T00:00:00.000Z
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |

  Scenario: Running an if request that contains a non contradictory beforeOrAt constraint within its then statement should be successful
    Given foo is in set:
      | 2018-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z |
    And foo is anything but null
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-02T00:00:00.000Z |
      | 2010-01-03T00:00:00.000Z |
      | 2010-01-04T00:00:00.000Z |
      | 2010-01-05T00:00:00.000Z |
    And bar is anything but null
    And foo has type "datetime"
    And bar has type "datetime"
    When If Then and Else are described below
    And foo is equal to 2018-01-01T00:00:00.000Z
    And bar is before or at 2010-01-03T00:00:00.000Z
    And bar is equal to 2010-01-05T00:00:00.000Z
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-01-01T00:00:00.000Z | 2010-01-02T00:00:00.000Z |
      | 2018-01-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |

  Scenario: Running an if request that contains a non contradictory beforeOrAt constraint within its else statement should be successful
    Given foo is in set:
      | 2018-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z |
    And foo is anything but null
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-02T00:00:00.000Z |
      | 2010-01-03T00:00:00.000Z |
      | 2010-01-04T00:00:00.000Z |
      | 2010-01-05T00:00:00.000Z |
    And bar is anything but null
    And foo has type "datetime"
    And bar has type "datetime"
    When If Then and Else are described below
    And foo is equal to 2018-01-01T00:00:00.000Z
    And bar is equal to 2010-01-05T00:00:00.000Z
    And bar is before or at 2010-01-03T00:00:00.000Z
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-02T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-02T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-02T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-02T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-02T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |

  Scenario: Running an if request that contains a contradictory beforeOrAt constraint within its then statement should be successful
    Given foo is in set:
      | 2018-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z |
    And foo is anything but null
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-02T00:00:00.000Z |
      | 2010-01-03T00:00:00.000Z |
      | 2010-01-04T00:00:00.000Z |
      | 2010-01-05T00:00:00.000Z |
    And bar is anything but null
    And foo has type "datetime"
    And bar has type "datetime"
    When If Then and Else are described below
    And foo is equal to 2018-01-01T00:00:00.000Z
    And bar is before or at 2009-01-01T00:00:00.000Z
    And bar is equal to 2010-01-03T00:00:00.000Z
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-02-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |

  Scenario: Running an if request that contains a contradictory beforeOrAt constraint within its else statement should be successful
    Given foo is in set:
      | 2018-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z |
    And foo is anything but null
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-01-02T00:00:00.000Z |
      | 2010-01-03T00:00:00.000Z |
      | 2010-01-04T00:00:00.000Z |
      | 2010-01-05T00:00:00.000Z |
    And bar is anything but null
    And foo has type "datetime"
    And bar has type "datetime"
    When If Then and Else are described below
    And foo is equal to 2018-01-01T00:00:00.000Z
    And bar is equal to 2010-01-01T00:00:00.000Z
    And bar is before or at 2009-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |

  Scenario: Running an if request that contains a non contradictory not constraint within its if statement should be successful
    Given foo is in set:
      | "a" |
      | "b" |
    And foo is anything but null
    And bar is in set:
      | 10 |
      | 20 |
      | 30 |
    And bar is anything but null
    And foo has type "string"
    And bar has type "decimal"
    When If and Then are described below
    And foo is anything but equal to "a"
    And bar is equal to 10
    Then the following data should be generated:
      | foo | bar |
      | "a" | 10  |
      | "a" | 20  |
      | "a" | 30  |
      | "b" | 10  |

  Scenario: Running an if request that contains a non contradictory not constraint within its then statement should be successful
    Given foo is in set:
      | "a" |
      | "b" |
    And foo is anything but null
    And bar is in set:
      | 10 |
      | 20 |
      | 30 |
    And bar is anything but null
    And foo has type "string"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to "a"
    And bar is anything but equal to 10
    And bar is equal to 10
    Then the following data should be generated:
      | foo | bar |
      | "a" | 20  |
      | "a" | 30  |
      | "b" | 10  |

  Scenario: Running an if request that contains a non contradictory not constraint within its else statement should be successful
    Given foo is in set:
      | "a" |
      | "b" |
    And foo is anything but null
    And bar is in set:
      | 10 |
      | 20 |
      | 30 |
    And bar is anything but null
    And foo has type "string"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to "a"
    And bar is equal to 10
    And bar is anything but equal to 10
    Then the following data should be generated:
      | foo | bar |
      | "a" | 10  |
      | "b" | 20  |
      | "b" | 30  |

  Scenario: Running an if request that contains a contradictory not constraint within its then statement should be successful
    Given foo is in set:
      | "a" |
      | "b" |
    And foo is anything but null
    And bar is in set:
      | 10 |
    And bar is anything but null
    And foo has type "string"
    And bar has type "decimal"
    When If and Then are described below
    And foo is equal to "a"
    And bar is anything but equal to 10
    Then the following data should be generated:
      | foo | bar |
      | "b" | 10  |

  Scenario: Running an if request that contains a contradictory not constraint within its else statement should be successful
    Given foo is in set:
      | "a" |
      | "b" |
    And foo is anything but null
    And bar is in set:
      | 10 |
    And bar is anything but null
    And foo has type "string"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to "a"
    And bar is equal to 10
    And bar is anything but equal to 10
    Then the following data should be generated:
      | foo | bar |
      | "a" | 10  |

  Scenario: Running an if request that contains a non contradictory anyOf constraint within its if statement should be successful
    Given foo is in set:
      | "a" |
      | "b" |
      | "c" |
      | "d" |
      | "e" |
    And foo is anything but null
    And bar is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
      | 50 |
    And bar is anything but null
    And foo has type "string"
    And bar has type "decimal"
    When If Then and Else are described below
    And Any Of the next 2 constraints
      And foo is equal to "a"
      And foo is equal to "e"
    And bar is equal to 10
    And bar is equal to 50
    Then the following data should be generated:
      | foo | bar |
      | "a" | 10  |
      | "b" | 50  |
      | "c" | 50  |
      | "d" | 50  |
      | "e" | 10  |

  Scenario: Running an if request that contains a non contradictory anyOf constraint within its then statement should be successful
    Given foo is in set:
      | "a" |
      | "b" |
      | "c" |
      | "d" |
      | "e" |
    And foo is anything but null
    And bar is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
      | 50 |
    And bar is anything but null
    And foo has type "string"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to "a"
    And Any Of the next 2 constraints
      And bar is equal to 20
      And bar is equal to 40
    And bar is equal to 50
    Then the following data should be generated:
      | foo | bar |
      | "a" | 20  |
      | "a" | 40  |
      | "b" | 50  |
      | "c" | 50  |
      | "d" | 50  |
      | "e" | 50  |

  Scenario: Running an if request that contains a non contradictory anyOf constraint within its then statement should be successful
    Given foo is in set:
      | "a" |
      | "b" |
      | "c" |
      | "d" |
      | "e" |
    And foo is anything but null
    And bar is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
      | 50 |
    And bar is anything but null
    And foo has type "string"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to "a"
    And bar is equal to 10
    And Any Of the next 2 constraints
      And bar is equal to 20
      And bar is equal to 40
    Then the following data should be generated:
      | foo | bar |
      | "a" | 10  |
      | "b" | 20  |
      | "b" | 40  |
      | "c" | 20  |
      | "c" | 40  |
      | "d" | 20  |
      | "d" | 40  |
      | "e" | 20  |
      | "e" | 40  |

  Scenario: Running an if request that contains a contradictory anyOf constraint within its then statement should be successful
    Given foo is in set:
      | "a" |
      | "b" |
      | "c" |
      | "d" |
      | "e" |
    And foo is anything but null
    And bar is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
      | 50 |
    And bar is anything but null
    And foo has type "string"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to "a"
    And Any Of the next 2 constraints
      And bar is equal to 1
      And bar is equal to 2
    And bar is equal to 50
    Then the following data should be generated:
      | foo | bar |
      | "b" | 50  |
      | "c" | 50  |
      | "d" | 50  |
      | "e" | 50  |

  Scenario: Running an if request that contains a contradictory anyOf constraint within its else statement should be successful
    Given foo is in set:
      | "a" |
      | "b" |
      | "c" |
      | "d" |
      | "e" |
    And foo is anything but null
    And bar is in set:
      | 10 |
      | 20 |
      | 30 |
      | 40 |
      | 50 |
    And bar is anything but null
    And foo has type "string"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to "a"
    And bar is equal to 10
    And Any Of the next 2 constraints
      And bar is equal to 1
      And bar is equal to 2
    Then the following data should be generated:
      | foo | bar |
      | "a" | 10  |

  Scenario: Running an if request that contains a non contradictory allOf constraint within its if statement should be successful
    Given foo is in set:
      | "a"     |
      | "bb"    |
      | "ccc"   |
      | "dddd"  |
      | "eeeee" |
    And foo is anything but null
    And bar is in set:
      | 1     |
      | 22    |
      | 333   |
      | 4444  |
      | 55555 |
    And bar is anything but null
    And foo has type "string"
    And bar has type "decimal"
    When If Then and Else are described below
    And All Of the next 2 constraints
      And foo is longer than 0
      And foo is shorter than 2
    And bar is equal to 1
    And bar is equal to 55555
    Then the following data should be generated:
      | foo     | bar   |
      | "a"     | 1     |
      | "bb"    | 55555 |
      | "ccc"   | 55555 |
      | "dddd"  | 55555 |
      | "eeeee" | 55555 |

  Scenario: Running an if request that contains a non contradictory allOf constraint within its then statement should be successful
    Given foo is in set:
      | "a"     |
      | "bb"    |
      | "ccc"   |
      | "dddd"  |
      | "eeeee" |
    And foo is anything but null
    And bar is in set:
      | 1     |
      | 22    |
      | 333   |
      | 4444  |
      | 55555 |
    And bar is anything but null
    And foo has type "string"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to "a"
    And All Of the next 2 constraints
      And bar is greater than 20
      And bar is less than 30
    And bar is equal to 55555
    Then the following data should be generated:
      | foo     | bar   |
      | "a"     | 22    |
      | "bb"    | 55555 |
      | "ccc"   | 55555 |
      | "dddd"  | 55555 |
      | "eeeee" | 55555 |

  Scenario: Running an if request that contains a non contradictory allOf constraint within its else statement should be successful
    Given foo is in set:
      | "a"     |
      | "bb"    |
      | "ccc"   |
      | "dddd"  |
      | "eeeee" |
    And foo is anything but null
    And bar is in set:
      | 1     |
      | 22    |
      | 333   |
      | 4444  |
      | 55555 |
    And bar is anything but null
    And foo has type "string"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to "a"
    And bar is equal to 1
    And All Of the next 2 constraints
      And bar is greater than 20
      And bar is less than 30
    Then the following data should be generated:
      | foo     | bar |
      | "a"     | 1   |
      | "bb"    | 22  |
      | "ccc"   | 22  |
      | "dddd"  | 22  |
      | "eeeee" | 22  |

  Scenario: Running an if request that contains a non contradictory allOf constraint within its then statement should be successful
    Given foo is in set:
      | "a"     |
      | "bb"    |
      | "ccc"   |
      | "dddd"  |
      | "eeeee" |
    And foo is anything but null
    And bar is in set:
      | 1     |
      | 22    |
      | 333   |
      | 4444  |
      | 55555 |
    And bar is anything but null
    And foo has type "string"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to "a"
    And All Of the next 2 constraints
      And bar is greater than 200
      And bar is less than 300
    And bar is equal to 55555
    Then the following data should be generated:
      | foo     | bar   |
      | "bb"    | 55555 |
      | "ccc"   | 55555 |
      | "dddd"  | 55555 |
      | "eeeee" | 55555 |

  Scenario: Running an if request that contains a non contradictory allOf constraint within its else statement should be successful
    Given foo is in set:
      | "a"     |
      | "bb"    |
      | "ccc"   |
      | "dddd"  |
      | "eeeee" |
    And foo has type "string"
    And foo is anything but null
    And bar is in set:
      | 1     |
      | 22    |
      | 333   |
      | 4444  |
      | 55555 |
    And bar is anything but null
    And foo has type "string"
    And bar has type "decimal"
    When If Then and Else are described below
    And foo is equal to "a"
    And bar is equal to 1
    And All Of the next 2 constraints
      And bar is greater than 200
      And bar is less than 300
    Then the following data should be generated:
      | foo | bar |
      | "a" | 1   |
