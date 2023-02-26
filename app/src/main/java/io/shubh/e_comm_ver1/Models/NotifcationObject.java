package io.shubh.e_comm_ver1.Models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class NotifcationObject implements Serializable {

    /*@Exclude
   ItemsForSale itemObject;*/

    long time;
    String content ,image_url, title ,token ,type  ;
    boolean hasItBeenRead =false;



    public NotifcationObject() {

    }


  //-----------------------------

    @PropertyName("time of upload")
    public long getTime() {
        return time;
    }
    @PropertyName("time of upload")
    public void setTime(long time) {
        this.time = time;
    }

    @PropertyName("content")
    public String getContent() {
        return content;
    }
    @PropertyName("content")
    public void setContent(String content) {
        this.content = content;
    }

    @PropertyName("image url")
    public String getImage_url() {
        return image_url;
    }
    @PropertyName("image url")
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @PropertyName("title")
    public String getTitle() {
        return title;
    }
    @PropertyName("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @PropertyName("token")
    public String getToken() {
        return token;
    }
    @PropertyName("token")
    public void setToken(String token) {
        this.token = token;
    }

    @PropertyName("type")
    public String getType() {
        return type;
    }
    @PropertyName("type")
    public void setType(String type) {
        this.type = type;
    }

    @PropertyName("has it been read")
    public boolean isHasItBeenRead() {
        return hasItBeenRead;
    }
    @PropertyName("has it been read")
    public void setHasItBeenRead(boolean hasItBeenRead) {
        this.hasItBeenRead = hasItBeenRead;
    }
}
