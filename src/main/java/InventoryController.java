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

    private ObservableList<InventoryItem> tableData;
    private DateTimeFormatter dateFormatter;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        setupColumns();
        refreshTable();
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
        InventoryManager manager=AppContext.getState().getInventoryManager();
        List<InventoryItem>sorted=manager.getAllItemsSorted();

        tableData=FXCollections.observableArrayList();
        for (int i=0; i<sorted.size(); i++){
            tableData.add(sorted.get(i));
        }

        inventoryTable.setItems(tableData);
        updateTotals(manager);
    }

    private void updateTotals(InventoryManager manager){
        totalItemsLabel.setText("Total items: " + manager.getTotalItemCount());
        totalValueLabel.setText(String.format("Total value: Rs. %.2f",
                manager.getTotalInventoryValue()));
    }
}