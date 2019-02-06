Feature: User can set the generation strategy to violate mode and see test cases generated

Background:
  Given the generation strategy is interesting
  And the combination strategy is exhaustive
  And there is a field foo
  And the data requested is violating
  And the generator can generate at most 20 rows

  Scenario: Running the generator in violate mode for negated equal to is successful
   Given foo is anything but equal to 8
   Then the following data should be generated:
     | foo  |
     | 8    |
     | null |

  Scenario: Running the generator in violate mode for negated string type is successful
    Given foo is anything but of type "string"
    Then the following data should be generated:
      | foo            |
      | "Lorem Ipsum"  |
      | null           |

  Scenario: Running the generator in violate mode for negated numeric type is successful
    Given foo is anything but of type "numeric"
    Then the following data should be generated:
      | foo         |
      | -2147483648 |
      | 0           |
      | 2147483646  |
      | null        |

@ignore #Bug raised #521
  Scenario: Running the generator in violate mode for negated all of constraint is successful
    Given there is a constraint:
       """
         {
           "not" : {
             "allOf": [
               {"field": "foo", "is": "equalTo", "value": "Test01"}
             ]
           }
          }
       """
    Then the following data should be generated:
      | foo      |
      | "Test01" |
      | null     |

  Scenario: Running the generator in violate mode for less than is successful
    Given foo is less than 10
    Then the following data should be included in what is generated:
      | foo        |
      | 10         |
      | 11         |
      | null       |

  Scenario: Running the generator in violate but also saying not to violate the constraint is successful
    Given foo is less than 10
    When we do not violate any less than constraints
    Then the following data should be included in what is generated:
      | foo        |
      | 0         |
      | 9         |
      | null       |

  Scenario: Running the generator in violate mode for less than or equal to is successful
    Given foo is less than or equal to 10
    And foo is of type "numeric"
    Then the following data should be included in what is generated:
      | foo        |
      | 11         |
      | 12         |
      | null       |

  Scenario: Running the generator in violate mode for greater than is successful
    Given foo is greater than 10
    Then the following data should be included in what is generated:
      | foo         |
      | 10          |
      | 9           |
      | null        |

  Scenario: Running the generator in violate mode for greater than or equal to is successful
    Given foo is greater than or equal to 10
    Then the following data should be included in what is generated:
      | foo         |
      | 9           |
      | 8           |
      | null        |

  Scenario: Running the generator in violate mode for multiple constraints with strings is successful
    Given foo is of type "string"
    And foo is anything but equal to "hello"
    Then the following data should be included in what is generated:
      | foo                     |
      | "hello"                 |
      | 0                       |
      | -2147483648             |
      | 2147483646              |
      | 1900-01-01T00:00:00.000 |
      | 2100-01-01T00:00:00.000 |
      | null                    |

  Scenario: Running the generator in violate mode for multiple constraints with numbers is successful
    Given foo is of type "numeric"
    And foo is anything but equal to 8
    Then the following data should be included in what is generated:
      | foo                     |
      | "Lorem Ipsum"           |
      | 1900-01-01T00:00:00.000 |
      | 2100-01-01T00:00:00.000 |
      | 8                       |
      | null                    |