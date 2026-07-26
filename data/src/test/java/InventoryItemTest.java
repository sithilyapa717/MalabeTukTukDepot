import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class InventoryItemTest {

    @Test
    void constructorTrimsAndUppercasesCode() {
        InventoryItem item = new InventoryItem(" p001 ", "Brake Pad", "ABC Traders",
                1500.0, 20, "brake", LocalDate.of(2023, 5, 12), null);
        assertEquals("P001", item.getCode());
        assertEquals("BRAKE", item.getCategory());
    }

    @Test
    void negativePriceIsRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                new InventoryItem("P002", "Clutch Plate", "XYZ", -5.0, 10, "ENGINE",
                        LocalDate.now(), null));
    }

    @Test
    void lowStockDetectionUsesThreshold() {
        InventoryItem.setMinimumStockLevel(10);
        InventoryItem lowItem = new InventoryItem("P003", "Chain", "ABC", 500.0, 5,
                "ENGINE", LocalDate.now(), null);
        assertTrue(lowItem.isLowStock());

        InventoryItem okItem = new InventoryItem("P004", "Tyre", "ABC", 4500.0, 15,
                "TYRE", LocalDate.now(), null);
        assertFalse(okItem.isLowStock());
    }

    @Test
    void emptyNameIsRejected() {
        assertThrows(IllegalArgumentException.class, () ->
                new InventoryItem("P005", "  ", "ABC", 100.0, 10, "ENGINE",
                        LocalDate.now(), null));
    }
}
