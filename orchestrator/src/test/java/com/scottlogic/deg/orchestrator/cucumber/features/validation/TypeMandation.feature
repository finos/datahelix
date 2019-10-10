Feature: Type mandation validation
  Profiles should be rejected if they don't positively specify (to a certain standard) the types of all their fields.

  Scenario: A field with no relevant constraints should fail type mandation
    Given there is a field user_id
    And user_id is greater than 3
    And user_id is less than 10
    And user_id is granular to 1
    # ideally I guess we'd have more here - what's a sensible amount? maybe we should use scenario outlines?
    Then the profile is invalid because "Field \[user_id\]: is not typed; add its type to the field definition"

  Scenario: An explicit type constraint should satisfy type mandation
    Given there is a field user_id
    And user_id has type "string"
    Then the profile should be considered valid

  Scenario: An equalTo constraint should satisfy type mandation
    Given there is a field user_id
    And user_id is equal to "banana"
    Then the profile is invalid because "Field \[user_id\]: is not typed; add its type to the field definition"

  Scenario: An inSet constraint should satisfy type mandation
    Given there is a field user_id
    And user_id is in set:
      | "banana" |
      | "cactus" |
    Then the profile is invalid because "Field \[user_id\]: is not typed; add its type to the field definition"

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

  Scenario: A mandatorily absent field should satisfy type mandation
    Given there is a field user_id
    And user_id is null
    Then the profile is invalid because "Field \[user_id\]: is not typed; add its type to the field definition"

  Scenario: When only some fields fail type mandation, the errors should be specific to which
    Given there is a field user_id
    And user_id has type "string"
    And there is a field price
    And there is a field purchase_time
    Then the profile is invalid because "Fields price, purchase_time are not typed; add their type to the field definition"

