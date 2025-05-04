# Bank Service Application

## Описание проекта 

Банковское приложение, реализованное на Spring Boot, предоставляющее REST API для управления пользовательскими данными и банковскими операциями.

## Технологии 

- Java 11+
- Spring Boot
- PostgreSQL
- Maven
- JWT для аутентификации 
- Swagger для документации API 

## Требования 

- Java 11 или выше 
- Maven
- PostgreSQL

## Установка и запуск / Installation and Running

1. Клонировать репозиторий :
```bash
git clone [repository-url]
```

2. Настроить базу данных :
- Создать базу данных PostgreSQL (Запустить скрипт в docker-compose)
- Настроить параметры подключения в `application.properties` / 
Вставьте любой jwt.secret длиной не менее 32 символов, например: 
```
jwt.secret=your-256-bit-secret-key-must-be-at-least-32-chars-long
```
3. Запустить приложение (BankAppApplication):
```
mvn spring-boot:run
```

## Основные функции / Main Features

### Пользовательские операции / User Operations
- Поиск пользователей с фильтрацией и пагинацией
- Обновление пользовательских данных
  - Управление email
  - Управление телефонами

### Банковские операции / Banking Operations
- Автоматическое начисление процентов (10% каждые 30 секунд) 
- Перевод денег между пользователями 

### Безопасность / Security
- JWT аутентификация 
- Валидация входных данных 
- Потокобезопасные операции 

## API Документация / API Documentation

Swagger UI доступен по адресу :
```
http://localhost:8080/swagger-ui.html
```

## Тестирование / Testing

Проект включает unit-тесты и интеграционные тесты с использованием TestContainers.

Запуск тестов / Run tests:
```bash
mvn test
```

## Архитектура / Architecture

Приложение построено на трехслойной архитектуре / The application is built on a three-layer architecture:
- API Layer (Controllers)
- Service Layer (Business Logic)
- DAO Layer (Data Access)


## Примеры запросов для endpoint'ов:

1 http://localhost:8080/api/register

```
{
    "emails":"Bob@mail.ru",
    "name":"Bob",
    "password":"123456789",
    "dateOfBirth":"2000-02-02"
}
```

2 http://localhost:8080/api/auth

```
{
    "email":"Bob@mail.ru",
    "password":"123456789"
}
```
### Вставить токен
3 http://localhost:8080/api/users/1

4 http://localhost:8080/api/users/1/emails

```
{
    "email":"Bob1@mail.ru"
}
```

5 http://localhost:8080/api/users/search/by-dob

```
{
    "dateOfBirth":"1900-04-30",
    "page":0,
    "size":5
}
```

5 http://localhost:8080/api/users/search/by-name

```
{
    "name":"J",
    "page":0,
    "size":2
}
```

6 http://localhost:8080/api/transfers

```
{
    "recipientId": 1,
    "amount": 100.50
}
```
