# Звіт з лабораторної роботи №1
Студент: Дума Максим  
Національний університет «Львівська політехніка»

## 1. Мета роботи
Ознайомитися з основами розробки на Java, налаштувати систему збірки Maven, систему контролю версій Git та процес безперервної інтеграції (CI/CD) за допомогою GitHub Actions.

## 2. Структура проєкту
Проєкт побудовано за стандартною структурою Maven:
* `src/main/java/ua/lpnu/kzp/` — вихідний код програми (класи `Main`, `Movie`, `FileReport`).
* `src/test/java/ua/lpnu/kzp/` — код модульних тестів (клас `MainTest`).
* `.github/workflows/` — конфігурація CI/CD пайплайну (`ci.yml`).

## 3. Виконані завдання (Issues)
| Завдання | Гілка | Статус |
| :--- | :--- | :--- |
| #1 Створити структуру проєкту | `feature/init-project` | Злито |
| #2 Написати логіку парсингу | `feature/parsing` | Злито |
| #5 Сформувати звіт | `feature/file-report` | Злито |
| #4 Налаштувати GitHub Actions | `infra/github-actions`| Злито |
| #3 Написати тести | `feature/tests` | Злито |

## 4. Результати тестування
Програма успішно проходить перевірку та виконує всі JUnit тести. CI/CD пайплайн налаштовано для автоматичної перевірки коду на операційних системах Ubuntu, Windows та macOS.

<img width="1357" height="262" alt="Знімок екрана 2026-09-23 163054" src="https://github.com/user-attachments/assets/d44f9906-d58d-4345-bc1d-00cd68983397" />

<img width="1349" height="803" alt="Знімок екрана 2026-09-24 134423" src="https://github.com/user-attachments/assets/9d88451a-747b-4c8d-ba15-ca1201a2fb3f" />

<img width="957" height="301" alt="Знімок екрана 2026-09-23 204605" src="https://github.com/user-attachments/assets/4b39e1cf-6fa8-48b5-b9ea-eae2730f7499" />

<img width="520" height="161" alt="Знімок екрана 2026-09-24 134957" src="https://github.com/user-attachments/assets/67125595-1b5a-45df-8839-7dbd38967415" />

<img width="1213" height="589" alt="Знімок екрана 2026-10-12 214058" src="https://github.com/user-attachments/assets/215e71d6-48fd-48f0-992e-852e57324b19" />
