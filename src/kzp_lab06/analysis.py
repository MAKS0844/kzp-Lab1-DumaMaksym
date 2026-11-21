"""Функції аналізу записів каталогу фільмів (Варіант 8)."""

from collections.abc import Iterable

NUMERIC_METRICS = {"year", "duration", "rating"}


def filter_top_rated(
    records: Iterable[dict[str, object]],
    minimum_rating: float = 8.0,
) -> list[dict[str, object]]:
    """Відбирає фільми з оцінкою не нижчою за заданий поріг."""
    return [record for record in records if float(record["rating"]) >= minimum_rating]


def unique_directors(records: Iterable[dict[str, object]]) -> set[str]:
    """Повертає множину унікальних режисерів."""
    return {str(record["director"]) for record in records}


def count_by_director(records: list[dict[str, object]]) -> dict[str, int]:
    """Обчислює кількість фільмів для кожного режисера у стабільному порядку."""
    directors = unique_directors(records)
    return {
        director: sum(1 for record in records if record["director"] == director)
        for director in sorted(directors)
    }


def average_rating(records: list[dict[str, object]]) -> float:
    """Обчислює середню оцінку фільмів або 0.0 для порожнього списку."""
    if not records:
        return 0.0
    return sum(float(record["rating"]) for record in records) / len(records)


def longest_movie(records: list[dict[str, object]]) -> dict[str, object] | None:
    """Повертає найдовший фільм або None для порожнього списку."""
    if not records:
        return None
    return min(
        records,
        key=lambda record: (-int(record["duration"]), str(record["title"])),
    )


def make_summary(
    records: list[dict[str, object]],
    *metrics: str,
    minimum_rating: float = 8.0,
    **labels: str,
) -> dict[str, object]:
    """Формує підсумок аналізу та суми вибраних метрик для відібраних фільмів."""
    selected = filter_top_rated(records, minimum_rating=minimum_rating)
    result: dict[str, object] = {
        "total_count": len(records),
        "selected_count": len(selected),
        "minimum_rating": minimum_rating,
        "selected_titles": sorted(str(record["title"]) for record in selected),
        "directors": unique_directors(records),
        "count_by_director": count_by_director(records),
        "average_rating": average_rating(records),
        "longest_movie": longest_movie(records),
    }

    for metric in metrics:
        if metric not in NUMERIC_METRICS:
            raise ValueError(f"Невідома метрика: {metric}")
        label = labels.get(metric, metric)
        result[label] = sum(float(record[metric]) for record in selected)

    return result
