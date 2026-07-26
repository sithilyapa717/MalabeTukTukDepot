import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private List<InventoryItem> items;
    private String inventoryPath;
    private LegacyDataParser parser;

    public InventoryManager(String inventoryPath) {
        this.inventoryPath = inventoryPath;
        this.items = new ArrayList<>();
        this.parser = new LegacyDataParser();
    }


    public void load() throws IOException {
        items.clear();
        LegacyDataParser.InventoryParseResult result =
                parser.parseInventoryFile(inventoryPath);
        for (int i = 0; i < result.getItems().size(); i++) {
            items.add(result.getItems().get(i));
        }
    }
    
    public void save() throws IOException {
        parser.saveInventory(items, inventoryPath);
    }

    public List<InventoryItem> getAllItems() {
        return items;
    }

    public InventoryItem findByCode(String code) {
        if (code == null) {
            return null;
        }
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getCode().equalsIgnoreCase(code.trim())) {
                return items.get(i);
            }
        }
        return null;
    }

    public void addItem(InventoryItem item) throws IOException {
        if (findByCode(item.getCode()) != null) {
            throw new IllegalArgumentException("Part code already exists: " + item.getCode());
        }
        items.add(item);
        save();
    }

    public void updateItem(InventoryItem updated) throws IOException {
        boolean found = false;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getCode().equalsIgnoreCase(updated.getCode())) {
                items.set(i, updated);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("Part not found: " + updated.getCode());
        }
        save();
    }

    public void deleteItem(String code) throws IOException {
        boolean found = false;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getCode().equalsIgnoreCase(code)) {
                items.remove(i);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("Part not found: " + code);
        }
        save();
    }
}