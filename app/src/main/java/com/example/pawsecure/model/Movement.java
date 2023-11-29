package com.example.pawsecure.model;
import com.google.gson.annotations.SerializedName;

public class Movement {
    private String id;
    private String value;
    @SerializedName("feed_id")
    private int feedId;
    @SerializedName("created_at")
    private String createdAt;

    public Movement(String id, String value, int feedId, String createdAt) {
        this.id = id;
        this.value = value;
        this.feedId = feedId;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getFeedId() {
        return feedId;
    }

    public void setFeedId(int feedId) {
        this.feedId = feedId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
