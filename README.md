![aithana logo][logo]

# Aithana Platform Server

This is the server for the Aithana Platform.

## Running from Docker

Aithana server can be run directly from the Docker image: `aithana-platform/server:0.1`.

To run:

```bash
docker run --rm -it                         \
    -e GOOGLE_API_KEY=<your Google API Key> \
    -v /path/to/your/data:/data             \
    aithana-platform/server:0.1             \
    batch-csv-in-out                        \
    --input /data/input/demo.csv            \
    --output /data/output/demo.csv          \
    --context /data/context.txt
```

You can also build the image locally using the [Dockerfile][dockerfile].

```bash
docker image build                 \
    -t aithana-platform/server:0.1 \
    --target cli                   \
    .
```


## Compiling and Running

Aithana server is built using [Kotlin][kotlinlang] and [Gradle][gradle]. To compile it, use the `shadowJar` gradle task, then run the `.jar`:

```bash
# compiles and generate the fatJar
./gradlew shadowJar

# ensures the env var exists
export GOOGLE_API_KEY=<your Google API Key>

# runs the fatJar
java -jar build/libs/*-all.jar                  \
    batch-csv-in-out                            \
    --input /path/to/your/data/input/demo.csv   \
    --output /path/to/your/data/output/demo.csv \
    --context /path/to/your/data/context.txt
```

## Running tests

This application has two set of tests. First, `unit` and `integration` tests; second, `acceptance` tests.

The `unit` and `integration` tests are meant to be cheap. They heavily rely on local execution with mocks. Meanwhile, `acceptance` tests approach the application as a black box, with all its 3rd-party services and supporting technologies connected.

> **As a rule-of-thumb**: run `unit` and `integration` tests as often and as close to the development as possible; leave `acceptance` tests to run when needed.

```bash
# unit and integration testing
./gradlew test

# acceptance testing -- make sure GOOGLE_API_KEY env var is set
GOOGLE_API_KEY=<your Google API Key> ./gradlew acceptanceTest
```


## Contributing

To contribute to the project, follow this project's [contributing guidelines][contributing].

---

[logo]: ./aithana-banner.png
[dockerfile]: ./Dockerfile
[kotlinlang]: https://kotlinlang.org
[gradle]: https://gradle.org
[contributing]: ./CONTRIBUTING.md