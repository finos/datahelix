Feature: User can specify that a temporal date is lower than, or the same as, a specified threshold

Background:
     Given the generation strategy is full
       And there is a field foo
       And foo is of type "temporal"
       And foo is anything but null

@ignore #issue 594
Scenario: User requires to create a temporal field with date (YYYY-MM-DD) values within a given month that are less than or at a specified date
     Given foo is before or at 2018-10-10T00:00:00.000
       And the generator can generate at most 30 rows
     Then the following data should be generated:
       | foo                     |
       | 2018-10-01T00:00:00.000 |
       | 2018-10-02T00:00:00.000 |
       | 2018-10-03T00:00:00.000 |
       | 2018-10-04T00:00:00.000 |
       | 2018-10-05T00:00:00.000 |
       | 2018-10-06T00:00:00.000 |
       | 2018-10-07T00:00:00.000 |
       | 2018-10-08T00:00:00.000 |
       | 2018-10-09T00:00:00.000 |
       | 2018-10-10T00:00:00.000 |

@ignore #issue 594
Scenario: User requires to create a temporal field with date and time (YYYY-MM-DDTHH:MM:SS) values across a minute boundary that are less than or at specified date and time
     Given foo is before or at 2018-10-01T12:01:05.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | 2018-10-01T12:01:05.000 |
       | 2018-09-01T12:01:02.000 |
       | 2018-07-01T12:01:03.000 |
       | 2018-06-01T12:01:04.000 |


Scenario: Running beforeOrAt request that includes temporal field with date (YYYY-MM-DD) values that has invalid date should fail
     Given foo is before or at 2019-15-32T00:00:00.000
       And the generator can generate at most 5 rows
     Then I am presented with an error message
       And no data is created

Scenario: Running beforeOrAt request that includes temporal field with date and time (YYYY-MM-DDTHH:MM:SS) values that has invalid time should fail
     Given foo is before or at 2018-10-01T25:25:05.000
       And the generator can generate at most 5 rows
     Then I am presented with an error message
       And no data is created

Scenario: Running beforeOrAt request that includes temporal field with date and time (YYYY-MM-DDTHH:MM:SS) values that has invalid year should fail
     Given foo is before or at 0000-01-10T00:00:00.000
     Then I am presented with an error message
       And no data is created

Scenario: Running beforeOrAt request that includes temporal field with date and time (YYYY-MM-DDTHH:MM:SS) values that has invalid format should fail
     Given foo is before or at "2018-Feb-01T00:00:00.000"
       And the generator can generate at most 5 rows
     Then I am presented with an error message
       And no data is created

@ignore #issue 594
Scenario: Running beforeOrAt request that includes temporal field with date and time (YYYY-MM-DDTHH:MM:SS) values that has leap year
     Given foo is before or at 2024-01-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | 2016-10-01T12:01:05.000 |
       | 2012-09-01T12:01:02.000 |
       | 2024-07-01T12:01:03.000 |

Scenario: Running beforeOrAt request against a non-contradicting beforeOrAt constraint should be successful
     Given foo is before or at 2019-01-01T00:00:00.000
       And foo is before or at 2018-01-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | 2017-10-01T00:00:00.000 |
       | 2016-10-02T00:00:00.000 |
       | 2018-01-01T00:00:00.000 |
       | 2019-01-01T00:00:00.000 |

Scenario: Running beforeOrAt request against a non-contradicting beforeOrAt constraint should be successful
     Given foo is before or at 2019-01-01T00:00:00.000
       And foo is anything but before or at 2018-01-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | 2018-03-01T00:00:00.000 |
       | 2018-12-01T00:00:00.000 |
       | 2019-01-01T00:00:00.000 |

Scenario: Running beforeOrAt request against a non-contradicting beforeOrAt constraint should be successful
     Given foo is anything but before or at 2019-01-01T00:00:00.000
       And foo is anything but before or at 2018-01-01T00:00:00.000
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo                     |
       | 2019-10-01T00:00:00.000 |
       | 2019-11-02T00:00:00.000 |
       | 2020-01-01T00:00:00.000 |
       | 2021-01-01T00:00:00.000 |
Scenario: 'beforeOrEqualTo' run with minimum possible date should only generate null
    Given foo is before or at 0001-01-01T00:00:00.000
    Then the following data should be generated:
      | foo                     |
      | 0001-01-01T00:00:00.000 |
