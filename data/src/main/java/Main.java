public class Main{
    public static void main(String[] args) throws Exception {
        LegacyDataParser parser = new LegacyDataParser();

        var items = parser.loadInventory("data/inventory.txt", "data/inventory_legacy.txt");
        var dealers = parser.loadDealers("data/dealers.txt", "data/dealers_legacy.txt");

        System.out.println("items: " + items.getItems().size());
        System.out.println("dealers: " + dealers.getDealers().size());
        System.out.println("item errors: " + items.getErrors().size());
        System.out.println("dealer errors: " + dealers.getErrors().size());
    }
}