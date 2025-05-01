# Инструкция к запуску проекта

## 1. Запустите Docker

## 2. Запустите сервисы через docker-compose

## 3. Установите секрет для JWT

В файле настроек (`application.properties` или аналогичном) найдите параметр:
jwt.secret=

Вставьте любой секрет длиной не менее 32 символов, например:
jwt.secret=your-256-bit-secret-key-must-be-at-least-32-chars-long

## 4. Запустите приложение

Запустите основной класс приложения:
BankAppApplication

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
3 http://localhost:8080/api/users/28

4 http://localhost:8080/api/users/28/emails

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
