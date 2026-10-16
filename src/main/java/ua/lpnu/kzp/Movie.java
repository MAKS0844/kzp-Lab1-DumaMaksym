package ua.lpnu.kzp;

import java.util.Locale;
import java.util.Objects;

/**
 * Описує коректний запис про фільм.
 * Клас є незмінним (immutable).
 */
public final class Movie {
    private final String title;
    private final String director;
    private final int minutes;
    private final int year;
    private final double rating;

    /**
     * Створює коректний запис про фільм.
     *
     * @param title назва фільму
     * @param director ім'я режисера
     * @param minutes тривалість у хвилинах
     * @param year рік випуску
     * @param rating рейтинг фільму
     */
    public Movie(String title, String director, int minutes, int year, double rating) {
        // Просто перевіряємо, не присвоюючи одразу у поля
        Objects.requireNonNull(title, "Назва не може бути null");
        Objects.requireNonNull(director, "Режисер не може бути null");

        if (title.isBlank() || director.isBlank()) {
            throw new IllegalArgumentException("Назва та режисер не можуть бути порожніми");
        }
        if (minutes <= 0 || year <= 0) {
            throw new IllegalArgumentException("Тривалість та рік випуску мають бути більшими за нуль");
        }
        if (rating < 0 || !Double.isFinite(rating)) {
            throw new IllegalArgumentException("Рейтинг має бути не від'ємним і скінченним числом");
        }

        // Присвоюємо значення лише один раз
        this.title = title;
        this.director = director;
        this.minutes = minutes;
        this.year = year;
        this.rating = rating;
    }

    /**
     * Створює об'єкт фільму з CSV-рядка.
     *
     * @param line рядок у форматі: назва;режисер;тривалість;рік;рейтинг
     * @return створений об'єкт Movie
     */
    public static Movie fromCsv(String line) {
        Objects.requireNonNull(line, "Рядок не може бути null");
        String[] fields = line.split(";", -1);

        if (fields.length != 5) {
            throw new IllegalArgumentException("Очікується 5 полів");
        }

        try {
            return new Movie(
                    fields[0].trim(),
                    fields[1].trim(),
                    Integer.parseInt(fields[2].trim()),
                    Integer.parseInt(fields[3].trim()),
                    Double.parseDouble(fields[4].trim())
            );
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Числове поле має помилковий формат", exception);
        }
    }

    public String getTitle() { return title; }
    public String getDirector() { return director; }
    public int getMinutes() { return minutes; }
    public int getYear() { return year; }
    public double getRating() { return rating; }

    /** Повертає текстове подання фільму. */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s (%s): %d хв, %d рік, рейтинг: %.2f",
                title, director, minutes, year, rating);
    }
}
