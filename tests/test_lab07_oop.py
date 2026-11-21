"""Поведінкові тести об'єктної моделі, аналізу та CLI для лабораторної роботи № 7."""

import dataclasses
from pathlib import Path

import pytest

from kzp_lab07.analysis import (
    average_rating,
    count_by_director,
    filter_top_rated,
    longest_movie,
    summarize_catalog,
    top_scored_movie,
    unique_directors,
)
from kzp_lab07.cli import main as cli_main
from kzp_lab07.errors import DataError
from kzp_lab07.loader import load_movies
from kzp_lab07.models import CatalogSummary, Documentary, FeatureFilm, Recommendable
from kzp_lab07.reporting import format_report, write_report


def test_property_and_setter_validation() -> None:
    """Перевіряє коректні, граничні та хибні значення властивостей Movie."""
    movie = FeatureFilm("Довбуш", "Олесь Санін", 2023, 122, 8.0)
    assert movie.rating == 8.0
    assert movie.duration_minutes == 122

    # Граничні та коректні оновлення через setter
    movie.rating = 0.0
    assert movie.rating == 0.0
    movie.rating = 10.0
    assert movie.rating == 10.0
    movie.duration_minutes = 1
    assert movie.duration_minutes == 1

    # Помилкові значення мають відхилятися з ValueError
    with pytest.raises(ValueError):
        movie.rating = -0.1
    with pytest.raises(ValueError):
        movie.rating = 10.5
    with pytest.raises(ValueError):
        movie.duration_minutes = 0
    with pytest.raises(ValueError):
        movie.release_year = 1800
    with pytest.raises(ValueError):
        FeatureFilm("   ", "Режисер", 2020, 100, 7.0)


def test_subtypes_different_scores_and_protocol() -> None:
    """Перевіряє різні формули підтипів і роботу аналізу через протокол."""
    feature = FeatureFilm("Дюна", "Дені Вільнев", 2021, 155, 8.2)
    doc = Documentary("Документальний фільм про Карпати", "Олесь Санін", 2022, 100, 8.2)

    assert isinstance(feature, Recommendable)
    assert isinstance(doc, Recommendable)
    assert feature.recommendation_score() != doc.recommendation_score()

    items: list[Recommendable] = [feature, doc]
    best = top_scored_movie(items)
    assert best == doc
    assert len(filter_top_rated(items, minimum_rating=8.0)) == 2
    assert unique_directors(items) == frozenset({"Дені Вільнев", "Олесь Санін"})
    assert count_by_director(items) == {"Дені Вільнев": 1, "Олесь Санін": 1}
    assert average_rating(items) == pytest.approx(8.2)
    assert longest_movie(items) == feature


def test_dunder_methods_and_frozen_dataclass() -> None:
    """Перевіряє __str__, __repr__, __eq__ та незмінність CatalogSummary."""
    m1 = FeatureFilm("Поводир", "Олесь Санін", 2014, 122, 7.9)
    m2 = FeatureFilm("Поводир", "Олесь Санін", 2014, 122, 7.9)
    same_title_other_type = Documentary("Поводир", "Олесь Санін", 2014, 122, 7.9)

    assert m1 == m2
    assert m1 != same_title_other_type
    assert "Поводир" in str(m1)
    assert "FeatureFilm" in repr(m1)

    summary = CatalogSummary(
        total_count=1,
        selected_count=0,
        minimum_rating=8.0,
        selected_titles=(),
        directors=frozenset({"Олесь Санін"}),
        count_by_director={"Олесь Санін": 1},
        average_rating=7.9,
        longest_movie=m1,
        top_scored_movie=m1,
        extra_metrics={"Сумарна тривалість": 0.0},
    )
    with pytest.raises(dataclasses.FrozenInstanceError):
        summary.total_count = 5  # type: ignore[misc]


def test_loader_empty_and_invalid_rows(tmp_path: Path) -> None:
    """Перевіряє порожній файл, файл лише із заголовком та помилкові рядки з номером рядка."""
    empty_file = tmp_path / "empty.csv"
    empty_file.write_text("", encoding="utf-8")
    with pytest.raises(DataError):
        load_movies(empty_file)

    header_only = tmp_path / "header_only.csv"
    header_only.write_text("title,director,year,duration,rating\n", encoding="utf-8")
    assert load_movies(header_only) == []

    bad_row = tmp_path / "bad_row.csv"
    bad_row.write_text(
        "title,director,year,duration,rating\n"
        '"Гаррі Поттер, частина 1",Кріс Коламбус,2001,152,7.6\n'
        "Помилковий фільм,Режисер,2020,не_число,8.0\n",
        encoding="utf-8",
    )
    with pytest.raises(DataError, match="Рядок 3"):
        load_movies(bad_row)


def test_reporting_and_cli_success_and_error(
    tmp_path: Path,
    capsys: pytest.CaptureFixture[str],
) -> None:
    """Перевіряє генерацію звіту, порожній підсумок та коди повернення CLI."""
    empty_summary = summarize_catalog([])
    empty_report = format_report(empty_summary)
    assert "Усього фільмів: 0" in empty_report
    assert "Найдовший фільм: немає даних" in empty_report

    csv_path = tmp_path / "movies.csv"
    csv_path.write_text(
        "title,director,year,duration,rating\n"
        "Довбуш,Олесь Санін,2023,122,8.0\n"
        "Дюна: Частина друга,Дені Вільнев,2024,166,8.5\n",
        encoding="utf-8",
    )
    report_path = tmp_path / "out" / "report.txt"
    exit_code = cli_main(["--input", str(csv_path), "--output", str(report_path)])
    assert exit_code == 0
    assert report_path.exists()
    write_report(report_path, format_report(summarize_catalog(load_movies(csv_path))))
    saved_text = report_path.read_text(encoding="utf-8")
    assert "Усього фільмів: 2" in saved_text

    missing_code = cli_main(
        ["--input", str(tmp_path / "missing.csv"), "--output", str(report_path)]
    )
    captured = capsys.readouterr()
    assert missing_code == 1
    assert "Помилка:" in captured.err
