# Job Scheduler API

README на русском: [тык](README_RU.md)

API for asynchronous task processing.

---

## What it does

- Accepts a task via HTTP.
- Puts the task into a Redis queue.
- Processes the task in the background using workers.
- Updates the status and attempt count.

---

## Stack

- Java 26
- Spring Boot 4
- Redis

---

## Running
Via Docker Compose: `docker-compose up`

---

## Task Statuses

- `PENDING`
- `PROCESSING`
- `DONE`
- `FAILED`

---

## Task Types

These task types were created as examples. Each one is meaningful and affects the system differently, testing its reliability.

### WAIT
Type: `WAIT`
Payload: `{"duration": 3.0}`
The only field *duration* is a floating-point number (float).

This job suspends the current thread for *duration* seconds using `Thread.sleep()`. This tests thread parallelism, since in a single thread all such tasks will complete in the total sum of *duration* across all jobs.

Usage example:
```bash
curl -X POST http://localhost:8080/api/v1/job \
    -H "Content-Type: application/json" \
    -d '{"type":"WAIT","payload":"{\"duration\":3.0}"}'
```

### FAILING
Type: `FAILING`
Payload: `{"probability": 0.9}`
The only field *probability* is a probability value (float, 0.0 <= *probability* <= 1.0).

This job throws a RuntimeException with a probability of *probability* during processing. It is designed to test how the system behaves when exceptions occur, as well as to verify the retry mechanism.

Usage example:
```bash
curl -X POST http://localhost:8080/api/v1/job \
    -H "Content-Type: application/json" \
    -d '{"type":"FAILING","payload":"{\"probability\":0.9}"}'
```

### CPU_TASK
Type: `CPU_TASK`
Payload: `{"n": 10}`
The only field *n* is a natural number (int).

This job computes the *n*-th [Fibonacci number](https://en.wikipedia.org/wiki/Fibonacci_sequence). Since it does so recursively, the task takes a considerable amount of CPU time for reasonably large values of *n* (around 40+).

Usage example:
```bash
curl -X POST http://localhost:8080/api/v1/job \
    -H "Content-Type: application/json" \
    -d '{"type":"CPU_TASK","payload":"{\"n\":20}"}'
```

**Note:** the `payload` field is passed as a JSON string in all task types.

---

## Mock Data
The **JobSpamGenerator** class creates 200 tasks to demonstrate the system in action. 20% are of type Wait, 40% are CPU, and 40% are Failing.

---

## API

Base URL: `http://localhost:8080`

- `POST /api/v1/job` — create a task
- `GET /api/v1/job/{id}` — get a task by ID
- `GET /api/v1/job?page=0&size=10` — list tasks
- `GET /api/v1/health/ping` — service health check
- `GET /api/v1/health/redis` — Redis health check (`/api/v1/health/database` is kept as an alias)

---

## Retry Behavior

- Maximum 3 attempts.
- Success → `DONE`.
- Failure with fewer than 3 attempts → back to `PENDING`.
- Failure on the 3rd attempt → `FAILED`.
