import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class LegacyDataParserTest {

    @Test
    void inventoryFileLoadsTenItems() throws Exception {
        LegacyDataParser parser = new LegacyDataParser();
        LegacyDataParser.InventoryParseResult result =
                parser.parseInventoryFile("src/test/resources/inventory_legacy.txt");

        assertEquals(10, result.getItems().size());
        assertEquals(0, result.getErrors().size());
    }

    @Test
    void dealerFileLoadsEightDealers() throws Exception {
        LegacyDataParser parser = new LegacyDataParser();
        LegacyDataParser.DealerParseResult result =
                parser.parseDealerFile("src/test/resources/dealers_legacy.txt");

        assertEquals(8, result.getDealers().size());
        assertEquals(0, result.getErrors().size());
    }

    @Test
    void p003DateIsCorrect() throws Exception {
        LegacyDataParser parser = new LegacyDataParser();
        LegacyDataParser.InventoryParseResult result =
                parser.parseInventoryFile("src/test/resources/inventory_legacy.txt");

        InventoryItem p003 = null;
        for (int i = 0; i < result.getItems().size(); i++) {
            InventoryItem item = result.getItems().get(i);
            if (item.getCode().equals("P003")) {
                p003 = item;
            }
        }

        assertNotNull(p003);
        assertEquals(LocalDate.of(2023, 10, 15), p003.getDate());
    }

    @Test
    void blankDealerPhoneBecomesNA() throws Exception {
        LegacyDataParser parser = new LegacyDataParser();
        LegacyDataParser.DealerParseResult result =
                parser.parseDealerFile("src/test/resources/dealers_legacy.txt");

        Dealer d103 = null;
        for (int i = 0; i < result.getDealers().size(); i++) {
            Dealer dealer = result.getDealers().get(i);
            if (dealer.getId().equals("D103")) {
                d103 = dealer;
            }
        }

        assertNotNull(d103);
        assertEquals("N/A", d103.getPhone());
    }
}