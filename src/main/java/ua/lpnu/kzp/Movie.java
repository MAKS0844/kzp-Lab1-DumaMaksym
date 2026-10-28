package ua.lpnu.kzp;
import java.util.Objects;

/**
 * Базовий клас для всіх типів фільмів.
 * Містить спільні дані, необхідні для роботи Stream API.
 */
public abstract class Movie {
    // Спільні базові характеристики
    private final String title;
    private final int releaseYear;
    private final MovieKind kind;
    
    // Відновлені поля для виконання потокових запитів у Лабі №4
    private final String director;
    private final int duration;
    private final double rating;

    protected Movie(String title, int releaseYear, MovieKind kind, String director, int duration, double rating) {
        // Базова валідація
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Назва не може бути порожньою");
        }
        if (releaseYear < 1895) {
            throw new IllegalArgumentException("Рік випуску не може бути меншим за 1895");
        }
        if (kind == null) {
            throw new IllegalArgumentException("Категорія не може бути null");
        }
        
        // Перевірка додаткових полів
        if (director == null || director.isBlank()) {
            throw new IllegalArgumentException("Режисер не може бути порожнім");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Тривалість має бути більшою за нуль");
        }
        if (rating < 0 || !Double.isFinite(rating)) {
            throw new IllegalArgumentException("Рейтинг має бути коректним не від'ємним числом");
        }

        this.title = title;
        this.releaseYear = releaseYear;
        this.kind = kind;
        this.director = director;
        this.duration = duration;
        this.rating = rating;
    }

    public String getTitle() { return title; }
    public int getReleaseYear() { return releaseYear; }
    public MovieKind getKind() { return kind; }
    public String getDirector() { return director; }
    public int getDuration() { return duration; }
    public double getRating() { return rating; }

    // Поліморфний метод для розрахунку рекомендаційного бала
    public abstract double calculateRecommendationScore();

    @Override
    public final boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Movie movie = (Movie) other;
        return releaseYear == movie.releaseYear && title.equals(movie.title);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass(), title, releaseYear);
    }

    // Заглушка для SpotBugs (CT_CONSTRUCTOR_THROW)
    @Override
    @SuppressWarnings({"deprecation", "removal"})
    protected final void finalize() { }
}
