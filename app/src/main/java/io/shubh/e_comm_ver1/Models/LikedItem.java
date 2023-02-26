package io.shubh.e_comm_ver1.Models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class LikedItem implements Serializable {

    @Exclude
   ItemsForSale itemObject;

    long timeOfsaving;
    String likedItemDocId ,itemId, userId ;


    //these below are false by deafult because if user is able to add the item into bag at first place
    //then that means both are false ..but in future they might change
    @Exclude
    boolean isTheOriginalItemDeleted=false;


    public LikedItem() {

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


  //-----------------------------
    @PropertyName("time of upload")
    public long getTimeOfsaving() {
        return timeOfsaving;
    }

    @PropertyName("time of upload")
    public void setTimeOfsaving(long timeOfsaving) {
        this.timeOfsaving = timeOfsaving;
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

    @PropertyName("liked item doc id")
    public String getLikedItemDocId() {
        return likedItemDocId;
    }
    @PropertyName("liked item doc id")
    public void setLikedItemDocId(String likedItemDocId) {
        this.likedItemDocId = likedItemDocId;
    }

    @Exclude
    public boolean isTheOriginalItemDeleted() {
        return isTheOriginalItemDeleted;
    }

    @Exclude
    public void setTheOriginalItemDeleted(boolean theOriginalItemDeleted) {
        isTheOriginalItemDeleted = theOriginalItemDeleted;
    }


}
