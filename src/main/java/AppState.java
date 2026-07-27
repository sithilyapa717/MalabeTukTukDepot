import java.io.IOException;

public class AppState {
    private InventoryManager inventoryManager;
    private DealerManager dealerManager;
    private Cart cart;
    private Dealer selectedDealer;

    public AppState() {
        inventoryManager = new InventoryManager("data/inventory.txt");
        dealerManager = new DealerManager("data/dealers.txt");
        cart = new Cart(inventoryManager);
        selectedDealer = null;
    }

    public void loadAll() throws IOException {
        inventoryManager.load();
        dealerManager.load();
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public DealerManager getDealerManager() {
        return dealerManager;
    }

    public Cart getCart() {
        return cart;
    }

    public Dealer getSelectedDealer() {
        return selectedDealer;
    }

    public void setSelectedDealer(Dealer dealer) {
        selectedDealer = dealer;
    }
}