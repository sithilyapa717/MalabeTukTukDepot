import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private List<InventoryItem> items;
    private String inventoryPath;
    private LegacyDataParser parser;

    public InventoryManager(String inventoryPath) {
        this.inventoryPath =inventoryPath;
        this.items=new ArrayList<>();
        this.parser=new LegacyDataParser();
    }


    public void load() throws IOException {
        items.clear();
        LegacyDataParser.InventoryParseResult result=parser.parseInventoryFile(inventoryPath);
        for (int i=0; i<result.getItems().size(); i++){
            items.add(result.getItems().get(i));
        }
    }
    
    public void save() throws IOException{
        parser.saveInventory(items, inventoryPath);
    }

    public List<InventoryItem> getAllItems(){
        return items;
    }

    public InventoryItem findByCode(String code){
        if (code==null){
            return null;
        }
        for (int i=0; i<items.size(); i++) {
            if (items.get(i).getCode().equalsIgnoreCase(code.trim())){
                return items.get(i);
            }
        }
        return null;
    }

    public void addItem(InventoryItem item) throws IOException{
        if (findByCode(item.getCode()) !=null){
            throw new IllegalArgumentException("Part code already exists: " + item.getCode());
        }
        items.add(item);
        save();
        AuditLogger.logAction("ADD_PART", item.getCode());
    }

    public void updateItem(InventoryItem updated) throws IOException{
        boolean found=false;
        for (int i=0; i<items.size(); i++){
            if (items.get(i).getCode().equalsIgnoreCase(updated.getCode())){
                items.set(i, updated);
                found=true;
                break;
            }
        }
        if (!found){
            throw new IllegalArgumentException("Part not found: " + updated.getCode());
        }
        save();
        AuditLogger.logAction("UPDATE_PART", updated.getCode());
    }

    public void deleteItem(String code) throws IOException{
        boolean found=false;
        for (int i=0; i<items.size(); i++){
            if (items.get(i).getCode().equalsIgnoreCase(code)){
                items.remove(i);
                found=true;
                break;
            }
        }
        if (!found){
            throw new IllegalArgumentException("Part not found: " + code);
        }
        save();
        AuditLogger.logAction("DELETE_PART", code);
    }

        public List<InventoryItem> getAllItemsSorted(){
        List<InventoryItem> copy = new ArrayList<>();
        for (int i=0; i<items.size(); i++) {
            copy.add(items.get(i));
        }
        Sort.sortInventoryByCategoryThenCode(copy);
        return copy;
    }

    public int getTotalItemCount(){
        int total=0;
        for (int i=0; i<items.size(); i++){
            total=total + items.get(i).getQuantity();
        }
        return total;
    }

    public double getTotalInventoryValue() {
        double total=0.0;
        for (int i=0; i<items.size(); i++) {
            InventoryItem item=items.get(i);
            total=total + (item.getPrice() * item.getQuantity());
        }
        return total;
    }

    public List<InventoryItem>getLowStockItems(){
        List<InventoryItem> lowStock = new ArrayList<>();
        for (int i =0; i<items.size(); i++) {
            if (items.get(i).isLowStock()){
                lowStock.add(items.get(i));
            }
        }
        Sort.sortInventoryByCategoryThenCode(lowStock);
        return lowStock;
    }

    public List<InventoryItem> search(String categoryFilter, double minPrice, double maxPrice, String keyword){
        List<InventoryItem> results = new ArrayList<>();

        String category="";
        if (categoryFilter!=null) {
            category = categoryFilter.trim();
        }

        String key="";
        if (keyword!=null) {
            key = keyword.trim().toLowerCase();
        }

        for (int i = 0; i < items.size(); i++){
            InventoryItem item=items.get(i);
            boolean match=true;

            // filter 1 — category (skip if blank)
            if (category.length() > 0){
                if (!item.getCategory().equalsIgnoreCase(category)){
                    match = false;
                }
            }

            // filter 2 — price range
            if (item.getPrice() < minPrice || item.getPrice() > maxPrice){
                match=false;
            }

            // filter 3 — keyword in name, code, or dealer name
            if (key.length()>0) {
                boolean foundInName=item.getName().toLowerCase().contains(key);
                boolean foundInCode=item.getCode().toLowerCase().contains(key);
                String dealer=item.getDealerName();
                if (dealer==null) {
                    dealer="";
                }
                boolean foundInDealer = dealer.toLowerCase().contains(key);

                if (!foundInName && !foundInCode && !foundInDealer){
                    match = false;
                }
            }

            if (match) {
                results.add(item);
            }
        }

        Sort.sortInventoryByCategoryThenCode(results);
        return results;
    }

    public void deductStock(String code, int quantity) throws IOException {
        InventoryItem item = findByCode(code);

        if (item == null) {
            throw new IllegalArgumentException("Part not found: " + code);
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be more than 0");
        }
        if (quantity > item.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock for " + code);
        }

        item.setQuantity(item.getQuantity() - quantity);
        save();
    }


    
}