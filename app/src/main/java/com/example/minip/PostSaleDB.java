package com.example.minip;

public class PostSaleDB {

    private String username;
    private String userblock;
    private String userflat;
    private String useremail;
    private String prod_cost;
    private String prod_name;
    private String prod_desc;
    private String imageId;

    public PostSaleDB() {
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserblock() {
        return userblock;
    }

    public void setUserblock(String userblock) {
        this.userblock = userblock;
    }

    public String getUserflat() {
        return userflat;
    }

    public void setUserflat(String userflat) {
        this.userflat = userflat;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getProd_cost() {
        return prod_cost;
    }

    public void setProd_cost(String prod_cost) {
        this.prod_cost = prod_cost;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_desc() {
        return prod_desc;
    }

    public void setProd_desc(String prod_desc) {
        this.prod_desc = prod_desc;
    }
}
