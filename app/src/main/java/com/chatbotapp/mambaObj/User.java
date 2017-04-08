package com.chatbotapp.mambaObj;

import com.chatbotapp.AResponse;

/**
 * Created by aossa on 08.04.2017.
 */

public class User extends AResponse {


    private int id;
    private String name;
    private int userId;
    private int age;
    private String metro;
    private String city;
    private String region;
    private String country;
    private String[] lookFor;
    private String lookForWithAge;
    private int photosCount;
    private String isOnlineString;
    private OnlineStatus onlineStatus;
    private boolean isVip;
    private int distance;
    private String squarePhotoUrl;
    private String gender;
    private Picture photo;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMetro() {
        return metro;
    }

    public void setMetro(String metro) {
        this.metro = metro;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String[] getLookFor() {
        return lookFor;
    }

    public void setLookFor(String[] lookFor) {
        this.lookFor = lookFor;
    }

    public String getLookForWithAge() {
        return lookForWithAge;
    }

    public void setLookForWithAge(String lookForWithAge) {
        this.lookForWithAge = lookForWithAge;
    }

    public int getPhotosCount() {
        return photosCount;
    }

    public void setPhotosCount(int photosCount) {
        this.photosCount = photosCount;
    }

    public String getIsOnlineString() {
        return isOnlineString;
    }

    public void setIsOnlineString(String isOnlineString) {
        this.isOnlineString = isOnlineString;
    }

    public OnlineStatus getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(OnlineStatus onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getSquarePhotoUrl() {
        return squarePhotoUrl;
    }

    public void setSquarePhotoUrl(String squarePhotoUrl) {
        this.squarePhotoUrl = squarePhotoUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Picture getPhoto() {
        return photo;
    }

    public void setPhoto(Picture photo) {
        this.photo = photo;
    }
}
