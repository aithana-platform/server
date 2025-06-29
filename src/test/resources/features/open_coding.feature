Feature: Open Coding of Raw Material
  As a researcher using the tool
  I want it to suggest initial codes for the quotes I selected
  So that I have less friction starting with the coding

  Scenario: It open codes from a table
    Given a collection containing the artifact id, the section of the artifact, and the text quote
    When I ask to open code my table
    Then I get a copy of the table with a new column with the code for each quote

  Scenario: Its open coding can generate more than one code for the same quote
    Given a collection containing the artifact id, the section of the artifact, and the text quote
    And a single quote with enough content to be coded using more than code
    When I ask to open code my table
    Then I get more than one line for the same quote, but with different codes