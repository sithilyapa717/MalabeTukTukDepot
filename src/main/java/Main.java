import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        InventoryManager manager = new InventoryManager("data/inventory.txt");
        manager.load();

        System.out.println("Total pieces: " + manager.getTotalItemCount());
        System.out.println("Total value: Rs." + manager.getTotalInventoryValue());

        System.out.println("\nLow stock:");
        List<InventoryItem> low = manager.getLowStockItems();
        for (int i = 0; i < low.size(); i++) {
            InventoryItem item = low.get(i);
            System.out.println(item.getCode() + " qty:" + item.getQuantity());
        }

        System.out.println("\nSearch ENGINE, price 0-5000, keyword bajaj:");
        List<InventoryItem> found = manager.search("ENGINE", 0, 5000, "bajaj");
        for (int i = 0; i < found.size(); i++) {
            InventoryItem item = found.get(i);
            System.out.println(item.getCode() + " | " + item.getName());
        }
    }
}