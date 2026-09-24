package ua.lpnu.kzp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        Path input = Path.of("data", "input.csv");
        List <String> lines;

        try {
            // Явне кодування не залежить від налаштувань операційної системи
            lines = Files.readAllLines(input, StandardCharsets.UTF_8);
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
            // Розбиваємо рядок, зберігаючи порожні поля в кінці
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
                // Числові перетворення виконуються в блоці try
                int minutes = Integer.parseInt(fields[2]);
                int year = Integer.parseInt(fields[3]);
                double rating = Double.parseDouble(fields[4]);

                if (minutes <= 0 || year <= 0 || rating < 0) {
                    errors.add("Рядок " + (index + 1) + ": від'ємне або нульове числове значення");
                    continue;
                }

                // До статистики потрапляють лише коректні записи
                validCount++;
                totalRating += rating;
                maxMinutes = Math.max(maxMinutes, minutes);
                oldestYear = Math.min(oldestYear, year);

            } catch (NumberFormatException exception) {
                errors.add("Рядок " + (index + 1) + ": числове поле має помилковий формат");
            }
        }

        // Захист від ділення на нуль, якщо жоден фільм не пройшов перевірку
        double averageRating = validCount == 0 ? 0.0 : totalRating / validCount;

        System.out.printf(Locale.ROOT, "Коректних записів: %d%n", validCount);
        System.out.printf(Locale.ROOT, "Середній рейтинг: %.2f%n", averageRating);
        System.out.printf(Locale.ROOT, "Найдовший фільм (хв): %d%n", maxMinutes == Integer.MIN_VALUE ? 0 : maxMinutes);
        System.out.printf(Locale.ROOT, "Найстаріший рік: %d%n", oldestYear == Integer.MAX_VALUE ? 0 : oldestYear);
        System.out.printf(Locale.ROOT, "Помилок: %d%n", errors.size());
        
        errors.forEach(System.out::println);
    }
}
