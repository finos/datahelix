Feature: User can specify that a numeric value is lower than, but not equal to, a specified threshold
    
Scenario: User requires to create a numeric field with data values that are less than ten
     Given there is a field foo
       And foo is lessThan 10
       And foo is greaterThan 0
       And foo is granularTo 1
     When I ask to generate all values for field foo
     Then the following data should be generated:
       | foo |
       | 1   |
       | 2   |
       | 3   |
       | 4   |
       | 5   |
       | 6   |
       | 7   |
       | 8   |
       | 9   |

Scenario: User requires to create a field with decimal values that are less than ten, specified as an interger
     Given that there is a field foo
       And foo is granularTo 0.1
       And foo is lessThan 10
       And foo is greaterThan 9
     When I ask to generate all values for field foo
     Then the following data should be generated:
       | foo |
       | 9.1 |
       | 9.2 |
       | 9.3 |
       | 9.4 |
       | 9.5 |
       | 9.6 |
       | 9.7 |
       | 9.8 |
       | 9.9 |

Scenario: User requires to create a field with decimal values that are less than ten, specifed as a decimal
     Given that there is a field foo
       And foo is granularTo 0.1
       And foo is lessThan 10.0
       And foo is greaterThan 9.0
     When I ask to generate all values for field foo
     Then the following data should be generated:
       | foo |
       | 9.1 |
       | 9.2 |
       | 9.3 |
       | 9.4 |
       | 9.5 |
       | 9.6 |
       | 9.7 |
       | 9.8 |
       | 9.9 |

Scenario: User requires to create a numeric field with data values that are less than a zero
     Given there is a field foo
       And foo is lessThan 0
       And foo is greaterThan -10
       And foo is granularTo 1
     When I ask to generate all values for field foo
     Then the following data should be generated:
       | foo |
       | -1  |
       | -2  |
       | -3  |
       | -4  |
       | -5  |
       | -6  |
       | -7  |
       | -8  |
       | -9  |

Scenario: User requires to create a numeric field with data values that are less than ten and less than nine
     Given there is a field foo
       And foo is lessThan 10
       And foo is lessThan 9
       And foo is greaterThan 0
       And foo is granularTo 1
     When I ask to generate all values for field foo
     Then the following data should be generated:
       | foo |
       | 1   |
       | 2   |
       | 3   |
       | 4   |
       | 5   |
       | 6   |
       | 7   |
       | 8   |

Scenario: User attempts to create a numeric field with data value that are less than zero using an incorrect field value type of string
     Given there is a field foo
       And foo is lessThan "Zero"
       And foo is not null
     When I ask to generate all values for field foo
     Then I am presented with an error message
        And no data is created