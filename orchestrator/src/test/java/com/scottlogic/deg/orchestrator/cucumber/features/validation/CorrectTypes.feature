Feature: Correct Constraint Types, validation exceptions should be raised if the wrong typed constraint is applied to a field

  Scenario: Running a 'inSet' with numbers and type = string should throw exception
    Given there is a field foo
    And foo has type "string"
    And foo is in set:
      | 1 |
      | 2 |
      | 3 |
    Then the profile is invalid because "Field \[foo\]: is type STRING , but you are trying to apply a inSet constraint which requires NUMERIC"

  Scenario: Running a 'inSet' with numbers and type = datetime should throw exception
    Given there is a field foo
    And foo has type "datetime"
    And foo is in set:
      | 1 |
      | 2 |
      | 3 |
    Then the profile is invalid because "Field foo is a datetime but you are trying to assign non datetime value"

  Scenario: Running a 'equalTo' with numbers and type = datetime should throw exception
    Given there is a field foo
    And foo has type "datetime"
    And foo is equal to 1
    Then the profile is invalid because "Field foo is a datetime but you are trying to assign non datetime value"

  Scenario: Running a 'inSet' with strings and type = datetime should throw exception
    Given there is a field foo
    And foo has type "datetime"
    And foo is in set:
      | "aaa"|
      | "bbb |
      | "ccc |
    Then the profile is invalid because "Date string 'aaa' must be in ISO-8601 format: Either yyyy-MM-ddTHH:mm:ss.SSS\[Z\] between 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z or yyyy-mm-dd between 0001-01-01 and 9999-12-31"

  Scenario Outline: <wrongType> constraint <constraint> cannot be applied to <type> fields
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

      | datetime| ofLength              | STRING    | of length 3                       | DATETIME  |
      | datetime| shorterThan           | STRING    | shorter than 3                    | DATETIME  |
      | datetime| longerThan            | STRING    | longer than 3                     | DATETIME  |

      | decimal | granularTo            | DATETIME  | granular to "seconds"             | NUMERIC   |

      | decimal | matchingRegex         | STRING    | matching regex /ab/               | NUMERIC   |
      | decimal | containingRegex       | STRING    | containing regex /ab/             | NUMERIC   |
      | decimal | ofLength              | STRING    | of length 3                       | NUMERIC   |
      | decimal | shorterThan           | STRING    | shorter than 3                    | NUMERIC   |
      | decimal | longerThan            | STRING    | longer than 3                     | NUMERIC   |
      | decimal | equalTo               | STRING    | equal to "test"                   | NUMERIC   |
