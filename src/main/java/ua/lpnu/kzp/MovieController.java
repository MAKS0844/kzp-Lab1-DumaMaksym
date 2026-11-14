package ua.lpnu.kzp;

import java.nio.file.Path;

public class MovieController {

    public void process(String inputPath, String outputPath) {
        try {
            System.out.println("Починаємо обробку даних...");

            // 1. Створюємо наше нове параметризоване сховище (Lab 5)
            Repository<MovieRecord> repository = new Repository<>();

            // 2. Додаємо дані (включаючи випадки з комою та перенесенням рядка)
            repository.add(new MovieRecord("Довбуш", "Олесь Санін", 2023, 122, 8.0));
            repository.add(new MovieRecord("Тіні забутих предків", "Сергій Параджанов", 1964, 97, 8.1));
            repository.add(new MovieRecord("Гаррі Поттер, частина 1", "Кріс Коламбус", 2001, 152, 7.6));
            repository.add(new MovieRecord("Фільм \"Аватар\"\n(Спецвипуск)", "Джеймс Кемерон", 2009, 162, 7.9));
            repository.add(new MovieRecord("Дюна", "Дені Вільнев", 2021, 155, 8.0));

            // 3. Визначаємо шлях збереження (якщо outputPath не передано, пишемо в target/)
            Path path = (outputPath != null && !outputPath.isBlank()) 
                    ? Path.of(outputPath) 
                    : Path.of("target", "movies.csv");

            // 4. Експортуємо дані у файл
            CsvExporter.write(path, MovieRecord.class, repository.all());

            System.out.println("Успіх! Дані збережено у CSV-файл: " + path.toAbsolutePath());

        } catch (DataStorageException e) {
            System.err.println("Помилка збереження даних: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Першопричина: " + e.getCause());
            }
        } catch (Exception e) {
            System.err.println("Неочікувана помилка в контролері: " + e.getMessage());
        }
    }
}
