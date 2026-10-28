package ua.lpnu.kzp;

public final class Documentary extends Movie {
    // Унікальне поле для документального фільму
    private final int historicalAccuracy;

    public Documentary(String title, int releaseYear, String director, int duration, double rating, int historicalAccuracy) {
        // Виклик конструктора Movie
        super(title, releaseYear, MovieKind.DOCUMENTARY, director, duration, rating);
        
        if (historicalAccuracy < 1 || historicalAccuracy > 10) {
            throw new IllegalArgumentException("Історична достовірність має бути від 1 до 10");
        }
        this.historicalAccuracy = historicalAccuracy;
    }

    public int getHistoricalAccuracy() {
        return historicalAccuracy;
    }

    @Override
    public double calculateRecommendationScore() {
        // Бал для документалок розраховується на основі точності
        return historicalAccuracy * 0.9;
    }
}
