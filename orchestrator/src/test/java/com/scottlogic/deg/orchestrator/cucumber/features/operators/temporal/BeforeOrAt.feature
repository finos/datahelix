Feature: User can specify that a datetime date is lower than, or the same as, a specified threshold

  Background:
    Given the generation strategy is full
    And there is a field foo
    And foo has type "datetime"
    And foo is anything but null

  Scenario: Running beforeOrAt request that includes datetime field with date (YYYY-MM-DD) values that has invalid date should fail
    Given foo is before or at 2019-15-32T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the profile is invalid because "Date string '2019-15-32T00:00:00.000Z' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS\[Z\] between \(inclusive\) 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z"
    And no data is created

  Scenario: Running beforeOrAt request that includes datetime field with date and time (YYYY-MM-DDTHH:MM:SS) values that has invalid time should fail
    Given foo is before or at 2018-10-01T25:25:05.000Z
    And the generator can generate at most 5 rows
    Then the profile is invalid because "Date string '2018-10-01T25:25:05.000Z' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS[Z] between (inclusive) 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z"
    And no data is created

  Scenario: Running beforeOrAt request that includes datetime field with date and time (YYYY-MM-DDTHH:MM:SS) values that has invalid year should fail
    Given foo is before or at 0000-01-10T00:00:00.000Z
    Then the profile is invalid because "Dates must be between 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z"
    And no data is created

  Scenario: Running beforeOrAt request against a non-contradicting beforeOrAt constraint should be successful
    Given foo is before or at 2019-01-01T00:00:00.000Z
    And foo is anything but before or at 2018-01-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 2018-01-01T00:00:00.001Z |
      | 2018-01-01T00:00:00.002Z |
      | 2018-01-01T00:00:00.003Z |
      | 2018-01-01T00:00:00.004Z |
      | 2018-01-01T00:00:00.005Z |

  Scenario: Running beforeOrAt request against a non-contradicting beforeOrAt constraint should be successful
    Given foo is anything but before or at 2019-01-01T00:00:00.000Z
    And foo is anything but before or at 2018-01-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.001Z |
      | 2019-01-01T00:00:00.002Z |
      | 2019-01-01T00:00:00.003Z |
      | 2019-01-01T00:00:00.004Z |
      | 2019-01-01T00:00:00.005Z |

  Scenario: 'beforeOrEqualTo' run with minimum possible date should only generate null
    Given foo is before or at 0001-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo                      |
      | 0001-01-01T00:00:00.000Z |
