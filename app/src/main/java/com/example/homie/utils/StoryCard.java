package com.example.homie.utils;

import android.graphics.Bitmap;

public class StoryCard {
    public Bitmap backgroundImage;
    public String name;
    public String description;
    public int moneyRaised;
    public int moneyRaisedGoal;
    public int numLikes;
    public int numKarma;

    public StoryCard(Bitmap image, String name, String description, int moneyRaised, int moneyRaisedGoal, int numLikes, int numKarma) {
        this.backgroundImage = image;
        this.name = name;
        this.description = description;
        this.moneyRaised = moneyRaised;
        this.moneyRaisedGoal = moneyRaisedGoal;
        this.numLikes = numLikes;
        this.numKarma = numKarma;
    }
}