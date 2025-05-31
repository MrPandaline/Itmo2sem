package laba5.client.l18n;
import java.util.ListResourceBundle;
import java.util.Locale;

public abstract class Messages extends ListResourceBundle {
    private static Locale currentLocale = Locale.getDefault();
    private static Messages instance;

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        instance = null; // Сбросить кэш
    }

    public static Messages getBundle() {
        if (instance == null) {
            if (currentLocale.getLanguage().equals(new Locale("ru").getLanguage())) {
                instance = new Messages_ru();
            } else {
                instance = new Messages_en_ZA();
            }
        }
        return instance;
    }
}