# Job Scheduler API

API для асинхронной обработки задач.

## Что делает

- Принимает задачу через HTTP.
- Кладет задачу в очередь Redis.
- Обрабатывает задачу в фоне воркерами.
- Обновляет статус и число попыток.

## Стек

- Java 26
- Spring Boot 4
- Redis

## Статусы

- `PENDING`
- `PROCESSING`
- `DONE`
- `FAILED`

## Типы задач

- `WAIT` payload: `{"duration": 3.0}`
- `FAILING` payload: `{"probability": 0.9}`
- `CPU_TASK` payload: `{"n": 20}`

Важно: поле `payload` передается строкой JSON.

## API

База: `http://localhost:8080`

- `POST /api/v1/job` создать задачу
- `GET /api/v1/job/{id}` получить задачу
- `GET /api/v1/job?page=0&size=10` список задач
- `GET /api/v1/health/ping` проверка сервиса
- `GET /api/v1/health/redis` проверка Redis (`/api/v1/health/database` оставлен как алиас)

Пример запроса:

```json
{
	"type": "WAIT",
	"payload": "{\"duration\":3.0}"
}
```

Примеры создания всех трех типов задач:

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

## Запуск

Требуется:

- Redis на `localhost:6379`
- JDK 26

Команда:

```bash
./gradlew bootRun
```

## Основные настройки

`src/main/resources/application.properties`

- `spring.data.redis.host`
- `spring.data.redis.port`
- `spring.task.execution.pool.core-size`
- `spring.task.execution.pool.max-size`
- `spring.task.execution.pool.queue-capacity`

## Поведение retry

- Максимум 3 попытки.
- Успех -> `DONE`.
- Ошибка и попыток меньше 3 -> снова `PENDING`.
- Ошибка на 3-й попытке -> `FAILED`.
