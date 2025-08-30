# Word Challenge

A tiny Spring Boot 3 service that returns a **fresh random word** and its **definitions** on every request.

- **GET /** → JSON with `word` and `definitions` (changes on every refresh)
- **GET /wordOfTheDay** → same payload as `/` (kept for API clients)
- **Swagger UI:** `/swagger-ui/index.html`
- **H2 console (dev only):** `/h2-console`

> Note: the DB is not required for the live endpoint; it’s left in place for potential future features.

---

## Table of Contents

- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Running (Windows, macOS, Linux)](#running-windows-macos-linux)
- [Docker](#docker)
- [Configuration](#configuration)
- [API](#api)
- [Run Tests](#run-tests)
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)

---

## Prerequisites

- **JDK 17+** (Java 21 or 23 works great)
- **Maven** optional — project includes the Maven Wrapper (`mvnw` / `mvnw.cmd`)
- Internet access (the service calls):
  - Random word API → `https://random-word-api.herokuapp.com/word`
  - Dictionary API → `https://api.dictionaryapi.dev/api/v2/entries/{lang}/{word}`

---

## Quick Start

```bash
# from the project root
./mvnw spring-boot:run
```

Once started, open:

- <http://localhost:8081/> → returns `{ "word": "...", "definitions": [...] }`
- <http://localhost:8081/swagger-ui/index.html> → interactive docs

> Port comes from `server.port` (default **8081**). Change it in
> `src/main/resources/application.properties` if needed.

---

## Running (Windows, macOS, Linux)

### macOS / Linux (bash/zsh)

```bash
# build a runnable JAR
./mvnw clean package

# run the app
java -jar target/wordchallenge-*.jar
```

### Windows (PowerShell or CMD)

```powershell
# build a runnable JAR
mvnw.cmd clean package

# run the app
java -jar target\wordchallenge-*.jar
```

> Don’t see “bash” on Windows? Use **PowerShell** or **CMD**.
> You can also install **Git for Windows** (which provides Git Bash) or enable **WSL**.

---

## Docker

```bash
# build image
docker build -t wordchallenge .

# run (maps container port 8081 → host 8081)
docker run --rm -p 8081:8081 --name wordchallenge wordchallenge
```

Override port at runtime:

```bash
docker run --rm -p 9090:9090 -e SERVER_PORT=9090 wordchallenge
```

---

## Configuration

All config lives in `src/main/resources/application.properties`.

Defaults (already present):

```properties
spring.application.name=wordchallenge
server.port=8081

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# External APIs
wordchallenge.randomWordApiBaseUrl=https://random-word-api.herokuapp.com
wordchallenge.dictionaryApiBaseUrl=https://api.dictionaryapi.dev
wordchallenge.language=en
wordchallenge.timezone=Europe/Berlin
```

You can override via environment variables:

- Unix/macOS:
  ```bash
  export SERVER_PORT=9090
  export WORDCHALLENGE_LANGUAGE=en
  ```
- Windows PowerShell:
  ```powershell
  $env:SERVER_PORT=9090
  $env:WORDCHALLENGE_LANGUAGE='en'
  ```

---

## API

### GET `/`

Returns a **fresh random word** and its definitions on every request.

**Response 200**
```json
{
  "word": "unlaced",
  "definitions": [
    { "definition": "not under constraint in action or expression", "partOfSpeech": "adjective" },
    { "definition": "with laces not tied", "partOfSpeech": "adjective" }
  ]
}
```

**Response 404**
```json
{
  "timestamp": "2025-08-28T08:25:15.779Z",
  "status": 404,
  "error": "Not Found",
  "message": "Could not find a random word with definitions after several attempts.",
  "path": "/"
}
```

### GET `/wordOfTheDay`

Same payload as `/` (kept for clients and Swagger examples).

---

## Run Tests

```bash
# All tests
./mvnw test

# Or from IDE: run the test classes under src/test/java
```

Test files:

```
src/test/java/com/challenge/wordchallenge/WordchallengeApplicationTests.java
src/test/java/com/challenge/wordchallenge/service/ProjectServiceTest.java
src/test/java/com/challenge/wordchallenge/service/WordsApiClientTest.java
```

- `ProjectServiceTest` — verifies “fresh word every call” logic (mocks HTTP clients).
- `WordsApiClientTest` — parses dictionary responses using a stubbed `WebClient`.
- `WordchallengeApplicationTests` — boots the Spring context.

---

## Project Structure

```
src
├── main
│   ├── java/com/challenge/wordchallenge
│   │   ├── config/           # WebClient beans, props
│   │   ├── controller/       # JSON at / and /wordOfTheDay
│   │   ├── exception/        # GlobalExceptionHandler + custom exceptions
│   │   ├── model/            # domain + dto
│   │   ├── persistence/      # (kept for future use)
│   │   ├── service/          # ProjectService, RandomWordClient, WordsApiClient
│   │   └── WordchallengeApplication.java
│   └── resources/
│       └── application.properties
└── test
    └── java/com/challenge/wordchallenge
        ├── WordchallengeApplicationTests.java
        └── service/
            ├── ProjectServiceTest.java
            └── WordsApiClientTest.java
```

---

## Troubleshooting

- **Port already in use**  
  Change `server.port` or free the port.
- **404 “Could not find a random word…”**  
  Random word API sometimes returns inflected/rare words missing from the public dictionary.
  The service retries multiple candidates per call; simply refresh if you hit a 404.
- **Slow responses / timeouts**  
  You can add/adjust WebClient timeouts in `WordConfig` if your network is slow.
- **Swagger shows 502/500**  
  Dictionary 404s are handled as empty and retried. Genuine upstream failures bubble up as 502 via `ApiClientException`.

---

Happy building! If you want filters like `/?pos=noun&limit=3`, open an issue or ask — easy to add.
