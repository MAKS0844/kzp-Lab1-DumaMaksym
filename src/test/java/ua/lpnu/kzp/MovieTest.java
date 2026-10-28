package ua.lpnu.kzp;

import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    @Test
    void subtypesHaveDifferentBehavior() {
        // Додано: режисер ("Параджанов"), тривалість (97), рейтинг (8.1)
        Movie feature = new FeatureFilm("Тіні забутих предків", 1964, "Параджанов", 97, 8.1, 50_000_000.0);
        Movie doc = new Documentary("Чорнобиль", 2019, "Крейг Мейзін", 330, 9.4, 9);
        
        assertNotEquals(feature.calculateRecommendationScore(), doc.calculateRecommendationScore(),
                "Поліморфний метод має давати різний результат для різних підтипів");
    }

    @Test
    void equalObjectsHaveOneSetEntry() {
        Set<Movie> movies = new HashSet<>();
        
        // Створюємо два об'єкти з однаковою назвою і роком
        Movie m1 = new FeatureFilm("Аватар", 2009, "Кемерон", 162, 7.9, 100_000.0);
        Movie m2 = new FeatureFilm("Аватар", 2009, "Кемерон", 162, 7.9, 100_000.0);
        
        movies.add(m1);
        movies.add(m2);
        
        assertEquals(1, movies.size(), "HashSet не повинен дублювати логічно рівні об'єкти");
    }

    @Test
    void rejectsInvalidSpecificValueForFeatureFilm() {
        assertThrows(IllegalArgumentException.class,
                () -> new FeatureFilm("Тіні", 1964, "Параджанов", 97, 8.1, -100.0),
                "Має викидати виняток при від'ємних касових зборах");
    }

    @Test
    void rejectsInvalidSpecificValueForDocumentary() {
        assertThrows(IllegalArgumentException.class,
                () -> new Documentary("Док", 2020, "Режисер", 100, 7.0, 15),
                "Має викидати виняток при історичній достовірності більше 10");
    }
}
