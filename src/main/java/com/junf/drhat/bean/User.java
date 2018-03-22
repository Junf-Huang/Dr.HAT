package com.junf.drhat.bean;



import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

//用户类
@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  //产生自增表

    private Integer uid;

    @Column(unique=true)
    @NotBlank(message = "号码不能空")
    private String moNum;

    public String getMoNum() {
        return moNum;
    }

    public void setMoNum(String moNum) {
        this.moNum = moNum;
    }

    private String userName;

    @NotBlank(message = "密码不能空")
    private String password;

    private String profilePhoto;

    private boolean sex;            //1为男，0为女

    private int integration;        //积分

    private String description;//自我描述

    public User() {
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public int getIntegration() {
        return integration;
    }

    public void setIntegration(int integration) {
        this.integration = integration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
