"""Функції аналізу об'єктів каталогу фільмів через спільний протокол (Варіант 8)."""

from collections.abc import Callable, Iterable

from .models import CatalogSummary, Recommendable

METRIC_GETTERS: dict[str, Callable[[Recommendable], float]] = {
    "duration": lambda item: float(item.duration_minutes),
    "rating": lambda item: float(item.rating),
    "score": lambda item: float(item.recommendation_score()),
}


def filter_top_rated(
    items: Iterable[Recommendable],
    minimum_rating: float = 8.0,
) -> list[Recommendable]:
    """Відбирає об'єкти з оцінкою не нижчою за заданий поріг[cite: 29, 30]."""
    return [item for item in items if item.rating >= minimum_rating]


def unique_directors(items: Iterable[Recommendable]) -> frozenset[str]:
    """Повертає незмінну множину унікальних режисерів[cite: 29, 30]."""
    return frozenset(item.director for item in items)


def count_by_director(items: list[Recommendable]) -> dict[str, int]:
    """Підраховує кількість фільмів для кожного режисера у стабільному порядку[cite: 29, 30]."""
    directors = sorted(unique_directors(items))
    return {
        director: sum(1 for item in items if item.director == director) for director in directors
    }


def average_rating(items: list[Recommendable]) -> float:
    """Обчислює середню оцінку фільмів або 0.0 для порожнього списку[cite: 29, 30]."""
    if not items:
        return 0.0
    return sum(item.rating for item in items) / len(items)


def longest_movie(items: list[Recommendable]) -> Recommendable | None:
    """Повертає найдовший фільм або None для порожнього списку[cite: 29, 30]."""
    if not items:
        return None
    return min(items, key=lambda item: (-item.duration_minutes, item.title))


def top_scored_movie(items: list[Recommendable]) -> Recommendable | None:
    """Повертає фільм із найбільшим балом recommendation_score()[cite: 26, 29, 30]."""
    if not items:
        return None
    return max(items, key=lambda item: item.recommendation_score())


def summarize_catalog(
    items: list[Recommendable],
    *metrics: str,
    minimum_rating: float = 8.0,
    **labels: str,
) -> CatalogSummary:
    """Формує незмінний підсумок CatalogSummary за вибраними метриками[cite: 29, 30, 32]."""
    selected = filter_top_rated(items, minimum_rating=minimum_rating)
    extra_metrics: dict[str, float] = {}

    for metric in metrics:
        getter = METRIC_GETTERS.get(metric)
        if getter is None:
            raise ValueError(f"Невідома метрика: {metric}")
        label = labels.get(metric, metric)
        extra_metrics[label] = sum(getter(item) for item in selected)

    return CatalogSummary(
        total_count=len(items),
        selected_count=len(selected),
        minimum_rating=minimum_rating,
        selected_titles=tuple(sorted(item.title for item in selected)),
        directors=unique_directors(items),
        count_by_director=count_by_director(items),
        average_rating=average_rating(items),
        longest_movie=longest_movie(items),
        top_scored_movie=top_scored_movie(items),
        extra_metrics=extra_metrics,
    )
