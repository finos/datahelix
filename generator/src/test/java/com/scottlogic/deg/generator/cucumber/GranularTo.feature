Feature: User can specify that a numeric value is of a decimalised value to a specified level of unit

Background:
     Given the generation strategy is full
    
Scenario: User requires to create a numeric field with data values that include a decimal value to one decimal point
     Given there is a field foo
       And foo is granular to 0.1
       And foo is greater than or equal to 0
       And foo is less than or equal to 1
       And foo is anything but null
     Then the following data should be generated:
       | foo  |
       | 0.0  |
       | 0.1  |
       | 0.2  |
       | 0.3  |
       | 0.4  |
       | 0.5  |
       | 0.6  |
       | 0.7  |
       | 0.8  |
       | 0.9  |
       | 1.0  |

Scenario: User requires to create a numeric field with data values that include a decimal value to two decimal points
     Given there is a field foo
       And foo is granular to 0.01
       And foo is greater than or equal to 0
       And foo is less than or equal to 0.2
       And foo is anything but null
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
     Given there is a field foo
       And foo is granular to 0.00001
       And foo is greater than or equal to 0
       And foo is less than or equal to 0.0001
       And foo is anything but null
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
     Given there is a field foo
       And foo is granular to 0.1
       And foo is less than or equal to 0
       And foo is greater than or equal to -1
       And foo is anything but null
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
  Given there is a field foo
  And foo is granular to "0.1"
     Then the profile is invalid
        And no data is created