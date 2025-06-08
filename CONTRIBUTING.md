# Contributing to Aithana

## Acceptance Tests

This project uses [Cucumber][cucumber] to define the acceptance tests. The features can be found at [features folder][featuresFolder]. We strongly recommend the usage of the extended TDD/BDD cycle:

![TDD cycle expanded to BDD][tdd-bdd-cycle]

### Adding new features

To add a new feature, you need to create a new `.feature` file in the appropriate folder: `src/tests/resources/features`. The definition of the steps must be at: `src/tests/kotlin/org/aithana/platform/server/steps`.

```gherkin
Feature: Is it Friday yet?
  Everybody wants to know when it's Friday

  Scenario: Sunday isn't Friday
    Given today is "Sunday"
    When I ask whether it's Friday yet
    Then I should be told "Nope"
    
  Scenario: Friday is Friday
    Given today is "Friday"
    When I ask whether it's Friday yet
    Then I should be told "TGIF!"
```

```kotlin
class StepDefs {
    private lateinit var today: String
    private lateinit var actualAnswer: String

    @Given("today is {string}")
    fun today_is(day: String) {
        today = day
    }

    @When("I ask whether it's Friday yet")
    fun i_ask_whether_it_s_Friday_yet() {
        val aithana = Aithana()
        actualAnswer = aithana.isItFriday(today)
    }

    @Then("I should be told {string}")
    fun i_should_be_told(expectedAnswer: String) {
        assertEquals(expectedAnswer, actualAnswer)
    }
}
```


---

[tdd-bdd-cycle]: ./tdd-bdd-cycle.jpg
[cucumber]: https://cucumber.io
[featuresFolder]: https://github.com/aithana-platform/server/tree/main/src/tests/resources/features
[stepsFolder]: https://github.com/aithana-platform/server/tree/main/src/tests/kotlin/org/aithana/platform/server/steps