package com.chatbotapp.mambaObj;

import com.chatbotapp.AResponse;

/**
 * Created by aossa on 08.04.2017.
 */

public class Picture extends AResponse {

    private int id  ;
    private String name  ;
    private boolean erotic;
    private String smallPhotoUrl  ;
    private String mediumPhotoUrl ;
    private String hugePhotoUrl  ;
    private String largePhotoUrl  ;
    private String squarePhotoUrl ;
    private String squareSmallPhotoUrl ;
    private int ratingId ;
    private int albumId ;
    private int width  ;
    private int height  ;
    //private String default  ;
    private String squareLargePhotoUrl  ;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isErotic() {
        return erotic;
    }

    public void setErotic(boolean erotic) {
        this.erotic = erotic;
    }

    public String getSmallPhotoUrl() {
        return smallPhotoUrl;
    }

    public void setSmallPhotoUrl(String smallPhotoUrl) {
        this.smallPhotoUrl = smallPhotoUrl;
    }

    public String getMediumPhotoUrl() {
        return mediumPhotoUrl;
    }

    public void setMediumPhotoUrl(String mediumPhotoUrl) {
        this.mediumPhotoUrl = mediumPhotoUrl;
    }

    public String getHugePhotoUrl() {
        return hugePhotoUrl;
    }

    public void setHugePhotoUrl(String hugePhotoUrl) {
        this.hugePhotoUrl = hugePhotoUrl;
    }

    public String getLargePhotoUrl() {
        return largePhotoUrl;
    }

    public void setLargePhotoUrl(String largePhotoUrl) {
        this.largePhotoUrl = largePhotoUrl;
    }

    public String getSquarePhotoUrl() {
        return squarePhotoUrl;
    }

    public void setSquarePhotoUrl(String squarePhotoUrl) {
        this.squarePhotoUrl = squarePhotoUrl;
    }

    public String getSquareSmallPhotoUrl() {
        return squareSmallPhotoUrl;
    }

    public void setSquareSmallPhotoUrl(String squareSmallPhotoUrl) {
        this.squareSmallPhotoUrl = squareSmallPhotoUrl;
    }

    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getSquareLargePhotoUrl() {
        return squareLargePhotoUrl;
    }

    public void setSquareLargePhotoUrl(String squareLargePhotoUrl) {
        this.squareLargePhotoUrl = squareLargePhotoUrl;
    }
}
