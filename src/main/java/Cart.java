import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class Cart {
    private List<CartItem> items;
    private InventoryManager inventoryManager;

    public Cart(InventoryManager inventoryManager){
        this.inventoryManager = inventoryManager;
        this.items = new ArrayList<>();
    }

    public void addItem(String code, int quantity){
        if (quantity<=0) {
            throw new IllegalArgumentException("Quantity must be more than 0");
        }

        InventoryItem item=inventoryManager.findByCode(code);
        if (item == null){
            throw new IllegalArgumentException("Part not found: "+code);
        }
        if (quantity > item.getQuantity()){
            throw new IllegalArgumentException("Not enough stock for "+code);
        }

        // merge if same code already in cart
        for (int i=0; i<items.size(); i++){
            CartItem existing=items.get(i);
            if (existing.getPartCode().equalsIgnoreCase(code)){
                int newQty=existing.getQuantity() + quantity;
                if (newQty>item.getQuantity()){
                    throw new IllegalArgumentException("Not enough stock for " + code);
                }
                existing.setQuantity(newQty);
                return;
            }
        }

        items.add(new CartItem(
                item.getCode(), item.getName(), item.getPrice(), quantity
        ));
    }

    public void removeItem(String code){
        for (int i = 0; i < items.size(); i++){
            if (items.get(i).getPartCode().equalsIgnoreCase(code)){
                items.remove(i);
                return;
            }
        }
        throw new IllegalArgumentException("Item not in cart: " + code);
    }

    public void clear(){
        items.clear();
    }

    public boolean isEmpty(){
        return items.size()==0;
    }

    public List<CartItem> getItems(){
        return items;
    }

    public double getSubtotal(){
        double total=0.0;
        for (int i=0; i<items.size(); i++) {
            total=total+items.get(i).getLineTotal();
        }
        return total;
    }

    public void ensureNotEmpty(){
        if (items.size()==0) {
            throw new IllegalStateException("Cart is empty");
        }
    }

    public double calculateSubtotalAfterBulkDiscounts(){
        double total=0.0;
        for (int i=0; i<items.size(); i++) {
            total=total+items.get(i).getLineSubtotalAfterDiscount();
        }
        return total;
    }
    public boolean hasEngineAndElectrical(){
        boolean hasEngine=false;
        boolean hasElectrical=false;
        for (int i=0; i<items.size(); i++) {
            InventoryItem inv=inventoryManager.findByCode(items.get(i).getPartCode());
            if (inv.getCategory().equalsIgnoreCase("ENGINE")) {
                hasEngine=true;
            }
            if (inv.getCategory().equalsIgnoreCase("ELECTRICAL")){
                hasElectrical=true;
            }
        }
        return hasEngine && hasElectrical;
    }
    public double calculateSynergyDiscountAmount(){
        if (hasEngineAndElectrical()) {
            return calculateSubtotalAfterBulkDiscounts() *0.10;
        }
        return 0.0;
    }
    public double calculateFinalTotal(){
        return calculateSubtotalAfterBulkDiscounts() - calculateSynergyDiscountAmount();
    }
    public double checkout() throws IOException {
        ensureNotEmpty();
        for (int i=0; i<items.size(); i++){
            CartItem line=items.get(i);
            InventoryItem inv=inventoryManager.findByCode(line.getPartCode());
            if (line.getQuantity()>inv.getQuantity()) {
                throw new IllegalStateException("Not enough stock for " + line.getPartCode());
            }
        }
        double finalTotal = calculateFinalTotal();
        for (int i=0; i<items.size(); i++){
            CartItem line=items.get(i);
            inventoryManager.deductStock(line.getPartCode(), line.getQuantity());
            AuditLogger.log("CHECKOUT", line.getPartCode(), line.getQuantity());
        }
        items.clear();
        return finalTotal;
    }


}