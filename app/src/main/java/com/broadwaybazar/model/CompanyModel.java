package com.broadwaybazar.model;

public class CompanyModel {
    private String id = "";
    private String name = "";
    private String pcode = "";
    private boolean checkeda = false;

    public CompanyModel(String id, String name, String pcode, boolean checkeda) {
        this.id = id;
        this.name = name;
        this.pcode = pcode;
        this.checkeda = checkeda;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public boolean isCheckeda() {
        return checkeda;
    }

    public void setCheckeda(boolean checkeda) {
        this.checkeda = checkeda;
    }
}
