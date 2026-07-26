

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

    //save inventry
    public void saveInventory(List<InventoryItem> items, String path) throws IOException{
        PrintWriter writer=new PrintWriter(new FileWriter(path));
        for(int i=0; i<items.size(); i++){
            InventoryItem item=items.get(i);
            String image=item.getImage();
            if(image==null){
                image="";
            }
            String dealer=item.getDealerName();
            if(dealer==null){
                dealer="";
            }
            writer.println(
                item.getCode()+","+
                item.getName()+","+
                dealer+","+
                item.getPrice()+","+
                item.getQuantity()+","+
                item.getCategory()+","+
                item.getDate().toString()+","+
                image
            );
        }
        writer.close();
    }

    //load inventory
    public InventoryParseResult loadInventory(String cleanPath, String legacyPath) throws IOException {
        File clean = new File(cleanPath);
        if (clean.exists()) {
            return parseInventoryFile(cleanPath);
        }
        InventoryParseResult result = parseInventoryFile(legacyPath);
        saveInventory(result.getItems(), cleanPath);
        return result;
    }

    //save dealers
    public void saveDealers(List<Dealer> dealers, String path) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(path));
        for (int i = 0; i < dealers.size(); i++) {
            Dealer d = dealers.get(i);
            writer.println(
                d.getId() + "," +
                d.getName() + "," +
                d.getPhone() + "," +
                d.getLocation()
            );
        }
        writer.close();
    }

    //load dealers
    public DealerParseResult loadDealers(String cleanPath, String legacyPath) throws IOException {
        File clean = new File(cleanPath);
        if (clean.exists()) {
            return parseDealerFile(cleanPath);
        }
        DealerParseResult result = parseDealerFile(legacyPath);
        saveDealers(result.getDealers(), cleanPath);
        return result;
    }


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

                String[] fields = splitUnLine(line);
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
                    double price = tryPriceFormat(fields[3]);
                    int quantity = Integer.parseInt(fields[4]);
                    String category = fields[5];
                    LocalDate date = tryDateDormat(fields[6]);
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
            return fields; // not the date-comma case
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

                String[] fields = splitUnLine(line);

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


    // filtering messed data
    private String[] splitUnLine(String line){
        String[] raw=line.split(separators, -1);
        String[] fields=new String[raw.length];
        for(int i=0; i<raw.length; i++){
            fields[i]=raw[i].trim();
        }
        return fields;
    }

    private double tryPriceFormat(String raw){
        if(raw == null || raw.trim().isEmpty()){
            throw new IllegalArgumentException("Price is empty");
        }
        String cleaned=raw.trim().replace("Rs.","").replace("Rs","").replace(" ","");
        return Double.parseDouble(cleaned);
    }

    private LocalDate tryDateDormat(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            throw new IllegalArgumentException("date is empty");
        }
        String text = raw.trim();
        for (int i = 0; i < dateFormats.length; i++) {
            try {
                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern(dateFormats[i], Locale.ENGLISH);
                return LocalDate.parse(text, formatter);
            } catch (DateTimeParseException ignored) {
                // try next format
            }
        }
        throw new IllegalArgumentException("unrecognised date: " + text);
    }
}