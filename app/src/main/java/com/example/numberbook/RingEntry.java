package com.example.numberbook;

import com.google.gson.annotations.SerializedName;

public class RingEntry {

    @SerializedName("entry_id")
    private int entryId;

    @SerializedName("display_name")
    private String displayName;

    @SerializedName("dial_number")
    private String dialNumber;

    @SerializedName("entry_origin")
    private String entryOrigin;

    @SerializedName("saved_at")
    private String savedAt;

    public RingEntry() {}

    public RingEntry(String displayName, String dialNumber) {
        this.displayName = displayName;
        this.dialNumber = dialNumber;
    }

    public int getEntryId() { return entryId; }
    public String getDisplayName() { return displayName; }
    public String getDialNumber() { return dialNumber; }
    public String getEntryOrigin() { return entryOrigin; }
    public String getSavedAt() { return savedAt; }

    public void setEntryId(int entryId) { this.entryId = entryId; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setDialNumber(String dialNumber) { this.dialNumber = dialNumber; }
    public void setEntryOrigin(String entryOrigin) { this.entryOrigin = entryOrigin; }
    public void setSavedAt(String savedAt) { this.savedAt = savedAt; }
}