Feature: The violations mode of the Data Helix app can be run in violations mode to create data

  Background:
    Given there is a field foo
      And the data requested is violating
      And the walker type is REDUCTIVE
      And the generator can generate at most 5 rows

Scenario: Running the generator in violate mode for not equal to is successful
  Given foo is anything but equal to 8
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
    | foo                     |
    | "hello"                 |
    | 0                       |
    | -2147483648             |
    | 2147483646              |
    | 1900-01-01T00:00:00.000 |
    | 2100-01-01T00:00:00.000 |
    | null                    |

### Random

Scenario: The generator produces violating (incorrect type) data in random mode for type 'String'
  Given foo is of type "string"
    And the generation strategy is random
    And the data requested is violating
  Then 5 rows of data are generated
    And foo contains anything but string data

Scenario: The generator produces violating (incorrect type) data in random mode for type 'Temporal'
  Given foo is of type "temporal"
    And the generation strategy is random
    And the data requested is violating
  Then 5 rows of data are generated
    And foo contains anything but temporal data

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

Scenario: The generator produces violating (not type) 'Temporal' data in random mode
  Given foo is of type "temporal"
    And the generation strategy is random
    And foo is anything but null
    And foo is before 2019-01-01T00:00:00.000
    And the data requested is violating
    And we do not violate any of type constraints
  Then 5 rows of data are generated
    And foo contains temporal data
    And foo contains temporal values after or at 2019-01-01T00:00:00.000

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
