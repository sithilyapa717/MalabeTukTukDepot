public class dealer{
    private String id;
    private String name;
    private String phone;
    private String location;


    //check validity of dealers
    public Dealer(String id, String name, String phone, String location){
        setId(id);
        setName(name);
        this.phone=(phone==null)?"N/A":phone.trim();    //not nessasary for it to implement
        setLocation(location);
    }
    public void setId(String id){
        if(id==null || id.trim().isEmpty()){
            throw illegalSrgumentException("dealer can not be empty");
        }
        this.id=id.trim().toUpperCase();
    }
    public void setName(String name){
        if(name==null || name.trim().isEmpty()){
            throw illegalArgumentException("Name can not be empty");
        }
        this.name=name.trim();
    }
    public void setLocation(String location){
        if(location==null || location.trim().isEmpty()){
            throw illegalArgumentException("dealer location can not be empty");
        }
        this.location=location.trim();
    }



    //getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getLocation() { return location; }
}