package com.li.dryfruits.data.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class OrdersModel implements Parcelable {

    private String orderId;
    private String userId;
    private OrderStatus orderStatus;
    private ArrayList<ProductModel> productModelArrayList;
    private String orderCreatedOn;
    private String orderAmount;
    private String shipmentAddress;
    private String estimatedTime;


    public OrdersModel() {
    }

    public OrdersModel(String orderId, String userId, OrderStatus orderStatus, ArrayList<ProductModel> productModelArrayList, String orderCreatedOn, String orderAmount, String shipmentAddress, String estimatedTime) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.productModelArrayList = productModelArrayList;
        this.estimatedTime = estimatedTime;
        this.shipmentAddress=shipmentAddress;
        this.orderAmount=orderAmount;
        this.orderCreatedOn=orderCreatedOn;
    }

    protected OrdersModel(Parcel in) {
        readFromIn(in);
    }

    private void readFromIn(Parcel in) {
        Bundle bundle=in.readBundle();
        if(bundle!=null) {
            orderId=bundle.getString("orderId");
            userId=bundle.getString("userId");
            orderCreatedOn=bundle.getString("orderCreatedOn");
            orderAmount=bundle.getString("orderAmount");
            shipmentAddress=bundle.getString("shipmentAddress");
            estimatedTime=bundle.getString("estimatedTime");
            orderStatus=OrderStatus.fromInt(bundle.getInt("orderStatus"));
        }
    }

    public static final Creator<OrdersModel> CREATOR = new Creator<OrdersModel>() {
        @Override
        public OrdersModel createFromParcel(Parcel in) {
            return new OrdersModel(in);
        }

        @Override
        public OrdersModel[] newArray(int size) {
            return new OrdersModel[size];
        }
    };

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public ArrayList<ProductModel> getProductModelArrayList() {
        return productModelArrayList;
    }

    public void setProductModelArrayList(ArrayList<ProductModel> productModelArrayList) {
        this.productModelArrayList = productModelArrayList;
    }

    public String getOrderCreatedOn() {
        return orderCreatedOn;
    }

    public void setOrderCreatedOn(String orderCreatedOn) {
        this.orderCreatedOn = orderCreatedOn;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getShipmentAddress() {
        return shipmentAddress;
    }

    public void setShipmentAddress(String shipmentAddress) {
        this.shipmentAddress = shipmentAddress;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle=new Bundle();
        bundle.putString("orderId",orderId);
        bundle.putString("userId",userId);
        bundle.putString("orderCreatedOn",orderCreatedOn);
        bundle.putString("orderAmount",orderAmount);
        bundle.putString("shipmentAddress",shipmentAddress);
        bundle.putString("estimatedTime",estimatedTime);
        bundle.putInt("orderStatus",orderStatus.getId());
        dest.writeBundle(bundle);
    }
}
