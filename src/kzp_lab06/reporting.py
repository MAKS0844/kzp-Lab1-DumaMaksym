"""Формування та збереження текстового звіту каталогу фільмів."""

from pathlib import Path

from .errors import DataError


def format_report(summary: dict[str, object]) -> str:
    """Перетворює словник підсумків аналізу на структурований текстовий звіт."""
    total_count = int(summary["total_count"])
    selected_count = int(summary["selected_count"])
    minimum_rating = float(summary["minimum_rating"])
    avg_rating = float(summary["average_rating"])
    selected_titles = list(summary["selected_titles"])
    directors = sorted(str(name) for name in summary["directors"])
    counts = dict(summary["count_by_director"])
    longest = summary["longest_movie"]

    lines = [
        "=== Звіт за каталогом фільмів (Варіант 8) ===",
        f"Усього фільмів: {total_count}",
        f"Унікальних режисерів: {len(directors)}",
        f"Середня оцінка: {avg_rating:.2f}",
    ]

    if isinstance(longest, dict):
        lines.append(
            f"Найдовший фільм: {longest['title']} ({longest['duration']} хв, {longest['year']})"
        )
    else:
        lines.append("Найдовший фільм: немає даних")

    lines.append("")
    lines.append(f"Фільми з оцінкою >= {minimum_rating:.1f} ({selected_count}):")
    if selected_titles:
        for title in selected_titles:
            lines.append(f"  - {title}")
    else:
        lines.append("  - немає")

    if "Сумарна тривалість" in summary:
        total_duration = int(float(summary["Сумарна тривалість"]))
        lines.append(f"Сумарна тривалість відібраних: {total_duration} хв")
    if "Сума оцінок" in summary:
        total_rating = float(summary["Сума оцінок"])
        lines.append(f"Сума оцінок відібраних: {total_rating:.2f}")

    lines.append("")
    lines.append("Кількість фільмів за режисерами:")
    if counts:
        for director, count in counts.items():
            lines.append(f"  - {director}: {count}")
    else:
        lines.append("  - немає")

    return "\n".join(lines) + "\n"


def write_report(path: Path, content: str) -> None:
    """Записує текст звіту у файл у кодуванні UTF-8."""
    try:
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(content, encoding="utf-8")
    except OSError as error:
        raise DataError(f"Не вдалося зберегти звіт у файл: {path}") from error
