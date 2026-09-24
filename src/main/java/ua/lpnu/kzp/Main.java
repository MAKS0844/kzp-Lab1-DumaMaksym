package ua.lpnu.kzp;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        Path inputPath = Path.of("data", "input.csv");
        Path outputPath = Path.of("out", "report.txt");

        // Розбір аргументів командного рядка
        if (args.length > 0 && "--help".equals(args[0])) {
            System.out.println("Використання: java -jar lab01-1.0.0.jar [--input ] [--output ]");
            return;
        }
        
        for (int i = 0; i < args.length - 1; i++) {
            if ("--input".equals(args[i])) {
                inputPath = Path.of(args[i + 1]);
            } else if ("--output".equals(args[i])) {
                outputPath = Path.of(args[i + 1]);
            }
        }

        List <String> lines;
        try {
            lines = FileReport.readLines(inputPath);
        } catch (IOException e) {
            System.out.println("Помилка читання файлу: " + e.getMessage());
            return;
        }

        List <String> errors = new ArrayList<>();
        int validCount = 0;
        double totalRating = 0;
        int maxMinutes = Integer.MIN_VALUE;
        int oldestYear = Integer.MAX_VALUE;

        for (int index = 0; index < lines.size(); index++) {
            String[] fields = lines.get(index).split(";", -1);
            
            if (fields.length != 5) {
                errors.add("Рядок " + (index + 1) + ": очікується 5 полів");
                continue;
            }
            
            if (fields[0].isBlank() || fields[1].isBlank()) {
                errors.add("Рядок " + (index + 1) + ": порожня назва або режисер");
                continue;
            }

            try {
                int minutes = Integer.parseInt(fields[2]);
                int year = Integer.parseInt(fields[3]);
                double rating = Double.parseDouble(fields[4]);

                if (minutes <= 0 || year <= 0 || rating < 0) {
                    errors.add("Рядок " + (index + 1) + ": від'ємне або нульове числове значення");
                    continue;
                }

                validCount++;
                totalRating += rating;
                maxMinutes = Math.max(maxMinutes, minutes);
                oldestYear = Math.min(oldestYear, year);

            } catch (NumberFormatException exception) {
                errors.add("Рядок " + (index + 1) + ": числове поле має помилковий формат");
            }
        }

        double averageRating = validCount == 0 ? 0.0 : totalRating / validCount;

        // Збираю весь звіт у єдину текстову змінну
        StringBuilder reportBuilder = new StringBuilder();
        
        reportBuilder.append(String.format(Locale.ROOT, "Коректних записів: %d%n", validCount));
        reportBuilder.append(String.format(Locale.ROOT, "Середній рейтинг: %.2f%n", averageRating));
        reportBuilder.append(String.format(Locale.ROOT, "Найдовший фільм (хв): %d%n", maxMinutes == Integer.MIN_VALUE ? 0 : maxMinutes));
        reportBuilder.append(String.format(Locale.ROOT, "Найстаріший рік: %d%n", oldestYear == Integer.MAX_VALUE ? 0 : oldestYear));
        reportBuilder.append(String.format(Locale.ROOT, "Помилок: %d%n", errors.size()));
        
        for (String error : errors) {
            reportBuilder.append(error).append(System.lineSeparator());
        }

        String finalReport = reportBuilder.toString();

        // Виводжу звіт у консоль
        System.out.print(finalReport);

        // Записую той самий звіт у файл
        try {
            FileReport.writeReport(outputPath, finalReport);
            System.out.println("\n[УСПІХ] Звіт збережено у файл: " + outputPath.toString());
        } catch (IOException e) {
            System.out.println("\n[ПОМИЛКА] Не вдалося записати звіт у файл: " + e.getMessage());
        }
    }
}
