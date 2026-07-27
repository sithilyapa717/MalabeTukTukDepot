import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javafx.scene.control.ListView;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {

    @FXML private TableView<InventoryItem> inventoryTable;

    @FXML private TableColumn<InventoryItem, String> codeColumn;
    @FXML private TableColumn<InventoryItem, String> nameColumn;
    @FXML private TableColumn<InventoryItem, String> dealerColumn;
    @FXML private TableColumn<InventoryItem, Number> priceColumn;
    @FXML private TableColumn<InventoryItem, Number> qtyColumn;
    @FXML private TableColumn<InventoryItem, String> categoryColumn;
    @FXML private TableColumn<InventoryItem, String> dateColumn;

    @FXML private Label totalItemsLabel;
    @FXML private Label totalValueLabel;

    @FXML private TextField codeField;
    @FXML private TextField nameField;
    @FXML private TextField dealerField;
    @FXML private TextField priceField;
    @FXML private TextField qtyField;
    @FXML private TextField categoryField;
    @FXML private TextField dateField;
    @FXML private TextField imageField;

    @FXML private TextField searchCategoryField;
    @FXML private TextField searchMinPriceField;
    @FXML private TextField searchMaxPriceField;
    @FXML private TextField searchKeywordField;
    @FXML private Button searchButton;
    @FXML private Button resetSearchButton;
    @FXML private ListView<String> lowStockList;

    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;

    private ObservableList<InventoryItem> tableData;
    private DateTimeFormatter dateFormatter;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        setupColumns();
        setupSearchDefaults();
        refreshTable();

        inventoryTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldItem, newItem) -> fillFormFromSelection(newItem));

        clearForm();
    }

    private void setupSearchDefaults() {
        searchMinPriceField.setText("0");
        searchMaxPriceField.setText("999999");
    }

    private void setupColumns(){
        codeColumn.setCellValueFactory(row ->new SimpleStringProperty(row.getValue().getCode()));

        nameColumn.setCellValueFactory(row ->new SimpleStringProperty(row.getValue().getName()));

        dealerColumn.setCellValueFactory(row -> {String dealer = row.getValue().getDealerName();
            if (dealer == null) {
                dealer = "";
            }
            return new SimpleStringProperty(dealer);
        });

        priceColumn.setCellValueFactory(row ->new SimpleDoubleProperty(row.getValue().getPrice()));

        qtyColumn.setCellValueFactory(row ->new SimpleIntegerProperty(row.getValue().getQuantity()));

        categoryColumn.setCellValueFactory(row ->new SimpleStringProperty(row.getValue().getCategory()));

        dateColumn.setCellValueFactory(row ->new SimpleStringProperty(row.getValue().getDate().format(dateFormatter)));
    }

    public void refreshTable(){
        InventoryManager manager = AppContext.getState().getInventoryManager();
        List<InventoryItem> sorted = manager.getAllItemsSorted();

        tableData = FXCollections.observableArrayList();
        for (int i = 0; i < sorted.size(); i++) {
            tableData.add(sorted.get(i));
        }

        inventoryTable.setItems(tableData);
        updateTotals(manager);
        refreshLowStockPanel();
    }

    private void updateTotals(InventoryManager manager){
        totalItemsLabel.setText("Total items: " + manager.getTotalItemCount());
        totalValueLabel.setText(String.format("Total value: Rs. %.2f",
                manager.getTotalInventoryValue()));
    }

    private void fillFormFromSelection(InventoryItem item) {
        if (item == null) {
            return;
        }
        codeField.setText(item.getCode());
        nameField.setText(item.getName());

        String dealer = item.getDealerName();
        if (dealer == null) {
            dealer = "";
        }
        dealerField.setText(dealer);

        priceField.setText(String.valueOf(item.getPrice()));
        qtyField.setText(String.valueOf(item.getQuantity()));
        categoryField.setText(item.getCategory());
        dateField.setText(item.getDate().format(dateFormatter));

        String image = item.getImage();
        if (image == null) {
            image = "";
        }
        imageField.setText(image);

        codeField.setDisable(true);
    }

    @FXML
    private void onClear() {
        clearForm();
    }

    private void clearForm() {
        codeField.clear();
        nameField.clear();
        dealerField.clear();
        priceField.clear();
        qtyField.clear();
        categoryField.clear();
        dateField.clear();
        imageField.clear();
        codeField.setDisable(false);
        inventoryTable.getSelectionModel().clearSelection();
    }

    private InventoryItem buildItemFromForm() {
        String code = codeField.getText();
        String name = nameField.getText();

        String dealer = dealerField.getText().trim();
        if (dealer.length() == 0) {
            dealer = null;
        }

        double price = Double.parseDouble(priceField.getText().trim());
        int qty = Integer.parseInt(qtyField.getText().trim());
        String category = categoryField.getText();
        LocalDate date = LocalDate.parse(dateField.getText().trim(), dateFormatter);

        String image = imageField.getText().trim();
        if (image.length() == 0) {
            image = null;
        }

        return new InventoryItem(code, name, dealer, price, qty, category, date, image);
    }

    @FXML
    private void onAdd() {
        try {
            InventoryItem item = buildItemFromForm();
            AppContext.getState().getInventoryManager().addItem(item);
            refreshTable();
            clearForm();
            showInfo("Part added: " + item.getCode());
        } catch (NumberFormatException e) {
            showError("Invalid number", "Price and quantity must be valid numbers.");
        } catch (DateTimeParseException e) {
            showError("Invalid date", "Use format yyyy-MM-dd.");
        } catch (IllegalArgumentException e) {
            showError("Validation error", e.getMessage());
        } catch (IOException e) {
            showError("Save failed", e.getMessage());
        }
    }

    @FXML
    private void onUpdate() {
        try {
            if (codeField.getText().trim().length() == 0) {
                showError("Nothing selected", "Select a row or enter a code to update.");
                return;
            }
            InventoryItem item = buildItemFromForm();
            AppContext.getState().getInventoryManager().updateItem(item);
            refreshTable();
            clearForm();
            showInfo("Part updated: " + item.getCode());
        } catch (NumberFormatException e) {
            showError("Invalid number", "Price and quantity must be valid numbers.");
        } catch (DateTimeParseException e) {
            showError("Invalid date", "Use format yyyy-MM-dd.");
        } catch (IllegalArgumentException e) {
            showError("Validation error", e.getMessage());
        } catch (IOException e) {
            showError("Save failed", e.getMessage());
        }
    }

    @FXML
    private void onDelete() {
        String code = codeField.getText().trim();
        if (code.length() == 0) {
            showError("Nothing selected", "Select a row to delete.");
            return;
        }
        try {
            AppContext.getState().getInventoryManager().deleteItem(code);
            refreshTable();
            clearForm();
            showInfo("Part deleted: " + code);
        } catch (IllegalArgumentException e) {
            showError("Delete failed", e.getMessage());
        } catch (IOException e) {
            showError("Save failed", e.getMessage());
        }
    }

@FXML
private void onSearch() {
    try {
        InventoryManager manager = AppContext.getState().getInventoryManager();

        String category = searchCategoryField.getText();
        String keyword = searchKeywordField.getText();

        double minPrice = Double.parseDouble(searchMinPriceField.getText().trim());
        double maxPrice = Double.parseDouble(searchMaxPriceField.getText().trim());

        if (minPrice > maxPrice) {
            showError("Invalid range", "Min price cannot be greater than max price.");
            return;
        }

        List<InventoryItem> results = manager.search(category, minPrice, maxPrice, keyword);

        tableData = FXCollections.observableArrayList();
        for (int i = 0; i < results.size(); i++) {
            tableData.add(results.get(i));
        }
        inventoryTable.setItems(tableData);

    } catch (NumberFormatException e) {
        showError("Invalid price", "Min and max price must be valid numbers.");
    }
}

    @FXML
    private void onResetSearch() {
        searchCategoryField.clear();
        searchKeywordField.clear();
        searchMinPriceField.setText("0");
        searchMaxPriceField.setText("999999");
        refreshTable();
    }

    private void refreshLowStockPanel() {
        InventoryManager manager = AppContext.getState().getInventoryManager();
        List<InventoryItem> lowStock = manager.getLowStockItems();

        ObservableList<String> lines = FXCollections.observableArrayList();
        for (int i = 0; i < lowStock.size(); i++) {
            InventoryItem item = lowStock.get(i);
            lines.add(item.getCode() + " | qty:" + item.getQuantity() + " | " + item.getName());
        }

        lowStockList.setItems(lines);
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
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}