Feature: Correct Constraint Types, validation exceptions should be raised if the wrong typed constraint is applied to a field

  Scenario: Running a 'inSet' request alongside a contradicting ofType = string should produce null
    Given there is a field foo
    And foo has type "string"
    And foo is in set:
      | 1 |
      | 2 |
      | 3 |
    And foo has type "string"
    Then the profile is invalid because "Field \[foo\]: is type STRING , but you are trying to apply a inSet constraint which requires NUMERIC"

  Scenario Outline: <wrongType> constraints cannot be applied to <type> fields
    Given there is a field foo
    And foo has type "<type>"
    And foo is <constraint>
    Then the profile is invalid because "Field \[foo\]: is type <upperType> , but you are trying to apply a <constraintType> constraint which requires <wrongType>"

    Examples:
      | type    | constraintType        | wrongType |  constraint                       | upperType |
      | string  | greaterThan           | NUMERIC   | greater than 4                    | STRING    |
      | string  | greaterThanOrEqualTo  | NUMERIC   | greater than or equal to 4        | STRING    |
      | string  | lessThan              | NUMERIC   | less than 4                       | STRING    |
      | string  | lessThanOrEqualTo     | NUMERIC   | less than or equal to 4           | STRING    |
      | string  | granularTo            | NUMERIC   | granular to 0.1                   | STRING    |
      | string  | equalTo               | NUMERIC   | equal to 1                        | STRING    |

      | string  | granularTo            | DATETIME  | granular to "seconds"             | STRING    |

      | datetime| greaterThan           | NUMERIC   | greater than 4                    | DATETIME  |
      | datetime| greaterThanOrEqualTo  | NUMERIC   | greater than or equal to 4        | DATETIME  |
      | datetime| lessThan              | NUMERIC   | less than 4                       | DATETIME  |
      | datetime| lessThanOrEqualTo     | NUMERIC   | less than or equal to 4           | DATETIME  |
      | datetime| granularTo            | NUMERIC   | granular to 0.1                   | DATETIME  |
      | datetime| equalTo               | NUMERIC   | equal to 1                        | DATETIME    |

      | datetime| ofLength              | STRING    | of length 3                       | DATETIME  |
      | datetime| shorterThan           | STRING    | shorter than 3                    | DATETIME  |
      | datetime| longerThan            | STRING    | longer than 3                     | DATETIME  |

      | decimal | granularTo            | DATETIME  | granular to "seconds"             | NUMERIC   |

      | decimal | matchingRegex         | STRING    | matching regex "ab"               | NUMERIC   |
      | decimal | containingRegex       | STRING    | containing regex "ab"             | NUMERIC   |
      | decimal | ofLength              | STRING    | of length 3                       | NUMERIC   |
      | decimal | shorterThan           | STRING    | shorter than 3                    | NUMERIC   |
      | decimal | longerThan            | STRING    | longer than 3                     | NUMERIC   |
      | decimal | equalTo               | STRING    | equal to "test"                   | NUMERIC   |
