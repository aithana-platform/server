# Aithana Platform Server

This is the server for the Aithana Platform.

## Running tests

This application has two set of tests. First, `unit` and `integration` tests; second, `acceptance` tests.

The `unit` and `integration` tests are meant to be cheap. They heavily rely on local execution with mocks. Meanwhile, `acceptance` tests approach the application as a black box, with all its 3rd-party services and supporting technologies connected.

> **As a rule-of-thumb**: run `unit` and `integration` tests as often and as close to the development as possible; leave `acceptance` tests to run when needed.

```bash
# unit and integration testing
./gradlew test

# acceptance testing
./gradlew acceptanceTest
```


## Contributing

To contribute to the project, follow this project's [contributing guidelines][contributing].

---

[contributing]: ./CONTRIBUTING.md