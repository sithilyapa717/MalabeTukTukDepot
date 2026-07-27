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
}