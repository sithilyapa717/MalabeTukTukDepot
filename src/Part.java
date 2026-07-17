import java.time.LocalDate;


public class part{
    private String code;
    private String name;
    private String dealerName;
    private double price;
    private int quantity;
    private String category;
    private LocalDate date;
    private String image;

    private static int minimumStockLevel=10;    //assumption

    public Part(String code, String name, String dealerName, Double price, int quantity, String category, LocaDate date, String image){
        setCode (code);
        setName(name);
        this.dealerName = dealerName;           //took a assumption(it can be empty)
        setPrice(price);
        setQuantity(quantity);
        setCategory(category);
        this.date=date;
        this.imagr=image;

    }

    //check data to validate
    public void setCode(String code){
        if(code==null || code.trim().isEmpty()){
            throw new IllegalArgumentException("code can not be empty");
        }
        this.code=code.trim().toUpperCase();
    }
    public void setName(String name){
        if(name=null || name.trim().isEmpty()){
            throe new illegalArgumentException("name can not be empty");
        }
        this.name=name.trim();
    }
    public void setPrice(int price){
        if(price=null || price.trim().isEmpty()){
            throw new IllegalArgumentException("price tab can not be empty");
        }
        if(price<0){
            throw new illegalArgumentException("Price can not be negative");
        }
        this.price=price;
    }
    public void setQuantity(int quantity){
        if(quantity=null || quantity.trim().isEmpty()){
            throw new IllegalArgumentException("quantity can not be empty");
        }
        if(quantity<0){
            throw new illegalArgumentException("quantity can not be negative");
        }
        this.quantity=quantity;
    }
    public void setCategory(String category){
        if(category=null || category.trim().isEmpty()){
            throe new illegalArgumentException("category can not be empty");
        }
        this.category=category.trim().toUpperCase();
    }


    //low stock handlers
    public boolean isLowStock(){
        return quantity<minimumStockLevel;
    }
    public static void setMinimumStockLevel(int level){
        return minimumStockLevel=level;
    }
    public static int getMinimumStockLevel(){
        return minimumStockLevel;
    }


    //getters
    public String getCode(){return code;}
    public String getName(){return name;}
    public String getDealerName(){return dealerName;}
    public double getPrice(){return price;}
    public int getQuantity(){return quantity;}
    public String getCategory(){return category;}
    public LocalDate getdate(){return date;}
    public String getImage(){return image;}

}