@ignore #pending development of #1235 - Allow a Numeric Field to Depend On Another Numeric Field
Feature:As a  User
        I can specify that a numeric value is greater than, less than or equal to a numeric value in a different field
        So that I can setup the test data to meet my requirements

  Background:
    Given the generation strategy is full
    And the combination strategy is minimal
    And there is a field foo
    And foo is of type "integer"
    And foo is greater than 1
    And foo is anything but null
    And there is a field bar
    And bar is of type "integer"
    And bar is anything but null


###Integer

  Scenario: The one where a user can specify that one number should be greater than another number
    Given bar is greater than 1
    And the generator can generate at most 3 rows
    And there is a constraint:
      """
        {
          "field": "bar",
          "is": "greaterThan",
          "otherField": "foo"
        }
      """
    Then the following data should be generated:
      | foo| bar|
      | 1  | 2  |
      | 2  | 3  |
      | 3  | 4  |

  Scenario: The one where a user can specify that one number should be greater than or equal to another number
    Given bar is greater than 1
    And the generator can generate at most 3 rows
    And there is a constraint:
      """
        {
          "field": "bar",
          "is": "greaterThanOrEqualTo",
          "otherField": "foo"
        }
      """
    Then the following data should be generated:
      | foo| bar|
      | 1  | 1  |
      | 2  | 2  |
      | 3  | 3  |

  Scenario: The one where a user can specify that one number should be less than another number
    Given bar is greater than 1
    And the generator can generate at most 3 rows
    And there is a constraint:
      """
        {
          "field": "bar",
          "is": "lessThan",
          "otherField": "foo"
        }
      """
    Then the following data should be generated:
      | foo| bar|
      | 1  | 0  |
      | 2  | 1  |
      | 3  | 2  |

  Scenario: The one where a user can specify that one number should be less than or equal to another number
    Given bar is greater than 1
    And the generator can generate at most 3 rows
    And there is a constraint:
      """
        {
          "field": "bar",
          "is": "lessThanOrEqualTo",
          "otherField": "foo"
        }
      """
    Then the following data should be generated:
      | foo| bar|
      | 1  | 1  |
      | 2  | 2  |

  Scenario: The one where a user can specify that one number should be equal to another number
    Given bar is greater than 1
    And the generator can generate at most 3 rows
    And there is a constraint:
      """
        {
          "field": "bar",
          "is": "equalTo",
          "otherField": "foo"
        }
      """
    Then the following data should be generated:
      | foo| bar|
      | 1  | 1  |
      | 2  | 2  |
      | 3  | 3  |

  Scenario: The one where a user can specify that one number should be equal to another number with a positive offset
    Given bar is greater than 1
    And the generator can generate at most 3 rows
    And there is a constraint:
      """
        {
          "field": "bar",
          "is": "equalTo",
          "otherField": "foo",
          "offset": 3,
          "offsetUnit": "integer"
        }
      """
    Then the following data should be generated:
      | foo| bar|
      | 1  | 4  |
      | 2  | 5  |
      | 3  | 6  |

  Scenario: The one where a user can specify that one number should be equal to another number with a negative offset
    Given the generator can generate at most 3 rows
    And there is a constraint:
      """
        {
          "field": "bar",
          "is": "equalTo",
          "otherField": "foo",
          "offset": -3,
          "offsetUnit": "integer"
        }
      """
    Then the following data should be generated:
      | foo| bar|
      | 1  | -2 |
      | 2  | -1 |
      | 3  | 0  |

  Scenario: The one where a user can specify that one number should be greater than another number with a positive offset
    Given bar is greater than 1
    And the generator can generate at most 3 rows
    And there is a constraint:
      """
        {
          "field": "bar",
          "is": "greaterThan",
          "otherField": "foo",
          "offset": 3,
          "offsetUnit": "integer"
        }
      """
    Then the following data should be generated:
      | foo| bar|
      | 1  | 4  |
      | 2  | 5  |
      | 3  | 6  |

  Scenario: The one where a user can specify that one number should be less than another number with a negative offset
    Given the generator can generate at most 3 rows
    And there is a constraint:
      """
        {
          "field": "bar",
          "is": "lessThan",
          "otherField": "foo",
          "offset": -3,
          "offsetUnit": "integer"
        }
      """
    Then the following data should be generated:
      | foo| bar|
      | 1  | -2 |
      | 2  | -1 |
      | 3  | 0  |

      ###Decimal

  Scenario: The one where a user can specify that one decimal number should be greater than another decimal number
    Given foo is of type "decimal"
    And foo is granular to 0.1
    And foo is greater than 1.0
    And bar is of type "decimal"
    And bar is granular to 0.1
    And bar is greater than 1.0
    And the generator can generate at most 3 rows
    And there is a constraint:
      """
        {
          "field": "bar",
          "is": "greaterThan",
          "otherField": "foo"
        }
      """
    Then the following data should be generated:
      | foo | bar |
      | 1.0 | 1.1 |
      | 1.1 | 1.2 |
      | 1.2 | 1.3 |

     ###Exhaustive

  Scenario: The one where a user can specify that one number should be greater than another number - exhaustive
    Given the combination strategy is exhaustive
    And bar is greater than 0
    And bar is less than 5
    And the generator can generate at most 6 rows
    And there is a constraint:
      """
        {
          "field": "bar",
          "is": "greaterThan",
          "otherField": "foo"
        }
      """
    Then the following data should be generated:
      | foo| bar|
      | 1  | 2  |
      | 1  | 3  |
      | 1  | 4  |
      | 2  | 3  |
      | 2  | 4  |
      | 3  | 4  |

