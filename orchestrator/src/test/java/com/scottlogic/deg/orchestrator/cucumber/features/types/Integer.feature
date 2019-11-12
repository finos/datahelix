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
Feature: User can specify that a number has type integer and does not have any decimal places

  Background:
    Given the generation strategy is full
    And there is a non nullable field foo
    And foo has type "integer"
### Numeric Constraints
  Scenario: Greater than constraint with integer type produces valid integers
    Given foo is greater than 10
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo |
      | 11  |
      | 12  |
      | 13  |
      | 14  |
      | 15  |

  Scenario: Greater than or equal to constraint with integer type produces valid integers
    Given foo is greater than or equal to 10
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo |
      | 10  |
      | 11  |
      | 12  |
      | 13  |
      | 14  |
#594 - Change this to descending order?
  Scenario: Less than or equal to constraint with integer type produces valid integers
    Given foo is less than or equal to 10
    And foo has type "integer"
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                    |
      | -100000000000000000000 |
      | -99999999999999999999  |
      | -99999999999999999998  |
      | -99999999999999999997  |
      | -99999999999999999996  |
#594 - Change this to descending order?
  Scenario: Less than constraint with integer type produces valid integers
    Given foo is less than 10
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                    |
      | -100000000000000000000 |
      | -99999999999999999999  |
      | -99999999999999999998  |
      | -99999999999999999997  |
      | -99999999999999999996  |

  Scenario: Equal to constraint with integer type produces valid integer
    Given foo is equal to 10
    Then the following data should be generated:
      | foo |
      | 10  |

  Scenario: Equal to constraint with integer type rejects invalid integer (decimal)
    Given foo is equal to 10.1
    Then no data is created

  Scenario: In Set constraint with integer type only produces valid integers
    Given foo is in set:
      | 1   |
      | 1.1 |
    Then the following data should be generated:
      | foo |
      | 1   |

  Scenario: Contradictory granular to constraint with integer type only produces valid integers
    Given foo is in set:
      | 1   |
      | 1.1 |
    And foo is granular to 0.1
    Then the following data should be generated:
      | foo |
      | 1   |

  Scenario: Granular to constraint does not affect integer
    Given foo is greater than 10
    And the generator can generate at most 5 rows
    And foo is granular to 0.01
    Then the following data should be generated:
      | foo |
      | 11  |
      | 12  |
      | 13  |
      | 14  |
      | 15  |
