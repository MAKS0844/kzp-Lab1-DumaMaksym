package ua.lpnu.kzp;

public abstract class Movie {
    // Спільні поля для всіх фільмів
    private final String title;
    private final int releaseYear;
    private final MovieKind kind;

    // Робимо конструктор protected
    protected Movie(String title, int releaseYear, MovieKind kind) {
        // Тут залишається вся твоя надійна валідація
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Назва не може бути порожньою");
        }
        if (releaseYear < 1895) {
            throw new IllegalArgumentException("Рік випуску не може бути меншим за 1895");
        }
        
        // ДОДАВ ПЕРЕВІРКУ НА NULL
        if (kind == null) {
            throw new IllegalArgumentException("Категорія не може бути null");
        }

        this.title = title;
        this.releaseYear = releaseYear;
        this.kind = kind;
    }

    // Звичайні гетери (щоб нащадки могли читати ці поля)
    public String getTitle() { return title; }
    public int getReleaseYear() { return releaseYear; }

    public MovieKind getKind() { 
        return kind; 
    }

    // Абстрактний метод (без тіла), який кожен нащадок реалізує по-своєму
    public abstract double calculateRecommendationScore();
}
