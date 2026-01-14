# intershop

Простой интернет-магазин. Можно добавлять товары в корзину и оформлять заказ, добавлять новые товары

Java, Spring Boot, Spring Web MVC, Thymeleaf, Spring Data JPA, Liquibase, Docker

## Требования

* Java 21+
* Postgres 18+

## Как собирать

1. `./gradlew clean build`

## Как тестировать

1. `./gradlew test`

## Как запустить локально

1. Создать базу `intershop`
2. Передать переменные окружения:
    * `DB_HOST`
    * `DB_PORT`
    * `DB_NAME`
    * `POSTGRES_USER`
    * `POSTGRES_PASSWORD`

   Или переопределить значения по-умолчанию в [application.properties](src/main/resources/application.properties)
3. `./gradlew bootRun`
4. Перейти на `http://localhost:8080`

## Как запустить с помощью Docker

1. [Собрать приложение](#как-собирать)
2. `docker-compose up`
3. Перейти на `http://localhost:8080`