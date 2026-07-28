import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import java.io.PrintWriter;

class DealerManagerTest {

    private DealerManager manager;

    @BeforeEach
    void setUp() throws IOException {
        manager = new DealerManager("src/test/resources/dealers_legacy.txt");
        manager.load();
    }

    @Test
    void loadsEightDealers() {
        assertEquals(8, manager.getAllDealers().size());
    }

    @Test
    void randomFourReturnsFourUniqueDealers() {
        List<Dealer> picked = manager.selectRandomFourUniqueDealers();
        assertEquals(4, picked.size());

        Set<String> ids = new HashSet<>();
        for (int i = 0; i < picked.size(); i++) {
            ids.add(picked.get(i).getId());
        }
        assertEquals(4, ids.size());
    }

    @Test
    void randomFourSortedByLocation() {
        List<Dealer> picked = manager.selectRandomFourUniqueDealers();

        for (int i = 0; i < picked.size() - 1; i++) {
            String loc1 = picked.get(i).getLocation();
            String loc2 = picked.get(i + 1).getLocation();
            assertTrue(loc1.compareToIgnoreCase(loc2) <= 0);
        }
    }

    @Test
    void notEnoughDealersThrows() throws IOException {
        PrintWriter w = new PrintWriter("target/test_few_dealers.txt");
        w.println("D101,Shop A,0771111111,Malabe");
        w.println("D102,Shop B,0772222222,Kaduwela");
        w.close();

        DealerManager small = new DealerManager("target/test_few_dealers.txt");
        small.load();

        assertThrows(IllegalStateException.class, () -> small.selectRandomFourUniqueDealers());
    }
}