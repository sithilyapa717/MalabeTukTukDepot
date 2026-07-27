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

    @Test
    void badInventoryLinesAreReported() throws Exception {
        LegacyDataParser parser = new LegacyDataParser();
        LegacyDataParser.InventoryParseResult result =
                parser.parseInventoryFile("src/test/resources/inventory_bad_lines.txt");

        assertEquals(1, result.getItems().size());
        assertEquals("P001", result.getItems().get(0).getCode());
        assertEquals(3, result.getErrors().size());
    }

    @Test
    void badDealerLinesAreReported() throws Exception {
        LegacyDataParser parser = new LegacyDataParser();
        LegacyDataParser.DealerParseResult result =
                parser.parseDealerFile("src/test/resources/dealers_bad_lines.txt");

        assertEquals(1, result.getDealers().size());
        assertEquals("D101", result.getDealers().get(0).getId());
        assertEquals(2, result.getErrors().size());
    }

    @Test
    void rsPriceIsCleaned() throws Exception {
        LegacyDataParser parser = new LegacyDataParser();
        LegacyDataParser.InventoryParseResult result =
                parser.parseInventoryFile("src/test/resources/inventory_legacy.txt");

        InventoryItem p004 = null;
        for (int i = 0; i < result.getItems().size(); i++) {
            if (result.getItems().get(i).getCode().equals("P004")) {
                p004 = result.getItems().get(i);
            }
        }

        assertNotNull(p004);
        assertEquals(850.0, p004.getPrice(), 0.001);
    }

    @Test
    void slashDateIsParsed() throws Exception {
        LegacyDataParser parser = new LegacyDataParser();
        LegacyDataParser.InventoryParseResult result =
                parser.parseInventoryFile("src/test/resources/inventory_legacy.txt");

        InventoryItem p002 = null;
        for (int i = 0; i < result.getItems().size(); i++) {
            if (result.getItems().get(i).getCode().equals("P002")) {
                p002 = result.getItems().get(i);
            }
        }

        assertNotNull(p002);
        assertEquals(LocalDate.of(2023, 5, 12), p002.getDate());
    }

    @Test
    void dashMonthDateIsParsed() throws Exception {
        LegacyDataParser parser = new LegacyDataParser();
        LegacyDataParser.InventoryParseResult result =
                parser.parseInventoryFile("src/test/resources/inventory_legacy.txt");

        InventoryItem p007 = null;
        for (int i = 0; i < result.getItems().size(); i++) {
            if (result.getItems().get(i).getCode().equals("P007")) {
                p007 = result.getItems().get(i);
            }
        }

        assertNotNull(p007);
        assertEquals(LocalDate.of(2023, 8, 15), p007.getDate());
    }

    @Test
    void categoryIsUppercased() throws Exception {
        LegacyDataParser parser = new LegacyDataParser();
        LegacyDataParser.InventoryParseResult result =
                parser.parseInventoryFile("src/test/resources/inventory_legacy.txt");

        InventoryItem p002 = null;
        for (int i = 0; i < result.getItems().size(); i++) {
            if (result.getItems().get(i).getCode().equals("P002")) {
                p002 = result.getItems().get(i);
            }
        }

        assertNotNull(p002);
        assertEquals("BRAKES", p002.getCategory());
    }

    @Test
    void secondBlankPhoneBecomesNA() throws Exception {
        LegacyDataParser parser = new LegacyDataParser();
        LegacyDataParser.DealerParseResult result =
                parser.parseDealerFile("src/test/resources/dealers_legacy.txt");

        Dealer d107 = null;
        for (int i = 0; i < result.getDealers().size(); i++) {
            if (result.getDealers().get(i).getId().equals("D107")) {
                d107 = result.getDealers().get(i);
            }
        }

        assertNotNull(d107);
        assertEquals("N/A", d107.getPhone());
    }

}