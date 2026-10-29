package ua.lpnu.kzp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.IntSummaryStatistics; // Додано для статистики тривалості
import java.util.List;
import java.util.Locale;

public final class MovieController {

    public void process(String inputPath, String outputPath) throws IOException {
        System.out.println("Читаємо файл: " + inputPath);
        
        List<Movie> movies = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(inputPath));

        // 1. Читання та розгалуження (Фабрична логіка)
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(";");
            if (parts.length < 5) {
                System.out.println("Пропущено неповний рядок: " + line);
                continue;
            }

            try {
                String title = parts[0].trim();
                String director = parts[1].trim();
                int duration = Integer.parseInt(parts[2].trim());
                int year = Integer.parseInt(parts[3].trim()); 
                double rating = Double.parseDouble(parts[4].trim());

                Movie movie;
                if (year >= 2000) {
                    movie = new FeatureFilm(title, year, director, duration, rating, 50_000_000.0);
                } else {
                    movie = new Documentary(title, year, director, duration, rating, 8);
                }
                movies.add(movie);
                
            } catch (NumberFormatException e) {
                System.out.println("Пропущено рядок через невірний формат числа: " + line);
            } catch (IllegalArgumentException e) {
                System.out.println("Пропущено рядок через невалідні дані: " + line);
            }
        }

        System.out.println("\n--- РЕЗУЛЬТАТИ ОБРОБКИ ---");
        for (Movie item : movies) {
            double score = item.calculateRecommendationScore();
            System.out.printf(Locale.ROOT, "Фільм: %s | Рік: %d | Категорія: %s | Бал: %.2f%n",
                    item.getTitle(), 
                    item.getReleaseYear(), 
                    item.getKind().label(), 
                    score);
        }

        // --- НОВИЙ БЛОК: Вивід результатів Stream API (Лабораторна 4) ---
        System.out.println("\n--- РЕЗУЛЬТАТИ STREAM API ---");

        System.out.println("\n1. Фільми з рейтингом 8.0+:");
        List<Movie> highlyRated = MovieReport.getHighlyRatedMovies(movies);
        if (highlyRated.isEmpty()) {
            System.out.println(" - Немає фільмів з таким високим рейтингом.");
        } else {
            highlyRated.forEach(m -> System.out.println(" - " + m.getTitle() + " (" + m.getRating() + ")"));
        }

        System.out.println("\n2. Топ-2 фільми:");
        MovieReport.getTopMovies(movies, 2).forEach(title -> System.out.println(" - " + title));

        System.out.println("\n3. Середній рейтинг за режисером:");
        MovieReport.getAverageRatingByDirector(movies).forEach((director, rating) -> 
            System.out.printf(Locale.ROOT, " - %s: %.2f\n", director, rating));

        System.out.println("\n4. Статистика тривалості:");
        IntSummaryStatistics stats = MovieReport.getDurationStatistics(movies);
        System.out.printf(Locale.ROOT, " - Найкоротший: %d хв | Найдовший: %d хв | Середня: %.1f хв\n", 
            stats.getMin(), stats.getMax(), stats.getAverage());

        System.out.println("\n5. Пошук фільму 'Довбуш':");
        MovieReport.findByTitle(movies, "Довбуш")
            .ifPresentOrElse(
                m -> System.out.println(" - Знайдено! Режисер: " + m.getDirector()),
                () -> System.out.println(" - Фільм не знайдено.")
            );

        if (outputPath != null) {
            System.out.println("\nЗапис у файл " + outputPath + " поки пропущено для демонстрації.");
        }
    }
}
