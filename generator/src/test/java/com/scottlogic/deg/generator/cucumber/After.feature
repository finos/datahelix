Feature: User can specify that a temporal date is lower than, but not equal to, a specified threshold

  Background:
    Given the generation strategy is full
    And there is a field foo
    And foo is of type "temporal"

@ignore
Scenario: 'After' valid date is successful
  Given foo is after 2018-09-01T00:00:00.000
    And the generator can generate at most 4 rows
  Then the following data should be generated:
    | foo                     |
    | null                    |
    | 2018-09-01T00:00:00.001 |
    | 2018-09-01T00:00:00.002 |
    | 2018-09-01T00:00:00.003 |

@ignore
Scenario Outline: 'After' invalid datetime fails with error
  Given foo is after <dateValue>
  Then I am presented with an error message
    And no data is created
  Examples:
    | dateValue               |
    | 2018-09-32T00:00:00.000 |
    | 2018-09-01T25:00:00.000 |
    | 2018-13-01T00:00:00.000 |
    | 0000-09-01T00:00:00.000 |
    | 2019-02-29T00:00:00.000 |

  ### after ###

@ignore
Scenario: 'After' with a non contradicting 'After' is successful
  Given foo is after 2019-01-01T00:00:00.000
    And foo is after 2019-02-01T00:00:00.000
    And the generator can generate at most 5 rows
  Then the following data should be generated:
    | foo                     |
    | null                    |
    | 2019-02-01T00:00:00.001 |
    | 2019-02-01T00:00:00.002 |
    | 2019-02-01T00:00:00.003 |
    | 2019-02-01T00:00:00.004 |

@ignore
Scenario: 'After' with a non contradicting 'Not After' is successful
  Given foo is after 2019-01-01T00:00:00.000
    And foo is anything but after 2020-01-01T00:00:00.000
    And the generator can generate at most 5 rows
  Then the following data should be generated:
    | foo                     |
    | null                    |
    | 2019-01-01T00:00:00.001 |
    | 2019-01-01T00:00:00.002 |
    | 2019-01-01T00:00:00.003 |
    | 2019-01-01T00:00:00.004 |

@ignore
Scenario: 'Not After' with a non contradicting 'Not After' is successful
  Given foo is anything but after 2019-01-01T00:00:00.000
    And foo is anything but after 2020-01-01T00:00:00.000
    And the generator can generate at most 5 rows
  Then the following data should be generated:
    | foo                     |
    | null                    |
    | 2019-01-01T00:00:00.000 |
    | 2019-12-31T00:00:00.999 |
    | 2019-12-31T00:00:00.998 |
    | 2019-12-31T00:00:00.997 |


### Contradictions ###

@ignore
Scenario: 'After' with a contradicting 'Not after' generates null
  Given foo is after 2019-01-01T00:00:00.000
    And foo is anything but after 2019-01-01T00:00:00.000
  Then the following data should be generated:
    | foo                     |
    | null                    |
