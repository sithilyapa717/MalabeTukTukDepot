import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLogger {

    private static String logFile = "data/audit_log.txt";

    public static void log(String action, String itemCode, int quantity) throws IOException{
        LocalDateTime now=LocalDateTime.now();
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time=now.format(formatter);

        String code=itemCode;
        if (code==null) {
            code = "N/A";
        }

        String line=time + " | " + action + " | " + code + " | qty=" + quantity;

        // true = append, do not overwrite
        PrintWriter writer=new PrintWriter(new FileWriter(logFile, true));
        writer.println(line);
        writer.close();
    }

    public static void logAction(String action, String itemCode) throws IOException{
        log(action, itemCode, 0);
    }
}