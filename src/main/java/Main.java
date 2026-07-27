import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
    InventoryManager manager = new InventoryManager("data/inventory.txt");
    manager.load();

    InventoryItem test = new InventoryItem(
        "P999", "Test Part", "Bajaj", 100.0, 5,
        "ENGINE", LocalDate.of(2024, 1, 1), "test.jpg"
    );
    manager.addItem(test);
    manager.updateItem(new InventoryItem(
        "P999", "Updated Part", "Bajaj", 120.0, 10,
        "ENGINE", LocalDate.of(2024, 1, 1), "test.jpg"
    ));
    manager.deleteItem("P999");
    }
}