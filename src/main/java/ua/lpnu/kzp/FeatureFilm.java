package ua.lpnu.kzp;

public final class FeatureFilm extends Movie {
    // Унікальне поле для художнього фільму
    private final double boxOffice;

    public FeatureFilm(String title, int releaseYear, String director, int duration, double rating, double boxOffice) {
        // Передаємо спільні параметри в батьківський клас
        super(title, releaseYear, MovieKind.FEATURE, director, duration, rating);
        
        if (boxOffice < 0) {
            throw new IllegalArgumentException("Касові збори не можуть бути від'ємними");
        }
        this.boxOffice = boxOffice;
    }

    public double getBoxOffice() {
        return boxOffice;
    }

    @Override
    public double calculateRecommendationScore() {
        // Бал для художніх фільмів залежить від року та зборів
        double baseScore = (getReleaseYear() > 2000) ? 7.0 : 8.0;
        return baseScore + (boxOffice / 100_000_000.0);
    }
}
