package com.li.dryfruits.data.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class CartModel {
    private String cartId;
    private ArrayList<ProductModel> productModelArrayList;
    private boolean isActive;
    private String userId;

    public CartModel() {
    }

    public CartModel(String cartId, ArrayList<ProductModel> productModelArrayList, boolean isActive, String userId) {
        this.cartId = cartId;
        this.productModelArrayList = productModelArrayList;
        this.isActive = isActive;
        this.userId = userId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public ArrayList<ProductModel> getProductModelArrayList() {
        return productModelArrayList;
    }

    public void setProductModelArrayList(ArrayList<ProductModel> productModelArrayList) {
        this.productModelArrayList = productModelArrayList;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}