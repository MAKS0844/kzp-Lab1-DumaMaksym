package ua.lpnu.kzp;

import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** 
 * Клас для потокової обробки каталогу фільмів.
 * Усі методи чисті (не змінюють вхідний список).
 */
public final class MovieReport {
    
    // Утилітний клас не повинен мати публічного конструктора
    private MovieReport() {
    }

    /** 1. Відбір (filter): залишаємо тільки фільми з оцінкою 8.0 і вище. */
    public static List<Movie> getHighlyRatedMovies(List<Movie> movies) {
        return streamOf(movies)
                .filter(movie -> movie.getRating() >= 8.0)
                .toList(); // toList() створює незмінний список (Java 16+)
    }

    /** 2. Перетворення (map): витягуємо зі списку об'єктів лише назви фільмів. */
    public static List<String> getTitles(List<Movie> movies) {
        return streamOf(movies)
                .map(Movie::getTitle)
                .toList();
    }

    /** 3. Групування (groupingBy): рахуємо середній рейтинг для кожного режисера. */
    public static Map<String, Double> getAverageRatingByDirector(List<Movie> movies) {
        return streamOf(movies)
                .collect(Collectors.groupingBy(
                        Movie::getDirector,
                        Collectors.averagingDouble(Movie::getRating)
                ));
    }

    /** 4. Статистика (summarizing): збираємо мінімум, максимум, суму та середнє по тривалості. */
    public static IntSummaryStatistics getDurationStatistics(List<Movie> movies) {
        return streamOf(movies)
                .collect(Collectors.summarizingInt(Movie::getDuration));
    }

    /** 5. Top-N (складений компаратор + limit): найкращі фільми за оцінкою, при рівності - за назвою. */
    public static List<String> getTopMovies(List<Movie> movies, int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Ліміт не може бути від'ємним");
        }
        
        // Спочатку сортуємо за рейтингом (спадання), а потім за назвою (зростання)
        Comparator<Movie> byRatingDescAndTitle = Comparator
                .comparingDouble(Movie::getRating)
                .reversed()
                .thenComparing(Movie::getTitle);

        return streamOf(movies)
                .sorted(byRatingDescAndTitle)
                .limit(limit)
                .map(Movie::getTitle)
                .toList();
    }

    /** 6. Пошук (Optional): безпечно шукаємо фільм за назвою без ризику отримати null. */
    public static Optional<Movie> findByTitle(List<Movie> movies, String title) {
        Objects.requireNonNull(title, "Назва не може бути null");
        return streamOf(movies)
                .filter(movie -> movie.getTitle().equals(title))
                .findFirst();
    }

    // Допоміжний метод для швидкої перевірки на null і відкриття потоку
    private static Stream<Movie> streamOf(List<Movie> movies) {
        return Objects.requireNonNull(movies, "Список фільмів не може бути null").stream();
    }
}
