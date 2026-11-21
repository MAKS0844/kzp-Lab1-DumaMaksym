"""Тести формування звіту та інтерфейсу командного рядка."""

from pathlib import Path

import pytest

from kzp_lab06.analysis import make_summary
from kzp_lab06.cli import main
from kzp_lab06.reporting import format_report


def test_format_report_with_empty_summary() -> None:
    """Перевіряє коректний текст звіту для порожнього набору даних."""
    summary = make_summary(
        [],
        "duration",
        "rating",
        duration="Сумарна тривалість",
        rating="Сума оцінок",
    )
    text = format_report(summary)

    assert "Усього фільмів: 0" in text
    assert "Найдовший фільм: немає даних" in text
    assert "Середня оцінка: 0.00" in text


def test_cli_generates_report_file(
    tmp_path: Path,
    capsys: pytest.CaptureFixture[str],
) -> None:
    """Перевіряє успішний цикл CLI: читання CSV і створення звіту."""
    csv_file = tmp_path / "movies.csv"
    csv_file.write_text(
        "title,director,year,duration,rating\n"
        "Довбуш,Олесь Санін,2023,122,8.0\n"
        "Поводир,Олесь Санін,2014,122,7.9\n"
        "Дюна: Частина друга,Дені Вільнев,2024,166,8.5\n",
        encoding="utf-8",
    )
    output_file = tmp_path / "nested" / "report.txt"

    exit_code = main(["--input", str(csv_file), "--output", str(output_file)])
    captured = capsys.readouterr()

    assert exit_code == 0
    assert output_file.exists()
    assert "Звіт успішно збережено" in captured.out

    content = output_file.read_text(encoding="utf-8")
    assert "Усього фільмів: 3" in content
    assert "Унікальних режисерів: 2" in content
    assert "Дюна: Частина друга (166 хв, 2024)" in content
    assert "  - Довбуш" in content
    assert "  - Поводир" not in content
    assert "  - Олесь Санін: 2" in content


def test_cli_returns_error_on_missing_file(
    tmp_path: Path,
    capsys: pytest.CaptureFixture[str],
) -> None:
    """Перевіряє код повернення 1 і повідомлення в stderr при відсутньому CSV."""
    missing_csv = tmp_path / "absent.csv"
    output_file = tmp_path / "report.txt"

    exit_code = main(["--input", str(missing_csv), "--output", str(output_file)])
    captured = capsys.readouterr()

    assert exit_code == 1
    assert "Помилка:" in captured.err
    assert not output_file.exists()


def test_cli_returns_error_on_corrupted_csv(
    tmp_path: Path,
    capsys: pytest.CaptureFixture[str],
) -> None:
    """Перевіряє код повернення 1 при пошкодженому числовому значенні."""
    bad_csv = tmp_path / "bad.csv"
    bad_csv.write_text(
        "title,director,year,duration,rating\nДовбуш,Олесь Санін,2023,abc,8.0\n",
        encoding="utf-8",
    )
    output_file = tmp_path / "report.txt"

    exit_code = main(["--input", str(bad_csv), "--output", str(output_file)])
    captured = capsys.readouterr()

    assert exit_code == 1
    assert "Рядок 2" in captured.err
    assert not output_file.exists()
