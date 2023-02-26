package io.shubh.e_comm_ver1.Models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class BagItem implements Serializable {

    //below  field is not supposed to have the same in the BagItem document in firestore
    //this below object will be added manually
    //its purpose is for the time when on the obejct is clicked on am=nd then we have to show the itemdetail page
    @Exclude
    ItemsForSale itemObject;

    long time_of_upload;
    String itemAmount, itemId, userId;
    //giving it default value in case if item doesnt ahve
    String selectedVarietyIndexInList = "null";

    //these below are false by deafult because if user is able to add the item into bag at first place
    //then that means both are false ..but in future they might change
    boolean isTheOriginalItemDeleted=false;


    public BagItem() {

    }

    //non property field
    @Exclude
    public ItemsForSale getItemObject() {
        return itemObject;
    }
    @Exclude
    public void setItemObject(ItemsForSale itemObject) {
        this.itemObject = itemObject;
    }

//---------------------------------------
    @PropertyName("item amount")
    public String getItemAmount() {
        return itemAmount;
    }

    @PropertyName("item amount")
    public void setItemAmount(String itemAmount) {
        this.itemAmount = itemAmount;
    }

    @PropertyName("time of upload")
    public long getTime_of_upload() {
        return time_of_upload;
    }

    @PropertyName("time of upload")
    public void setTime_of_upload(long time_of_upload) {
        this.time_of_upload = time_of_upload;
    }

    @PropertyName("item id")
    public String getItemId() {
        return itemId;
    }

    @PropertyName("item id")
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @PropertyName("user id")
    public String getUserId() {
        return userId;
    }

    @PropertyName("user id")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @PropertyName("selected variety index in list")
    public String getSelectedVarietyIndexInList() {
        return selectedVarietyIndexInList;
    }

    @PropertyName("selected variety index in list")
    public void setSelectedVarietyIndexInList(String selectedVarietyIndexInList) {
        this.selectedVarietyIndexInList = selectedVarietyIndexInList;
    }


    public boolean isTheOriginalItemDeleted() {
        return isTheOriginalItemDeleted;
    }

    public void setTheOriginalItemDeleted(boolean theOriginalItemDeleted) {
        isTheOriginalItemDeleted = theOriginalItemDeleted;
    }


}
