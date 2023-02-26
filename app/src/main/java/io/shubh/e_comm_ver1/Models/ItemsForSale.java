package io.shubh.e_comm_ver1.Models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemsForSale implements Serializable {


    String category , description ,item_price , name ,root_category ,seller_id ,sub_category ,sub_sub_category ;
    int item_id;
    long time_of_upload ;
    String varietyName;
    boolean visibility;


    List<String> listOfImageURLs = new ArrayList<String>();
    List<String> varieies = new ArrayList<String>();

    List<String> arrayOfNameKeywords = new ArrayList<String>(); //this array will make search in array easy ..I would just have
    //to use WhereArrayContains function for searching an item in firestore .I can use algolia like service for search functionality
    //but it will need maintain another third party service
    //also with this method pagination is easy..but I aint doing with this app

    @Exclude
    boolean isItemLiked =false;

    public ItemsForSale() {
    }

    @Exclude
    public boolean isItemLiked() {
        return isItemLiked;
    }
    @Exclude
    public void setItemLiked(boolean itemLiked) {
        isItemLiked = itemLiked;
    }

    @PropertyName("category")
    public String getCategory() {
        return category;
    }
    @PropertyName("category")
    public void setCategory(String category) {
        this.category = category;
    }

    @PropertyName("description")
    public String getDescription() {
        return description;
    }
    @PropertyName("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @PropertyName("item price")
    public String getItem_price() {
        return item_price;
    }
    @PropertyName("item price")
    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    @PropertyName("name")
    public String getName() {
        return name;
    }
    @PropertyName("name")
    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("root category")
    public String getRoot_category() {
        return root_category;
    }
    @PropertyName("root category")
    public void setRoot_category(String root_category) {
        this.root_category = root_category;
    }

    @PropertyName("seller id")
    public String getSeller_id() {
        return seller_id;
    }
    @PropertyName("seller id")
    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    @PropertyName("sub category")
    public String getSub_category() {
        return sub_category;
    }
    @PropertyName("sub category")
    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    @PropertyName("sub sub category")
    public String getSub_sub_category() {
        return sub_sub_category;
    }
    @PropertyName("sub sub category")
    public void setSub_sub_category(String sub_sub_category) {
        this.sub_sub_category = sub_sub_category;
    }

    @PropertyName("item id")
    public int getItem_id() {
        return item_id;
    }
    @PropertyName("item id")
    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    @PropertyName("time of upload")
    public long getTime_of_upload() {
        return time_of_upload;
    }
    @PropertyName("time of upload")
    public void setTime_of_upload(long time_of_upload) {
        this.time_of_upload = time_of_upload;
    }


    @PropertyName("uploaded images urls")
    public List<String> getListOfImageURLs() {
        return listOfImageURLs;
    }
    @PropertyName("uploaded images urls")
    public void setListOfImageURLs(List<String> listOfImageURLs) {
        this.listOfImageURLs = listOfImageURLs;
    }

    //the variety list is uploaded only when variety name is not null
    @PropertyName("variety name")
    public String getVarietyName() {
        return varietyName;
    }
    @PropertyName("variety name")
    public void setVarietyName(String varietyName) {
        this.varietyName = varietyName;
    }

    @PropertyName("visibility")
    public boolean isVisibility() {
        return visibility;
    }
    @PropertyName("visibility")
    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    @PropertyName("varieties")
    public List<String> getVarieies() {
        return varieies;
    }
    @PropertyName("varieties")
    public void setVarieies(List<String> varieies) {
        this.varieies = varieies;
    }

    @PropertyName("array of item name keywords")
    public List<String> getArrayOfNameKeywords() {
        return arrayOfNameKeywords;
    }
    @PropertyName("array of item name keywords")
    public void setArrayOfNameKeywords(List<String> arrayOfNameKeywords) {
        this.arrayOfNameKeywords = arrayOfNameKeywords;
    }
}
