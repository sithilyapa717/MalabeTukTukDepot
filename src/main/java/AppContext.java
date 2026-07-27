public class AppContext {
    private static AppState state;

    public static void init(AppState appState) {
        state = appState;
    }

    public static AppState getState() {
        if (state == null) {
            throw new IllegalStateException("AppContext not initialized");
        }
        return state;
    }

    private static InventoryController inventoryController;

    public static void setInventoryController(InventoryController controller) {
        inventoryController = controller;
    }

    public static InventoryController getInventoryController() {
        return inventoryController;
    }

    private static PosController posController;

    public static void setPosController(PosController controller) {
        posController = controller;
    }

    public static PosController getPosController() {
        return posController;
    }
}