package ua.lpnu.kzp;

/** Художній фільм. */
public final class FeatureFilm extends Movie {
    private final double boxOffice;

    public FeatureFilm(String title, int releaseYear, double boxOffice) {
        // 1. Спочатку передаємо спільні дані в базовий клас
        super(title, releaseYear, MovieKind.FEATURE);
        
        // 2. Потім перевіряємо власне специфічне поле
        if (boxOffice < 0) {
            throw new IllegalArgumentException("Касові збори не можуть бути від'ємними");
        }
        this.boxOffice = boxOffice;
    }

    public double getBoxOffice() {
        return boxOffice;
    }

    /** Реалізація поліморфного методу для художнього фільму. */
    @Override
    public double calculateRecommendationScore() {
        // Наприклад, для художнього фільму бал залежить від року випуску та касових зборів
        double baseScore = (getReleaseYear() > 2000) ? 7.0 : 8.0;
        return baseScore + (boxOffice / 100_000_000.0);
    }
}
