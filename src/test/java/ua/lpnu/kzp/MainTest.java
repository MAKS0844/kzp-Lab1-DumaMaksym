package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class MainTest {

    /* === ТЕСТИ З ЛАБОРАТОРНОЇ №1 (Адаптовані під ООП) === */
    
    @Test
    void movieCreationStoresDataCorrectly() {
        Movie movie = new Movie("Тіні забутих предків", "Сергій Параджанов", 97, 1964, 8.1);

        // Тепер використовуємо геттери замість прямого доступу до приватних полів
        assertEquals("Тіні забутих предків", movie.getTitle());
        assertEquals("Сергій Параджанов", movie.getDirector());
        assertEquals(97, movie.getMinutes());
        assertEquals(1964, movie.getYear());
        assertEquals(8.1, movie.getRating(), 0.0001);
    }

    @Test
    void arraySplitHandlesEmptyFields() {
        String line = "Помилковий фільм;;100;2020;5.0";
        String[] fields = line.split(";", -1);
        
        assertEquals(5, fields.length);
        assertEquals("", fields[1]);
    }

    /* === НОВІ ТЕСТИ З ЛАБОРАТОРНОЇ №2 === */

    @Test
    void fromCsvCreatesValidEntity() {
        Movie movie = Movie.fromCsv("Матриця; Вачовскі; 136; 1999; 8.7");
        
        assertEquals("Матриця", movie.getTitle());
        assertEquals("Вачовскі", movie.getDirector());
        assertEquals(136, movie.getMinutes());
        assertEquals(1999, movie.getYear());
        assertEquals(8.7, movie.getRating(), 0.0001);
    }

    @Test
    void constructorRejectsNegativeMinutes() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Movie("Матриця", "Вачовскі", -10, 1999, 8.7));
    }

    @Test
    void fromCsvRejectsInvalidNumber() {
        assertThrows(IllegalArgumentException.class, 
            () -> Movie.fromCsv("Фільм; Режисер; помилка; 2020; 5.0"));
    }

    @Test
    void fromCsvRejectsWrongFieldCount() {
        assertThrows(IllegalArgumentException.class, 
            () -> Movie.fromCsv("Фільм; Режисер; 120"));
    }

    @Test
    void recordUsesValueEquality() {
        MovieSummary first = new MovieSummary(3, 8.0, 120, 1999);
        MovieSummary second = new MovieSummary(3, 8.0, 120, 1999);
        assertEquals(first, second);
    }
}
