import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class DealerManager {
    private List<Dealer> dealers;
    private String dealerPath;
    private LegacyDataParser parser;
    private Random random;

    public DealerManager(String dealerPath) {
        this.dealerPath = dealerPath;
        this.dealers = new ArrayList<>();
        this.parser = new LegacyDataParser();
        this.random = new Random();
    }

    public void load() throws IOException {
        dealers.clear();
        LegacyDataParser.DealerParseResult result =
                parser.parseDealerFile(dealerPath);
        for (int i = 0; i < result.getDealers().size(); i++) {
            dealers.add(result.getDealers().get(i));
        }
    }

    public List<Dealer> getAllDealers() {
        return dealers;
    }

    public List<Dealer> selectRandomFourUniqueDealers(){
        if (dealers.size() < 4) {
            throw new IllegalStateException("Need at least 4 dealers");
        }

        List<Dealer> selected = new ArrayList<>();

        while (selected.size()<4) {
            int index = random.nextInt(dealers.size());
            Dealer picked = dealers.get(index);

            boolean alreadyPicked = false;
            for (int i = 0; i < selected.size(); i++) {
                if (selected.get(i).getId().equals(picked.getId())){
                    alreadyPicked = true;
                    break;
                }
            }

            if (!alreadyPicked){
                selected.add(picked);
            }
        }

        Sort.sortDealersByLocation(selected);
        return selected;
    }
}


