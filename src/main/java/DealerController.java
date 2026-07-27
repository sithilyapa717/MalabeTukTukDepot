import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DealerController implements Initializable {

    @FXML private Label selectedDealerLabel;
    @FXML private TableView<Dealer> dealerTable;

    @FXML private TableColumn<Dealer, String> idColumn;
    @FXML private TableColumn<Dealer, String> nameColumn;
    @FXML private TableColumn<Dealer, String> locationColumn;
    @FXML private TableColumn<Dealer, String> phoneColumn;

    @FXML private Button pickButton;
    @FXML private Button selectButton;

    private ObservableList<Dealer> tableData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupColumns();
        updateSelectedLabel();
    }

    private void setupColumns() {
        idColumn.setCellValueFactory(row ->
                new SimpleStringProperty(row.getValue().getId()));

        nameColumn.setCellValueFactory(row ->
                new SimpleStringProperty(row.getValue().getName()));

        locationColumn.setCellValueFactory(row ->
                new SimpleStringProperty(row.getValue().getLocation()));

        phoneColumn.setCellValueFactory(row ->
                new SimpleStringProperty(row.getValue().getPhone()));
    }

    @FXML
    private void onPickRandom() {
        try {
            DealerManager manager = AppContext.getState().getDealerManager();
            List<Dealer> picked = manager.selectRandomFourUniqueDealers();

            tableData = FXCollections.observableArrayList();
            for (int i = 0; i < picked.size(); i++) {
                tableData.add(picked.get(i));
            }
            dealerTable.setItems(tableData);

        } catch (IllegalStateException e) {
            showError("Cannot pick dealers", e.getMessage());
        }
    }

    @FXML
    private void onSelectDealer() {
        Dealer selected = dealerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No selection", "Click a dealer row first, then Select for Cart.");
            return;
        }

        AppContext.getState().setSelectedDealer(selected);
        updateSelectedLabel();

        PosController pos = AppContext.getPosController();
        if (pos != null) {
            pos.refreshCartTable();
        }

        showInfo("Selected dealer: " + selected.getName());
    }

    private void updateSelectedLabel() {
        Dealer current = AppContext.getState().getSelectedDealer();
        if (current == null) {
            selectedDealerLabel.setText("No dealer selected for cart");
        } else {
            selectedDealerLabel.setText("Selected for cart: " + current.getId()
                    + " - " + current.getName() + " (" + current.getLocation() + ")");
        }
    }

    private void showError(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dealer");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}