package com.example.homie.utils;

import android.graphics.Bitmap;

public class DonationRow {
    public Bitmap profilePic;
    public String donationTitle;
    public String donationDescription;
    public int donationAmount;

    public DonationRow(Bitmap image, String name, String description, int money) {
        this.profilePic = image;
        this.donationTitle = name;
        this.donationDescription = description;
        this.donationAmount = money;
    }
}
