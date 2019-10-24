Feature: User can specify that a datetime date is more than, or the same as, a specified threshold

  Background:
    Given the generation strategy is full
    And there is a non nullable field foo
    And foo has type "datetime"


  Scenario: User requires to create a datetime field with values that are after or at a specified date
    Given foo is after or at 2018-01-01T00:00:00.000Z
    And the generator can generate at most 6 rows
    Then the following data should be generated:
      | foo                      |
      | 2018-01-01T00:00:00.000Z |
      | 2018-01-01T00:00:00.001Z |
      | 2018-01-01T00:00:00.002Z |
      | 2018-01-01T00:00:00.003Z |
      | 2018-01-01T00:00:00.004Z |
      | 2018-01-01T00:00:00.005Z |

  Scenario: User requires to create a datetime field with date and time (YYYY-MM-DDTHH:MM:SS) values that are after or at specified date and time
    Given foo is after or at 2018-01-01T12:00:00.000Z
    And foo is in set:
      | 2016-01-01T12:00:00.000Z |
      | 2017-01-01T12:00:00.000Z |
      | 2018-01-01T12:00:00.000Z |
      | 2019-01-01T12:00:00.000Z |
      | 2019-01-01T12:01:05.000Z |
      | 2020-01-01T12:01:00.000Z |
    Then the following data should be generated:
      | foo                      |
      | 2018-01-01T12:00:00.000Z |
      | 2019-01-01T12:00:00.000Z |
      | 2019-01-01T12:01:05.000Z |
      | 2020-01-01T12:01:00.000Z |

  Scenario: Running afterOrAt request that includes datetime field with date (YYYY-MM-DD) values that has invalid date should fail
    Given foo is after or at 2019-15-32T00:00:00.000Z
    Then the profile is invalid because "Date string '2019-15-32T00:00:00.000Z' must be in ISO-8601 format: Either yyyy-MM-ddTHH:mm:ss.SSS\[Z\] between 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z or yyyy-mm-dd between 0001-01-01 and 9999-12-31"
    And no data is created

  Scenario: Running afterOrAt request that includes datetime field with date and time (YYYY-MM-DDTHH:MM:SS) values that has invalid time should fail
    Given foo is after or at 2018-10-01T25:25:05.000Z
    Then the profile is invalid because "Date string '2018-10-01T25:25:05.000Z' must be in ISO-8601 format: Either yyyy-MM-ddTHH:mm:ss.SSS\[Z\] between 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z or yyyy-mm-dd between 0001-01-01 and 9999-12-31"
    And no data is created

  Scenario: Running afterOrAt request that includes datetime field with date and time (YYYY-MM-DDTHH:MM:SS) values that has invalid year should fail
    Given foo is after or at 0000-01-10T00:00:00.000Z
    Then the profile is invalid because "Dates must be between 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z"
    And no data is created

  Scenario: Running afterOrAt request that includes datetime field with date and time (YYYY-MM-DDTHH:MM:SS) values that has leap year
    Given foo is after or at 2019-01-01T00:00:00.000Z
    And foo is in set:
      | 2017-01-01T00:00:00.000Z |
      | 2018-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.000Z |
      | 2020-01-01T00:00:00.000Z |
      | 2021-01-01T00:00:00.000Z |
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.000Z |
      | 2020-01-01T00:00:00.000Z |
      | 2021-01-01T00:00:00.000Z |

  Scenario: Running afterOrAt request against a non-contradicting afterOrAt constraint should be successful
    Given foo is after or at 2019-01-01T00:00:00.000Z
    And foo is after or at 2020-01-01T00:00:00.000Z
    And foo is in set:
      | 2017-01-01T00:00:00.000Z |
      | 2018-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.000Z |
      | 2020-01-01T00:00:00.000Z |
      | 2021-01-01T00:00:00.000Z |
      | 2022-01-01T00:00:00.000Z |
    Then the following data should be generated:
      | foo                      |
      | 2020-01-01T00:00:00.000Z |
      | 2021-01-01T00:00:00.000Z |
      | 2022-01-01T00:00:00.000Z |

  Scenario: Running afterOrAt request against a non contradicting beforeOrAt should be successful
    Given foo is after or at 2019-01-01T00:00:00.000Z
    And foo is before or at 2020-01-01T00:00:00.000Z
    And foo is in set:
      | 2017-01-01T00:00:00.000Z |
      | 2018-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.000Z |
      | 2020-01-01T00:00:00.000Z |
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.000Z |
      | 2020-01-01T00:00:00.000Z |

  Scenario: Running afterOrAt request against a non contradicting beforeOrAt should be successful
    Given foo is anything but after or at 2019-01-01T00:00:00.000Z
    And foo is before or at 2019-01-01T00:00:00.000Z
    And foo is in set:
      | 2017-01-01T00:00:00.000Z |
      | 2018-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.000Z |
      | 2020-01-01T00:00:00.000Z |
    Then the following data should be generated:
      | foo                      |
      | 2017-01-01T00:00:00.000Z |
      | 2018-01-01T00:00:00.000Z |

  Scenario: Running afterOrAt request against a non contradicting beforeOrAt should be successful
    Given foo is after or at 2019-01-01T00:00:00.000Z
    And foo is anything but before or at 2019-01-02T00:00:00.000Z
    And foo is in set:
      | 2017-01-01T00:00:00.000Z |
      | 2018-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.000Z |
      | 2020-01-01T00:00:00.000Z |
      | 2021-01-01T00:00:00.000Z |
    Then the following data should be generated:
      | foo                      |
      | 2020-01-01T00:00:00.000Z |
      | 2021-01-01T00:00:00.000Z |

  Scenario: Running afterOrAt request against a non contradicting beforeOrAt should be successful
    Given foo is anything but after or at 2019-01-02T00:00:00.000Z
    And foo is before or at 2019-01-01T00:00:00.000Z
    And foo is in set:
      | 2017-01-01T00:00:00.000Z |
      | 2018-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.000Z |
      | 2020-01-01T00:00:00.000Z |
      | 2021-01-01T00:00:00.000Z |
    Then the following data should be generated:
      | foo                      |
      | 2017-01-01T00:00:00.000Z |
      | 2018-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.000Z |

  Scenario: Running afterOrAt request against a non contradicting beforeOrAt should be successful
    Given foo is anything but after or at 2022-01-01T00:00:00.000Z
    And foo is anything but before or at 2019-01-01T00:00:00.000Z
    And foo is in set:
      | 2017-01-01T00:00:00.000Z |
      | 2018-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.000Z |
      | 2020-01-01T00:00:00.000Z |
      | 2021-01-01T00:00:00.000Z |
    Then the following data should be generated:
      | foo                      |
      | 2020-01-01T00:00:00.000Z |
      | 2021-01-01T00:00:00.000Z |
