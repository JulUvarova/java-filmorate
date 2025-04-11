# java-filmorate

Бекенд сервиса для оценки фильмов и рекомендаций. Помогает пользователям выбирать фильмы на основе оценок сообщества.

## Оглавление
- [Технологии](#%EF%B8%8F-технологии)
- [Функции](#-функции)
- [REST API](#%EF%B8%8F-rest-api)
- [Модели данных](#-модели-данных)
- [Валидация](#-валидация)
- [Запуск](#-запуск)
- [Настройка БД](#%EF%B8%8F-настройка-бд)
- [Схема БД](#-схема-бд)
- [Особенности](#-особенности)

## ⚙️ Технологии
- Java
- Spring Boot
- H2 Database
- Maven
- REST API

## 🎯 Функции
### Фильмы
- Добавление/обновление/поиск фильмов
- Лайки и топ-N популярных фильмов
- Управление жанрами и рейтингами MPA

### Пользователи
- Регистрация и обновление профиля
- Односторонняя система друзей
- Поиск общих друзей

## 🛠️ REST API
### Фильмы
| Метод   | Путь                          | Действие                     |
|---------|-------------------------------|------------------------------|
| POST    | `/films`                      | Создать фильм                |
| PUT     | `/films`                      | Обновить фильм               |
| GET     | `/films`                      | Все фильмы                   |
| GET     | `/films/{id}`                 | Фильм по ID                  |
| PUT     | `/films/{id}/like/{userId}`   | Лайкнуть фильм               |
| DELETE  | `/films/{id}/like/{userId}`   | Удалить лайк                 |
| GET     | `/films/popular?count={n}`    | Топ-N популярных фильмов     |

### Пользователи
| Метод   | Путь                                  | Действие                     |
|---------|---------------------------------------|------------------------------|
| POST    | `/users`                              | Создать пользователя         |
| PUT     | `/users`                              | Обновить пользователя        |
| GET     | `/users`                              | Все пользователи             |
| GET     | `/users/{id}`                         | Пользователь по ID           |
| PUT     | `/users/{id}/friends/{friendId}`      | Добавить друга               |
| DELETE  | `/users/{id}/friends/{friendId}`      | Удалить друга                |
| GET     | `/users/{id}/friends`                 | Список друзей                |
| GET     | `/users/{id}/friends/common/{otherId}`| Общие друзья                |

### Дополнительно
- `GET /genres` - Получить все жанры
- `GET /genres/{id}` - Получить жанр по ID
- `GET /mpa` - Получить все рейтинги MPA
- `GET /mpa/{id}` - Получить рейтинг MPA по ID

## 🗃️ Модели данных 
### Класс `Film`
```java
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Genre> genres;
    private Mpa mpa;
}
```
### Класс User
```java
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private List<Integer> friends;
}
```
### Класс Genre
```java
public class Genre {
    private int id;
    private String name;
}
```
### Класс Mpa
```java
public class Mpa {
    private int id;
    private String name;
}
```

## 🔍 Валидация
Для фильмов:
- Название не пустое (@NotBlank)
- Описание ≤ 200 символов (@Size)
- Дата релиза ≥ 28 декабря 1895 (@PastOrPresent)
- Продолжительность > 0 (@Positive)
Для пользователей:
- Email содержит @ (@Email)
- Логин без пробелов (@Pattern)
- Имя заменяется логином, если пустое
- День рождения ≤ текущей даты (@Past)

## 🚀 Запуск
Клонировать репозиторий:

```bash
git clone https://github.com/your-username/filmorate.git
```
Собрать проект:
```bash
mvn clean package
```
Запустить приложение:
```bash
java -jar target/filmorate.jar
```

## ⚙️ Настройка БД
application.properties:

```
spring.sql.init.mode=always
spring.datasource.url=jdbc:h2:file:./db/filmorate
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
```

## 📊 Схема БД

![scheme DB](/src/main/resources/Scheme%20of%20DB.PNG)

DDL-скрипты доступны в src/main/resources/schema.sql

## 🌟 Особенности
- Трехуровневая архитектура (Controller → Service → Storage)
- Поддержка двух режимов H2: in-memory и persistent
- Подробное логирование операций
- Гибкая система ошибок с HTTP-статусами:
- 400 Bad Request - ошибка валидации
- 404 Not Found - объект не найден
- 500 Internal Server Error - серверная ошибка
