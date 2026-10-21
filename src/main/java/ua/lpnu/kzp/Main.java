package ua.lpnu.kzp;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Головний клас програми для аналізу каталогу фільмів.
 * Відрефакторено з використанням ООП.
 */
public final class Main {
    
    private Main() {
    }

    public static void main(String[] args) {
        Path inputPath = Path.of("data", "input.csv");
        Path outputPath = Path.of("out", "report.txt");

        if (args.length > 0) {
            if ("--help".equals(args[0])) {
                System.out.printf("Використання: java -jar lab01-1.1.0.jar [--help] [--version] [--input <файл>] [--output <файл>]%n");
                return;
            }
            if ("--version".equals(args[0])) {
                System.out.printf("Версія програми: %s%n", VersionProvider.getVersion());
                return;
            }
        }
        
        for (int i = 0; i < args.length - 1; i++) {
            if ("--input".equals(args[i])) {
                inputPath = Path.of(args[i + 1]);
            } else if ("--output".equals(args[i])) {
                outputPath = Path.of(args[i + 1]);
            }
        }

        List<String> lines;
        try {
            lines = FileReport.readLines(inputPath);
        } catch (IOException e) {
            System.out.println("Помилка читання файлу: " + e.getMessage());
            return;
        }

        List<Movie> movies = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        int maxMinutes = Integer.MIN_VALUE;
        int oldestYear = Integer.MAX_VALUE;
        double totalRating = 0.0;

        for (int index = 0; index < lines.size(); index++) {
            try {
                // Створюємо об'єкт через фабричний метод (інкапсуляція розбору та валідації)
                Movie movie = Movie.fromCsv(lines.get(index));
                movies.add(movie);
                
                totalRating += movie.getRating();
                maxMinutes = Math.max(maxMinutes, movie.getMinutes());
                oldestYear = Math.min(oldestYear, movie.getYear());
            } catch (IllegalArgumentException exception) {
                // Зберігаємо помилку разом із номером рядка на рівні циклу
                errors.add("Рядок " + (index + 1) + ": " + exception.getMessage());
            }
        }

        double averageRating = movies.isEmpty() ? 0.0 : totalRating / movies.size();
        
        // Зберігаємо підсумки у незмінний record
        MovieSummary summary = new MovieSummary(
            movies.size(), 
            averageRating, 
            maxMinutes == Integer.MIN_VALUE ? 0 : maxMinutes, 
            oldestYear == Integer.MAX_VALUE ? 0 : oldestYear
        );

        StringBuilder reportBuilder = new StringBuilder();
        
        reportBuilder.append(String.format(Locale.ROOT, "Коректних записів: %d%n", summary.count()));
        reportBuilder.append(String.format(Locale.ROOT, "Середній рейтинг: %.2f%n", summary.averageRating()));
        reportBuilder.append(String.format(Locale.ROOT, "Найдовший фільм (хв): %d%n", summary.maxMinutes()));
        reportBuilder.append(String.format(Locale.ROOT, "Найстаріший рік: %d%n", summary.oldestYear()));
        reportBuilder.append(String.format(Locale.ROOT, "Помилок: %d%n", errors.size()));
        
        for (String error : errors) {
            reportBuilder.append(error).append(System.lineSeparator());
        }

        String finalReport = reportBuilder.toString();
        System.out.print(finalReport);

        try {
            FileReport.writeReport(outputPath, finalReport);
            System.out.println("\n[УСПІХ] Звіт збережено у файл: " + outputPath.toString());
        } catch (IOException e) {
            System.out.println("\n[ПОМИЛКА] Не вдалося записати звіт у файл: " + e.getMessage());
        }
    }
}
