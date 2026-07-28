import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class InventoryManagerTest {

    private InventoryManager manager;

    @BeforeEach
    void setUp() throws IOException {
        manager = new InventoryManager("src/test/resources/test_inventory.txt");
        manager.load();
    }

    @Test
    void findByCodeIgnoresCase() {
        InventoryItem item = manager.findByCode("p001");
        assertNotNull(item);
        assertEquals("P001", item.getCode());
    }

    @Test
    void searchByCategory() {
        var results = manager.search("ENGINE", 0, 999999, "");
        assertEquals(2, results.size());
    }

    @Test
    void searchByKeywordInName() {
        var results = manager.search("", 0, 999999, "electrical");
        assertEquals(1, results.size());
        assertEquals("P002", results.get(0).getCode());
    }

    @Test
    void searchByPriceRange() {
        var results = manager.search("", 400, 600, "");
        assertEquals(1, results.size());
        assertEquals("P002", results.get(0).getCode());
    }

    @Test
    void lowStockFindsItemsBelowTen() {
        var low = manager.getLowStockItems();
        assertEquals(2, low.size());
        assertEquals("P003", low.get(0).getCode());
        assertEquals("P004", low.get(1).getCode());
    }

    @Test
    void totalItemCountSumsQuantities() {
        assertEquals(28, manager.getTotalItemCount()); // 10+10+5+3
    }

    @Test
    void getItemsByDealerName() {
        var results = manager.getItemsByDealerName("DealerA");
        assertEquals(1, results.size());
        assertEquals("P001", results.get(0).getCode());
    }

    @Test
    void deductStockReducesQuantity() throws IOException {
        PrintWriter w = new PrintWriter("target/test_deduct_inventory.txt");
        w.println("P001,Engine Part A,DealerA,1000.0,10,ENGINE,2023-10-12,");
        w.close();

        InventoryManager temp = new InventoryManager("target/test_deduct_inventory.txt");
        temp.load();
        temp.deductStock("P001", 4);

        assertEquals(6, temp.findByCode("P001").getQuantity());
    }
}