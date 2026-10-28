package ua.lpnu.kzp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.IntSummaryStatistics;

import static org.junit.jupiter.api.Assertions.*;

class MovieReportTest {

    private List<Movie> movies;

    @BeforeEach
    void setUp() {
        // Контрольний набір із 6 фільмів згідно з вимогами методички
        movies = List.of(
                new Documentary("Чорнобиль", 2019, "Мейзін", 330, 9.4, 9),      // Найвищий рейтинг
                new FeatureFilm("Темний лицар", 2008, "Нолан", 152, 9.0, 1e9),  // Топ-2
                new FeatureFilm("Інтерстеллар", 2014, "Нолан", 169, 8.6, 7e8),  // Дублікат режисера (Нолан)
                new FeatureFilm("Дюна", 2021, "Вільнев", 155, 8.0, 4e8),        // На межі фільтра (рівно 8.0)
                new FeatureFilm("Дюна 2", 2024, "Вільнев", 166, 8.0, 7e8),      // Перевірка 2-го критерію сортування
                new FeatureFilm("Аватар", 2009, "Кемерон", 162, 7.9, 2e9)       // Менше порогу (7.9)
        );
    }

    @Test
    void filtersHighlyRatedMovies() {
        List<Movie> filtered = MovieReport.getHighlyRatedMovies(movies);
        
        assertEquals(5, filtered.size(), "Має відсіяти 1 фільм з оцінкою < 8.0");
        assertFalse(filtered.stream().anyMatch(m -> m.getTitle().equals("Аватар")), 
                "Фільм з оцінкою 7.9 не повинен потрапити у вибірку");
    }

    @Test
    void mapsToTitles() {
        List<String> titles = MovieReport.getTitles(movies);
        
        assertEquals(6, titles.size());
        assertTrue(titles.contains("Чорнобиль"));
    }

    @Test
    void groupsByDirectorAndAveragesRating() {
        Map<String, Double> byDirector = MovieReport.getAverageRatingByDirector(movies);
        
        assertEquals(4, byDirector.size(), "Має бути 4 унікальних режисери");
        // У Нолана два фільми: (9.0 + 8.6) / 2 = 8.8
        assertEquals(8.8, byDirector.get("Нолан"), 0.001, "Середня оцінка Нолана розрахована неправильно");
    }

    @Test
    void calculatesDurationStatistics() {
        IntSummaryStatistics stats = MovieReport.getDurationStatistics(movies);
        
        assertEquals(6, stats.getCount());
        assertEquals(330, stats.getMax(), "Максимальна тривалість має бути 330 (Чорнобиль)");
        assertEquals(152, stats.getMin(), "Мінімальна тривалість має бути 152 (Темний лицар)");
    }

    @Test
    void ordersTopMoviesAndAppliesLimit() {
        // Очікуваний порядок: Чорнобиль(9.4), Темний лицар(9.0), Інтерстеллар(8.6), Дюна(8.0), Дюна 2(8.0)
        List<String> top3 = MovieReport.getTopMovies(movies, 3);
        
        assertEquals(3, top3.size(), "Ліміт має усікати результат до 3");
        assertEquals("Чорнобиль", top3.get(0));
        assertEquals("Темний лицар", top3.get(1));
        assertEquals("Інтерстеллар", top3.get(2));
    }

    @Test
    void checksSecondSortingCriterion() {
        // Дюна і Дюна 2 мають однакову оцінку (8.0). За алфавітом "Дюна" має йти першою
        List<String> top5 = MovieReport.getTopMovies(movies, 5);
        assertEquals("Дюна", top5.get(3));
        assertEquals("Дюна 2", top5.get(4));
    }

    @Test
    void rejectsNegativeLimit() {
        assertThrows(IllegalArgumentException.class, () -> MovieReport.getTopMovies(movies, -1));
    }

    @Test
    void findsByTitleReturnsOptional() {
        Optional<Movie> found = MovieReport.findByTitle(movies, "Дюна");
        assertTrue(found.isPresent());
        assertEquals(2021, found.get().getReleaseYear());

        Optional<Movie> notFound = MovieReport.findByTitle(movies, "Месники");
        assertTrue(notFound.isEmpty(), "Для неіснуючого фільму має повертатись порожній Optional");
    }

    @Test
    void queriesDoNotMutateInput() {
        List<Movie> originalCopy = new ArrayList<>(movies);
        
        MovieReport.getHighlyRatedMovies(movies);
        MovieReport.getTopMovies(movies, 5);
        MovieReport.getAverageRatingByDirector(movies);
        
        assertEquals(originalCopy, movies, "Вхідна колекція не повинна змінюватись після виконання потоків");
    }

    @Test
    void handlesEmptyInput() {
        List<Movie> emptyList = List.of();
        
        assertEquals(0, MovieReport.getHighlyRatedMovies(emptyList).size());
        assertEquals(0, MovieReport.getDurationStatistics(emptyList).getCount());
        assertTrue(MovieReport.findByTitle(emptyList, "Тест").isEmpty());
    }
}
