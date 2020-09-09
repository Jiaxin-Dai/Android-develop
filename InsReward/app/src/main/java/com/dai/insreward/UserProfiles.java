package com.dai.insreward;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.List;

public class UserProfiles implements Comparable<UserProfiles> , Serializable {
    private String studentId;
    private String username;
    private String password;
    private String oldPassword;
    private String firstName;
    private String lastName;
    private String location;
    private String department;
    private String position;
    private String story;
    private String admin;
    private Integer pointsToAward;
    private String imageBytes;
    private List<Rewards> rewardsList;
    private Integer pointsAwarded;

    public UserProfiles(String username, String password,
                        String firstName, String lastName, String location,
                        String department, String position, String story,
                        String admin, Integer pointsToAward, String imageBytes, List<Rewards> rewardsList){

        this.username = username;
        this.password = password;
        //this.oldPassword = oldPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.department = department;
        this.position = position;
        this.story = story;
        this.admin = admin;
        this.pointsToAward = pointsToAward;
        this.imageBytes = imageBytes;
        this.rewardsList = rewardsList;
    }

    public UserProfiles(String username, String firstName, String lastName,
                        String location, String department, String position,
                        String story, String admin, Integer pointsToAward,
                        String imageBytes) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.department = department;
        this.position = position;
        this.story = story;
        this.admin = admin;
        this.pointsToAward = pointsToAward;
        this.imageBytes = imageBytes;


    }


    public UserProfiles(String username, String firstName, String lastName,
                        String department, String position, String story,
                        Integer pointsAwarded,String imageBytes) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.position = position;
        this.story = story;
        this.pointsAwarded = pointsAwarded;
        this.imageBytes = imageBytes;
    }

    public UserProfiles(String studentID, String firstName, String lastName, String username, String department, String story, String position, String password, Integer pointsToAward, String admin, String imageBytes, String location) {
        this.studentId = studentID;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.department = department;
        this.position = position;
        this.story = story;
        this.admin = admin;
        this.pointsToAward = pointsToAward;
        this.imageBytes = imageBytes;
    }

    public UserProfiles(String username, String firstName, String lastName, String location, String department, String position, String story, String admin, Integer pointsToAward, String imageBytes, List<Rewards> rewardsList) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.department = department;
        this.position = position;
        this.story = story;
        this.admin = admin;
        this.pointsToAward = pointsToAward;
        this.imageBytes = imageBytes;
        this.rewardsList = rewardsList;

    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public List<Rewards> getRewards() {
        return rewardsList;
    }

    public void setRewards(List<Rewards> rewards) {
        this.rewardsList = rewardsList;
    }

    public int countRewards(){
        if(rewardsList == null)
            return 0;
        else
            return rewardsList.size();
    }

    public Integer setPointsAwarded() {
        if(rewardsList == null)
            return 0;

        else{
            pointsAwarded = 0;
            for (int i = 0; i < rewardsList.size(); i++) {

                pointsAwarded += rewardsList.get(i).getPointsToRewards();
            }
            return pointsAwarded;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public Integer getPointsToAward() {
        return pointsToAward;
    }

    public void setPointsToAward(Integer pointsToAward) {
        this.pointsToAward = pointsToAward;
    }

    public String getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(String imageBytes) {
        this.imageBytes = imageBytes;
    }


    @Override
    public int compareTo(UserProfiles s){
        return this.getPointsToAward().compareTo( s.getPointsToAward() );
    }

}
