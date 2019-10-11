Feature: Generator can produce correct data for complex profiles.

  Scenario: Running a random strategy on a profile where there are hard to detect contradictions within or statements does not crash
    Given the generation strategy is Random
    And the generator can generate at most 5 rows
    And the following fields exist:
      | foo |
      | bar |
    And foo has type "integer"
    And foo is anything but null
    And bar has type "integer"
    And bar is anything but null
    And Any Of the next 2 constraints
    And All Of the next 3 constraints
      And Any Of the next 2 constraints
        And bar is equal to 1
        And bar is equal to 2
      And Any Of the next 2 constraints
        And foo is equal to 1
        And bar is equal to 3
      And Any Of the next 2 constraints
        And foo is equal to 2
        And bar is equal to 4
    And All Of the next 2 constraints
      And foo is equal to 10
      And bar is equal to 10
    Then the following data should be generated:
      | foo | bar |
      | 10  | 10  |
      | 10  | 10  |
      | 10  | 10  |
      | 10  | 10  |
      | 10  | 10  |
