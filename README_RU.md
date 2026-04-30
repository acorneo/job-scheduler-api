# Job Scheduler API

English README: [click](README.md)

API для асинхронной обработки задач.

---

## Что делает

- Принимает задачу через HTTP.
- Кладет задачу в очередь Redis.
- Обрабатывает задачу в фоне воркерами.
- Обновляет статус и число попыток.

---

## Стек

- Java 26
- Spring Boot 4
- Redis

---

## Запуск
Через Docker Compose: ``docker-compose up``

---

## Статусы задач

- `PENDING`
- `PROCESSING`
- `DONE`
- `FAILED`

---

## Типы задач

Эти типы задач были созданы как примеры. Каждая из них имеет смысл и по-разному влияют на систему и проверяют ее работоспособность.

### WAIT
Тип: `WAIT`  
Payload: `{"duration": 3.0}`  
Единственное поле *duration* - вещественное число (float).  

Эта джоба останавливает текущий поток на *duration* секунд при помощи `Thread.sleep()`. Таким образом проверяется параллельность потоков, так как в одном потоке все такие задачи выполнятся за сумму *duration* во всех джобах.

Пример использования:
```bash
curl -X POST http://localhost:8080/api/v1/job \
	-H "Content-Type: application/json" \
	-d '{"type":"WAIT","payload":"{\"duration\":3.0}"}'
  ```

### FAILING
Тип: `FAILING`  
Payload: `{"probability": 0.9}`  
Единственное поле *probability* - вероятность (float, 0.0 <= *probability* <= 1.0).

Эта джоба при обработке вызывает RuntimeException с вероятностью *probability*. Она создана для того, чтобы проверять как будет вести себя система при вызове исключений, а также retry-механизм. 

Пример использования:
```bash
curl -X POST http://localhost:8080/api/v1/job \
	-H "Content-Type: application/json" \
	-d '{"type":"FAILING","payload":"{\"probability\":0.9}"}'
  ```

### CPU_TASK
Тип: `CPU_TASK`  
Payload: `{"n": 10}`  
Единственное поле *n* - натуральное число (int).

Эта джоба считает *n*-ное [число Фибоначчи](https://ru.wikipedia.org/wiki/Числа_Фибоначчи). Так как она делает это рекурсивно, задача занимает приличное количество процессорного времени при хоть сколько-то больших *n* (+-40).

Пример использования:
```bash
curl -X POST http://localhost:8080/api/v1/job \
	-H "Content-Type: application/json" \
	-d '{"type":"CPU_TASK","payload":"{\"n\":20}"}'
  ```


Важно: поле `payload` передается строкой JSON во всех типах задач.

---

## Mock данные
В **JobSpamGenerator** классе создаются 200 задач для демонстрации работы системы. Из них 20% типа Wait, 40% - CPU, 40% - Failing.

---

## API

База: `http://localhost:8080`

- `POST /api/v1/job` создать задачу
- `GET /api/v1/job/{id}` получить задачу
- `GET /api/v1/job?page=0&size=10` список задач
- `GET /api/v1/health/ping` проверка сервиса
- `GET /api/v1/health/redis` проверка Redis (`/api/v1/health/database` оставлен как алиас)


---


## Поведение retry

- Максимум 3 попытки.
- Успех -> `DONE`.
- Ошибка и попыток меньше 3 → снова `PENDING`.
- Ошибка на 3-й попытке -> `FAILED`.
