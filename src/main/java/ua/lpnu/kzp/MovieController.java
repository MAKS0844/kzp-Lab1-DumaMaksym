package ua.lpnu.kzp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class MovieController {

    public void process(String inputPath, String outputPath) throws IOException {
        System.out.println("Читаємо файл: " + inputPath);
        
        List<Movie> movies = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(inputPath));

        // 1. Читання та розгалуження (Фабрична логіка)
        // Починаємо з 0, оскільки немає рядка заголовків
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(";");
            if (parts.length < 5) {
                System.out.println("Пропущено неповний рядок: " + line);
                continue;
            }

            try {
                // Беремо назву (індекс 0) і рік (індекс 3)
                String title = parts[0].trim();
                int year = Integer.parseInt(parts[3].trim()); 
                

                Movie movie;
                if (year >= 2000) {
                    movie = new FeatureFilm(title, year, 50_000_000.0);
                } else {
                    movie = new Documentary(title, year, 8);
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
            
            // Java сама знає, чий метод calculateRecommendationScore() викликати
            double score = item.calculateRecommendationScore();
            
            System.out.printf(Locale.ROOT, "Фільм: %s | Рік: %d | Категорія: %s | Бал: %.2f%n",
                    item.getTitle(), 
                    item.getReleaseYear(), 
                    item.getKind().label(), 
                    score);
        }

        if (outputPath != null) {
            
            System.out.println("Запис у файл " + outputPath + " поки пропущено для демонстрації.");
        }
    }
}
