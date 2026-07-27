import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        DealerManager manager = new DealerManager("data/dealers.txt");
        manager.load();
        System.out.println("All dealers: " + manager.getAllDealers().size());

        List<Dealer> picked = manager.selectRandomFourUniqueDealers();
        System.out.println("\nRandom 4 (sorted by location):");
        for (int i = 0; i < picked.size(); i++) {
            Dealer d = picked.get(i);
            System.out.println(d.getId() + " | " + d.getName() + " | " + d.getLocation());
        }
    }
}