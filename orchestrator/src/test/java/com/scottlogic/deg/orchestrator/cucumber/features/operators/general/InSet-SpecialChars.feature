# (1075) ignoring these tests as only allowing latin character set for now but we will turn them back on when allow them
@ignore
Feature: Whilst including non-latin characters, User can specify that a field value belongs to a set of predetermined options.

  Background:
    Given the generation strategy is full

### inSet alone ###

  Scenario: Running an 'inSet' request that includes strings with special characters (standard) should be successful
    Given there is a field foo
    And foo is in set:
      | "!£$%^&*()"   |
      | "{}:@~;'#<>?" |
    Then the following data should be generated:
      | foo           |
      | null          |
      | "!£$%^&*()"   |
      | "{}:@~;'#<>?" |

  Scenario: Running an 'inSet' request that includes strings with special characters (white spaces) should be successful
    Given there is a field foo
    And foo is in set:
      | "]	[] [] [] [" |
      | "] [] [] ["    |
    Then the following data should be generated:
      | foo            |
      | null           |
      | "]	[] [] [] [" |
      | "] [] [] ["    |

  Scenario: Running an 'inSet' request that includes strings with special characters (unicode symbols) should be successful
    Given there is a field foo
    And foo is in set:
      | "†ŠŒŽ™¼ǅ©" |
      | "®…¶Σ֎"    |
    Then the following data should be generated:
      | foo        |
      | null       |
      | "†ŠŒŽ™¼ǅ©" |
      | "®…¶Σ֎"    |

  Scenario: Running an 'inSet' request that includes strings with special characters (emoji) should be successful
    Given there is a field foo
    And foo is in set:
      | "🚫⌛⚡🐢"   |
      | "👟💪😈🔬" |
    Then the following data should be generated:
      | foo        |
      | null       |
      | "🚫⌛⚡🐢"   |
      | "👟💪😈🔬" |

  Scenario: Running an 'inSet' request that includes strings with special characters (non roman character maps) should be successful
    Given there is a field foo
    And foo is in set:
      | "Ω" |
      | "ڦ" |
      | "আ" |
      | "⾉" |
      | "㑹" |
      | "㾹" |
    Then the following data should be generated:
      | foo  |
      | null |
      | "Ω"  |
      | "ڦ"  |
      | "আ"  |
      | "⾉"  |
      | "㑹"  |
      | "㾹"  |

  Scenario: Running an 'inSet' request that includes roman numeric strings that include numbers with Preceding zeros should be successful
    Given there is a field foo
    And foo is in set:
      | "£1.00"   |
      | "€5,99"   |
      | "¥10,000" |
    Then the following data should be generated:
      | foo       |
      | null      |
      | "£1.00"   |
      | "€5,99"   |
      | "¥10,000" |
