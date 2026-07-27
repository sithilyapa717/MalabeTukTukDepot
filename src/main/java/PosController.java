import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PosController implements Initializable {

    @FXML private Label selectedDealerLabel;
    @FXML private Label subtotalLabel;

    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> codeColumn;
    @FXML private TableColumn<CartItem, String> nameColumn;
    @FXML private TableColumn<CartItem, Number> qtyColumn;
    @FXML private TableColumn<CartItem, Number> priceColumn;
    @FXML private TableColumn<CartItem, Number> lineTotalColumn;

    @FXML private Label bulkDiscountLabel;
    @FXML private Label afterBulkLabel;
    @FXML private Label synergyDiscountLabel;
    @FXML private Label finalTotalLabel;
    @FXML private Button checkoutButton;

    @FXML private TextField partCodeField;
    @FXML private TextField qtyField;
    @FXML private Button addToCartButton;
    @FXML private Button removeButton;
    @FXML private Button clearCartButton;

    private ObservableList<CartItem> tableData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupColumns();
        refreshCartTable();
    }

    private void setupColumns() {
        codeColumn.setCellValueFactory(row ->
                new SimpleStringProperty(row.getValue().getPartCode()));

        nameColumn.setCellValueFactory(row ->
                new SimpleStringProperty(row.getValue().getPartName()));

        qtyColumn.setCellValueFactory(row ->
                new SimpleIntegerProperty(row.getValue().getQuantity()));

        priceColumn.setCellValueFactory(row ->
                new SimpleDoubleProperty(row.getValue().getUnitPrice()));

        lineTotalColumn.setCellValueFactory(row ->
                new SimpleDoubleProperty(row.getValue().getLineTotal()));
    }

    @FXML
    private void onAddToCart() {
        try {
            if (AppContext.getState().getSelectedDealer() == null) {
                showError("No dealer", "Select a dealer on the Dealers tab first.");
                return;
            }

            String code = partCodeField.getText().trim();
            int qty = Integer.parseInt(qtyField.getText().trim());

            if (code.length() == 0) {
                showError("Missing code", "Enter a part code.");
                return;
            }

            Cart cart = AppContext.getState().getCart();
            cart.addItem(code, qty);

            partCodeField.clear();
            qtyField.clear();
            refreshCartTable();

        } catch (NumberFormatException e) {
            showError("Invalid quantity", "Quantity must be a whole number.");
        } catch (IllegalArgumentException e) {
            showError("Cannot add", e.getMessage());
        }
    }

    @FXML
    private void onRemoveItem() {
        CartItem selected = cartTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No selection", "Select a cart row to remove.");
            return;
        }
        try {
            AppContext.getState().getCart().removeItem(selected.getPartCode());
            refreshCartTable();
        } catch (IllegalArgumentException e) {
            showError("Remove failed", e.getMessage());
        }
    }

    @FXML
    private void onClearCart() {
        AppContext.getState().getCart().clear();
        refreshCartTable();
    }

    public void refreshCartTable() {
        Cart cart = AppContext.getState().getCart();
        List<CartItem> items = cart.getItems();

        tableData = FXCollections.observableArrayList();
        for (int i = 0; i < items.size(); i++) {
            tableData.add(items.get(i));
        }

        cartTable.setItems(tableData);
        updateReceiptLabels();
        updateDealerLabel();
    }

    private void updateDealerLabel() {
        Dealer dealer = AppContext.getState().getSelectedDealer();
        if (dealer == null) {
            selectedDealerLabel.setText("No dealer selected — pick one on Dealers tab");
        } else {
            selectedDealerLabel.setText("Dealer: " + dealer.getId()
                    + " - " + dealer.getName());
        }
    }

    private void showError(String header, String message){
        Alert alert=new Alert(Alert.AlertType.ERROR);
        alert.setTitle("POS");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateReceiptLabels(){
        Cart cart = AppContext.getState().getCart();

        double subtotal=cart.getSubtotal();
        double afterBulk=cart.calculateSubtotalAfterBulkDiscounts();
        double bulkDiscount=subtotal - afterBulk;
        double synergy=cart.calculateSynergyDiscountAmount();
        double finalTotal = cart.calculateFinalTotal();


        subtotalLabel.setText(String.format("Subtotal: Rs. %.2f", subtotal));
        bulkDiscountLabel.setText(String.format("Bulk discount: Rs. %.2f", bulkDiscount));
        afterBulkLabel.setText(String.format("After bulk: Rs. %.2f", afterBulk));

        if (synergy>0) {
            synergyDiscountLabel.setText(String.format("Synergy (10%%): Rs. %.2f", synergy));
        } else {
            synergyDiscountLabel.setText("Synergy (10%): Rs. 0.00");
        }

        finalTotalLabel.setText(String.format("FINAL TOTAL: Rs. %.2f", finalTotal));
    }
    @FXML
    private void onPreviewCheckout() {
        if (AppContext.getState().getSelectedDealer() == null) {
            showError("No dealer", "Select a dealer on the Dealers tab first.");
            return;
        }

        Cart cart = AppContext.getState().getCart();

        try {
            cart.ensureNotEmpty();
        } catch (IllegalStateException e) {
            showError("Empty cart", "Add items before checkout.");
            return;
        }

        double subtotal = cart.getSubtotal();
        double afterBulk = cart.calculateSubtotalAfterBulkDiscounts();
        double bulkDiscount = subtotal - afterBulk;
        double synergy = cart.calculateSynergyDiscountAmount();
        double finalTotal = cart.calculateFinalTotal();

        Dealer dealer = AppContext.getState().getSelectedDealer();

        String receipt = "Dealer: " + dealer.getName() + "\n\n"
                + String.format("Subtotal:        Rs. %.2f\n", subtotal)
                + String.format("Bulk discount:   Rs. %.2f\n", bulkDiscount)
                + String.format("After bulk:      Rs. %.2f\n", afterBulk)
                + String.format("Synergy (10%%):  Rs. %.2f\n", synergy)
                + String.format("FINAL TOTAL:     Rs. %.2f", finalTotal);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Checkout Preview");
        alert.setHeaderText("Receipt preview (not processed yet)");
        alert.setContentText(receipt);
        alert.showAndWait();
    }
}