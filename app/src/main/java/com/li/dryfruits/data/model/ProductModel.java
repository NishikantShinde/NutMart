package com.li.dryfruits.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ProductModel implements Parcelable {

    private String productId;
    private String productName;
    private String imageId;
    private Float rate;
    private float weight;
    private String measurement;
    private String type;
    private boolean isAdded;
    private int quantity=1;
    public ProductModel() {
    }

    public ProductModel(String productId, String productName, String imageId, Float rate, float weight,String measurement, String type) {
        this.productId = productId;
        this.productName = productName;
        this.imageId = imageId;
        this.rate = rate;
        this.weight=weight;
        this.measurement=measurement;
        this.type=type;
    }

    protected ProductModel(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        imageId = in.readString();
        if (in.readByte() == 0) {
            rate = null;
        } else {
            rate = in.readFloat();
        }
        weight = in.readFloat();
        measurement = in.readString();
        isAdded = in.readByte() != 0;
        quantity = in.readInt();
        type=in.readString();
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public boolean getAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productId);
        parcel.writeString(productName);
        parcel.writeString(imageId);
        if (rate == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(rate);
        }
        parcel.writeFloat(weight);
        parcel.writeString(measurement);
        parcel.writeByte((byte) (isAdded ? 1 : 0));
        parcel.writeInt(quantity);
        parcel.writeString(type);
    }
}

