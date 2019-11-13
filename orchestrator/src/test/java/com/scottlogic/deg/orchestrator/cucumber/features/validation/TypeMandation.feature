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
Feature: Type mandation validation
  Profiles should be rejected if they don't positively specify (to a certain standard) the types of all their fields.

  Scenario: An explicit type constraint should satisfy type mandation
    Given there is a non nullable field user_id
    And user_id has type "string"
    Then the profile should be considered valid

  Scenario: An equalTo constraint should satisfy type mandation
    Given there is a non nullable field user_id
    And user_id is equal to "banana"
    And user_id has type "string"
    Then the profile should be considered valid

  Scenario: An inSet constraint should satisfy type mandation
    Given there is a non nullable field user_id
    And user_id has type "string"
    And user_id is in set:
      | "banana" |
      | "cactus" |
    Then the profile should be considered valid

  Scenario: An ISIN constraint should satisfy type mandation
    Given there is a non nullable field foo
    And foo has type "ISIN"
    Then the profile should be considered valid

  Scenario: A SEDOL constraint should satisfy type mandation
    Given there is a non nullable field foo
    And foo has type "SEDOL"
    Then the profile should be considered valid

  Scenario: A CUSIP constraint should satisfy type mandation
    Given there is a non nullable field foo
    And foo has type "CUSIP"
    Then the profile should be considered valid
