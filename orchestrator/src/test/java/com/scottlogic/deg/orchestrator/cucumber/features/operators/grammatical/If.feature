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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "a" },
        "then": { "field": "bar", "is": "equalTo", "value": 10 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "a" },
        "then": { "field": "bar", "is": "equalTo", "value": 3 },
        "else": { "field": "bar", "is": "equalTo", "value": 5 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if":
          { "anyOf": [
            { "field": "foo", "is": "equalTo", "value": 1 },
            { "field": "foo", "is": "equalTo", "value": 2 }
          ]},
        "then": { "field": "bar", "is": "equalTo", "value": "a" },
        "else": { "field": "bar", "is": "equalTo", "value": "b" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": {
          "allOf": [
            { "field": "foo", "is": "greaterThan", "value": 1 },
            { "field": "foo", "is": "lessThan", "value": 4 }
          ]},
        "then": { "field": "bar", "is": "equalTo", "value": "a" },
        "else": { "field": "bar", "is": "equalTo", "value": "b" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "not": { "field": "foo", "is": "equalTo", "value": 1 } },
        "then": { "field": "bar", "is": "equalTo", "value": "a" },
        "else": { "field": "bar", "is": "equalTo", "value": "b" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "greaterThan", "value": 1 },
        "then": {
          "if": { "field": "foo", "is": "greaterThan", "value": 3 },
          "then": { "field": "bar", "is": "equalTo", "value": "a" },
          "else": { "field": "bar", "is": "equalTo", "value": "b" }
        },
        "else": { "field": "bar", "is": "equalTo", "value": "c" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 5 },
        "then": { "field": "bar", "is": "equalTo", "value": "a" },
        "else": { "field": "bar", "is": "equalTo", "value": "b" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 1 },
        "then": { "field": "bar", "is": "equalTo", "value": "X" },
        "else": { "field": "bar", "is": "equalTo", "value": "b" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 1 },
        "then": { "field": "bar", "is": "equalTo", "value": "a" },
        "else": { "field": "bar", "is": "equalTo", "value": "X" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "inSet", "values": [1, 2] },
        "then": { "field": "bar", "is": "equalTo", "value": "a" },
        "else": { "field": "bar", "is": "equalTo", "value": "b" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 1 },
        "then": { "field": "bar", "is": "inSet", "values": [ "a", "b" ] },
        "else": { "field": "bar", "is": "equalTo", "value": "c" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 1 },
        "then": { "field": "bar", "is": "equalTo", "value": "b" },
        "else": { "field": "bar", "is": "inSet", "values": [ "a", "c" ] }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "inSet", "values": [ 8, 9 ] },
        "then": { "field": "bar", "is": "equalTo", "value": "a" },
        "else": { "field": "bar", "is": "equalTo", "value": "b" }
      }
      """
    Then the following data should be generated:
      | foo | bar |
      | 1   | "b" |
      | 2   | "b" |
      | 3   | "b" |
      | 4   | "b" |

  Scenario: Running an if request that contains a contradictory inSet constraint within its then statement should be successful
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
        "then": { "field": "bar", "is": "inSet", "value": [ "X", "Y" ] },
        "else": { "field": "bar", "is": "equalTo", "value": "c" }
      }
      """
    Then the profile is invalid because "Field \[bar\]: Couldn't recognise 'values' property, it must not contain 'null'"
    And no data is created

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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 1 },
        "then": { "field": "bar", "is": "equalTo", "value": "a" },
        "else": { "field": "bar", "is": "inSet", "values": [ "X", "Y" ] }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "null" },
        "then": { "field": "bar", "is": "equalTo", "value": "b" },
        "else": { "field": "bar", "is": "equalTo", "value": "c" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 2 },
        "then": { "field": "bar", "is": "null" },
        "else": { "field": "bar", "is": "equalTo", "value": "c" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 2 },
        "then": { "field": "bar", "is": "equalTo", "value": "b" },
        "else": { "field": "bar", "is": "null" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "null" },
        "then": { "field": "bar", "is": "equalTo", "value": "a" },
        "else": { "field": "bar", "is": "equalTo", "value": "b" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 1},
        "then": { "field": "bar", "is": "null" },
        "else": { "field": "bar", "is": "equalTo", "value": "a" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 1},
        "then": { "field": "bar", "is": "equalTo", "value": "a" },
        "else": { "field": "bar", "is": "null" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "matchingRegex", "value": "[a-z]{1}" },
        "then": { "field": "bar", "is": "equalTo", "value": "AA" },
        "else": { "field": "bar", "is": "equalTo", "value": "10" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "1" },
        "then": { "field": "bar", "is": "matchingRegex", "value": "[A-Z]{2}" },
        "else": { "field": "bar", "is": "equalTo", "value": "10" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "1" },
        "then": { "field": "bar", "is": "equalTo", "value": "AA" },
        "else": { "field": "bar", "is": "matchingRegex", "value": "[0-9]{2}" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "matchingRegex", "value": "[0-9]{10}" },
        "then": { "field": "bar", "is": "equalTo", "value": "AA" },
        "else": { "field": "bar", "is": "equalTo", "value": "10" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "a" },
        "then": { "field": "bar", "is": "matchingRegex", "value": "[😁-😡]{1}"},
        "else": { "field": "bar", "is": "equalTo", "value": "10" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "a" },
        "then": { "field": "bar", "is": "equalTo", "value": "BB"},
        "else": { "field": "bar", "is": "matchingRegex", "value": "[😁-😡]{1}"}
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "containingRegex", "value": "[1]{1}" },
        "then": { "field": "bar", "is": "equalTo", "value": "AA" },
        "else": { "field": "bar", "is": "equalTo", "value": "10" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "1" },
        "then": { "field": "bar", "is": "containingRegex", "value": "[0]{1}" },
        "else": { "field": "bar", "is": "equalTo", "value": "AA" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "1" },
        "then": { "field": "bar", "is": "equalTo", "value": "BB" },
        "else": { "field": "bar", "is": "containingRegex", "value": "[0]{1}" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "containingRegex", "value": "[🚫]{1}" },
        "then": { "field": "bar", "is": "equalTo", "value": "AA" },
        "else": { "field": "bar", "is": "equalTo", "value": "10" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "1" },
        "then": { "field": "bar", "is": "containingRegex", "value": "[🚫]{1}" },
        "else": { "field": "bar", "is": "equalTo", "value": "10" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "1" },
        "then": { "field": "bar", "is": "equalTo", "value": "10" },
        "else": { "field": "bar", "is": "containingRegex", "value": "[🚫]{1}" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "ofLength", "value": 2 },
        "then": { "field": "bar", "is": "equalTo", "value": "1" },
        "else": { "field": "bar", "is": "equalTo", "value": "4444" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "bb" },
        "then": { "field": "bar", "is": "ofLength", "value": 3 },
        "else": { "field": "bar", "is": "equalTo", "value": "1" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "a" },
        "then": { "field": "bar", "is": "equalTo", "value": "1" },
        "else": { "field": "bar", "is": "ofLength", "value": 4 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "ofLength", "value": 7 },
        "then": { "field": "bar", "is": "equalTo", "value": "1" },
        "else": { "field": "bar", "is": "equalTo", "value": "4444" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "a" },
        "then": { "field": "bar", "is": "ofLength", "value": 10 },
        "else": { "field": "bar", "is": "equalTo", "value": "4444" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "a" },
        "then": { "field": "bar", "is": "equalTo", "value": "1" },
        "else": { "field": "bar", "is": "ofLength", "value": 10 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "longerThan", "value": 2 },
        "then": { "field": "bar", "is": "equalTo", "value": "1" },
        "else": { "field": "bar", "is": "equalTo", "value": "4444" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "bb" },
        "then": { "field": "bar", "is": "longerThan", "value": 2 },
        "else": { "field": "bar", "is": "equalTo", "value": "1" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "dddd" },
        "then": { "field": "bar", "is": "equalTo", "value": "1" },
        "else": { "field": "bar", "is": "longerThan", "value": 2  }
      }
      """
    Then the following data should be generated:
      | foo    | bar    |
      | "a"    | "333"  |
      | "a"    | "4444" |
      | "bb"   | "333"  |
      | "bb"   | "4444" |
      | "ccc"  | "333"  |
      | "ccc"  | "4444" |
      | "dddd" | "1"    |

  Scenario: Running an if request that contains a contradictory longerThan constraint within its if statement should be successful
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "longerThan", "value": 25 },
        "then": { "field": "bar", "is": "equalTo", "value": "1" },
        "else": { "field": "bar", "is": "equalTo", "value": "4444" }
      }
      """
    Then the following data should be generated:
      | foo    | bar    |
      | "a"    | "4444" |
      | "bb"   | "4444" |
      | "ccc"  | "4444" |
      | "dddd" | "4444" |

  Scenario: Running an if request that contains a contradictory longerThan constraint within its then statement should be successful
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "a" },
        "then": { "field": "bar", "is": "longerThan", "value": 100 },
        "else": { "field": "bar", "is": "equalTo", "value": "4444" }
      }
      """
    Then the following data should be generated:
      | foo    | bar    |
      | "bb"   | "4444" |
      | "ccc"  | "4444" |
      | "dddd" | "4444" |

  Scenario: Running an if request that contains a contradictory longerThan constraint within its else statement should be successful
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "a" },
        "then": { "field": "bar", "is": "equalTo", "value": "1" },
        "else": { "field": "bar", "is": "longerThan", "value": 100 }
      }
      """
    Then the following data should be generated:
      | foo | bar |
      | "a" | "1" |

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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "shorterThan", "value": 3 },
        "then": { "field": "bar", "is": "equalTo", "value": "1" },
        "else": { "field": "bar", "is": "equalTo", "value": "4444" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "ccc" },
        "then": { "field": "bar", "is": "shorterThan", "value": 3 },
        "else": { "field": "bar", "is": "equalTo", "value": "333" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "dddd" },
        "then": { "field": "bar", "is": "equalTo", "value": "4444" },
        "else": { "field": "bar", "is": "shorterThan", "value": 4 }
      }
      """
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

  Scenario: Running an if request that contains a contradictory shorterThan constraint within its if statement should be successful
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "shorterThan", "value": 1 },
        "then": { "field": "bar", "is": "equalTo", "value": "1" },
        "else": { "field": "bar", "is": "equalTo", "value": "4444" }
      }
      """
    Then the following data should be generated:
      | foo    | bar    |
      | "a"    | "4444" |
      | "bb"   | "4444" |
      | "ccc"  | "4444" |
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "dddd" },
        "then": { "field": "bar", "is": "shorterThan", "value": 1 },
        "else": { "field": "bar", "is": "equalTo", "value": "4444" }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "dddd" },
        "then": { "field": "bar", "is": "equalTo", "value": "4444" },
        "else": { "field": "bar", "is": "shorterThan", "value": 1 }
      }
      """
    Then the following data should be generated:
      | foo    | bar    |
      | "dddd" | "4444" |

  Scenario: An if constraint that contains an ISIN constraint in the then clause generates valid ISINs when the if clause applies
    Given foo is in set:
      | "GB0002634946" |
      | "bb"           |
      | "ccc"          |
      | "dddd"         |
    And foo is anything but null
    And bar is in set:
      | "GB0002634946" |
      | "22"           |
      | "333"          |
      | "4444"         |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "bb" },
        "then": { "field": "bar", "is": "ofType", "value": "ISIN" },
        "else": { "field": "bar", "is": "equalTo", "value": "333" }
      }
      """
    Then the following data should be generated:
      | foo            | bar            |
      | "GB0002634946" | "333"          |
      | "bb"           | "GB0002634946" |
      | "ccc"          | "333"          |
      | "dddd"         | "333"          |

  Scenario: An if constraint that contains an ISIN constraint in the else clause generates valid ISINs when the if clause does not apply
    Given foo is in set:
      | "GB0002634946" |
      | "bb"           |
      | "ccc"          |
      | "dddd"         |
    And foo is anything but null
    And bar is in set:
      | "GB0002634946" |
      | "22"           |
      | "333"          |
      | "4444"         |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "bb" },
        "then": { "field": "bar", "is": "equalTo", "value": "22" },
        "else": { "field": "bar", "is": "ofType", "value": "ISIN" }
      }
      """
    Then the following data should be generated:
      | foo            | bar            |
      | "GB0002634946" | "GB0002634946" |
      | "bb"           | "22"           |
      | "ccc"          | "GB0002634946" |
      | "dddd"         | "GB0002634946" |

  Scenario: An if constraint that contains an ISIN constraint in the then clause combined with an in set constraint that does not contain any valid ISINs only generates data that matches the else clause
    Given foo is in set:
      | "aa"   |
      | "bb"   |
      | "ccc"  |
      | "dddd" |
    And foo is anything but null
    And bar is in set:
      | "11"   |
      | "22"   |
      | "333"  |
      | "4444" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "ccc" },
        "then": { "field": "bar", "is": "ofType", "value": "ISIN" },
        "else": { "field": "bar", "is": "equalTo", "value": "333" }
      }
      """
    Then the following data should be generated:
      | foo    | bar   |
      | "aa"   | "333" |
      | "bb"   | "333" |
      | "dddd" | "333" |

  Scenario: An if constraint with an else clause containing an ISIN constraint which contradicts an in set constraint generates data that only matches the then clause
    Given foo is in set:
      | "aa"   |
      | "bb"   |
      | "ccc"  |
      | "dddd" |
    And foo is anything but null
    And bar is in set:
      | "11"   |
      | "22"   |
      | "333"  |
      | "4444" |
    And bar is anything but null
    And foo has type "string"
    And bar has type "string"
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "ccc" },
        "then": { "field": "bar", "is": "equalTo", "value": "333" },
        "else": { "field": "bar", "is": "ofType", "value": "ISIN" }
      }
      """
    Then the following data should be generated:
      | foo   | bar   |
      | "ccc" | "333" |

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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "greaterThan", "value": 29 },
        "then": { "field": "bar", "is": "equalTo", "value": 22 },
        "else": { "field": "bar", "is": "equalTo", "value": 1 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 40 },
        "then": { "field": "bar", "is": "greaterThan", "value": 20 },
        "else": { "field": "bar", "is": "equalTo", "value": 1 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 40 },
        "then": { "field": "bar", "is": "equalTo", "value": 1 },
        "else": { "field": "bar", "is": "greaterThan", "value": 300 }
      }
      """
    Then the following data should be generated:
      | foo | bar  |
      | 10  | 333  |
      | 10  | 4444 |
      | 20  | 333  |
      | 20  | 4444 |
      | 30  | 333  |
      | 30  | 4444 |
      | 40  | 1    |

  Scenario: Running an if request that contains a contradictory greaterThan constraint within its if statement should be successful
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "greaterThan", "value": 8000 },
        "then": { "field": "bar", "is": "equalTo", "value": 22 },
        "else": { "field": "bar", "is": "equalTo", "value": 1 }
      }
      """
    Then the following data should be generated:
      | foo | bar |
      | 10  | 1   |
      | 20  | 1   |
      | 30  | 1   |
      | 40  | 1   |

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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 40 },
        "then": { "field": "bar", "is": "greaterThan", "value": 8000 },
        "else": { "field": "bar", "is": "equalTo", "value": 1 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 40 },
        "then": { "field": "bar", "is": "equalTo", "value": 1 },
        "else": { "field": "bar", "is": "greaterThan", "value": 8000 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "greaterThanOrEqualTo", "value": 20 },
        "then": { "field": "bar", "is": "equalTo", "value": 22 },
        "else": { "field": "bar", "is": "equalTo", "value": 1 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 20 },
        "then": { "field": "bar", "is": "greaterThanOrEqualTo", "value": 22 },
        "else": { "field": "bar", "is": "equalTo", "value": 1 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 20 },
        "then": { "field": "bar", "is": "equalTo", "value": 1 },
        "else": { "field": "bar", "is": "greaterThanOrEqualTo", "value": 22 }
      }
      """
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

  Scenario: Running an if request that contains a contradictory greaterThanOrEqualTo constraint within its if statement should be successful
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "greaterThanOrEqualTo", "value": 8000 },
        "then": { "field": "bar", "is": "equalTo", "value": 22 },
        "else": { "field": "bar", "is": "equalTo", "value": 1 }
      }
      """
    Then the following data should be generated:
      | foo | bar |
      | 10  | 1   |
      | 20  | 1   |
      | 30  | 1   |
      | 40  | 1   |

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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 10 },
        "then": { "field": "bar", "is": "greaterThanOrEqualTo", "value": 8000 },
        "else": { "field": "bar", "is": "equalTo", "value": 333 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 10 },
        "then": { "field": "bar", "is": "equalTo", "value": 4444 },
        "else": { "field": "bar", "is": "greaterThanOrEqualTo", "value": 8000 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "lessThan", "value": 20 },
        "then": { "field": "bar", "is": "equalTo", "value": 4444 },
        "else": { "field": "bar", "is": "equalTo", "value": 1 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 40 },
        "then": { "field": "bar", "is": "lessThan", "value": 4400 },
        "else": { "field": "bar", "is": "equalTo", "value": 1 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 40 },
        "then": { "field": "bar", "is": "equalTo", "value": 333 },
        "else": { "field": "bar", "is": "lessThan", "value": 300 }
      }
      """
    Then the following data should be generated:
      | foo | bar |
      | 10  | 1   |
      | 10  | 22  |
      | 20  | 1   |
      | 20  | 22  |
      | 30  | 1   |
      | 30  | 22  |
      | 40  | 333 |

  Scenario: Running an if request that contains a contradictory lessThan constraint within its if statement should be successful
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "lessThan", "value": 1 },
        "then": { "field": "bar", "is": "equalTo", "value": 333 },
        "else": { "field": "bar", "is": "equalTo", "value": 22 }
      }
      """
    Then the following data should be generated:
      | foo | bar |
      | 10  | 22  |
      | 20  | 22  |
      | 30  | 22  |
      | 40  | 22  |

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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 10 },
        "then": { "field": "bar", "is": "lessThan", "value": 1 },
        "else": { "field": "bar", "is": "equalTo", "value": 333 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 30 },
        "then": { "field": "bar", "is": "equalTo", "value": 1 },
        "else": { "field": "bar", "is": "lessThan", "value": 1 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "lessThanOrEqualTo", "value": 20 },
        "then": { "field": "bar", "is": "equalTo", "value": 1 },
        "else": { "field": "bar", "is": "equalTo", "value": 4444 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 20 },
        "then": { "field": "bar", "is": "lessThanOrEqualTo", "value": 333 },
        "else": { "field": "bar", "is": "equalTo", "value": 4444 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 20 },
        "then": { "field": "bar", "is": "equalTo", "value": 333 },
        "else": { "field": "bar", "is": "lessThanOrEqualTo", "value": 4444 }
      }
      """
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

  Scenario: Running an if request that contains a contradictory lessThanOrEqualTo constraint within its if statement should be successful
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "lessThanOrEqualTo", "value": 1 },
        "then": { "field": "bar", "is": "equalTo", "value": 333 },
        "else": { "field": "bar", "is": "equalTo", "value": 4444 }
      }
      """
    Then the following data should be generated:
      | foo | bar  |
      | 10  | 4444 |
      | 20  | 4444 |
      | 30  | 4444 |
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 10 },
        "then": { "field": "bar", "is": "lessThanOrEqualTo", "value": 0 },
        "else": { "field": "bar", "is": "equalTo", "value": 4444 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 10 },
        "then": { "field": "bar", "is": "equalTo", "value": 1 },
        "else": { "field": "bar", "is": "lessThanOrEqualTo", "value": 0 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 1 },
        "then": { "field": "bar", "is": "equalTo", "value": 3.33 },
        "else": { "field": "bar", "is": "granularTo", "value": 0.1 }
      }
      """
    Then the following data should be generated:
      | foo   | bar  |
      | 1     | 3.33 |
      | 1.1   | 2.2  |
      | 1.11  | 2.2  |
      | 1.111 | 2.2  |

  Scenario: Running an if request that contains a contradictory granularTo constraint within its if statement should be successful
    Given foo is in set:
      | 1.1   |
      | 1.11  |
      | 1.111 |
    And foo is anything but null
    And bar is in set:
      | 1     |
      | 2.2   |
      | 3.33  |
      | 4.444 |
    And bar is anything but null
    And foo has type "decimal"
    And bar has type "decimal"
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "granularTo", "value": 1 },
        "then": { "field": "bar", "is": "equalTo", "value": 1 },
        "else": { "field": "bar", "is": "equalTo", "value": 2.2 }
      }
      """
    Then the following data should be generated:
      | foo   | bar |
      | 1.1   | 2.2 |
      | 1.11  | 2.2 |
      | 1.111 | 2.2 |

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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 1 },
        "then": { "field": "bar", "is": "granularTo", "value": 1 },
        "else": { "field": "bar", "is": "equalTo", "value": 2.2 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": 1 },
        "then": { "field": "bar", "is": "equalTo", "value": 3.33 },
        "else": { "field": "bar", "is": "granularTo", "value": 1 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "after", "value": { "date": "2018-01-02T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-01T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-05T00:00:00.000Z" } }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": { "date": "2018-02-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "after", "value": { "date": "2010-01-04T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-01T00:00:00.000Z" } }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": { "date": "2018-01-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-01T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "after", "value": { "date": "2010-01-04T00:00:00.000Z" } }
      }
      """
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-01T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |

  Scenario: Running an if request that contains a contradictory after constraint within its if statement should be successful
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "after", "value": { "date": "2020-01-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-01T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-04T00:00:00.000Z" } }
      }
      """
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-04T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-04T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-04T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-04T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-04T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-04T00:00:00.000Z |

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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": { "date": "2018-01-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "after", "value": { "date": "2020-01-01T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-04T00:00:00.000Z" } }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": { "date": "2018-01-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-01T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "after", "value": { "date": "2020-01-01T00:00:00.000Z" } }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "afterOrAt", "value": { "date": "2018-05-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-05T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-01T00:00:00.000Z" } }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": { "date": "2018-05-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "afterOrAt", "value": { "date": "2010-01-04T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-01T00:00:00.000Z" } }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": { "date": "2018-06-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-01T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "afterOrAt", "value": { "date": "2010-01-04T00:00:00.000Z" } }
      }
      """
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

  Scenario: Running an if request that contains a contradictory afterOrAt constraint within its if statement should be successful
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "afterOrAt", "value": { "date": "2020-01-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-01T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-05T00:00:00.000Z" } }
      }
      """
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-05T00:00:00.000Z |

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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": { "date": "2018-01-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "afterOrAt", "value": { "date": "2020-01-01T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-05T00:00:00.000Z" } }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": { "date": "2018-01-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-01T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "afterOrAt", "value": { "date": "2020-01-05T00:00:00.000Z" } }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "before", "value": { "date": "2018-02-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-01T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-05T00:00:00.000Z" } }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": { "date": "2018-01-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "before", "value": { "date": "2010-01-03T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-05T00:00:00.000Z" } }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": { "date": "2018-01-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-05T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "before", "value": { "date": "2010-01-03T00:00:00.000Z" } }
      }
      """
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

  Scenario: Running an if request that contains a contradictory before constraint within its if statement should be successful
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "before", "value": { "date": "2010-01-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-05T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-03T00:00:00.000Z" } }
      }
      """
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-06-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |

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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": { "date": "2018-01-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "before", "value": { "date": "2010-01-01T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-03T00:00:00.000Z" } }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": { "date": "2018-01-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-01T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "before", "value": { "date": "2010-01-01T00:00:00.000Z" } }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "beforeOrAt", "value": { "date": "2018-02-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-01T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-05T00:00:00.000Z" } }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": { "date": "2018-01-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "beforeOrAt", "value": { "date": "2010-01-03T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-05T00:00:00.000Z" } }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": { "date": "2018-01-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-05T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "beforeOrAt", "value": { "date": "2010-01-03T00:00:00.000Z" } }
      }
      """
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

  Scenario: Running an if request that contains a contradictory beforeOrAt constraint within its if statement should be successful
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "beforeOrAt", "value": { "date": "2009-01-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-05T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-03T00:00:00.000Z" } }
      }
      """
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-01-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-02-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-03-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-04-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
      | 2018-05-01T00:00:00.000Z | 2010-01-03T00:00:00.000Z |
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": { "date": "2018-01-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "beforeOrAt", "value": { "date": "2009-01-01T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-03T00:00:00.000Z" } }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": { "date": "2018-01-01T00:00:00.000Z" } },
        "then": { "field": "bar", "is": "equalTo", "value": { "date": "2010-01-01T00:00:00.000Z" } },
        "else": { "field": "bar", "is": "beforeOrAt", "value": { "date": "2009-01-01T00:00:00.000Z" } }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "not": { "field": "foo", "is": "equalTo", "value": "a" } },
        "then": { "field": "bar", "is": "equalTo", "value": 10 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "a" },
        "then": { "not": { "field": "bar", "is": "equalTo", "value": 10 } },
        "else": { "field": "bar", "is": "equalTo", "value": 10 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "a" },
        "then": { "field": "bar", "is": "equalTo", "value": 10 },
        "else": { "not": { "field": "bar", "is": "equalTo", "value": 10 } }
      }
      """
    Then the following data should be generated:
      | foo | bar |
      | "a" | 10  |
      | "b" | 20  |
      | "b" | 30  |

  Scenario: Running an if request that contains a contradictory not constraint within its if statement should be successful
    Given foo is in set:
      | "a" |
    And foo is anything but null
    And bar is in set:
      | 10 |
      | 20 |
      | 30 |
    And bar is anything but null
    And foo has type "string"
    And bar has type "decimal"
    And there is a constraint:
      """
      {
        "if": { "not": { "field": "foo", "is": "equalTo", "value": "a" } },
        "then": { "field": "bar", "is": "equalTo", "value": 10 },
        "else": { "field": "bar", "is": "equalTo", "value": 30 }
      }
      """
    Then the following data should be generated:
      | foo | bar |
      | "a" | 30  |

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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "a" },
        "then": { "not": { "field": "bar", "is": "equalTo", "value": 10 } }
      }
      """
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
    And there is a constraint:
      """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "a" },
        "then": { "field": "bar", "is": "equalTo", "value": 10 },
        "else": { "not": { "field": "bar", "is": "equalTo", "value": 10 } }
      }
      """
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
    And there is a constraint:
      """
      {
        "if":
          { "anyOf": [
            { "field": "foo", "is": "equalTo", "value": "a" },
            { "field": "foo", "is": "equalTo", "value": "e" }
          ]},
        "then": { "field": "bar", "is": "equalTo", "value": 10 },
        "else": { "field": "bar", "is": "equalTo", "value": 50 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if":  { "field": "foo", "is": "equalTo", "value": "a" },
        "then":
          { "anyOf": [
            { "field": "bar", "is": "equalTo", "value": 20 },
            { "field": "bar", "is": "equalTo", "value": 40 }
          ]},
        "else": { "field": "bar", "is": "equalTo", "value": 50 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if":  { "field": "foo", "is": "equalTo", "value": "a" },
        "then": { "field": "bar", "is": "equalTo", "value": 10 },
        "else":
          { "anyOf": [
            { "field": "bar", "is": "equalTo", "value": 20 },
            { "field": "bar", "is": "equalTo", "value": 40 }
          ]}
      }
      """
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

  Scenario: Running an if request that contains a contradictory anyOf constraint within its if statement should be successful
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
    And there is a constraint:
      """
      {
        "if":
          { "anyOf": [
            { "field": "foo", "is": "equalTo", "value": "Test1" },
            { "field": "foo", "is": "equalTo", "value": "Test2" }
          ]},
        "then": { "field": "bar", "is": "equalTo", "value": 10 },
        "else": { "field": "bar", "is": "equalTo", "value": 50 }
      }
      """
    Then the following data should be generated:
      | foo | bar |
      | "a" | 50  |
      | "b" | 50  |
      | "c" | 50  |
      | "d" | 50  |
      | "e" | 50  |


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
    And there is a constraint:
      """
      {
        "if":  { "field": "foo", "is": "equalTo", "value": "a" },
        "then":
          { "anyOf": [
            { "field": "bar", "is": "equalTo", "value": 1 },
            { "field": "bar", "is": "equalTo", "value": 2 }
          ]},
        "else": { "field": "bar", "is": "equalTo", "value": 50 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if":  { "field": "foo", "is": "equalTo", "value": "a" },
        "then": { "field": "bar", "is": "equalTo", "value": 10 },
        "else": {
          "anyOf": [
            { "field": "bar", "is": "equalTo", "value": 1 },
            { "field": "bar", "is": "equalTo", "value": 2 }
          ]}
      }
      """
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
    And there is a constraint:
      """
      {
        "if":
          { "allOf": [
            { "field": "foo", "is": "longerThan", "value": 0 },
            { "field": "foo", "is": "shorterThan", "value": 2 }
          ]},
        "then": { "field": "bar", "is": "equalTo", "value": 1 },
        "else": { "field": "bar", "is": "equalTo", "value": 55555 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if":  { "field": "foo", "is": "equalTo", "value": "a" },
        "then":
          { "allOf": [
            { "field": "bar", "is": "greaterThan", "value": 20 },
            { "field": "bar", "is": "lessThan", "value": 30 }
          ]},
        "else": { "field": "bar", "is": "equalTo", "value": 55555 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if":  { "field": "foo", "is": "equalTo", "value": "a" },
        "then": { "field": "bar", "is": "equalTo", "value": 1 },
        "else":
          { "allOf": [
            { "field": "bar", "is": "greaterThan", "value": 20 },
            { "field": "bar", "is": "lessThan", "value": 30 }
          ]}
      }
      """
    Then the following data should be generated:
      | foo     | bar |
      | "a"     | 1   |
      | "bb"    | 22  |
      | "ccc"   | 22  |
      | "dddd"  | 22  |
      | "eeeee" | 22  |

  Scenario: Running an if request that contains a contradictory allOf constraint within its if statement should be successful
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
    And there is a constraint:
      """
      {
        "if":
          { "allOf": [
            { "field": "foo", "is": "longerThan", "value": 88 },
            { "field": "foo", "is": "shorterThan", "value": 90 }
          ]},
        "then": { "field": "bar", "is": "equalTo", "value": 1 },
        "else": { "field": "bar", "is": "equalTo", "value": 55555 }
      }
      """
    Then the following data should be generated:
      | foo     | bar   |
      | "a"     | 55555 |
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
    And there is a constraint:
      """
      {
        "if":  { "field": "foo", "is": "equalTo", "value": "a" },
        "then":
          { "allOf": [
            { "field": "bar", "is": "greaterThan", "value": 200 },
            { "field": "bar", "is": "lessThan", "value": 300 }
          ]},
        "else": { "field": "bar", "is": "equalTo", "value": 55555 }
      }
      """
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
    And there is a constraint:
      """
      {
        "if":  { "field": "foo", "is": "equalTo", "value": "a" },
        "then": { "field": "bar", "is": "equalTo", "value": 1 },
        "else":
          { "allOf": [
            { "field": "bar", "is": "greaterThan", "value": 200 },
            { "field": "bar", "is": "lessThan", "value": 300 }
          ]}
      }
      """
    Then the following data should be generated:
      | foo | bar |
      | "a" | 1   |
