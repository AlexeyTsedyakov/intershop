# intershop

Демо-проект простого интернет-магазина. Можно регистрировать пользователей, добавлять товары в корзину и оформлять
заказ, заводить новые товары.

Проект состоит из двух модулей:

* `mvc-app` - Витрина магазина
* `payment-service` - Мок-сервис, который эмулирует оплату через RESTful-интерфейс. Баланс пользователя задается
  настройкой `config.init-balance` в [application.properties](payment-service/src/main/resources/application.properties)
  и сбрасывается до начального значения после рестарта

В качестве инфраструктуры используются следующие сервисы:

* `Postgres 18` - в качестве основной базы данных
* `Redis 8` - хранилище для кеша
* `Keycloak 26` - сервер аутентификации Oauth2 (`mvc-app` - клиент, `payment-service` - сервер ресурсов)
* `Docker Сompose` - для локального запуска всей ифраструктуры

Java, Spring Boot, Spring WebFlux, Thymeleaf, Spring Data R2DBC, Liquibase, Redis, Docker, OpenApi, Spring Security,
Keycloak

## Требования

* Java 21+
* Postgres 18+
* Redis 8+
* Keycloak 26+

Либо

* Docker

## Как собирать

1. Создать `.env` файл из шаблона [.env.example](.env.example) в корне проекта. Значения по-умолчанию можно оставить
2. `./gradlew clean build`

## Как запустить с помощью Docker

1. [Собрать приложение](#как-собирать)
2. `docker-compose up`
3. Перейти на `http://localhost:8080`

## Как запустить локально

1. [Собрать проект](#как-собирать)
2. Установить Redis
3. Установить Keycloak и импортировать realm из файла [realm-export.json](keycloak/realm-export.json)
4. Параметры подключений установить в `.env` файле
5. Запустить модули приложения:

* `./gradlew :payment-service:bootRun`
* `./gradlew :mvc-app:bootRun`

6. Перейти на `http://localhost:8080`

## Как тестировать

Юнит-тесты:

1. `./gradlew test`

Интеграционные тесты:

1. `./gradlew integrationTest`