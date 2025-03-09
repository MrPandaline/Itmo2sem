package laba5.misc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//TODO: починить парсер.
public class ReflectionParser {
    public static List<Class<?>> getClassesFromDirectory(String dirPath, boolean isCompiled) throws ClassNotFoundException {
        String fileType;
        if (isCompiled) {
            fileType = ".class";
        } else {
            fileType = ".java";
        }
        dirPath = System.getProperty("user.dir") + File.separator + dirPath;
        File directory = new File(dirPath);
        List<Class<?>> classes = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((d, name) -> name.endsWith(fileType));
            if (files != null) {
                for (File file : files) {
                    String className = file.getName().substring(0, file.getName().length() - fileType.length());
                    classes.add(Class.forName("laba5."+className));

                }
            }
        }
        return classes;
    }
}
