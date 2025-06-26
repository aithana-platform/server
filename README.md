![aithana logo][logo]

# Aithana Platform Server

This is the server for the Aithana Platform.


## Usage

Aithana is meant to be used with CSV files (so far, with `;` as the actual separator). There are four things one should inform Aithana:

1. Aithana is powered by [Google Gemini][gemini], and for that you need an API Key (get one [here][gemini-key]) -- you'll need to inform it via the env var `GOOGLE_API_KEY`;
2. Aithana instructs Gemini on how to proceed (check the prompt's base [here][prompt-template]), but it's way better if the prompt contains specifications of your concrete application/research -- you'll be able to inform it with the `--context` flag;
3. Aithana can give you **a good starting point** for the open coding, but you need to provide a collection of pre-selected quotes from your research's artifacts -- for that, you'll use the `--input` flag to inform the path to the input CSV file;
4. Aithana expects an output file to write its results, so you need to inform it with `--output` flag; you can select the same as the input (to be overwritten) or a different one (which is recommended)

With that, since Aithana is (so far) a CLI tool, an example of use would be:

```bash
GOOGLE_API_KEY=AIz... aithana_server      \
    batch-csv-in-out                      \
    --input ./data/an_artifact.csv        \
    --output ./data/an_artifact.coded.csv \
    --context ./project_description.txt
```

> :exclamation: Note: the actual command might depend on how you get the latest release; you can: [download][download-sec], [run from docker][docker-sec], or even [compile and run][compile-sec]

## Downloading Release

Aithana server is delivered using GitHub Releases. Check out the [latest Aithana Server release][latest-release].

## Running from Docker

Aithana server can be run directly from the Docker image: `jooaodanieel/aithana-server:0.1`.

To run:

```bash
docker run --rm -it                         \
    -e GOOGLE_API_KEY=<your Google API Key> \
    -v /path/to/your/data:/data             \
    aithana-server:0.1                      \
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
[gemini]: https://gemini.google.com
[gemini-key]: https://ai.google.dev/gemini-api/docs/api-key
[prompt-template]: https://github.com/aithana-platform/server/blob/main/src/main/resources/open_coding_prompt.txt
[download-sec]: #downloading-release
[docker-sec]: #running-from-docker
[compile-sec]: #compiling-and-running
[latest-release]: https://github.com/aithana-platform/server/releases/latest
[dockerfile]: ./Dockerfile
[kotlinlang]: https://kotlinlang.org
[gradle]: https://gradle.org
[contributing]: ./CONTRIBUTING.md