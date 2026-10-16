package ua.lpnu.kzp;

import java.util.Locale;
import java.util.Objects;

/**
 * Описує коректний запис про фільм.
 * Клас є повністю незмінним (immutable).
 */
public final class Movie {
    private final String title;
    private final String director;
    private final int minutes;
    private final int year;
    private final double rating;

    // Конструктор тепер приватний. Об'єкти створюються ТІЛЬКИ через Builder або fromCsv.
    private Movie(Builder builder) {
        // Перевіряємо інваріанти в самому конструкторі, щоб Builder не міг їх обійти
        Objects.requireNonNull(builder.title, "Назва не може бути null");
        Objects.requireNonNull(builder.director, "Режисер не може бути null");

        if (builder.title.isBlank() || builder.director.isBlank()) {
            throw new IllegalArgumentException("Назва та режисер не можуть бути порожніми");
        }
        if (builder.minutes <= 0 || builder.year <= 0) {
            throw new IllegalArgumentException("Тривалість та рік випуску мають бути більшими за нуль");
        }
        if (builder.rating < 0 || !Double.isFinite(builder.rating)) {
            throw new IllegalArgumentException("Рейтинг має бути не від'ємним і скінченним числом");
        }

        this.title = builder.title;
        this.director = builder.director;
        this.minutes = builder.minutes;
        this.year = builder.year;
        this.rating = builder.rating;
    }

    public static Movie fromCsv(String line) {
        Objects.requireNonNull(line, "Рядок не може бути null");
        String[] fields = line.split(";", -1);

        if (fields.length != 5) {
            throw new IllegalArgumentException("Очікується 5 полів");
        }

        try {
            // Використовуємо Builder всередині фабрики
            return new Movie.Builder()
                    .title(fields[0].trim())
                    .director(fields[1].trim())
                    .minutes(Integer.parseInt(fields[2].trim()))
                    .year(Integer.parseInt(fields[3].trim()))
                    .rating(Double.parseDouble(fields[4].trim()))
                    .build();
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Числове поле має помилковий формат", exception);
        }
    }

    public String getTitle() { return title; }
    public String getDirector() { return director; }
    public int getMinutes() { return minutes; }
    public int getYear() { return year; }
    public double getRating() { return rating; }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s (%s): %d хв, %d рік, рейтинг: %.2f",
                title, director, minutes, year, rating);
    }

    /**
     * Статичний внутрішній клас Builder для покрокового створення об'єкта Movie.
     */
    public static final class Builder {
        private String title;
        private String director;
        private int minutes;
        private int year;
        private double rating;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder director(String director) {
            this.director = director;
            return this;
        }

        public Builder minutes(int minutes) {
            this.minutes = minutes;
            return this;
        }

        public Builder year(int year) {
            this.year = year;
            return this;
        }

        public Builder rating(double rating) {
            this.rating = rating;
            return this;
        }

        public Movie build() {
            // Виклик приватного конструктора Movie, де і відбудеться валідація
            return new Movie(this);
        }
    }
}
