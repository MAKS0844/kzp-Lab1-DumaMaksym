# KZP Lab 06 — Каталог фільмів (Варіант 8)

## Предметна область та Варіант 8

Програма зчитує CSV-файл із даними про фільми (`title`, `director`, `year`, `duration`, `rating`), виконує валідацію значень, аналізує каталог і формує текстовий звіт:

- відбір фільмів із рейтингом `rating >= 8.0` (з можливістю змінити поріг через `--min-rating`);
- обчислення множини унікальних режисерів та кількості фільмів для кожного режисера;
- визначення середньої оцінки та найдовшого фільму;
- формування підсумку з використанням типового параметра, `*args` та `**kwargs`.

## Структура проєкту

```text
.
├── .github/workflows/
│   └── python-ci.yml
├── data/
│   └── movies.csv
├── reports/
│   └── report.txt
├── src/
│   └── kzp_lab06/
│       ├── __init__.py
│       ├── errors.py
│       ├── loader.py
│       ├── analysis.py
│       ├── reporting.py
│       └── cli.py
├── tests/
│   ├── test_loader.py
│   ├── test_analysis.py
│   └── test_reporting_cli.py
├── .editorconfig
├── .gitignore
├── pyproject.toml
├── README.md
└── REPORT.md