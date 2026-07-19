package test.java;

public class LegacyDataParserTest {
    
}
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LegacyDataParserTest {

    private static final String INVENTORY_FILE = "src/test/resources/inventory_legacy.txt";
    private static final String DEALER_FILE = "src/test/resources/dealers_legacy.txt";

    @Test
    void allTenInventoryRowsLoadIncludingP003() throws Exception {
        LegacyDataParser parser = new LegacyDataParser();
        LegacyDataParser.ParseOutcome<InventoryItem> result = parser.parseInventoryFile(INVENTORY_FILE);

        assertEquals(0, result.getErrors().size(), "Expected no parse errors: " + result.getErrors());
        assertEquals(10, result.getRecords().size());

        boolean foundP003 = false;
        for (InventoryItem item : result.getRecords()) {
            if (item.getCode().equals("P003")) {
                foundP003 = true;
                assertEquals("205/50-10 Tyre", item.getName());
                assertEquals(LocalDate.of(2023, 10, 15), item.getDate());
                assertEquals(6500.00, item.getPrice(), 0.001);
                assertEquals("", item.getDealerName());
            }
        }
        assertTrue(foundP003, "P003 (semicolon-delimited row with a comma inside its date) must load");
    }

    @Test
    void allEightDealerRowsLoad() throws Exception {
        LegacyDataParser parser = new LegacyDataParser();
        LegacyDataParser.ParseOutcome<Dealer> result = parser.parseDealerFile(DEALER_FILE);

        assertEquals(0, result.getErrors().size(), "Expected no parse errors: " + result.getErrors());
        assertEquals(8, result.getRecords().size());
    }

    @Test
    void missingDealerPhoneDefaultsToNA() throws Exception {
        LegacyDataParser parser = new LegacyDataParser();
        LegacyDataParser.ParseOutcome<Dealer> result = parser.parseDealerFile(DEALER_FILE);

        boolean foundD103 = false;
        for (Dealer d : result.getRecords()) {
            if (d.getId().equals("D103")) {
                foundD103 = true;
                assertEquals("N/A", d.getPhone());
            }
        }
        assertTrue(foundD103);
    }

    @Test
    void currencyPrefixIsStrippedFromPriceCorrectly() throws Exception {
        // Regression test for a real bug caught during development: "Rs. 450"
        // was being misparsed as 0.45 because the dot in "Rs." was mistaken
        // for the decimal point.
        LegacyDataParser parser = new LegacyDataParser();
        LegacyDataParser.ParseOutcome<InventoryItem> result = parser.parseInventoryFile(INVENTORY_FILE);

        for (InventoryItem item : result.getRecords()) {
            if (item.getCode().equals("P006")) {
                assertEquals(450.0, item.getPrice(), 0.001);
            }
            if (item.getCode().equals("P001")) {
                assertEquals(4500.0, item.getPrice(), 0.001);
            }
        }
    }

    @Test
    void mixedDelimitersInSingleLineAreHandled() throws Exception {
        // P005 uses comma, pipe, AND semicolon within the same line.
        LegacyDataParser parser = new LegacyDataParser();
        LegacyDataParser.ParseOutcome<InventoryItem> result = parser.parseInventoryFile(INVENTORY_FILE);

        boolean foundP005 = false;
        for (InventoryItem item : result.getRecords()) {
            if (item.getCode().equals("P005")) {
                foundP005 = true;
                assertEquals("Clutch Cable Bajaj RE", item.getName());
                assertEquals("Bajaj", item.getDealerName());
                assertEquals(LocalDate.of(2024, 2, 1), item.getDate());
            }
        }
        assertTrue(foundP005);
    }

    @Test
    void zeroQuantityIsValidAndCountsAsLowStock() throws Exception {
        // P008 has quantity 0, which is a valid (out of stock) value, not an error.
        LegacyDataParser parser = new LegacyDataParser();
        LegacyDataParser.ParseOutcome<InventoryItem> result = parser.parseInventoryFile(INVENTORY_FILE);

        boolean foundP008 = false;
        for (InventoryItem item : result.getRecords()) {
            if (item.getCode().equals("P008")) {
                foundP008 = true;
                assertEquals(0, item.getQuantity());
                assertTrue(item.isLowStock());
            }
        }
        assertTrue(foundP008);
    }
}
