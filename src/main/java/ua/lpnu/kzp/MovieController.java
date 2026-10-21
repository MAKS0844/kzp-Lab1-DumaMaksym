package ua.lpnu.kzp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class MovieController {

    public void process(String inputPath, String outputPath) throws IOException {
        // УВАГА: Сюди тобі треба перенести (скопіювати) ВЕСЬ код із твого старого Main,
        // починаючи від зчитування файлу (Files.readAllLines) і до виводу результатів.
        
        System.out.println("Читаємо файл: " + inputPath);
        
        // ... твій код створення списку фільмів та генерації MovieSummary ...
        
        if (outputPath != null) {
            // ... запис у файл ...
            System.out.println("Результат збережено у: " + outputPath);
        }
    }
}
