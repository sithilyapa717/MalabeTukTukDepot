import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws Exception {
        InventoryManager manager = new InventoryManager("data/inventory.txt");
        manager.load();
        System.out.println("loaded: " + manager.getAllItems().size());

        InventoryItem test = new InventoryItem(
                "P999", "Test Part", "Bajaj", 100.0, 5,
                "ENGINE", LocalDate.of(2024, 1, 1), "test.jpg"
        );
        manager.addItem(test);
        System.out.println("after add: " + manager.getAllItems().size());

        InventoryItem updated = new InventoryItem(
                "P999", "Test Part Updated", "Bajaj", 120.0, 10,
                "ENGINE", LocalDate.of(2024, 1, 1), "test.jpg"
        );
        manager.updateItem(updated);

        manager.deleteItem("P999");
        System.out.println("after delete: " + manager.getAllItems().size());
    }
}