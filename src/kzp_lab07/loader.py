"""Завантаження та валідація CSV-каталогу фільмів у об'єкти моделі[cite: 29, 30, 32]."""

import csv
from pathlib import Path

from .errors import DataError
from .models import Documentary, FeatureFilm, Movie

EXPECTED_COLUMNS = {"title", "director", "year", "duration", "rating"}


def _parse_int_field(row: dict[str, str | None], field: str, row_number: int) -> int:
    """Зчитує та перевіряє цілочисельне поле рядка CSV[cite: 29, 32]."""
    raw = row.get(field)
    if raw is None or not raw.strip():
        raise DataError(f"Рядок {row_number}: порожнє значення у полі '{field}'")
    try:
        return int(raw.strip())
    except ValueError as error:
        raise DataError(f"Рядок {row_number}: поле '{field}' має бути цілим числом") from error


def _parse_float_field(row: dict[str, str | None], field: str, row_number: int) -> float:
    """Зчитує та перевіряє дійсне поле рядка CSV[cite: 29, 32]."""
    raw = row.get(field)
    if raw is None or not raw.strip():
        raise DataError(f"Рядок {row_number}: порожнє значення у полі '{field}'")
    try:
        return float(raw.strip())
    except ValueError as error:
        raise DataError(f"Рядок {row_number}: поле '{field}' має бути числом") from error


def _build_movie(row: dict[str, str | None], row_number: int) -> Movie:
    """Перетворює валідний рядок CSV на об'єкт FeatureFilm або Documentary[cite: 29, 30, 32]."""
    if None in row or len(row) != len(EXPECTED_COLUMNS):
        raise DataError(f"Рядок {row_number}: кількість полів не відповідає заголовку")

    raw_title = row.get("title")
    raw_director = row.get("director")
    if raw_title is None or not raw_title.strip():
        raise DataError(f"Рядок {row_number}: порожнє значення у полі 'title'")
    if raw_director is None or not raw_director.strip():
        raise DataError(f"Рядок {row_number}: порожнє значення у полі 'director'")

    year = _parse_int_field(row, "year", row_number)
    duration = _parse_int_field(row, "duration", row_number)
    rating = _parse_float_field(row, "rating", row_number)

    title = raw_title.strip()
    director = raw_director.strip()

    try:
        if "документ" in title.lower():
            return Documentary(title, director, year, duration, rating)
        return FeatureFilm(title, director, year, duration, rating)
    except ValueError as error:
        raise DataError(f"Рядок {row_number}: {error}") from error


def load_movies(path: Path) -> list[Movie]:
    """Завантажує список об'єктів Movie із CSV-файлу у кодуванні UTF-8[cite: 29, 30, 32]."""
    if not path.exists():
        raise DataError(f"Файл не знайдено: {path}")
    if not path.is_file():
        raise DataError(f"Шлях не є файлом: {path}")

    try:
        with path.open("r", encoding="utf-8", newline="") as csv_file:
            reader = csv.DictReader(csv_file)
            if reader.fieldnames is None:
                raise DataError("CSV-файл порожній або не містить заголовка")

            actual_columns = {col.strip() for col in reader.fieldnames if col is not None}
            if actual_columns != EXPECTED_COLUMNS or len(reader.fieldnames) != len(
                EXPECTED_COLUMNS
            ):
                raise DataError(
                    f"Некоректний набір стовпців CSV. Очікується: {sorted(EXPECTED_COLUMNS)}"
                )

            movies: list[Movie] = []
            for row_number, row in enumerate(reader, start=2):
                movies.append(_build_movie(row, row_number))
            return movies
    except DataError:
        raise
    except UnicodeDecodeError as error:
        raise DataError(f"Помилка кодування UTF-8 у файлі: {path}") from error
    except csv.Error as error:
        raise DataError(f"Помилка формату CSV у файлі {path}: {error}") from error
    except OSError as error:
        raise DataError(f"Не вдалося прочитати файл: {path}") from error
