package com.grup4.yemektarifapp.Model;

import com.google.gson.annotations.SerializedName;

public class FoodRecipe {
    @SerializedName("index")
    private long index;

    @SerializedName("type")
    private String type;

    @SerializedName("color")
    private String color;

    @SerializedName("enabled")
    private boolean enabled;

    @SerializedName("value")
    private double value;

    // Getters and Setters
    public long getIndex() { return index; }
    public void setIndex(long value) { this.index = value; }

    public String getType() { return type; }
    public void setType(String value) { this.type = value; }

    public String getColor() { return color; }
    public void setColor(String value) { this.color = value; }

    public boolean getEnabled() { return enabled; }
    public void setEnabled(boolean value) { this.enabled = value; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
}
