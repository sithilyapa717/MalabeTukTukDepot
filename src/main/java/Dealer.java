public class Dealer {
    private String id;
    private String name;
    private String phone;
    private String location;

    // check validity of dealers
    public Dealer(String id, String name, String phone, String location){
        setId(id);
        setName(name);
        setPhone(phone); // assumption: can be empty
        setLocation(location);
    }

    public void setId(String id){
        if(id == null || id.trim().isEmpty()){
            throw new IllegalArgumentException("dealer id can not be empty");
        }
        this.id = id.trim().toUpperCase();
    }

    public void setName(String name){
        if(name == null || name.trim().isEmpty()){
            throw new IllegalArgumentException("Name can not be empty");
        }
        this.name = name.trim();
    }
    public void setPhone(String phone){
        if (phone == null || phone.trim().isEmpty()) {
            this.phone = "N/A";
        } else {
            this.phone = phone.trim();
        }
    }

    public void setLocation(String location){
        if(location == null || location.trim().isEmpty()){
            throw new IllegalArgumentException("dealer location can not be empty");
        }
        this.location = location.trim();
    }

    // getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getLocation() { return location; }



    @Override
    public String toString() {
        return id + " | " + name + " | " + location + " | " + phone;
    }
}