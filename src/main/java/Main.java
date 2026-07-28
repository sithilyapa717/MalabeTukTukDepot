import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        AppState appState = new AppState();
        AppContext.init(appState);

        try {
            appState.loadAll();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Load Error");
            alert.setHeaderText("Could not load depot data");
            alert.setContentText(e.getMessage() + "\n\nCheck that data/inventory.txt and data/dealers.txt exist.");
            alert.showAndWait();
            return;  // do not open empty app
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/main.fxml"));
        Parent root = loader.load();

        stage.setTitle("Malabe Tuk Tuk Depot");
        stage.setScene(new Scene(root, 1100, 720));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}