package com.example.numberbook;

import com.google.gson.annotations.SerializedName;

public class SearchResult {

    @SerializedName("display_name")
    private String displayName;

    @SerializedName("dial_number")
    private String dialNumber;

    @SerializedName("occurrence")
    private int occurrence;

    @SerializedName("percentage")
    private double percentage;

    public String getDisplayName() { return displayName; }
    public String getDialNumber() { return dialNumber; }
    public int getOccurrence() { return occurrence; }
    public double getPercentage() { return percentage; }
}