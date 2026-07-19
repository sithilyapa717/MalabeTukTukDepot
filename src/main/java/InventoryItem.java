import java.time.LocalDate;

public class InventoryItem{
    private String code;
    private String name;
    private String dealerName;
    private double price;
    private int quantity;
    private String category;
    private LocalDate date;
    private String image;

    private static int minimumStockLevel = 10;    // assumption

    public InventoryItem(String code, String name, String dealerName, double price, int quantity, String category, LocalDate date, String image){
        setCode(code);
        setName(name);
        this.dealerName = dealerName;           // assumption: can be empty
        setPrice(price);
        setQuantity(quantity);
        setCategory(category);
        this.date = date;
        this.image = image;
    }

    // check data to validate
    public void setCode(String code){
        if(code == null || code.trim().isEmpty()){
            throw new IllegalArgumentException("code can not be empty");
        }
        this.code = code.trim().toUpperCase();
    }

    public void setName(String name){
        if(name == null || name.trim().isEmpty()){
            throw new IllegalArgumentException("name can not be empty");
        }
        this.name = name.trim();
    }

    public void setPrice(double price){
        if(price < 0){
            throw new IllegalArgumentException("Price can not be negative");
        }
        this.price = price;
    }

    public void setQuantity(int quantity){
        if(quantity < 0){
            throw new IllegalArgumentException("quantity can not be negative");
        }
        this.quantity = quantity;
    }

    public void setCategory(String category){
        if(category == null || category.trim().isEmpty()){
            throw new IllegalArgumentException("category can not be empty");
        }
        this.category = category.trim().toUpperCase();
    }

    // low stock handlers
    public boolean isLowStock(){
        return quantity < minimumStockLevel;
    }

    public static void setMinimumStockLevel(int level){
        minimumStockLevel = level;
    }

    public static int getMinimumStockLevel(){
        return minimumStockLevel;
    }

    // getters
    public String getCode(){return code;}
    public String getName(){return name;}
    public String getDealerName(){return dealerName;}
    public double getPrice(){return price;}
    public int getQuantity(){return quantity;}
    public String getCategory(){return category;}
    public LocalDate getDate(){return date;}
    public String getImage(){return image;}


    @Override
    public String toString() {
        return code + " | " + name + " | Rs." + price + " | qty:" + quantity + " | " + category;
    }
}