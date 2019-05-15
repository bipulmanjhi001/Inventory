package com.broadwaybazar.model;

public class CompanyModel {
    private String id = "";
    private String name = "";
    private boolean checkeda = false;

    public CompanyModel(String id, String name, boolean checkeda) {
        this.id = id;
        this.name = name;
        this.checkeda = checkeda;
    }

    public String getIda() {
        return id;
    }

    public void setIda(String ida) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheckeda() {
        return checkeda;
    }

    public void setCheckeda(boolean checkeda) {
        this.checkeda = checkeda;
    }
}
