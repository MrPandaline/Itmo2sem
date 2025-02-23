package laba5.storage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class OnCrashStorageWriter {
    public static void write(ArrayList<String> message) throws IOException {
        String filename = "crash-report_" + ZonedDateTime.now().toString().substring(0, 19).replace(':', '_') +".txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for(int i = 0; i < message.size(); i++){
            writer.write(message.get(i) + "\n");
        }
        writer.close();
    }
}
