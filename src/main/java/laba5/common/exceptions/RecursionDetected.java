package laba5.common.exceptions;

/**
 * Класс исключения, бросаемого в случае, если в скрипте замечена рекурсия.
 * @author Homoursus
 * @version 1.0
 */
public class RecursionDetected extends RuntimeException{
    public String getMessage(){
        return "В скрипте замечена рекурсия!";
    }

}
