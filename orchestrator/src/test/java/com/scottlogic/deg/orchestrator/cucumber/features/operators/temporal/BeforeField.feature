Feature: User can specify that one date should be before another date

  Background:
    Given the generation strategy is full

  Scenario: Running a "beforeField" constraint allows one date to be always earlier than another
    Given there is a field foo
    And foo is of type "datetime"
    And there is a field bar
    And bar is of type "datetime"
    And the generator can generate at most 3 rows
    And there is a constraint:
      """
        {
          "field": "foo",
          "is": "beforeField",
          "value": "bar"
        }
      """
    Then the following data should be generated:
      | foo                      | bar                      |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.001Z |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.002Z |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.003Z |

