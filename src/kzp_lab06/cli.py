"""Інтерфейс командного рядка для генерації звіту каталогу фільмів."""

import argparse
import sys
from pathlib import Path

from .analysis import make_summary
from .errors import DataError
from .loader import load_records
from .reporting import format_report, write_report


def build_parser() -> argparse.ArgumentParser:
    """Створює парсер аргументів командного рядка."""
    parser = argparse.ArgumentParser(
        prog="kzp-lab06",
        description="Аналіз CSV-каталогу фільмів і формування текстового звіту.",
    )
    parser.add_argument(
        "-i",
        "--input",
        type=Path,
        default=Path("data/movies.csv"),
        help="Шлях до вхідного CSV-файлу (типово: data/movies.csv).",
    )
    parser.add_argument(
        "-o",
        "--output",
        type=Path,
        default=Path("reports/report.txt"),
        help="Шлях до вихідного текстового звіту (типово: reports/report.txt).",
    )
    parser.add_argument(
        "--min-rating",
        type=float,
        default=8.0,
        help="Мінімальна оцінка для відбору фільмів (типово: 8.0).",
    )
    return parser


def main(argv: list[str] | None = None) -> int:
    """Запускає повний конвеєр: завантаження -> аналіз -> запис звіту."""
    parser = build_parser()
    args = parser.parse_args(argv)

    try:
        records = load_records(args.input)
        summary = make_summary(
            records,
            "duration",
            "rating",
            minimum_rating=args.min_rating,
            duration="Сумарна тривалість",
            rating="Сума оцінок",
        )
        report_text = format_report(summary)
        write_report(args.output, report_text)
    except DataError as error:
        print(f"Помилка: {error}", file=sys.stderr)
        return 1

    print(f"Звіт успішно збережено: {args.output}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
