package laba5.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Класс, который наследуют все классы модели.
 * @version 1.0
 */
public abstract class AbstractModel {
    @Override
    public String toString() {
        final String SEPARATOR = "----------------------------------------------------------\n";
        final String MAIN_SEPARATOR = "**********************************************************";
        final StringBuilder sb = new StringBuilder();
        Field[] fields = this.getClass().getDeclaredFields();
        sb.append(MAIN_SEPARATOR).append("\n");
        sb.append(this.getClass().getSimpleName()).append("\n");
        sb.append(SEPARATOR);
        for (Field field : fields) {
            field.setAccessible(true);
            if (!Modifier.isStatic(field.getModifiers())) {
                try {
                    sb.append(field.getName()).append(": ");
                    if (field.get(this) instanceof AbstractModel) {
                        sb.append('\n');
                    }
                    if (field.get(this) instanceof java.time.ZonedDateTime) {
                        String date = field.get(this).toString();
                        sb.append(date, 0, 10).append(" ").append(date, 11, 19).append("\n");
                    }
                    else {
                        sb.append(field.get(this)).append("\n");
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        sb.append(MAIN_SEPARATOR);
        return sb.toString();
    }
}
