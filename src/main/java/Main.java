import java.time.LocalDate;
import java.util.List;

public class Main {
        public static void main(String[] args) throws Exception {
        InventoryManager manager = new InventoryManager("data/inventory.txt");
        manager.load();

        Cart cart = new Cart(manager);

        cart.addItem("P001", 2);
        cart.addItem("P001", 1);  // merge → 3 total
        System.out.println("Cart lines: " + cart.getItems().size());  // 1
        System.out.println("Subtotal: Rs." + cart.getSubtotal());

        cart.removeItem("P001");
        System.out.println("Empty? " + cart.isEmpty());  // true

       
        try {
            cart.ensureNotEmpty();
        } catch (IllegalStateException e) {
            System.out.println("Empty cart blocked: " + e.getMessage());
        }

        
        try {
            cart.addItem("P008", 1);
        } catch (IllegalArgumentException e) {
            System.out.println("Stock blocked: " + e.getMessage());
        }
    }
}