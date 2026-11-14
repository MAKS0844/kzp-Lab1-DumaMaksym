package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class StorageTest {

    @TempDir
    Path temporaryDirectory;

    /* Перевіряє типобезпечне сховище та пошук за умовою. */
    @Test
    void repositoryIsGenericAndFindsRecords() {
        Repository<MovieRecord> repository = new Repository<>();
        MovieRecord dovbush = new MovieRecord("Довбуш", "Олесь Санін", 2023, 122, 8.0);
        
        repository.add(new MovieRecord("Тіні забутих предків", "Сергій Параджанов", 1964, 97, 8.1));
        repository.add(dovbush);

        // Шукаємо конкретний об'єкт через перевизначений equals
        assertEquals(1, repository.find(m -> m.equals(dovbush)).size());
        assertEquals(2, repository.all().size());
    }

    /* Перевіряє екранування коми та round-trip для CSV. */
    @Test
    void exportEscapesCommaAndRoundTripPreservesData() throws Exception {
        List<MovieRecord> original = List.of(
                // Назва містить кому - це перевірить, чи працює екранування CSV!
                new MovieRecord("Гаррі Поттер, частина 1", "Кріс Коламбус", 2001, 152, 7.6),
                new MovieRecord("Довбуш", "Олесь Санін", 2023, 122, 8.0)
        );

        Path path = temporaryDirectory.resolve("movies.csv");
        CsvExporter.write(path, MovieRecord.class, original);

        String csv = Files.readString(path, StandardCharsets.UTF_8);
        List<List<String>> rows = CsvParser.parseDocument(csv);

        List<MovieRecord> restored = rows.stream()
                .skip(1)
                .map(fields -> {
                    try {
                        return MovieRecord.fromFields(fields);
                    } catch (DataStorageException exception) {
                        throw new IllegalStateException(exception);
                    }
                })
                .toList();

        assertEquals(3, rows.size());
        // Заголовок має бути відсортований за алфавітом назв полів у Java
        assertEquals("Режисер,Тривалість,Оцінка,Назва,Рік", String.join(",", rows.get(0)));
        assertTrue(csv.contains("\"Гаррі Поттер, частина 1\""));
        assertEquals(original, restored);
    }

    /* Перевіряє збереження перенесення рядка всередині CSV-поля. */
    @Test
    void roundTripPreservesMultilineField() throws Exception {
        List<MovieRecord> original = List.of(
                new MovieRecord("Фільм\nз перенесенням", "Невідомий", 2000, 100, 5.0)
        );

        Path path = temporaryDirectory.resolve("multiline.csv");
        CsvExporter.write(path, MovieRecord.class, original);

        String csv = Files.readString(path, StandardCharsets.UTF_8);
        List<List<String>> rows = CsvParser.parseDocument(csv);
        List<MovieRecord> restored = List.of(MovieRecord.fromFields(rows.get(1)));

        assertTrue(csv.contains("\"Фільм\nз перенесенням\""));
        assertEquals(original, restored);
    }

    /* Перевіряє порожній експорт і відхилення хибного CSV. */
    @Test
    void handlesEmptyInputAndInvalidCsv() throws Exception {
        Path path = temporaryDirectory.resolve("empty.csv");
        CsvExporter.write(path, MovieRecord.class, List.of());

        assertEquals(List.of(List.of("Режисер", "Тривалість", "Оцінка", "Назва", "Рік")),
                CsvParser.parseDocument(Files.readString(path, StandardCharsets.UTF_8)));

        // Очікуємо помилку при 4 полях замість 5 або зламаних даних
        assertThrows(DataStorageException.class,
                () -> MovieRecord.fromCsv("Олесь Санін,122,8.0,broken"));
    }

    /* Перевіряє збереження першопричини помилки файла. */
    @Test
    void wrapsFileErrorWithCause() throws Exception {
        Path directory = temporaryDirectory.resolve("not-a-file");
        Files.createDirectory(directory);

        DataStorageException exception = assertThrows(DataStorageException.class,
                () -> CsvExporter.write(directory, MovieRecord.class, List.of()));

        assertTrue(exception.getCause() instanceof IOException);
    }
}
