package com.example.demo.Models;

public class cartModel {

    int image;
    String RecommendPrice;

    public cartModel(int image, String recommendPrice) {
        this.image = image;
        RecommendPrice = recommendPrice;
    }

    public String getRecommendPrice() {
        return RecommendPrice;
    }

    public void setRecommendPrice(String recommendPrice) {
        RecommendPrice = recommendPrice;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
