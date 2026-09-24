package ua.lpnu.kzp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/* Ізолює кросплатформне читання та запис текстових файлів. */
public final class FileReport {
    private FileReport() {
    }

    public static List readLines(Path input) throws IOException {
        return Files.readAllLines(input, StandardCharsets.UTF_8);
    }

    public static void writeReport(Path output, String report) throws IOException {
        Path parent = output.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
            Files.writeString(output, report, StandardCharsets.UTF_8);
        }
    }
}
