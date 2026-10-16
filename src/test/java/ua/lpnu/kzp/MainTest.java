package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class MainTest {

    /* === ТЕСТИ З ЛАБОРАТОРНОЇ №1 (Адаптовані під ООП та Builder) === */
    
    @Test
    void movieCreationStoresDataCorrectly() {
        // Тепер використовуємо Builder замість прямого виклику конструктора
        Movie movie = new Movie.Builder()
                .title("Тіні забутих предків")
                .director("Сергій Параджанов")
                .minutes(97)
                .year(1964)
                .rating(8.1)
                .build();

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
        // Перевіряємо, що Builder не дозволяє обійти валідацію і падає з помилкою
        assertThrows(IllegalArgumentException.class, 
            () -> new Movie.Builder()
                    .title("Матриця")
                    .director("Вачовскі")
                    .minutes(-10)
                    .year(1999)
                    .rating(8.7)
                    .build());
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
