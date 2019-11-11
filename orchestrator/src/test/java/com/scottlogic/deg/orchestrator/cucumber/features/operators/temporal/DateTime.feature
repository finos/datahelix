Feature: User can specify a datetime in a particular format

  Background:
    Given the generation strategy is full
    And there is a nullable field foo
    And foo has type "datetime"

  Scenario: 'EqualTo' valid date time is successful for date time specified fully
    Given foo is equal to 2018-09-01T00:00:00.000Z
    And the generator can generate at most 1 rows
    Then the following data should be generated:
      | foo                      |
      | 2018-09-01T00:00:00.000Z |

  Scenario: 'EqualTo' valid date time is successful for date time specified to seconds
    Given foo is equal to 2018-09-01T00:00:00
    And the generator can generate at most 1 rows
    Then the following data should be generated:
      | foo                      |
      | 2018-09-01T00:00:00.000Z |

  Scenario: 'EqualTo' valid date time is successful for date time specified to minutes
    Given foo is equal to 2018-09-01T00:00
    And the generator can generate at most 1 rows
    Then the following data should be generated:
      | foo                      |
      | 2018-09-01T00:00:00.000Z |
