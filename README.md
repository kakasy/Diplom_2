# Diplom_2
Дипломный проект по тестированию API приложения по заказу бургеров Stellar Burgers.
## Описание

Версия Java 11
Проект использует следующие библиотеки:
- JUnit 4
- RestAssured
- Allure

## Документация

[Ссылка](https://code.s3.yandex.net/qa-automation-engineer/java/cheatsheets/paid-track/diplom/api-documentation.pdf) на документацию проекта.

### Запуск автотестов

Для запуска автотестов необходимо:

1. Скачать код проекта

 ```sh
   git clone https://github.com/kakasy/Diplom_2.git
   ```

2. Запустить автотесты

```sh
mvn clean test
```

3. Для создания Allure отчета ввести команду

```sh
mvn allure:report
```

4. Для просмотра в браузере информации из отчета ввести команду

```sh
allure serve target/surefire-reports/
```
