package ua.lpnu.kzp;

import java.io.InputStream;
import java.util.Properties;

/** Клас для отримання версії програми з єдиного джерела правди (pom.xml). */
public final class VersionProvider {
    
    private VersionProvider() {
        // Приховуємо конструктор утилітного класу
    }

    public static String getVersion() {
        try (InputStream is = VersionProvider.class.getResourceAsStream("/application.properties")) {
            if (is == null) {
                return "невідомо";
            }
            Properties props = new Properties();
            props.load(is);
            return props.getProperty("app.version", "невідомо");
        } catch (Exception e) {
            return "помилка читання";
        }
    }
}
