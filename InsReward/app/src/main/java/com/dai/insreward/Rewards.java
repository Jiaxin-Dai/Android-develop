package com.dai.insreward;

import java.io.Serializable;

public class Rewards implements Comparable<Rewards> , Serializable {
    private String userNameTarget;
    private String userNameSource;
    private String date;
    private String fullName;
    private Integer pointsToRewards;
    private String comment;
    private String passwordSource;

    public Rewards(String userNameTarget, String userNameSource, String date, String fullName, Integer pointsToRewards, String comment, String passwordSource) {
        this.userNameTarget = userNameTarget;
        this.userNameSource = userNameSource;
        this.date = date;
        this.fullName = fullName;
        this.pointsToRewards = pointsToRewards;
        this.comment = comment;
        this.passwordSource = passwordSource;
    }

    public Rewards(String usernameSource, String date, String fullName, Integer pointsToRewards, String comment) {
        this.userNameSource = usernameSource;
        this.date = date;
        this.fullName = fullName;
        this.pointsToRewards = pointsToRewards;
        this.comment = comment;
    }

    public Rewards(String date, String fullName, Integer pointsToRewards, String comment) {
        this.date = date;
        this.fullName = fullName;
        this.pointsToRewards = pointsToRewards;
        this.comment = comment;
    }

    public String getUserNameTarget() {
        return userNameTarget;
    }

    public void setUserNameTarget(String userNameTarget) {
        this.userNameTarget = userNameTarget;
    }

    public String getUserNameSource() {
        return userNameSource;
    }

    public void setUserNameSource(String userNameSource) {
        this.userNameSource = userNameSource;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getPointsToRewards() {
        return pointsToRewards;
    }

    public void setPointsToRewards(Integer pointsToRewards) {
        this.pointsToRewards = pointsToRewards;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPasswordSource() {
        return passwordSource;
    }

    public void setPasswordSource(String passwordSource) {
        this.passwordSource = passwordSource;
    }

    @Override
    public int compareTo(Rewards rewards) {
        return this.getPointsToRewards().compareTo(rewards.getPointsToRewards());
    }


}


