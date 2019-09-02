Feature: As a user
        I want to be able to specify that a field is unique
        So that I can avoid duplication issues for certain fields

  Background:
    Given there is a field unique
    And unique is unique
    And unique is of type "integer"
    And unique is greater than 0
    And unique is anything but null
    And the combination strategy is minimal
    And the generator can generate at most 5 rows

    Scenario: The one where there are 2 unique fields and 1 non unique field
      Given there is a field foo
      And foo is unique
      And foo is of type "integer"
      And foo is greater than 0
      And foo is anything but null
      And there is a field bar
      And bar is equal to "not unique"
      And the generation strategy is random
      Then the following data should be generated:
        | unique | foo | bar         |
        | 1      | 1   | "not unique"|
        | 2      | 2   | "not unique"|
        | 3      | 3   | "not unique"|
        | 4      | 4   | "not unique"|
        | 5      | 5   | "not unique"|

    Scenario: The one where the range is exceeded
      Given unique is less than 3
      And there is a field foo
      And foo is of type "integer"
      And foo is greater than 0
      And foo is anything but null
      Then the following data should be generated:
        |unique | foo |
        | 1     | 1   |
        | 2     | 2   |

    Scenario: The one where combination strategy is minimal
      And there is a field foo
      And foo is of type "integer"
      And foo is greater than 0
      And foo is less than 4
      And foo is anything but null
      And there is a field bar
      And bar is of type "integer"
      And bar is greater than 0
      And bar is less than 4
      And bar is anything but null
      Then the following data should be generated:
        |unique | foo | bar|
        | 1     | 1   | 1  |
        | 2     | 2   | 2  |
        | 3     | 3   | 3  |
        | 4     | 3   | 3  |
        | 5     | 3   | 3  |

  Scenario: The one where combination strategy is exhaustive
    Given the combination strategy is exhaustive
    And there is a field foo
    And foo is of type "integer"
    And foo is greater than 0
    And foo is less than 4
    And foo is anything but null
    And there is a field bar
    And bar is of type "integer"
    And bar is greater than 0
    And bar is less than 4
    And bar is anything but null
    Then the profile is invalid because "Unique fields do not work when not using Minimal combination strategy"
    And no data is created
