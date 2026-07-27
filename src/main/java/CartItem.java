public class CartItem{
    private String partCode;
    private String partName;
    private double unitPrice;
    private int quantity;


    public CartItem(String partCode, String partName, double unitPrice, int quantity){
        setPartCode(partCode);
        setPartName(partName);
        setUnitPrice(unitPrice);
        setQuantity(quantity);
    }
    public void setPartCode(String partCode){
        if (partCode == null || partCode.trim().isEmpty()){
            throw new IllegalArgumentException("Part code can not be empty");
        }
        this.partCode=partCode.trim().toUpperCase();
    }
    public void setPartName(String partName){
        if (partName == null || partName.trim().isEmpty()){
            throw new IllegalArgumentException("Part name can not be empty");
        }
        this.partName=partName.trim();
    }
    public void setUnitPrice(double unitPrice){
        if (unitPrice < 0){
            throw new IllegalArgumentException("Unit price can not be negative");
        }
        this.unitPrice=unitPrice;
    }
    public void setQuantity(int quantity){
        if (quantity <= 0){
            throw new IllegalArgumentException("Quantity can not be zero or negative");
        }
        this.quantity=quantity;
    }
    public double getLineTotal(){
        return unitPrice * quantity;
    }

    public double getLineSubtotalBeforeDiscount(){
        return unitPrice * quantity;
    }

    public double getBulkDiscountAmount(){
        if (quantity>=3) {
            return getLineSubtotalBeforeDiscount()*0.05;
        }
        return 0.0;
    }

    public double getLineSubtotalAfterDiscount(){
        return getLineSubtotalBeforeDiscount() - getBulkDiscountAmount();
    }

    //Getters
    public String getPartCode() { return partCode; }
    public String getPartName() { return partName; }
    public double getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }


@Override
    public String toString() {
        return partCode + " x" + quantity + " @ Rs." + unitPrice + " = Rs." + getLineTotal();
    }
}
