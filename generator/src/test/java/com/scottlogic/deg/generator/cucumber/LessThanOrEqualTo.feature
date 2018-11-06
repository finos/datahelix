Feature: User can specify that a numeric value is lower than, or equal to, a specified threshold

Background:
     Given the generation strategy is full
  
Scenario: User requires to create a numeric field with data values that are less than or the same as ten
     Given there is a field foo
       And foo is less than or equal to 10
       And foo is greater than 0
       And foo is granular to 1
       And foo is anything but null
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
       | 10  |

Scenario: User requires to create a numeric field with data values that are less than or the same as one but constrained to not be greater than zero
     Given there is a field foo
       And foo is less than or equal to 1
       And foo is greater than 0
       And foo is granular to 1
       And foo is anything but null
     Then the following data should be generated:
       | foo |
       | 1   |

Scenario: User requires to create a field with decimal values that are less than or the same as one, specified as an integer
     Given there is a field foo
       And foo is less than or equal to 1
       And foo is greater than 0
       And foo is granular to 0.1
       And foo is anything but null
     Then the following data should be generated:
       | foo |
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

Scenario: User requires to create a field with decimal values that are less than or the same as one, specified as a decimal
     Given there is a field foo
       And foo is less than or equal to 1.0
       And foo is greater than 0.0
       And foo is granular to 0.1
       And foo is anything but null
     Then the following data should be generated:
       | foo |
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

Scenario: User requires to create a numeric field with data values that are less than or the same as a negative number
     Given there is a field foo
       And foo is less than or equal to -10
       And foo is greater than -20
       And foo is granular to 1
       And foo is anything but null
     Then the following data should be generated:
       | foo |
       | -19 |
       | -18 |
       | -17 |
       | -16 |
       | -15 |
       | -14 |
       | -13 |
       | -12 |
       | -11 |
       | -10 |

Scenario: User requires to create a numeric field with data values that are less than ten and less than or the same as nine
     Given there is a field foo
       And foo is less than 10
       And foo is less than or equal to 9
       And foo is greater than 0
       And foo is granular to 1
       And foo is anything but null
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

Scenario: User requires to create a numeric field with data values that are less than or the same as ten and less than nine
     Given there is a field foo
       And foo is less than or equal to 10
       And foo is less than 9
       And foo is greater than 0
       And foo is granular to 1
       And foo is anything but null
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

Scenario: User requires to create a numeric field with data values that are less than or the same as five and less than or the same as four
     Given there is a field foo
       And foo is less than or equal to 5
       And foo is less than or equal to 4
       And foo is greater than 0
       And foo is granular to 1
       And foo is anything but null
     Then the following data should be generated:
       | foo |
       | 1   |
       | 2   |
       | 3   |
       | 4   |

Scenario: User attempts to create a numeric field with data value that are less than or the same as zero using an incorrect field value type of string
     Given there is a field foo
       But the profile is invalid as foo can't be less than or equal to "Zero"
     Then I am presented with an error message
        And no data is created