import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        InventoryManager manager = new InventoryManager("data/inventory.txt");
        manager.load();

        List<InventoryItem> sorted = manager.getAllItemsSorted();
        for (int i = 0; i < sorted.size(); i++) {
            InventoryItem item = sorted.get(i);
            System.out.println(item.getCategory() + " | " + item.getCode() + " | " + item.getName());
        }
    }
}