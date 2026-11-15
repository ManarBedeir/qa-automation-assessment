# QA Automation Assessment
[![Tests](https://github.com/ManarBedeir/qa-automation-assessment/actions/workflows/tests.yml/badge.svg)](https://github.com/ManarBedeir/qa-automation-assessment/actions/workflows/tests.yml)

Two automation projects in one Maven build. `ui-tests` drives https://the-internet.herokuapp.com with Selenium WebDriver (file upload, dynamic loading). `api-tests` calls the FakeRESTApi Books endpoints with REST Assured. Both use TestNG.

## Prerequisites

- JDK 17 or newer, with `JAVA_HOME` set. Built and tested on Temurin 21.
- A browser: Chrome, Edge or Firefox. The tests use the system default browser if it is one of these, otherwise the first one installed (Chrome, Edge, Firefox).
- Internet access. Both targets are public sites.

Maven is not needed. The wrapper downloads it on the first run. Selenium Manager downloads the matching browser driver.

## Running the tests

From the repository root, in Git Bash, macOS or Linux:

```bash
./mvnw clean test
```

In Windows PowerShell:

```powershell
.\mvnw.cmd clean test
```

In Windows cmd:

```bat
mvnw.cmd clean test
```

The table below uses `./mvnw`. Swap in the command for your shell.

| Goal | Command |
|---|---|
| UI tests only | `./mvnw clean test -pl ui-tests -am` |
| API tests only | `./mvnw clean test -pl api-tests -am` |
| Show the browser | `./mvnw clean test -pl ui-tests -am -Dheadless=false` |
| Fixed browser | `./mvnw clean test -pl ui-tests -am -Dbrowser=chrome` (or `edge`, `firefox`) |
| Another target URL | `./mvnw clean test -Dbase.url=https://example.test -Dapi.base.url=https://api.example.test` |

UI tests run headless by default. Any key in `config.properties` can be overridden with `-Dkey=value` or an environment variable (`HEADLESS=false`, `API_BASE_URL=...`). Order of precedence: system property, environment variable, file.

Reports are written to `target/surefire-reports` in each module. A failed UI test also saves a screenshot to `ui-tests/target/screenshots`.

## Structure

```
test-commons/        Config reader and JSON test data loader used by both projects
ui-tests/
  driver/            Browser enum, creates Chrome, Edge or Firefox
  pages/             Page objects, one class per page
  data/              Records for test data rows
  tests/             BaseTest, FileUploadTest, DynamicLoadingTest
  resources/         config.properties, testdata/, files/
api-tests/
  services/          BaseService (request setup) and BooksService (endpoints)
  models/            Book record
  data/              Records for test data rows
  tests/             BooksTest
  resources/         config.properties, testdata/, schemas/
```

## Design notes

Page objects hold the locators and the actions of one page. Each action returns the page the user lands on, so a test reads as the manual steps: `openHomePage().openDynamicLoading().openExample(2).start().resultText()`.

The API follows the same idea. `BooksService` is the only place that knows paths and HTTP verbs. `BaseService` sets the base URI, content type, timeouts and failure logging once.

The browser is set with `browser=auto` in `config.properties`. `BrowserDetector` reads the system default (Windows registry, `xdg-settings` on Linux) and falls back to whichever supported browser is installed.

Test data lives in JSON files and is mapped to records. TestNG data providers feed the tests, so a new case is a new JSON entry. URLs, browser and timeouts are in `config.properties`.

Waits are explicit only. There is no `Thread.sleep` and no implicit wait. The dynamic loading result does not exist in the DOM until the 5 second delay ends, so the page waits for it to become visible, up to `explicit.wait.seconds`.

The negative API test sends ids just outside the seeded range (0 and 201), a negative id and a non-numeric id. These are the cases where the API validates input, and the test checks status, content type and the problem details body.

Allure is not used. Surefire reports and failure screenshots were enough for this size of suite.

## FakeRESTApi observations

Base URL: https://fakerestapi.azurewebsites.net. Observed on 2026-11-15.

- POST, PUT and DELETE return 200 but nothing is stored. A GET after POST for the new id returns 404, and a GET after PUT returns the original book. The tests only check the response, not a later read.
- POST returns 200, not 201.
- PUT accepts a body id that differs from the id in the path and returns the body id.
- PUT on an id that does not exist (999999) returns 200.
- DELETE on an id that does not exist returns 200 with an empty body.
- POST accepts an empty object and returns defaults (id 0, null text fields, `publishDate` `0001-01-01T00:00:00`). A negative `pageCount` is also accepted.
- Malformed JSON and a non-numeric id in the body return 400.
- `publishDate` on GET is calculated from the current time, so it changes between calls. The tests never assert it.
- The `Content-Type` header carries a version parameter: `application/json; charset=utf-8; v=1.0`.

## Known issues

Both sites are shared demo hosts and can be slow or unavailable. If a test fails with a timeout, open the site in a browser before debugging the code.

The first run is slower because Maven, the dependencies and the browser driver are downloaded.
