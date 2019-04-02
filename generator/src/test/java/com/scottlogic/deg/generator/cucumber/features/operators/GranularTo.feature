Feature: User can specify that a numeric value is of a decimalised value to a specified level of unit

  Background:
    Given the generation strategy is full
    And there is a field foo
    And foo is of type "decimal"
    And foo is anything but null

  Scenario: User requires to create a numeric field with data values that include a decimal value to one decimal point
    Given foo is granular to 0.1
    And foo is greater than or equal to 0
    And foo is less than or equal to 1
    Then the following data should be generated:
      | foo |
      | 0.0 |
      | 0.1 |
      | 0.2 |
      | 0.3 |
      | 0.4 |
      | 0.5 |
      | 0.6 |
      | 0.7 |
      | 0.8 |
      | 0.9 |
      | 1.0 |

  Scenario: User requires to create a numeric field with data values that include a decimal value to two decimal points
    Given foo is granular to 0.01
    And foo is greater than or equal to 0
    And foo is less than or equal to 0.2
    Then the following data should be generated:
      | foo  |
      | 0.00 |
      | 0.01 |
      | 0.02 |
      | 0.03 |
      | 0.04 |
      | 0.05 |
      | 0.06 |
      | 0.07 |
      | 0.08 |
      | 0.09 |
      | 0.10 |
      | 0.11 |
      | 0.12 |
      | 0.13 |
      | 0.14 |
      | 0.15 |
      | 0.16 |
      | 0.17 |
      | 0.18 |
      | 0.19 |
      | 0.20 |

  Scenario: User requires to create a numeric field with data values that include a decimal value to five decimal points
    Given foo is granular to 0.00001
    And foo is greater than or equal to 0
    And foo is less than or equal to 0.0001
    Then the following data should be generated:
      | foo     |
      | 0.00000 |
      | 0.00001 |
      | 0.00002 |
      | 0.00003 |
      | 0.00004 |
      | 0.00005 |
      | 0.00006 |
      | 0.00007 |
      | 0.00008 |
      | 0.00009 |
      | 0.00010 |

  Scenario: User requires to create a numeric field with negative data values that include a decimal value to one decimal point
    Given foo is granular to 0.1
    And foo is less than or equal to 0
    And foo is greater than or equal to -1
    Then the following data should be generated:
      | foo  |
      | 0.0  |
      | -0.1 |
      | -0.2 |
      | -0.3 |
      | -0.4 |
      | -0.5 |
      | -0.6 |
      | -0.7 |
      | -0.8 |
      | -0.9 |
      | -1.0 |

  Scenario: User attempts to create a numeric field with data value that include a decimal value to one decimal point incorrectly using a string to set the granularity
    Given foo is granular to "0.1"
    Then the profile is invalid
    And no data is created

  Scenario: Running a 'granularTo' request that specifies null should be unsuccessful
    Given foo is granular to null
    Then the profile is invalid because "Couldn't recognise 'value' property, it must be set to a value"
    And no data is created


  Scenario: Running granularTo against a non contradicting granularTo should be successful
    Given foo is granular to 1
    And foo is granular to 1
    And foo is greater than 0
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo |
      | 1   |
      | 2   |
      | 3   |
      | 4   |
      | 5   |

  Scenario: Running granularTo run against a non contradicting not granularTo should be successful
    Given foo is granular to 1
    And foo is anything but granular to 0.1
    And foo is greater than 0
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo |
      | 1   |
      | 2   |
      | 3   |
      | 4   |
      | 5   |

  @ignore #issue 769 not sure what is expected result
  Scenario: Running not granularTo against a non contradicting not granularTo should be successful
    Given foo is anything but granular to 1
    And foo is anything but granular to 0.1
    And the generator can generate at most 4 rows
    Then the following data should be generated:
      | foo |
      | 0.2 |
      | 0.3 |
      | 0.4 |
      | 0.5 |

  @ignore #issue 769 not sure what is expected result
  Scenario: Running granularTo run against a contradicting granularTo should only generate string, temporal null and integers
    Given foo is granular to 1
    And foo is anything but granular to 1
    And foo is greater than 0
    And the generator can generate at most 2 rows
    Then the following data should be generated:

  @ignore #issue 769 not sure what is expected result
  Scenario: Running granularTo against a non contradicting after should be successful
    Given foo is granular to 1
    And foo is after 2018-09-01T00:00:00.001Z
    And foo is greater than 0
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 1                        |
      | 2                        |
      | 2018-09-01T00:00:00.002Z |
      | 2018-09-01T00:00:00.003Z |
      | 2018-09-01T00:00:00.004Z |

  @ignore #issue 769 not sure what is expected result
  Scenario: Running granularTo against a non contradicting not after should be successful
    Given foo is granular to 1
    And foo is anything but after 2018-09-01T00:00:00.005Z
    And foo is greater than 0
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 1                        |
      | 2                        |
      | 2018-09-01T00:00:00.001Z |
      | 2018-09-01T00:00:00.002Z |
      | 2018-09-01T00:00:00.003Z |







