# (1075) ignoring these tests as only allowing latin character set for now but we will turn them back on when allow them
@ignore
Feature: Whilst including non-latin characters, user can specify that a value is equalTo a required value

  Background:
    Given the generation strategy is full

### alone ###

  Scenario: Running an 'equalTo' request that includes strings with special characters (standard) should be successful
    Given there is a field foo
    And foo is equal to ".,;:/()-+£$%€!?=&#@<>[]{}^*"
    Then the following data should be generated:
      | foo                           |
      | null                          |
      | ".,;:/()-+£$%€!?=&#@<>[]{}^*" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (like white space strings) should be successful
    Given there is a field foo
    And foo is equal to "]	[] [] [] [] [] [] ["
    Then the following data should be generated:
      | foo                     |
      | null                    |
      | "]	[] [] [] [] [] [] [" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (unicode symbols) should be successful
    Given there is a field foo
    And foo is equal to "†ŠŒŽ™¼ǅ©®…¶Σ֎"
    Then the following data should be generated:
      | foo             |
      | null            |
      | "†ŠŒŽ™¼ǅ©®…¶Σ֎" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (emoji) should be successful
    Given there is a field foo
    And foo is equal to "☺☹☻😀😁😂😃😄😅😆😇😈😉😊😋😌🚩🚪🚫🚬🚭🚮🚯🚰"
    Then the following data should be generated:
      | foo                                             |
      | null                                            |
      | "☺☹☻😀😁😂😃😄😅😆😇😈😉😊😋😌🚩🚪🚫🚬🚭🚮🚯🚰" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (non roman character maps: Chinese / Arabic / Russian) should be successful
    Given there is a field foo
    And foo is equal to "传/傳象形字ФХѰѾЦИتشرقصف"
    Then the following data should be generated:
      | foo                  |
      | null                 |
      | "传/傳象形字ФХѰѾЦИتشرقصف" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (non roman character maps: Chinese / Arabic / Russian) should be successful
    Given there is a field foo
    And foo is equal to "בְּרֵאשִׁית, בָּרָא אֱלֹהִים, אֵת הַשָּׁמַיִם, וְאֵת הָאָרֶץ"
    Then the following data should be generated:
      | foo                                                            |
      | null                                                           |
      | "בְּרֵאשִׁית, בָּרָא אֱלֹהִים, אֵת הַשָּׁמַיִם, וְאֵת הָאָרֶץ" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (standard) alongside roman alphanumeric characters should be successful
    Given there is a field foo
    And foo is equal to "abcdefghijk.,;:/()-+£$%€!?=&#@<>[]{}^*"
    Then the following data should be generated:
      | foo                                      |
      | null                                     |
      | "abcdefghijk.,;:/()-+£$%€!?=&#@<>[]{}^*" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (white spaces) alongside roman alphanumeric characters should be successful
    Given there is a field foo
    And foo is equal to "abcdefghijk]	[] [] [] [] [] [] ["
    Then the following data should be generated:
      | foo                                |
      | null                               |
      | "abcdefghijk]	[] [] [] [] [] [] [" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (unicode symbols) alongside roman alphanumeric characters should be successful
    Given there is a field foo
    And foo is equal to "abcdefghijk†ŠŒŽ™¼ǅ©®…¶Σ֎"
    Then the following data should be generated:
      | foo                        |
      | null                       |
      | "abcdefghijk†ŠŒŽ™¼ǅ©®…¶Σ֎" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (emoji) alongside roman alphanumeric characters should be successful
    Given there is a field foo
    And foo is equal to "abcdefghijk☺☹☻😀😁😂😃😄😅😆😇😈😉😊😋😌🚩🚪🚫🚬🚭🚮🚯🚰"
    Then the following data should be generated:
      | foo                                                        |
      | null                                                       |
      | "abcdefghijk☺☹☻😀😁😂😃😄😅😆😇😈😉😊😋😌🚩🚪🚫🚬🚭🚮🚯🚰" |

  Scenario: Running an 'equalTo' request that includes strings with special characters (non roman character maps: Chinese / Arabic / Russian) alongside roman alphanumeric characters should be successful
    Given there is a field foo
    And foo is equal to "abcdefghijk传/傳象形字ФХѰѾЦИتشرقصف"
    Then the following data should be generated:
      | foo                             |
      | null                            |
      | "abcdefghijk传/傳象形字ФХѰѾЦИتشرقصف" |

  Scenario: Running an 'equalTo' request that includes roman numeric strings that include numbers in a currency style should be successful
    Given there is a field foo
    And foo is equal to "£1.00"
    Then the following data should be generated:
      | foo     |
      | null    |
      | "£1.00" |
