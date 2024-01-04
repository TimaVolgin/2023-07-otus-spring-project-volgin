# 2023-07-otus-spring-project-volgin

Магазин книг на микросервисной архитектуре.

Цель: Научиться строить архитектуру микросервисных приложений, изучить возможности keycloak, eureka, zuul.

Приложение для продажи книг.
Состоять будет из следующих сервисов:

Сервис работы с пользователями. Для авторизации пользователей и изменения их данных.
Сервис книг. Для хранения и управления товаром(книга).
Сервис заказов. Для оформления заказов.
Админка (Spring Shell, возможно Vue).

Технологии:
SpringBoot, Keycloak, Docker, Postgres, Swagger, Eureka, Zuul.


### Экспорт конфигурации keycloak
- Прописать volume: `"./keycloak/export.json:/tmp/export.json"`
- Запустить контейнер и подключиться к нему
- Выполнить /opt/jboss/keycloak/bin/standalone.sh -Djboss.socket.binding.port-offset=100 -Dkeycloak.migration.action=export -Dkeycloak.migration.provider=singleFile -Dkeycloak.migration.realmName=shop -Dkeycloak.migration.usersExportStrategy=REALM_FILE -Dkeycloak.migration.file=/tmp/export.json
