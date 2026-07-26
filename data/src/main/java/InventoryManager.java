import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private List<InventoryItem> items;
    private String inventoryPath;
    private LegacyDataParser parser;

    public InventoryManager(String data/inventory.txt) {
        this.inventoryPath = "data/inventory.txt";
        this.items = new ArrayList<>();
        this.parser = new LegacyDataParser();
    }