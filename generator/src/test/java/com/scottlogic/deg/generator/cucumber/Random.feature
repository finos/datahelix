
Feature: User can generate valid data for all types (string, numeric or temporal) in random generation mode. Actual randomness of the data not tested.

  Background:
    Given the generation strategy is random
      And there is a field foo

Scenario: The generator produces valid 'Temporal' data in random mode
  Given foo is of type "temporal"
    And foo is anything but null
    And the generator can generate at most 5 rows
    And foo is before or at 2019-01-01T00:00:00.000
  Then 5 rows of data are generated
    And foo contains temporal data
    And foo contains anything but null
    And foo contains temporal values between 0001-01-01T00:00:00.000 and 2019-01-01T00:00:00.000 inclusively

Scenario: The generator produces valid 'Numeric' data in random mode
  Given foo is of type "numeric"
    And foo is anything but null
    And the generator can generate at most 5 rows
    And foo is less than or equal to 10
  Then 5 rows of data are generated
    And foo contains numeric data
    And foo contains numeric values between -179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000 and 10 inclusively
    And foo contains anything but null

Scenario: The generator produces valid 'String' data in random mode
  Given foo is of type "string"
    And foo is anything but null
    And the generator can generate at most 5 rows
    And foo is shorter than 10
  Then 5 rows of data are generated
    And foo contains string data
    And foo contains strings of length between 0 and 9 inclusively
    And foo contains anything but null

Scenario: The generator produces valid RegEx restricted 'String' data in random mode
  Given foo is of type "string"
    And foo is anything but null
    And the generator can generate at most 5 rows
    And foo is matching regex /[a-z]{0,9}/
  Then 5 rows of data are generated
    And foo contains string data
    And foo contains strings matching /[a-z]{0,9}/
    And foo contains anything but null

Scenario: The generator produces valid inverted RegEx restricted 'String' data in random mode
  Given foo is of type "string"
    And foo is anything but null
    And the generator can generate at most 5 rows
    And foo is anything but matching regex /[a-z]{0,9}/
  Then 5 rows of data are generated
    And foo contains string data
    And foo contains anything but strings matching /[a-z]{0,9}/
    And foo contains anything but null

Scenario: The generator produces valid 'Null' data in random mode
  Given foo is null
    And the generator can generate at most 5 rows
  Then the following data should be generated:
    | foo  |
    | null |
    | null |
    | null |
    | null |
    | null |
