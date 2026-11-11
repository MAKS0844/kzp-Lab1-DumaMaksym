package ua.lpnu.kzp;

import java.util.Objects;

/** Описує плоский запис фільму для експорту в CSV. */
public final class MovieRecord {

    @CsvColumn("Назва")
    private final String title;

    @CsvColumn("Режисер")
    private final String director;

    @CsvColumn("Рік")
    private final int year;

    @CsvColumn("Тривалість")
    private final int durationMinutes;

    @CsvColumn("Оцінка")
    private final double rating;

    /** Створює перевірений запис фільму. */
    public MovieRecord(String title, String director, int year, int durationMinutes, double rating) {
        if (title == null || title.isBlank() || director == null || director.isBlank()) {
            throw new IllegalArgumentException("Назва та режисер не можуть бути порожніми");
        }
        if (year < 1888 || year > 2100) {
            throw new IllegalArgumentException("Некоректний рік випуску фільму");
        }
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Тривалість фільму має бути додатною");
        }
        if (rating < 0.0 || rating > 10.0) {
            throw new IllegalArgumentException("Оцінка має бути в межах від 0 до 10");
        }

        this.title = title;
        this.director = director;
        this.year = year;
        this.durationMinutes = durationMinutes;
        this.rating = rating;
    }

    /** Порівнює записи фільмів за всіма полями для тесту round-trip. */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MovieRecord record)) {
            return false;
        }
        return year == record.year
                && durationMinutes == record.durationMinutes
                && Double.compare(record.rating, rating) == 0
                && title.equals(record.title)
                && director.equals(record.director);
    }

    /** Повертає хеш-код, узгоджений із equals. */
    @Override
    public int hashCode() {
        return Objects.hash(title, director, year, durationMinutes, rating);
    }
}
