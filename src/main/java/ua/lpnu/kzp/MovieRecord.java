package ua.lpnu.kzp;

import java.util.List;
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

    /** Розбирає один рядок у порядку полів експорту. */
    public static MovieRecord fromCsv(String line) throws DataStorageException {
        List<String> fields = CsvParser.parse(line);
        return fromFields(fields);
    }

    /* Створює запис із п'яти розібраних CSV-полів (в алфавітному порядку полів Java). */
    static MovieRecord fromFields(List<String> fields) throws DataStorageException {
        if (fields.size() != 5) {
            throw new DataStorageException("Очікується п'ять полів CSV");
        }
        try {
            // Порядок колонок у CSV формується рефлексією за алфавітом назв змінних:
            // 0: director, 1: durationMinutes, 2: rating, 3: title, 4: year
            return new MovieRecord(
                    fields.get(3),                           // title
                    fields.get(0),                           // director
                    Integer.parseInt(fields.get(4)),         // year
                    Integer.parseInt(fields.get(1)),         // durationMinutes
                    Double.parseDouble(fields.get(2))        // rating
            );
        } catch (IllegalArgumentException exception) {
            throw new DataStorageException("Некоректний рядок фільму", exception);
        }
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
