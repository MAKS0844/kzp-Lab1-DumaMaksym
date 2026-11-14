package ua.lpnu.kzp;

/** Повідомляє про помилку зберігання, обробки CSV або рефлексії. */
public class DataStorageException extends Exception {

    /** Створює виняток із повідомленням. */
    public DataStorageException(String message) {
        super(message);
    }

    /** Створює виняток із повідомленням і першопричиною. */
    public DataStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
