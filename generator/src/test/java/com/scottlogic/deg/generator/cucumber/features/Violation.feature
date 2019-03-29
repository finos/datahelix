Feature: The violations mode of the Data Helix app can be run in violations mode to create data

  Background:
    Given there is a field foo
      And the data requested is violating
      And the generator can generate at most 5 rows

@ignore #584, conditional violation doesn't work with 'ofType integer'
Scenario: Running the generator in violate mode for not equal to is successful (integer)
  Given foo is anything but equal to 8
    And foo is of type "integer"
    And we do not violate any of type constraints
    And the generation strategy is full
  Then the following data should be generated:
    | foo  |
    | 8    |
    | null |

Scenario: Running the generator in violate mode for not equal to is successful (decimal)
    Given foo is anything but equal to 8
    And foo is of type "decimal"
    And foo is granular to 1
    And we do not violate any of type constraints
    And we do not violate any granular to constraints
    And the generation strategy is full
    Then the following data should be generated:
      | foo  |
      | 8    |
      | null |

Scenario: Running the generator in violate mode where equal to is not violated is successful
  Given foo is equal to 8
    And the generation strategy is full
    And we do not violate any equal to constraints
  Then the following data should be generated:
    | foo  |
    | 8    |
    | null |

Scenario: Running the generator in violate mode for multiple constraints with strings is successful
  Given the generation strategy is interesting
    And foo is of type "string"
    And foo is anything but equal to "hello"
    And the generator can generate at most 10 rows
  Then the following data should be included in what is generated:
    | foo                                          |
    | "hello"                                      |
    | 0                                            |
    | -100000000000000000000.00000000000000000000  |
    | -99999999999999999999.99999999999999999999   |
    | 99999999999999999999.99999999999999999999    |
    | 100000000000000000000.00000000000000000000   |
    | 1900-01-01T00:00:00.000Z                     |
    | 2100-01-01T00:00:00.000Z                     |
    | null                                         |

### Random

Scenario: The generator produces violating (incorrect type) data in random mode for type 'String'
  Given foo is of type "string"
    And the generation strategy is random
    And the data requested is violating
  Then 5 rows of data are generated
    And foo contains anything but string data

Scenario: The generator produces violating (incorrect type) data in random mode for type 'DateTime'
  Given foo is of type "datetime"
    And the generation strategy is random
    And the data requested is violating
  Then 5 rows of data are generated
    And foo contains anything but datetime data

Scenario: The generator produces violating (incorrect type) data in random mode for type 'Decimal'
  Given foo is of type "decimal"
    And the generation strategy is random
    And the data requested is violating
  Then 5 rows of data are generated
    And foo contains anything but numeric data

Scenario: The generator produces violating 'Null' data in random mode
  Given foo is null
    And the generation strategy is random
    And the data requested is violating
  Then 5 rows of data are generated
    And foo contains anything but null

Scenario: The generator produces violating (not type) 'DateTime' data in random mode
  Given foo is of type "datetime"
    And the generation strategy is random
    And foo is anything but null
    And foo is before 2019-01-01T00:00:00.000Z
    And the data requested is violating
    And we do not violate any of type constraints
  Then 5 rows of data are generated
    And foo contains datetime data
    And foo contains datetimes after or at 2019-01-01T00:00:00.000Z

Scenario: The generator produces violating (not type) 'Decimal' data in random mode
  Given foo is of type "decimal"
    And foo is anything but null
    And the generation strategy is random
    And foo is less than 10
    And the data requested is violating
    And we do not violate any of type constraints
  Then 5 rows of data are generated
    And foo contains numeric data
    And foo contains numeric values greater than or equal to 10

Scenario: The generator produces violating (not type) 'String' data in random mode
  Given foo is of type "string"
    And the generation strategy is random
    And foo is anything but null
    And foo is shorter than 10
    And the data requested is violating
    And we do not violate any of type constraints
  Then 5 rows of data are generated
    And foo contains string data
    And foo contains strings longer than or equal to 10

Scenario: The generator produces violating (not type) RegEx restricted 'String' data in random mode
  Given foo is of type "string"
    And the generation strategy is random
    And foo is anything but null
    And foo is matching regex /[a-z]{0,9}/
    And the data requested is violating
    And we do not violate any of type constraints
  Then 5 rows of data are generated
    And foo contains string data
    And foo contains anything but strings matching /[a-z]{0,9}/

Scenario: The generator produces violating (not type) inverted RegEx restricted 'String' data in random mode
  Given foo is of type "string"
    And foo is anything but null
    And the generation strategy is random
    And foo is anything but matching regex /[a-z]{0,9}/
    And the data requested is violating
    And we do not violate any of type constraints
  Then 5 rows of data are generated
    And foo contains string data
    And foo contains strings matching /[a-z]{0,9}/
