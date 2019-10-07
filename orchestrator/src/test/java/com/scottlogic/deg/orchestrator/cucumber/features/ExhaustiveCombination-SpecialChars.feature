Feature: Whilst including non-latin characters, user can create data across multiple fields for all combinations available.

  Background:
    Given the generation strategy is full
    And the combination strategy is exhaustive

  Scenario: Running an exhaustive combination strategy with special character (emoji) strings should be successful
    Given the following fields exist:
      | foo |
      | bar |
    And foo has type "string"
    And foo is anything but null
    And bar has type "string"
    And bar is anything but null
    And foo is in set:
      | "😐" |
      | "☻"  |
    And bar is in set:
      | "🚍" |
      | "🚌" |
    Then the following data should be generated:
      | foo  | bar  |
      | "😐" | "🚍" |
      | "☻"  | "🚍" |
      | "😐" | "🚌" |
      | "☻"  | "🚌" |

  Scenario: Running an exhaustive combination strategy with special character (various white spaces) strings should be successful
    Given the following fields exist:
      | foo |
      | bar |
    And foo has type "string"
    And foo is anything but null
    And bar has type "string"
    And bar is anything but null
    And foo is in set:
      | " " |
      | " " |
    And bar is in set:
      | " " |
      | " " |
    Then the following data should be generated:
      | foo | bar |
      | " " | " " |
      | " " | " " |
      | " " | " " |
      | " " | " " |
