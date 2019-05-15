package com.broadwaybazar.model;

public class Company_Product_List {
    private String ids = "";
    private String types = "";
    private boolean checkeds = false;

    public Company_Product_List(String ids, String types, boolean checkeds) {
        this.ids = ids;
        this.types = types;
        this.checkeds = checkeds;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public boolean isCheckeds() {
        return checkeds;
    }

    public void setCheckeds(boolean checkeds) {
        this.checkeds = checkeds;
    }
}
