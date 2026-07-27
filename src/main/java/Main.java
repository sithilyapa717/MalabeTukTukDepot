import java.time.LocalDate;
import java.util.List;

public class Main {
        public static void main(String[] args) throws Exception {
            InventoryManager manager = new InventoryManager("data/inventory.txt");
            manager.load();

            Cart cart = new Cart(manager);

            cart.addItem("P001", 3);

            cart.addItem("P004", 1);

            System.out.println("After bulk: Rs." + cart.calculateSubtotalAfterBulkDiscounts());
            System.out.println("Synergy discount: Rs." + cart.calculateSynergyDiscountAmount());
            System.out.println("Final total: Rs." + cart.checkout());

            // verify 
            manager.load();
            System.out.println("P001 qty after checkout: " + manager.findByCode("P001").getQuantity());
    }
}