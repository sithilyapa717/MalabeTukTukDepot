import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.PrintWriter;
import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private InventoryManager manager;
    private Cart cart;

    @BeforeEach
    void setUp() throws IOException {
        PrintWriter w = new PrintWriter("target/test_cart_inventory.txt");
        w.println("P001,Engine Part A,DealerA,1000.0,10,ENGINE,2023-10-12,");
        w.println("P002,Electrical Part B,DealerB,500.0,10,ELECTRICAL,2023-10-12,");
        w.close();

        manager = new InventoryManager("target/test_cart_inventory.txt");
        manager.load();
        cart = new Cart(manager);
    }

    @Test
    void addItemIncreasesCartSize() {
        cart.addItem("P001", 2);
        assertEquals(1, cart.getItems().size());
        assertEquals(2000.0, cart.getSubtotal(), 0.001);
    }

    @Test
    void mergeSameCodeInCart() {
        cart.addItem("P001", 2);
        cart.addItem("P001", 1);
        assertEquals(1, cart.getItems().size());
        assertEquals(3, cart.getItems().get(0).getQuantity());
    }

    @Test
    void notEnoughStockThrows() {
        assertThrows(IllegalArgumentException.class, () -> cart.addItem("P001", 99));
    }

    @Test
    void bulkDiscountAppliedWhenQtyThreeOrMore() {
        cart.addItem("P001", 3);
        // 3 x 1000 = 3000, 5% bulk = 150, after bulk = 2850
        assertEquals(2850.0, cart.calculateSubtotalAfterBulkDiscounts(), 0.001);
    }

    @Test
    void synergyDiscountWhenEngineAndElectrical() {
        cart.addItem("P001", 1); // ENGINE, 1000
        cart.addItem("P002", 1); // ELECTRICAL, 500
        // after bulk = 1500, synergy 10% = 150, final = 1350
        assertEquals(150.0, cart.calculateSynergyDiscountAmount(), 0.001);
        assertEquals(1350.0, cart.calculateFinalTotal(), 0.001);
    }

    @Test
    void checkoutClearsCartAndDeductsStock() throws IOException {
        cart.addItem("P001", 2);
        double total = cart.checkout();

        assertTrue(cart.isEmpty());
        assertEquals(2000.0, total, 0.001);
        assertEquals(8, manager.findByCode("P001").getQuantity());
    }

    @Test
    void emptyCartCheckoutThrows() {
        assertThrows(IllegalStateException.class, () -> cart.checkout());
    }
}