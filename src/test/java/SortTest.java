import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SortTest {

    @Test
    void inventorySortedByCategoryThenCode() {
        List<InventoryItem> items = new ArrayList<>();
        items.add(new InventoryItem("P002", "B", null, 100, 1, "ENGINE", LocalDate.of(2023, 1, 1), null));
        items.add(new InventoryItem("P001", "A", null, 100, 1, "BRAKES", LocalDate.of(2023, 1, 1), null));
        items.add(new InventoryItem("P003", "C", null, 100, 1, "ENGINE", LocalDate.of(2023, 1, 1), null));

        Sort.sortInventoryByCategoryThenCode(items);

        assertEquals("BRAKES", items.get(0).getCategory());
        assertEquals("P001", items.get(0).getCode());
        assertEquals("ENGINE", items.get(1).getCategory());
        assertEquals("P002", items.get(1).getCode());
        assertEquals("P003", items.get(2).getCode());
    }

    @Test
    void dealersSortedByLocation() {
        List<Dealer> dealers = new ArrayList<>();
        dealers.add(new Dealer("D1", "Shop C", "0771111111", "Malabe"));
        dealers.add(new Dealer("D2", "Shop A", "0772222222", "Kaduwela"));
        dealers.add(new Dealer("D3", "Shop B", "0773333333", "Colombo"));

        Sort.sortDealersByLocation(dealers);

        assertEquals("Colombo", dealers.get(0).getLocation());
        assertEquals("Kaduwela", dealers.get(1).getLocation());
        assertEquals("Malabe", dealers.get(2).getLocation());
    }
}