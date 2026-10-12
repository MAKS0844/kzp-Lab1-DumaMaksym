package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void movieCreationStoresDataCorrectly() {
        Movie movie = new Movie("Тіні забутих предків", "Сергій Параджанов", 97, 1964, 8.1);

        assertEquals("Тіні забутих предків", movie.title);
        assertEquals("Сергій Параджанов", movie.director);
        assertEquals(97, movie.minutes);
        assertEquals(1964, movie.year);
        assertEquals(8.1, movie.rating, 0.0001);
    }

    @Test
    void arraySplitHandlesEmptyFields() {
        String line = "Помилковий фільм;;100;2020;5.0";
        String[] fields = line.split(";", -1);
        
        assertEquals(5, fields.length);
        assertEquals("", fields[1]); // Перевіряємо, що порожній режисер відловлюється правильно
    }
}
