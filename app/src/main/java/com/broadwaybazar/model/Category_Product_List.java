package com.broadwaybazar.model;

public class Category_Product_List {
    private String id = "";
    private String type = "";
    private boolean checked = false;

    public Category_Product_List(String id, String type, boolean checked) {
        this.id = id;
        this.type = type;
        this.checked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
