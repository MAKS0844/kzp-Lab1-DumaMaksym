package ua.lpnu.kzp;

/**
 * Незмінний набір підсумкових показників для звіту.
 *
 * @param count кількість коректних записів
 * @param averageRating середній рейтинг
 * @param maxMinutes найдовший фільм (хв)
 * @param oldestYear найстаріший рік випуску
 */
public record MovieSummary(int count, double averageRating, int maxMinutes, int oldestYear) {
    
    /** Перевіряє допустимість підсумкових показників. */
    public MovieSummary {
        if (count < 0 || averageRating < 0 || !Double.isFinite(averageRating)) {
            throw new IllegalArgumentException("Показники не можуть бути від'ємними або некоректними");
        }
    }
}
