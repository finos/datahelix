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
Feature: User can specify that a field must be a type of name

  Background:
    Given the generation strategy is random
    And there is a non nullable field foo
    And foo has type "string"

  Scenario: Generating with an of type firstname constraint generates valid firstnames
    Given foo has type "firstname"
    And the generator can generate at most 10 rows
    Then 10 rows of data are generated
    And foo contains strings longer than or equal to 1

  Scenario: Generating with an of type lastname constraint generates valid lastnames
    Given foo has type "lastname"
    And the generator can generate at most 10 rows
    Then 10 rows of data are generated
    And foo contains strings longer than or equal to 1

  Scenario: Generating with an of type fullname constraint generates valid fullnames
    Given foo has type "fullname"
    And the generator can generate at most 5 rows
    Then 5 rows of data are generated
    And foo contains strings matching /.+\s.+/
