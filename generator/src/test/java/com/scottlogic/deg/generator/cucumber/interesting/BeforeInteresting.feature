Feature: User can specify that a temporal date is lower than, but not equal to, a specified threshold

  Scenario: User requires to create a temporal field with date (YYYY-MM-DD) values within a given month that are less than a specified date
    Given there is a field foo
    And foo is before 2018-10-10T00:00:00.000
    And foo is of type "temporal"
    And foo is formatted as "%tF"
    And generation strategy is interesting
    Then the following data should be generated:
      | foo        |
      | 2018-10-01 |
      | 2018-10-02 |
      | 2018-10-03 |
      | 2018-10-04 |
      | 2018-10-05 |
      | 2018-10-06 |
      | 2018-10-07 |
      | 2018-10-08 |
      | 2018-10-09 |