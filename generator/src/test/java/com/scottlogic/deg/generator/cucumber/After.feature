Feature: User can generate interesting values whilst specifying that a date is greater than and not equal to, a specified threshold

  Background:
    When the generation strategy is interesting

  Scenario: User creates data after a specified date
    Given there is a field foo
    And foo is after 2018-10-10T00:00:00.000

    Then the following data should be included in what is generated:
      | foo                     |
      | 2018-10-10T00:00:00.000 |
      | 2018-10-10T00:00:00.001 |
      | 2018-10-10T01:00:00.000 |
      | 2018-10-11T00:00:00.000 |
      | 2018-10-17T00:00:00.000 |
      | 2018-11-10T00:00:00.000 |
      | 2019-10-10T00:00:00.000 |
      | 2020-02-29T00:00:00.000 |
      | 2018-12-25T00:00:00.000 |
      | 2018-12-25T00:00:00.000 |
      | 2038-12-19T00:00:00.000 |
      | 2079-06-06T00:00:00.000 |
      | 2999-12-31T23:59:59.999 |
      | 3000-01-01T00:00:00.000 |
      | 9999-12-31T00:00:00.000 |

    And the following data should not be included in what is generated:
      | foo                     |
      | 2018-10-10T23:59:59.999 |
      | 2018-10-11T00:00:00.000 |
      | 2018-10-11T01:00:00.000 |
      | 2018-10-11T23:59:59.999 |
      | 2018-10-17T23:59:59.999 |
      | 2018-11-10T23:59:59.999 |
      | 2019-10-10T23:59:59.999 |
      | 2020-02-29T00:00:00.000 |
      | 2018-12-25T00:00:00.000 |
      | 2018-12-25T00:00:00.000 |
      | 2038-12-19T00:00:00.000 |
      | 2079-06-06T00:00:00.000 |
      | 2999-12-31T23:59:59.999 |
      | 3000-01-01T00:00:00.000 |
      | 9999-12-31T00:00:00.000 |
