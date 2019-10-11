Feature: Type mandation validation
  Profiles should be rejected if they don't positively specify (to a certain standard) the types of all their fields.

  Scenario: An explicit type constraint should satisfy type mandation
    Given there is a field user_id
    And user_id has type "string"
    Then the profile should be considered valid

  Scenario: An equalTo constraint should satisfy type mandation
    Given there is a field user_id
    And user_id is equal to "banana"
    And user_id has type "string"
    Then the profile should be considered valid

  Scenario: An inSet constraint should satisfy type mandation
    Given there is a field user_id
    And user_id has type "string"
    And user_id is in set:
      | "banana" |
      | "cactus" |
    Then the profile should be considered valid

  Scenario: An ISIN constraint should satisfy type mandation
    Given there is a field foo
    And foo has type "ISIN"
    Then the profile should be considered valid

  Scenario: A SEDOL constraint should satisfy type mandation
    Given there is a field foo
    And foo has type "SEDOL"
    Then the profile should be considered valid

  Scenario: A CUSIP constraint should satisfy type mandation
    Given there is a field foo
    And foo has type "CUSIP"
    Then the profile should be considered valid
