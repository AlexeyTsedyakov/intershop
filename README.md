# intershop

Простой интернет-магазин. Можно добавлять товары в корзину и оформлять заказ, заводить новые товары.

Проект состоит из двух модулей:
* `mvc-app` - Витрина магазина
* `payment-service` - Мок-сервис, который эмулирует оплату через RESTful-интерфейс

Java, Spring Boot, Spring WebFlux, Thymeleaf, Spring Data R2DBC, Liquibase, Redis, Docker, OpenApi

## Требования

* Java 21+
* Postgres 18+
* Redis 8+

## Как собирать

1. `./gradlew clean build`

## Как тестировать

Юнит-тесты:
1. `./gradlew test`

Интеграционные тесты:
1. `./gradlew integrationTest`

## Как запустить локально

### payment-service
   
1.`./gradlew :payment-service:bootRun`

### mvc-app

1. Создать базу `intershop`
2. Подготовить Redis
3. Передать переменные окружения:
    * `DB_HOST`
    * `DB_PORT`
    * `DB_NAME`
    * `POSTGRES_USER`
    * `POSTGRES_PASSWORD`
    * `REDIS_HOST`
    * `REDIS_PORT`
    * `PAYMENT_HOST` - Адрес где запускается payment-service
    * `PAYMENT_PORT` - Порт payment-service, по-умолчанию 8081

   Или переопределить значения по-умолчанию в [application.properties](src/main/resources/application.properties)
4. `./gradlew :mvc-app:bootRun`
5. Перейти на `http://localhost:8080`

## Как запустить с помощью Docker

1. [Собрать приложение](#как-собирать)
2. `docker-compose up`
3. Перейти на `http://localhost:8080`