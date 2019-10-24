Feature:As a  User
  I can specify that a numeric value is greater than, less than or equal to a numeric value in a different field
  So that I can setup the test data to meet my requirements

  Background:
    Given the generation strategy is full
    And the combination strategy is exhaustive
    And there is a non nullable field foo
    And foo has type "integer"
    And foo is greater than 0
    And there is a non nullable field bar
    And bar has type "integer"
###Integer
  Scenario: The one where a user can specify that one number should be greater than another number
    Given bar is greater than 0
    And bar is less than 4
    And the generator can generate at most 5 rows
    And bar is greater than field foo
    Then the following data should be generated:
      | foo | bar |
      | 1   | 2   |
      | 1   | 3   |
      | 2   | 3   |

  Scenario: The one where a user can specify that one number should be greater than or equal to another number
    Given bar is greater than 0
    And bar is less than 4
    And the generator can generate at most 5 rows
    And bar is greater than or equal to field foo
    Then the following data should be generated:
      | foo | bar |
      | 1   | 1   |
      | 1   | 2   |
      | 1   | 3   |
      | 2   | 2   |
      | 2   | 3   |

  Scenario: The one where a user can specify that one number should be less than another number
    Given foo is less than 3
    And bar is greater than 0
    And the generator can generate at most 3 rows
    And bar is less than field foo
    Then the following data should be generated:
      | foo | bar |
      | 2   | 1   |

  Scenario: The one where a user can specify that one number should be less than or equal to another number
    Given the combination strategy is exhaustive
    And foo is less than 3
    And bar is greater than 0
    And bar is less than or equal to field foo
    Then the following data should be generated:
      | foo | bar |
      | 1   | 1   |
      | 2   | 1   |
      | 2   | 2   |

  Scenario: The one where a user can specify that one number should be equal to another number
    Given bar is greater than 0
    And the generator can generate at most 3 rows
    And bar is equal to field foo
    Then the following data should be generated:
      | foo | bar |
      | 1   | 1   |
      | 2   | 2   |
      | 3   | 3   |

  Scenario: The one where a user can specify that one number should be equal to another number with a positive offset
    Given bar is greater than 0
    And the generator can generate at most 3 rows
    And there is a constraint:
      """
        {
          "field": "bar",
          "equalToField": "foo",
          "offset": 3,
          "offsetUnit": 1
        }
      """
    Then the following data should be generated:
      | foo | bar |
      | 1   | 4   |
      | 2   | 5   |
      | 3   | 6   |

  Scenario: The one where a user can specify that one number should be equal to another number with a negative offset
    Given the generator can generate at most 3 rows
    And there is a constraint:
      """
        {
          "field": "bar",
          "equalToField": "foo",
          "offset": -3,
          "offsetUnit": 1
        }
      """
    Then the following data should be generated:
      | foo | bar |
      | 1   | -2  |
      | 2   | -1  |
      | 3   | 0   |

  @ignore #pending development of #1235 - Allow a Numeric Field to Depend On Another Numeric Field
  Scenario: The one where a user can specify that one number should be greater than another number with a positive offset
    Given bar is greater than 0
    And the generator can generate at most 3 rows
    And there is a constraint:
      """
        {
          "field": "bar",
          "is": "greaterThan",
          "otherField": "foo",
          "offset": 3,
          "offsetUnit": 1
        }
      """
    Then the following data should be generated:
      | foo | bar |
      | 1   | 4   |
      | 2   | 5   |
      | 3   | 6   |

  @ignore #pending development of #1235 - Allow a Numeric Field to Depend On Another Numeric Field
  Scenario: The one where a user can specify that one number should be less than another number with a negative offset
    Given the generator can generate at most 3 rows
    And there is a constraint:
      """
        {
          "field": "bar",
          "is": "lessThan",
          "otherField": "foo",
          "offset": -3,
          "offsetUnit": 1
        }
      """
    Then the following data should be generated:
      | foo | bar |
      | 1   | -2  |
      | 2   | -1  |
      | 3   | 0   |
     ###Exhaustive
  Scenario: The one where a user can specify that one number should be greater than another number - exhaustive
    Given the combination strategy is exhaustive
    And bar is greater than 0
    And bar is less than 5
    And the generator can generate at most 6 rows
    And bar is greater than field foo
    Then the following data should be generated:
      | foo | bar |
      | 1   | 2   |
      | 1   | 3   |
      | 1   | 4   |
      | 2   | 3   |
      | 2   | 4   |
      | 3   | 4   |
