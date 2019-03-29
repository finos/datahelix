Feature: User can create data across multiple fields for all combinations available.

Background:
     Given the generation strategy is full
     And the combination strategy is exhaustive

Scenario: Running an exhaustive combination strategy with roman alphabet character (a-z) strings should be successful
      Given the generation strategy is full
      And the combination strategy is exhaustive
      And the following fields exist:
       | foo |
       | bar |
       And foo is of type "string"
       And foo is anything but null
       And bar is of type "string"
       And bar is anything but null
       And foo is in set:
         | "a" |
         | "b" |
       And bar is in set:
         | "c" |
         | "d" |
     Then the following data should be generated:
       | foo | bar |
       | "a" | "c" |
       | "b" | "c" |
       | "a" | "d" |
       | "b" | "d" |

Scenario: Running an exhaustive combination strategy with upper case roman alphabet character (A-Z) strings should be successful
     Given the following fields exist:
       | foo |
       | bar |
       And foo is of type "string"
       And foo is anything but null
       And bar is of type "string"
       And bar is anything but null
       And foo is in set:
         | "A" |
         | "B" |
       And bar is in set:
         | "C" |
         | "D" |
     Then the following data should be generated:
       | foo | bar |
       | "A" | "C" |
       | "B" | "C" |
       | "A" | "D" |
       | "B" | "D" |

Scenario: Running an exhaustive combination strategy with roman numeric character (0-9) strings should be successful
     Given the following fields exist:
       | foo |
       | bar |
       And foo is of type "string"
       And foo is anything but null
       And bar is of type "string"
       And bar is anything but null
       And foo is in set:
         | "0" |
         | "1" |
       And bar is in set:
         | "6" |
         | "7" |
     Then the following data should be generated:
       | foo | bar |
       | "0" | "6" |
       | "1" | "6" |
       | "0" | "7" |
       | "1" | "7" |

Scenario: Running an exhaustive combination strategy with special character (emoji) strings should be successful
     Given the following fields exist:
       | foo |
       | bar |
       And foo is of type "string"
       And foo is anything but null
       And bar is of type "string"
       And bar is anything but null
       And foo is in set:
         | "😐" |
         | "☻" |
       And bar is in set:
         | "🚍" |
         | "🚌" |
     Then the following data should be generated:
       | foo  | bar  |
       | "😐" | "🚍" |
       | "☻" | "🚍" |
       | "😐" | "🚌" |
       | "☻" | "🚌" |

Scenario: Running an exhaustive combination strategy with special character (white spaces) strings should be successful
     Given the following fields exist:
        | foo |
        | bar |
       And foo is of type "string"
       And foo is anything but null
       And bar is of type "string"
       And bar is anything but null
       And foo is in set:
         | " " |
         | " "  |
       And bar is in set:
        | " " |
        | " " |
     Then the following data should be generated:
       | foo  | bar  |
       | " " | " "  |
       | " "  | " "  |
       | " " | " "  |
       | " "  | " "  |

Scenario: Running an exhaustive combination strategy with valid integer values should be successful
     Given the following fields exist:
       | foo |
       | bar |
       And foo is of type "integer"
       And foo is anything but null
       And bar is of type "integer"
       And bar is anything but null
       And foo is in set:
         | 999 |
         | -12 |
       And bar is in set:
         | 12 |
         | 0  |
     Then the following data should be generated:
       | foo | bar|
       | 999 | 12 |
       | -12 | 12 |
       | 999 | 0  |
       | -12 | 0  |

Scenario: Running an exhaustive combination strategy with valid decimal values should be successful
  Given the following fields exist:
    | foo |
    | bar |
    And foo is of type "decimal"
    And foo is anything but null
    And bar is of type "decimal"
    And bar is anything but null
    And foo is in set:
      | 999 |
      | -12 |
    And bar is in set:
      | 12.01 |
      | 0     |
  Then the following data should be generated:
    | foo | bar   |
    | 999 | 12.01 |
    | -12 | 12.01 |
    | 999 | 0     |
    | -12 | 0     |

Scenario: Running an exhaustive combination strategy with invalid integer values should fail with an appropriate error message
     Given the following fields exist:
       | foo |
       | bar |
       And foo is of type "integer"
       And foo is anything but null
       And bar is of type "integer"
       And bar is anything but null
       And foo is in set:
         | 999 |
         | +12 |
       And bar is in set:
         | 12 |
         | 0  |
     Then I am presented with an error message
       And no data is created

Scenario: Running an exhaustive combination strategy with invalid decimal values should fail with an appropriate error message
  Given the following fields exist:
    | foo |
    | bar |
    And foo is of type "decimal"
    And foo is anything but null
    And bar is of type "decimal"
    And bar is anything but null
    And foo is in set:
      | 999 |
      | +12 |
    And bar is in set:
      | 12.01 |
      | 0     |
  Then I am presented with an error message
    And no data is created

Scenario: Running an exhaustive combination strategy with valid date values should be successful
     Given the following fields exist:
       | foo |
       | bar |
       And foo is of type "datetime"
       And foo is anything but null
       And bar is of type "datetime"
       And bar is anything but null
       And foo is in set:
         | 2018-12-04T14:00:00.000Z |
         | 2018-12-05T14:00:00.000Z |
       And bar is in set:
         | 2010-01-01T00:00:00.000Z |
         | 2010-12-31T23:59:00.000Z |
     Then the following data should be generated:
       | foo                      | bar                      |
       | 2018-12-04T14:00:00.000Z | 2010-01-01T00:00:00.000Z |
       | 2018-12-05T14:00:00.000Z | 2010-01-01T00:00:00.000Z |
       | 2018-12-04T14:00:00.000Z | 2010-12-31T23:59:00.000Z |
       | 2018-12-05T14:00:00.000Z | 2010-12-31T23:59:00.000Z |

Scenario: Running an exhaustive combination strategy with invalid date values should fail with an appropriate error message
     Given the following fields exist:
       | foo |
       | bar |
       And foo is of type "datetime"
       And foo is anything but null
       And bar is of type "datetime"
       And bar is anything but null
       And foo is in set:
         | 2018-12-99T14:00:00.000Z |
         | 2018-12-05T14:00:00.000Z |
       And bar is in set:
         | 2010-01-01T00:00:00.000Z |
         | 2010-12-31T23:59:00.000Z |
     Then I am presented with an error message
       And no data is created
  
Scenario: Running an exhaustive combination strategy with invalid date formats should fail with an appropriate error message
     Given the following fields exist:
       | foo |
       | bar |
       And foo is of type "datetime"
       And foo is anything but null
       And bar is of type "datetime"
       And bar is anything but null
       And foo is in set:
         | 2018-12-01T14:00:00.000Z |
         | 2018-12-05T14:00:00.000Z |
       And bar is in set:
         | 01-01-2010T00:00:00.000Z |
         | 2010-12-31T23:59:00.000Z |
     Then I am presented with an error message
       And no data is created

Scenario: Running an exhaustive combination strategy with null values (empty strings) should be successful
     Given the following fields exist:
       | foo |
       | bar |
       And foo is anything but null
       And bar is anything but null
       And foo is in set:
         | "" |
         | 1  |
       And bar is in set:
         | 2 |
         | 3 |
    Then the following data should be generated:
      | foo | bar |
      | ""  | 2   |
      | 1   | 2   |
      | ""  | 3   |
      | 1   | 3   |

Scenario: Running an exhaustive combination strategy with null values (null) should be successful
     Given the following fields exist:
       | foo |
       | bar |
       And foo is of type "integer"
       And foo is anything but null
       And bar is of type "integer"
       And bar is anything but null
       And foo is in set:
         | 0 |
         | 1 |
       And bar is in set:
         | 2    |
         | null |
     Then the profile is invalid because "Cannot create an IsInSetConstraint for field 'bar' with a set containing null."

Scenario: Running an exhaustive combination strategy should be successful
     Given the following fields exist:
       | foo |
       | bar |
       And foo is of type "integer"
       And foo is anything but null
       And bar is of type "integer"
       And bar is anything but null
       And foo is in set:
         | 0 |
         | 1 |
       And bar is in set:
         | 0 |
         | 1 |
     Then the following data should be generated:
       | foo | bar |
       | 0   | 0   |
       | 1   | 0   |
       | 0   | 1   |
       | 1   | 1   |

Scenario: Running an exhaustive combination strategy with a string and an integer field should be successful
     Given the following fields exist:
       | foo |
       | bar |
       And foo is of type "string"
       And foo is anything but null
       And bar is of type "integer"
       And bar is anything but null
       And foo is in set:
         | "x" |
         | "y" |
       And bar is in set:
         | 0 |
         | 1 |
     Then the following data should be generated:
       | foo | bar |
       | "x" | 0   |
       | "y" | 0   |
       | "x" | 1   |
       | "y" | 1   |

Scenario: Running an exhaustive combination strategy with a string and a decimal field should be successful
  Given the following fields exist:
    | foo |
    | bar |
    And foo is of type "string"
    And foo is anything but null
    And bar is of type "decimal"
    And bar is anything but null
    And foo is in set:
      | "x" |
      | "y" |
    And bar is in set:
      | 0 |
      | 1 |
  Then the following data should be generated:
    | foo | bar |
    | "x" | 0   |
    | "y" | 0   |
    | "x" | 1   |
    | "y" | 1   |

Scenario: Running an exhaustive combination strategy with a string and a datetime field should be successful
     Given the following fields exist:
       | foo |
       | bar |
       And foo is of type "string"
       And foo is anything but null
       And bar is of type "datetime"
       And bar is anything but null
       And foo is in set:
         | "x" |
         | "y" |
       And bar is in set:
         | 2010-01-01T00:00:00.000Z |
         | 2010-12-31T23:59:00.000Z |
     Then the following data should be generated:
       | foo | bar                      |
       | "x" | 2010-01-01T00:00:00.000Z |
       | "y" | 2010-01-01T00:00:00.000Z |
       | "x" | 2010-12-31T23:59:00.000Z |
       | "y" | 2010-12-31T23:59:00.000Z |

Scenario: Running an exhaustive combination strategy with an integer and a datetime field should be successful
     Given the following fields exist:
       | foo |
       | bar |
       And foo is of type "integer"
       And foo is anything but null
       And bar is of type "datetime"
       And bar is anything but null
       And foo is in set:
         | 500  |
         | 1    |
       And bar is in set:
         | 2010-01-01T00:00:00.000Z |
         | 2010-12-31T23:59:00.000Z |
     Then the following data should be generated:
       | foo  | bar                      |
       | 500  | 2010-01-01T00:00:00.000Z |
       | 1    | 2010-01-01T00:00:00.000Z |
       | 500  | 2010-12-31T23:59:00.000Z |
       | 1    | 2010-12-31T23:59:00.000Z |

Scenario: Running an exhaustive combination strategy with a decimal and a datetime field should be successful
  Given the following fields exist:
    | foo |
    | bar |
    And foo is of type "decimal"
    And foo is anything but null
    And bar is of type "datetime"
    And bar is anything but null
    And foo is in set:
      | 500 |
      | 1.1 |
    And bar is in set:
      | 2010-01-01T00:00:00.000Z |
      | 2010-12-31T23:59:00.000Z |
  Then the following data should be generated:
    | foo | bar                      |
    | 500 | 2010-01-01T00:00:00.000Z |
    | 1.1 | 2010-01-01T00:00:00.000Z |
    | 500 | 2010-12-31T23:59:00.000Z |
    | 1.1 | 2010-12-31T23:59:00.000Z |

  Scenario: Running an exhaustive combination strategy across five fields should be successful
    Given the following fields exist:
      | foo1 |
      | foo2 |
      | foo3 |
      | foo4 |
      | foo5 |
    And foo1 is of type "integer"
    And foo1 is anything but null
    And foo2 is of type "integer"
    And foo2 is anything but null
    And foo3 is of type "integer"
    And foo3 is anything but null
    And foo4 is of type "integer"
    And foo4 is anything but null
    And foo5 is of type "integer"
    And foo5 is anything but null
    And foo1 is in set:
      | 1 |
      | 2 |
    And foo2 is in set:
      | 3 |
      | 4 |
    And foo3 is in set:
      | 5 |
      | 6 |
    And foo4 is in set:
      | 7 |
      | 8 |
    And foo5 is in set:
      | 9 |
      | 0 |
    Then the following data should be generated:
      | foo1 | foo2 | foo3 | foo4 | foo5 |
      | 1    | 3    | 5    | 7    | 9    |
      | 1    | 3    | 5    | 7    | 0    |
      | 1    | 3    | 5    | 8    | 9    |
      | 1    | 3    | 5    | 8    | 0    |
      | 1    | 3    | 6    | 7    | 9    |
      | 1    | 3    | 6    | 7    | 0    |
      | 1    | 3    | 6    | 8    | 9    |
      | 1    | 3    | 6    | 8    | 0    |
      | 1    | 4    | 5    | 7    | 9    |
      | 1    | 4    | 5    | 7    | 0    |
      | 1    | 4    | 5    | 8    | 9    |
      | 1    | 4    | 5    | 8    | 0    |
      | 1    | 4    | 6    | 7    | 9    |
      | 1    | 4    | 6    | 7    | 0    |
      | 1    | 4    | 6    | 8    | 9    |
      | 1    | 4    | 6    | 8    | 0    |
      | 2    | 3    | 5    | 7    | 9    |
      | 2    | 3    | 5    | 7    | 0    |
      | 2    | 3    | 5    | 8    | 9    |
      | 2    | 3    | 5    | 8    | 0    |
      | 2    | 3    | 6    | 7    | 9    |
      | 2    | 3    | 6    | 7    | 0    |
      | 2    | 3    | 6    | 8    | 9    |
      | 2    | 3    | 6    | 8    | 0    |
      | 2    | 4    | 5    | 7    | 9    |
      | 2    | 4    | 5    | 7    | 0    |
      | 2    | 4    | 5    | 8    | 9    |
      | 2    | 4    | 5    | 8    | 0    |
      | 2    | 4    | 6    | 7    | 9    |
      | 2    | 4    | 6    | 7    | 0    |
      | 2    | 4    | 6    | 8    | 9    |
      | 2    | 4    | 6    | 8    | 0    |


Scenario: Running an exhaustive combination strategy across three fields with five data options each should be successful
     Given the following fields exist:
       | foo1 |
       | foo2 |
       | foo3 |
       And foo1 is of type "integer"
       And foo1 is anything but null
       And foo2 is of type "integer"
       And foo2 is anything but null
       And foo3 is of type "integer"
       And foo3 is anything but null
       And foo1 is in set:
         | 1 |
         | 2 |
         | 3 |
         | 4 |
         | 5 |
       And foo2 is in set:
         | 6 |
         | 7 |
         | 8 |
         | 9 |
         | 0 |
       And foo3 is in set:
         | 10 |
         | 11 |
         | 12 |
         | 13 |
         | 14 |
     Then the following data should be generated:
       | foo1 | foo2 | foo3 |
       | 1    | 6    | 10   |
       | 1    | 6    | 11   |
       | 1    | 6    | 12   |
       | 1    | 6    | 13   |
       | 1    | 6    | 14   |
       | 1    | 7    | 10   |
       | 1    | 7    | 11   |
       | 1    | 7    | 12   |
       | 1    | 7    | 13   |
       | 1    | 7    | 14   |
       | 1    | 8    | 10   |
       | 1    | 8    | 11   |
       | 1    | 8    | 12   |
       | 1    | 8    | 13   |
       | 1    | 8    | 14   |
       | 1    | 9    | 10   |
       | 1    | 9    | 11   |
       | 1    | 9    | 12   |
       | 1    | 9    | 13   |
       | 1    | 9    | 14   |
       | 1    | 0    | 10   |
       | 1    | 0    | 11   |
       | 1    | 0    | 12   |
       | 1    | 0    | 13   |
       | 1    | 0    | 14   |
       | 2    | 6    | 10   |
       | 2    | 6    | 11   |
       | 2    | 6    | 12   |
       | 2    | 6    | 13   |
       | 2    | 6    | 14   |
       | 2    | 7    | 10   |
       | 2    | 7    | 11   |
       | 2    | 7    | 12   |
       | 2    | 7    | 13   |
       | 2    | 7    | 14   |
       | 2    | 8    | 10   |
       | 2    | 8    | 11   |
       | 2    | 8    | 12   |
       | 2    | 8    | 13   |
       | 2    | 8    | 14   |
       | 2    | 9    | 10   |
       | 2    | 9    | 11   |
       | 2    | 9    | 12   |
       | 2    | 9    | 13   |
       | 2    | 9    | 14   |
       | 2    | 0    | 10   |
       | 2    | 0    | 11   |
       | 2    | 0    | 12   |
       | 2    | 0    | 13   |
       | 2    | 0    | 14   |
       | 3    | 6    | 10   |
       | 3    | 6    | 11   |
       | 3    | 6    | 12   |
       | 3    | 6    | 13   |
       | 3    | 6    | 14   |
       | 3    | 7    | 10   |
       | 3    | 7    | 11   |
       | 3    | 7    | 12   |
       | 3    | 7    | 13   |
       | 3    | 7    | 14   |
       | 3    | 8    | 10   |
       | 3    | 8    | 11   |
       | 3    | 8    | 12   |
       | 3    | 8    | 13   |
       | 3    | 8    | 14   |
       | 3    | 9    | 10   |
       | 3    | 9    | 11   |
       | 3    | 9    | 12   |
       | 3    | 9    | 13   |
       | 3    | 9    | 14   |
       | 3    | 0    | 10   |
       | 3    | 0    | 11   |
       | 3    | 0    | 12   |
       | 3    | 0    | 13   |
       | 3    | 0    | 14   |
       | 4    | 6    | 10   |
       | 4    | 6    | 11   |
       | 4    | 6    | 12   |
       | 4    | 6    | 13   |
       | 4    | 6    | 14   |
       | 4    | 7    | 10   |
       | 4    | 7    | 11   |
       | 4    | 7    | 12   |
       | 4    | 7    | 13   |
       | 4    | 7    | 14   |
       | 4    | 8    | 10   |
       | 4    | 8    | 11   |
       | 4    | 8    | 12   |
       | 4    | 8    | 13   |
       | 4    | 8    | 14   |
       | 4    | 9    | 10   |
       | 4    | 9    | 11   |
       | 4    | 9    | 12   |
       | 4    | 9    | 13   |
       | 4    | 9    | 14   |
       | 4    | 0    | 10   |
       | 4    | 0    | 11   |
       | 4    | 0    | 12   |
       | 4    | 0    | 13   |
       | 4    | 0    | 14   |
       | 5    | 6    | 10   |
       | 5    | 6    | 11   |
       | 5    | 6    | 12   |
       | 5    | 6    | 13   |
       | 5    | 6    | 14   |
       | 5    | 7    | 10   |
       | 5    | 7    | 11   |
       | 5    | 7    | 12   |
       | 5    | 7    | 13   |
       | 5    | 7    | 14   |
       | 5    | 8    | 10   |
       | 5    | 8    | 11   |
       | 5    | 8    | 12   |
       | 5    | 8    | 13   |
       | 5    | 8    | 14   |
       | 5    | 9    | 10   |
       | 5    | 9    | 11   |
       | 5    | 9    | 12   |
       | 5    | 9    | 13   |
       | 5    | 9    | 14   |
       | 5    | 0    | 10   |
       | 5    | 0    | 11   |
       | 5    | 0    | 12   |
       | 5    | 0    | 13   |
       | 5    | 0    | 14   |


Scenario: Running an exhaustive combination strategy across fields with an uneven distribution of data options should be successful
     Given the following fields exist:
       | foo1 |
       | foo2 |
       | foo3 |
       And foo1 is of type "integer"
       And foo1 is anything but null
       And foo2 is of type "integer"
       And foo2 is anything but null
       And foo3 is of type "integer"
       And foo3 is anything but null
       And foo1 is in set:
         | 1 |
       And foo2 is in set:
         | 20 |
         | 21 |
         | 22 |
       And foo3 is in set:
         | 10 |
         | 11 |
         | 12 |
         | 13 |
         | 14 |
     Then the following data should be generated:
       | foo1 | foo2 | foo3 |
       | 1    | 20   | 10   |
       | 1    | 20   | 11   |
       | 1    | 20   | 12   |
       | 1    | 20   | 13   |
       | 1    | 20   | 14   |
       | 1    | 21   | 10   |
       | 1    | 21   | 11   |
       | 1    | 21   | 12   |
       | 1    | 21   | 13   |
       | 1    | 21   | 14   |
       | 1    | 22   | 10   |
       | 1    | 22   | 11   |
       | 1    | 22   | 12   |
       | 1    | 22   | 13   |
       | 1    | 22   | 14   |

Scenario: Running an exhaustive combination strategy across fields with a duplicate integer data option in a field should be successful
     Given the following fields exist:
       | foo1 |
       | foo2 |
       | foo3 |
       And foo1 is of type "integer"
       And foo1 is anything but null
       And foo2 is of type "integer"
       And foo2 is anything but null
       And foo3 is of type "integer"
       And foo3 is anything but null
       And foo1 is in set:
         | 1 |
       And foo2 is in set:
         | 20 |
         | 20 |
         | 20 |
       And foo3 is in set:
         | 10 |
         | 11 |
         | 12 |
         | 13 |
         | 14 |
     Then the following data should be generated:
       | foo1 | foo2 | foo3 |
       | 1    | 20   | 10   |
       | 1    | 20   | 11   |
       | 1    | 20   | 12   |
       | 1    | 20   | 13   |
       | 1    | 20   | 14   |

Scenario: Running an exhaustive combination strategy across fields with a duplicate data option in different formats (integer & string) in a field should be successful
     Given the following fields exist:
       | foo1 |
       | foo2 |
       | foo3 |
       And foo1 is anything but of type "datetime"
       And foo1 is anything but null
       And foo2 is anything but of type "datetime"
       And foo2 is anything but null
       And foo3 is anything but of type "datetime"
       And foo3 is anything but null
       And foo1 is in set:
         | 1 |
       And foo2 is in set:
         | 20   |
         | "20" |
         | 20   |
         | "20" |
       And foo3 is in set:
         | 10   |
         | 11   |
         | 12   |
         | 13   |
         | 14   |
         | "14" |
     Then the following data should be generated:
       | foo1 | foo2 | foo3 |
       | 1    | 20   | 10   |
       | 1    | 20   | 11   |
       | 1    | 20   | 12   |
       | 1    | 20   | 13   |
       | 1    | 20   | 14   |
       | 1    | 20   | "14" |
       | 1    | "20" | 10   |
       | 1    | "20" | 11   |
       | 1    | "20" | 12   |
       | 1    | "20" | 13   |
       | 1    | "20" | 14   |
       | 1    | "20" | "14" |

Scenario: Running an exhaustive combination strategy across fields with non ordered data options should be successful
     Given the following fields exist:
       | foo1 |
       | foo2 |
       | foo3 |
       And foo1 is of type "integer"
       And foo1 is anything but null
       And foo2 is of type "string"
       And foo2 is anything but null
       And foo3 is anything but of type "datetime"
       And foo3 is anything but null
       And foo1 is in set:
         | 1 |
         | 0 |
       And foo2 is in set:
         | "zab"  |
         | "zaa"  |
         | "test" |
       And foo3 is in set:
         | "!"     |
         | "testZ" |
         | "0"     |
         | "aaa"   |
         | 14      |
     Then the following data should be generated:
       | foo1 | foo2   | foo3    |
       | 1    | "zab"  | "!"     |
       | 1    | "zab"  | "testZ" |
       | 1    | "zab"  | "0"     |
       | 1    | "zab"  | "aaa"   |
       | 1    | "zab"  | 14      |
       | 1    | "zaa"  | "!"     |
       | 1    | "zaa"  | "testZ" |
       | 1    | "zaa"  | "0"     |
       | 1    | "zaa"  | "aaa"   |
       | 1    | "zaa"  | 14      |
       | 1    | "test" | "!"     |
       | 1    | "test" | "testZ" |
       | 1    | "test" | "0"     |
       | 1    | "test" | "aaa"   |
       | 1    | "test" | 14      |
       | 0    | "zab"  | "!"     |
       | 0    | "zab"  | "testZ" |
       | 0    | "zab"  | "0"     |
       | 0    | "zab"  | "aaa"   |
       | 0    | "zab"  | 14      |
       | 0    | "zaa"  | "!"     |
       | 0    | "zaa"  | "testZ" |
       | 0    | "zaa"  | "0"     |
       | 0    | "zaa"  | "aaa"   |
       | 0    | "zaa"  | 14      |
       | 0    | "test" | "!"     |
       | 0    | "test" | "testZ" |
       | 0    | "test" | "0"     |
       | 0    | "test" | "aaa"   |
       | 0    | "test" | 14      |

Scenario: Running an exhaustive combination strategy that includes an "if" statement should be successful
      Given the generation strategy is full
      And the combination strategy is exhaustive
      And the following fields exist:
       | foo1 |
       | foo2 |
       | foo3 |
       And foo1 is of type "string"
       And foo1 is anything but null
       And foo2 is of type "integer"
       And foo2 is anything but null
       And foo3 is of type "string"
       And foo3 is anything but null
       And foo1 is in set:
         | "alpha" |
       And foo2 is in set:
         | 1    |
         | 10   |
         | 100  |
         | 1000 |
       And foo3 is in set:
         | "test1"     |
         | "test10"    |
         | "test100"   |
         | "other"     |
         | "Not in If" |
       And there is a constraint:
       """
         {
         "if": { "field": "foo2", "is": "equalTo", "value": 1 },
         "then": { "field": "foo3", "is": "equalTo", "value": "test1" },
         "else": {
         "if": { "field": "foo2", "is": "equalTo", "value": 10 },
         "then": { "field": "foo3", "is": "equalTo", "value": "test10" },
         "else": {
         "if": { "field": "foo2", "is": "equalTo", "value": 100 },
         "then": { "field": "foo3", "is": "equalTo", "value": "test100" },
         "else": { "field": "foo3", "is": "equalTo", "value": "other" }
         }
         }
         }
       """
     Then the following data should be generated:
       | foo1    | foo2 | foo3      |
       | "alpha" | 1    | "test1"   |
       | "alpha" | 10   | "test10"  |
       | "alpha" | 100  | "test100" |
       | "alpha" | 1000 | "other"   |

