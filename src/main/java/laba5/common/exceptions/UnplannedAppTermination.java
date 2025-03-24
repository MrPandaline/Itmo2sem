package laba5.common.exceptions;

/**
 * Класс исключения, бросаемого в случае, если произошло событие, неминуемо ведущее к завершению программы.
 * @author Homoursus
 * @version 1.0
 */
public class UnplannedAppTermination extends RuntimeException{
    public String getMessage(){
        return "Работа приложения сейчас завершится.";
    }

}
