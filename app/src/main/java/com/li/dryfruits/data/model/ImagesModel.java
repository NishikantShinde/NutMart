package com.li.dryfruits.data.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ImagesModel {

    private String imageId;
    private String data;

    public ImagesModel(String imageId, String data) {
        this.imageId = imageId;
        this.data = data;
    }

    public ImagesModel() {
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
