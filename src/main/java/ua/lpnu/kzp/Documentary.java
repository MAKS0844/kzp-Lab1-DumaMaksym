package ua.lpnu.kzp;

/** Документальний фільм. */
public final class Documentary extends Movie {
    private final int historicalAccuracy;

    public Documentary(String title, int releaseYear, int historicalAccuracy) {
        super(title, releaseYear, MovieKind.DOCUMENTARY);
        
        if (historicalAccuracy < 1 || historicalAccuracy > 10) {
            throw new IllegalArgumentException("Історична достовірність має бути від 1 до 10");
        }
        this.historicalAccuracy = historicalAccuracy;
    }

    public int getHistoricalAccuracy() {
        return historicalAccuracy;
    }

    /** Реалізація поліморфного методу для документального фільму. */
    @Override
    public double calculateRecommendationScore() {
        // Для документального фільму бал базується на його достовірності
        return historicalAccuracy * 0.9;
    }
}
