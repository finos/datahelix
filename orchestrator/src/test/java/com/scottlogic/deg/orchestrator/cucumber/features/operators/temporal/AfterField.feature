Feature: User can specify that one date should be after another date

  Background:
    Given the generation strategy is full

  Scenario: Running an "afterField" constraint allows one date to be always later than another
    Given there is a field foo
    And foo is of type "datetime"
    And foo is after 2018-09-01T00:00:00.000Z
    And there is a field bar
    And bar is of type "datetime"
    And the generator can generate at most 3 rows
    And there is a constraint:
      """
        {
          "field": "bar",
          "is": "afterField",
          "value": "foo"
        }
      """
    Then the following data should be generated:
      | foo                      | bar                      |
      | 2018-09-01T00:00:00.001Z | 2018-09-01T00:00:00.002Z |
      | 2018-09-01T00:00:00.001Z | 2018-09-01T00:00:00.003Z |
      | 2018-09-01T00:00:00.001Z | 2018-09-01T00:00:00.004Z |

