Feature: Reading and Writing CSV files
  As a researcher using the tool
  I want it to read my selected quotes and suggest codes for them in a CSV file
  So that I can easily integrate the tool with my workflow

  Scenario: It reads one CSV file and writes to another CSV file
    Given the "src/test/resources/mockInputFiles/test.csv" file with artifact ids, the sections of the artifact, and the text quotes
    When I ask to run open coding on it
    Then I get the result file with a new column with the codes for each quote