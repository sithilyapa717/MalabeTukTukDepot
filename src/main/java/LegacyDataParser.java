

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class LegacyDataParser {

    private static final String separators ="[,|;]";


    private static final String[] dateFormats = {
            "yyyy/MM/dd",
            "yyyy-MM-dd",
            
            "dd/MM/yyyy",
            "dd-MM-yyyy",

            "MMM dd, yyyy",
            "dd-MMM-yyyy"
    };


    //parsing the inventory file
    public static class InventoryParseResult {
        private List<InventoryItem> items = new ArrayList<>();
        private List<String> errors = new ArrayList<>();

        public List<InventoryItem> getItems() { return items; }
        public List<String> getErrors() { return errors; }
    }


    //parsing the dealer file
    public static class DealerParseResult {
        private List<Dealer> dealers = new ArrayList<>();
        private List<String> errors = new ArrayList<>();

        public List<Dealer> getDealers() { return dealers; }
        public List<String> getErrors() { return errors; }
    }


    //inventory
    public InventoryParseResult parseInventoryFile(String filePath) throws IOException {
        InventoryParseResult result = new InventoryParseResult();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue; // blank lines are not errors, just skipped
                }

                String[] fields = splitDirtyLine(line);
                fields = fixInventoryDateSplit(fields);

                if (fields.length != 8) {
                    result.getErrors().add("Line " + lineNumber + ": expected 8 fields but found "
                            + fields.length + " -> skipped. Raw line: \"" + line + "\"");
                    continue;
                }

                try {
                    String code = fields[0];
                    String name = fields[1];
                    String dealerName = fields[2];
                    double price = parsePrice(fields[3]);
                    int quantity = Integer.parseInt(fields[4]);
                    String category = fields[5];
                    LocalDate date = parseDate(fields[6]);
                    String image = fields[7];

                    InventoryItem item = new InventoryItem(code, name, dealerName, price,
                            quantity, category, date, image);
                    result.getItems().add(item);

                } catch (Exception e) {
                    result.getErrors().add("Line " + lineNumber + ": " + e.getMessage()
                            + " -> skipped. Raw line: \"" + line + "\"");
                }
            }
        }

        return result;
    }
    //9 tokens insted of 8
    private String[] fixInventoryDateSplit(String[] fields) {
        if (fields.length != 9) {
            return fields;
        }

        boolean looksLikeMonthDay = fields[6].matches("^[A-Za-z]{3,9}\\s+\\d{1,2}$");
        boolean looksLikeYear = fields[7].matches("^\\d{4}$");

        if (!looksLikeMonthDay || !looksLikeYear) {
            return fields; // not the date-comma case; leave as-is so it gets flagged
        }

        String[] fixed = new String[8];
        for (int i = 0; i < 6; i++) {
            fixed[i] = fields[i];
        }
        fixed[6] = fields[6] + ", " + fields[7];
        fixed[7] = fields[8];
        return fixed;
    }

    //dealers
    public DealerParseResult parseDealerFile(String filePath) throws IOException {
        DealerParseResult result = new DealerParseResult();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = splitDirtyLine(line);

                if (fields.length != 4) {
                    result.getErrors().add("Line " + lineNumber + ": expected 4 fields but found "
                            + fields.length + " -> skipped. Raw line: \"" + line + "\"");
                    continue;
                }

                try {
                    String id = fields[0];
                    String name = fields[1];
                    String phone = fields[2]; // Dealer's constructor already treats blank as "N/A"
                    String location = fields[3];

                    Dealer dealer = new Dealer(id, name, phone, location);
                    result.getDealers().add(dealer);

                } catch (Exception e) {
                    result.getErrors().add("Line " + lineNumber + ": " + e.getMessage()
                            + " -> skipped. Raw line: \"" + line + "\"");
                }
            }
        }

        return result;
    }
}
