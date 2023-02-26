package io.shubh.e_comm_ver1.Models;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class AdressItem implements Serializable {


    String recieverName, pinCode, houseNo , area , city , state ,Uid;

    /*Todo -set the phone no through OTP in next update*/
    String phoneNo;
    long timeOfUpload;


    public AdressItem() {

    }


    @PropertyName("reciever name")
    public String getRecieverName() {
        return recieverName;
    }
    @PropertyName("reciever name")
    public void setRecieverName(String recieverName) {
        this.recieverName = recieverName;
    }

    @PropertyName("pin code")
    public String getPinCode() {
        return pinCode;
    }
    @PropertyName("pin code")
    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    @PropertyName("house no")
    public String getHouseNo() {
        return houseNo;
    }
    @PropertyName("house no")
    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    @PropertyName("area")
    public String getArea() {
        return area;
    }
    @PropertyName("area")
    public void setArea(String area) {
        this.area = area;
    }

    @PropertyName("city")
    public String getCity() {
        return city;
    }
    @PropertyName("city")
    public void setCity(String city) {
        this.city = city;
    }

    @PropertyName("state")
    public String getState() {
        return state;
    }
    @PropertyName("state")
    public void setState(String state) {
        this.state = state;
    }

    @PropertyName("phone no")
    public String getPhoneNo() {
        return phoneNo;
    }
    @PropertyName("phone no")
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @PropertyName("user id")
    public String getUid() {
        return Uid;
    }
    @PropertyName("user id")
    public void setUid(String uid) {
        Uid = uid;
    }

    @PropertyName("time of upload")
    public long getTimeOfUpload() {
        return timeOfUpload;
    }
    @PropertyName("time of upload")
    public void setTimeOfUpload(long timeOfUpload) {
        this.timeOfUpload = timeOfUpload;
    }
}
