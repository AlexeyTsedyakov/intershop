# intershop

Демо-проект простого интернет-магазина. Можно регистрировать пользователей, добавлять товары в корзину и оформлять
заказ, заводить новые товары.

Проект состоит из двух модулей:

* `mvc-app` - Витрина магазина
* `payment-service` - Мок-сервис, который эмулирует оплату через RESTful-интерфейс. Баланс пользователя задается настройкой `config.init-balance` в [application.properties](payment-service/src/main/resources/application.properties) и сбрасывается до начального значения после рестарта

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
2. [Настроить Keycloak](#настройка-keycloak)
3. `docker-compose up`
4. Перейти на `http://localhost:8080`

## Как запустить локально

1. [Собрать проект](#как-собирать)
2. Запустить модули проекта
3. Перейти на `http://localhost:8080`

### payment-service

1.`./gradlew :payment-service:bootRun`

### mvc-app

Настройки подключений берем из .env файла

1. Создать базу `intershop`
2. Подготовить Redis
3. [Настроить Keycloak](#настройка-keycloak)
4. `./gradlew :mvc-app:bootRun`

## Как тестировать

Юнит-тесты:

1. `./gradlew test`

Интеграционные тесты:

1. `./gradlew integrationTest`

## Настройка Keycloak

В фигурных скобках - {`env_name`:`default_value`}

1. Запускаем изолировано Keycloak: `docker compose up keycloak`
2. Переходим на `http://localhost:{KC_HTTP_PORT:8088}/admin`
3. Заходим под логином и паролем:
    - `{KC_BOOTSTRAP_ADMIN_USERNAME:admin}`
    - `{KC_BOOTSTRAP_ADMIN_PASSWORD:admin}`
4. Создаём клиента `mvc-app`:
    1. Переходим на вкладку **Clients**.
    2. Нажимаем **Create client**.
    3. Указываем:
        - **Client ID**: `mvc-app`
        - **Name**: `mvc-app`

       Остальное не меняем. Нажимаем **Next**.
    4. Включаем:
        - **Client authentication**
        - **Service account roles**

       Остальное выключаем. Нажимаем **Next**.
    5. Ничего не меняем. Нажимаем **Save**.
5. Получаем секрет клиента
    1. Переходим на вкладку **Clients**
    2. Выбираем клиента **mvc-app**
    3. Переходим на вкладку **Credentials**
    4. Копируем поле **Client Secret** и сохраняем значение в поле `KC_CLIENT_SECRET` .env файла. Либо
       сохраняем в одноименную переменную окружения