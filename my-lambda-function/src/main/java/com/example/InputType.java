package com.example;

import java.awt.Color;

public class InputType {

    private float threshold;
    private String imageStr;

    // Default constructor (required for deserialization)
    public InputType() {}

    public InputType(String imageStr, float threshold) {
        this.imageStr = imageStr;
        this.threshold = threshold;
    }

    public float getThreshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    public String getImageStr() {
      return imageStr;
    }

    public void setImageStr(String imageStr) {
      this.imageStr = imageStr;
    }
}
