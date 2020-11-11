package com.li.dryfruits.data.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class FeedbackModel {
    private String userId;
    private String orderId;
    private String feedback;
    private String feedbackId;
    public FeedbackModel() {
    }

    public FeedbackModel(String feedbackId,String userId, String orderId, String feedback) {
        this.feedbackId=feedbackId;
        this.userId = userId;
        this.orderId = orderId;
        this.feedback = feedback;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
