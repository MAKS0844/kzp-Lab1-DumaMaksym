# kzp-oop-report — Об'єктно-орієнтований аналіз каталогу фільмів (Варіант 8)

Пакет `kzp_lab07` реалізує лабораторну роботу № 7 з курсу «Кросплатформні засоби програмування» (Національний університет «Львівська політехніка», кафедра ЕОМ).

Проєкт переводить обробку CSV-каталогу фільмів (`data/movies.csv`) зі словникових записів на об'єктно-орієнтовану модель з інкапсульованим станом, перевіркою інваріантів через `@property`, поліморфними підтипами (`FeatureFilm`, `Documentary`), спільним протоколом `Recommendable` та незмінним підсумком `CatalogSummary` (`@dataclass(frozen=True)`).

## Структура проєкту

```text
src/
└── kzp_lab07/
    ├── __init__.py     # Ініціалізація пакета
    ├── errors.py       # Власний виняток DataError
    ├── models.py       # Recommendable, Movie, FeatureFilm, Documentary, CatalogSummary
    ├── loader.py       # Читання CSV у UTF-8 та перетворення рядків на об'єкти
    ├── analysis.py     # Поліморфний аналіз через спільний протокол
    ├── reporting.py    # Формування та збереження текстового звіту
    └── cli.py          # Інтерфейс командного рядка (kzp-oop-report)
tests/
└── test_lab07_oop.py   # Поведінкові тести pytest
```

## Встановлення та запуск

```bash
python -m pip install -e ".[dev]"
kzp-oop-report --input data/movies.csv --output reports/report_lab07.txt
```

### Додаткові параметри CLI

* `-i`, `--input` — шлях до вхідного CSV-файлу (типово `data/movies.csv`);
* `-o`, `--output` — шлях до вихідного звіту (типово `reports/report.txt`);
* `--min-rating` — поріг оцінки для відбору фільмів (типово `8.0`).

## Тестування, якість коду та збірка wheel

```bash
python -m pytest -v
ruff check
ruff format --check
python -m build --wheel
```