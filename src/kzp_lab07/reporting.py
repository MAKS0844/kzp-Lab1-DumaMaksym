"""Формування та збереження текстового звіту для об'єкта CatalogSummary[cite: 29, 30, 32]."""

from pathlib import Path

from .errors import DataError
from .models import CatalogSummary


def format_report(summary: CatalogSummary) -> str:
    """Формує детермінований текстовий звіт із підсумку аналізу[cite: 29, 30]."""
    directors = sorted(summary.directors)
    lines = [
        "=== Звіт за каталогом фільмів (Варіант 8) ===",
        f"Усього фільмів: {summary.total_count}",
        f"Унікальних режисерів: {len(directors)}",
        f"Середня оцінка: {summary.average_rating:.2f}",
    ]

    if summary.longest_movie is not None:
        lines.append(
            f"Найдовший фільм: {summary.longest_movie.title} "
            f"({summary.longest_movie.duration_minutes} хв, {summary.longest_movie.release_year})"
        )
    else:
        lines.append("Найдовший фільм: немає даних")

    lines.append("")
    lines.append(f"Фільми з оцінкою >= {summary.minimum_rating:.1f} ({summary.selected_count}):")
    if summary.selected_titles:
        for title in summary.selected_titles:
            lines.append(f"  - {title}")
    else:
        lines.append("  - немає")

    if "Сумарна тривалість" in summary.extra_metrics:
        total_duration = int(summary.extra_metrics["Сумарна тривалість"])
        lines.append(f"Сумарна тривалість відібраних: {total_duration} хв")
    if "Сума оцінок" in summary.extra_metrics:
        total_rating = summary.extra_metrics["Сума оцінок"]
        lines.append(f"Сума оцінок відібраних: {total_rating:.2f}")

    lines.append("")
    lines.append("Кількість фільмів за режисерами:")
    if summary.count_by_director:
        for director, count in summary.count_by_director.items():
            lines.append(f"  - {director}: {count}")
    else:
        lines.append("  - немає")

    return "\n".join(lines) + "\n"


def write_report(path: Path, content: str) -> None:
    """Записує текстовий звіт у файл у кодуванні UTF-8[cite: 29, 30]."""
    try:
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(content, encoding="utf-8")
    except OSError as error:
        raise DataError(f"Не вдалося зберегти звіт у файл: {path}") from error
