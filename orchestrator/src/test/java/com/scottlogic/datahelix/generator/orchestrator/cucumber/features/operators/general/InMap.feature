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
Feature: User can specify that a field value belongs to a set of predetermined options.

  Background:
    Given the generation strategy is full
### inMap alone ###
  Scenario: Running an 'inMap'
    Given the following non nullable fields exist:
      | HomeNation |
      | Capital    |
    And the file "testFile" contains the following data:
      | Country          | Capital   |
      | England          | London    |
      | Northern Ireland | Belfast   |
      | Scotland         | Edinburgh |
      | Wales            | Cardiff   |
    And HomeNation has type "string"
    And Capital has type "string"
    And HomeNation is from Country in testFile
    And Capital is from Capital in testFile
    Then the following data should be generated:
      | HomeNation         | Capital     |
      | "England"          | "London"    |
      | "Northern Ireland" | "Belfast"   |
      | "Scotland"         | "Edinburgh" |
      | "Wales"            | "Cardiff"   |

  Scenario: Running an 'inMap' with text a restriction
    Given the following non nullable fields exist:
      | HomeNation |
      | Capital    |
    And the file "testFile" contains the following data:
      | Country          | Capital   |
      | England          | London    |
      | Northern Ireland | Belfast   |
      | Scotland         | Edinburgh |
      | Wales            | Cardiff   |
    And HomeNation has type "string"
    And Capital has type "string"
    And HomeNation is from Country in testFile
    And Capital is from Capital in testFile
    When Capital is matching regex /^[BE].*/
    When HomeNation is matching regex /^[S].*/
    Then the following data should be generated:
      | HomeNation | Capital     |
      | "Scotland" | "Edinburgh" |

  Scenario: Running an 'inMap' multiple maps
    Given the following non nullable fields exist:
      | HomeNation |
      | Capital    |
      | Foo        |
      | Bar        |
    And the combination strategy is exhaustive
    And HomeNation is anything but null
    And Capital is anything but null
    And the file "testFile" contains the following data:
      | Country          | Capital   |
      | England          | London    |
      | Northern Ireland | Belfast   |
      | Scotland         | Edinburgh |
      | Wales            | Cardiff   |
    And the file "testFile2" contains the following data:
      | Foo | Bar |
      | 1   | one |
      | 2   | two |
    And HomeNation has type "string"
    And Capital has type "string"
    And Foo has type "decimal"
    And Bar has type "string"
    And HomeNation is from Country in testFile
    And Capital is from Capital in testFile
    And Foo is from Foo in testFile2
    And Bar is from Bar in testFile2
    Then the following data should be generated:
      | HomeNation            | Capital     | Foo | Bar   |
      | "England"          | "London"    | 1   | "one" |
      | "England"          | "London"    | 2   | "two" |
      | "Northern Ireland" | "Belfast"   | 1   | "one" |
      | "Northern Ireland" | "Belfast"   | 2   | "two" |
      | "Scotland"         | "Edinburgh" | 1   | "one" |
      | "Scotland"         | "Edinburgh" | 2   | "two" |
      | "Wales"            | "Cardiff"   | 1   | "one" |
      | "Wales"            | "Cardiff"   | 2   | "two" |


  Scenario: Running an 'inMap' with text a restriction
    Given the following non nullable fields exist:
      | HomeNation |
      | Population    |
    And the file "testFile" contains the following data:
      | Country          | Population |
      | England          |   56286961 |
      | Scotland         |    5463300 |
      | Wales            |   ~3150000 |
      | Northern Ireland |        TBC |
    And HomeNation has type "string"
    And Population has type "integer"
    And HomeNation is from Country in testFile
    And Population is from Population in testFile
    But the profile is invalid with errors:
      | Value '~3150000' must be a number \| Constraint: 'inMap' |
      | Value 'TBC' must be a number \| Constraint: 'inMap' |
