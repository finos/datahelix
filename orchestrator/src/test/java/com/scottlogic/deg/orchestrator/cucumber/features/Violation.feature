# Copyright 2019 Scott Logic Ltd
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
Feature: The violations mode of the Data Helix app can be run in violations mode to create data

  Background:
    Given there is a nullable field foo
    And the data requested is violating
    And the generator can generate at most 5 rows

  Scenario: Running the generator in violate mode for not equal to is successful (decimal)
    Given foo is anything but equal to 8
    And foo has type "decimal"
    And foo is anything but null
    And foo is granular to 1
    And we do not violate any granular to constraints
    And the generation strategy is full
    Then the following data should be generated:
      | foo  |
      | 8    |
      | null |

  Scenario: Running the generator in violate mode where equal to is not violated is successful
    Given foo is equal to 8
    And foo has type "decimal"
    And the generation strategy is full
    And we do not violate any equal to constraints
    Then the following data should be generated:
      | foo  |
      | 8    |
      | null |

  Scenario: Running the generator in violate mode for multiple constraints with strings is successful
    Given the generation strategy is interesting
    And foo has type "string"
    And foo is anything but null
    And foo is anything but equal to "hello"
    And the generator can generate at most 10 rows
    Then the following data should be included in what is generated:
      | foo     |
      | "hello" |
### Random
  Scenario: The generator produces violating 'Null' data in random mode
    Given foo is null
    And foo has type "decimal"
    And the generation strategy is random
    And the data requested is violating
    Then 5 rows of data are generated
    And foo contains anything but null

  Scenario: The generator produces violating (not type) 'DateTime' data in random mode
    Given foo has type "datetime"
    And the generation strategy is random
    And foo is before 2019-01-01T00:00:00.000Z
    And the data requested is violating
    Then 5 rows of data are generated
    And foo contains only datetime data
    And foo contains datetimes after or at 2019-01-01T00:00:00.000Z

  Scenario: The generator produces violating (not type) 'Decimal' data in random mode
    Given foo has type "decimal"
    And the generation strategy is random
    And foo is less than 10
    And the data requested is violating
    Then 5 rows of data are generated
    And foo contains only numeric data
    And foo contains numeric values greater than or equal to 10

  Scenario: The generator produces violating (not type) 'String' data in random mode
    Given foo has type "string"
    And the generation strategy is random
    And foo is shorter than 10
    And the data requested is violating
    Then 5 rows of data are generated
    And foo contains only string data
    And foo contains strings longer than or equal to 10

  Scenario: The generator produces violating (not type) RegEx restricted 'String' data in random mode
    Given foo has type "string"
    And the generation strategy is random
    And foo is matching regex /[a-z]{0,9}/
    And the data requested is violating
    Then 5 rows of data are generated
    And foo contains only string data
    And foo contains anything but strings matching /[a-z]{0,9}/

  Scenario: The generator produces violating (not type) inverted RegEx restricted 'String' data in random mode
    Given foo has type "string"
    And the generation strategy is random
    And foo is anything but matching regex /[a-z]{0,9}/
    And the data requested is violating
    Then 5 rows of data are generated
    And foo contains only string data
    And foo contains strings matching /[a-z]{0,9}/
