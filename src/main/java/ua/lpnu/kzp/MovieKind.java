package ua.lpnu.kzp;

/** Категорія фільму. */
public enum MovieKind {
    FEATURE("Художній"),
    DOCUMENTARY("Документальний");

    private final String label;

    MovieKind(String label) {
        this.label = label;
    }

    /** Повертає текстове подання категорії для звіту. */
    public String label() {
        return label;
    }
}
