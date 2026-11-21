"""Тести функцій аналізу каталогу фільмів."""

import pytest

from kzp_lab06.analysis import (
    average_rating,
    count_by_director,
    filter_top_rated,
    longest_movie,
    make_summary,
    unique_directors,
)

SAMPLE_RECORDS: list[dict[str, object]] = [
    {
        "title": "Довбуш",
        "director": "Олесь Санін",
        "year": 2023,
        "duration": 122,
        "rating": 8.0,
    },
    {
        "title": "Поводир",
        "director": "Олесь Санін",
        "year": 2014,
        "duration": 122,
        "rating": 7.9,
    },
    {
        "title": "Дюна: Частина друга",
        "director": "Дені Вільнев",
        "year": 2024,
        "duration": 166,
        "rating": 8.5,
    },
]


def test_filter_top_rated_includes_boundary() -> None:
    """Перевіряє відбір фільмів із граничною оцінкою 8.0."""
    selected = filter_top_rated(SAMPLE_RECORDS, minimum_rating=8.0)
    titles = [record["title"] for record in selected]
    assert titles == ["Довбуш", "Дюна: Частина друга"]


def test_unique_directors_removes_duplicates() -> None:
    """Перевіряє формування множини унікальних режисерів."""
    assert unique_directors(SAMPLE_RECORDS) == {"Олесь Санін", "Дені Вільнев"}


def test_count_by_director_is_deterministic() -> None:
    """Перевіряє підрахунок кількості фільмів і сортування ключів."""
    counts = count_by_director(SAMPLE_RECORDS)
    assert list(counts.keys()) == ["Дені Вільнев", "Олесь Санін"]
    assert counts == {"Дені Вільнев": 1, "Олесь Санін": 2}


def test_average_rating_and_longest_movie() -> None:
    """Перевіряє середню оцінку та пошук найдовшого фільму."""
    assert average_rating(SAMPLE_RECORDS) == pytest.approx((8.0 + 7.9 + 8.5) / 3)
    longest = longest_movie(SAMPLE_RECORDS)
    assert longest is not None
    assert longest["title"] == "Дюна: Частина друга"


def test_empty_records_handling() -> None:
    """Перевіряє роботу функцій аналізу на порожньому списку."""
    assert filter_top_rated([]) == []
    assert unique_directors([]) == set()
    assert count_by_director([]) == {}
    assert average_rating([]) == 0.0
    assert longest_movie([]) is None


def test_make_summary_args_defaults_and_kwargs() -> None:
    """Перевіряє типові параметри, *args, **kwargs і порожній відбір."""
    summary = make_summary(
        SAMPLE_RECORDS,
        "duration",
        "rating",
        minimum_rating=8.0,
        duration="Сумарна тривалість",
        rating="Сума оцінок",
    )
    assert summary["total_count"] == 3
    assert summary["selected_count"] == 2
    assert summary["Сумарна тривалість"] == 288.0
    assert summary["Сума оцінок"] == pytest.approx(16.5)

    empty_selection = make_summary(
        SAMPLE_RECORDS,
        "duration",
        minimum_rating=9.5,
        duration="Сумарна тривалість",
    )
    assert empty_selection["selected_count"] == 0
    assert empty_selection["Сумарна тривалість"] == 0.0


def test_make_summary_rejects_unknown_metric() -> None:
    """Перевіряє явну помилку ValueError для невідомої метрики."""
    with pytest.raises(ValueError, match="Невідома метрика"):
        make_summary(SAMPLE_RECORDS, "budget")
