package ua.lpnu.kzp;

import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    /* Перевіряє різну поведінку підтипів (поліморфізм). */
    @Test
    void subtypesHaveDifferentBehavior() {
        Movie feature = new FeatureFilm("Аватар", 2009, 2_000_000_000.0);
        Movie doc = new Documentary("Планета Земля", 2006, 10);
        
        // Переконуємося, що поліморфний метод видає різні результати
        assertNotEquals(feature.calculateRecommendationScore(), doc.calculateRecommendationScore());
    }

    /* Перевіряє відсутність дублювання логічно рівних об'єктів у множині. */
    @Test
    void equalObjectsHaveOneSetEntry() {
        Set<Movie> movies = new HashSet<>();
        movies.add(new FeatureFilm("Дюна", 2021, 400_000_000.0));
        movies.add(new FeatureFilm("Дюна", 2021, 400_000_000.0)); // Дублікат
        
        
        assertEquals(1, movies.size());
    }

    /* Перевіряє відхилення неприпустимого спільного стану в базовому класі. */
    @Test
    void baseClassRejectsInvalidState() {
        assertThrows(IllegalArgumentException.class, 
            () -> new FeatureFilm("", 2020, 1000.0));
    }
    
    /* Перевіряє відхилення специфічного значення у підтипі. */
    @Test
    void subtypeRejectsInvalidSpecificValue() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Documentary("Історія", 2020, 15)); // Достовірність > 10
    }
}
