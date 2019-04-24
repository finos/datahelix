Feature: User can specify that datetime fields are granular to a certain unit

  Background:
    Given the generation strategy is full
    And there is a field foo
    And foo is anything but null
    And foo is of type "datetime"


  Scenario Outline: User is able to specify supported temporal granularities
    Given foo is granular to <unit>
    And foo is after 2000-01-01T00:00:00.000Z
    And the generator can generate at most 1 rows
    Then the following data should be generated:
      | foo       |
      | <output>  |

    Examples:
      | unit      | output                    |
      | "millis"  | 2000-01-01T00:00:00.001Z  |
      | "seconds" | 2000-01-01T00:00:01.000Z  |
      | "minutes" | 2000-01-01T00:01:00.000Z  |
      | "hours"   | 2000-01-01T01:00:00.000Z  |
      | "days"    | 2000-01-02T00:00:00.000Z  |
      | "months"  | 2000-02-01T00:00:00.000Z  |
      | "years"   | 2001-01-01T00:00:00.000Z  |


  Scenario: Applying two valid datetime granularTo constraints generates data that matches both (coarser)
    Given foo is granular to "millis"
    And foo is granular to "seconds"
    And foo is after 2000-01-01T00:00:00.000Z
    And the generator can generate at most 1 rows
    Then the following data should be generated:
      | foo                       |
      | 2000-01-01T00:00:01.000Z  |


  Scenario: Applying an invalid datetime granularTo constraint fails with an appropriate error
    Given foo is granular to "decades"
    Then the profile is invalid because "Field \[foo\]: Couldn't recognise 'value' property, it must be a Number or one of the supported datetime units (millis, seconds, minutes, hours, days, months, years)"
    And no data is created

  Scenario: Applying a decimal granularTo constraint does not affect granularity
    Given foo is granular to 0.1
    And the generator can generate at most 1 rows
    And foo is after 2000-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                       |
      | 2000-01-01T00:00:00.001Z  |
