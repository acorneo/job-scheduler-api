# Job Scheduler API

README на русском: [тык](README_RU.md)

API for asynchronous job processing.

## What it does

- Accepts jobs over HTTP.
- Puts jobs into Redis queue.
- Processes jobs in background workers.
- Tracks status and attempts.

## Stack

- Java 26
- Spring Boot 4
- Redis

## Statuses

- `PENDING`
- `PROCESSING`
- `DONE`
- `FAILED`

## Job types

- `WAIT` payload: `{"duration": 3.0}`
- `FAILING` payload: `{"probability": 0.9}`
- `CPU_TASK` payload: `{"n": 20}`

Important: `payload` is a JSON string.

## API

Base URL: `http://localhost:8080`

- `POST /api/v1/job` create job
- `GET /api/v1/job/{id}` get job
- `GET /api/v1/job?page=0&size=10` list jobs
- `GET /api/v1/health/ping` service health
- `GET /api/v1/health/redis` Redis health (`/api/v1/health/database` kept as alias)

Request example:

```json
{
	"type": "WAIT",
	"payload": "{\"duration\":3.0}"
}
```

Create examples for all job types:

```bash
curl -X POST http://localhost:8080/api/v1/job \
	-H "Content-Type: application/json" \
	-d '{"type":"WAIT","payload":"{\"duration\":3.0}"}'

curl -X POST http://localhost:8080/api/v1/job \
	-H "Content-Type: application/json" \
	-d '{"type":"FAILING","payload":"{\"probability\":0.9}"}'

curl -X POST http://localhost:8080/api/v1/job \
	-H "Content-Type: application/json" \
	-d '{"type":"CPU_TASK","payload":"{\"n\":20}"}'
```

## Run

Requirements:

- Redis at `localhost:6379`
- JDK 26

Command:

```bash
./gradlew bootRun
```

## Main settings

`src/main/resources/application.properties`

- `spring.data.redis.host`
- `spring.data.redis.port`
- `spring.task.execution.pool.core-size`
- `spring.task.execution.pool.max-size`
- `spring.task.execution.pool.queue-capacity`

## Retry behavior

- Max 3 attempts.
- Success -> `DONE`.
- Failure and attempts < 3 -> `PENDING`.
- Failure on attempt 3 -> `FAILED`.
