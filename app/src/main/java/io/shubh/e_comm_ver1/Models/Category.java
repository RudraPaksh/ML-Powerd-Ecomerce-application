package io.shubh.e_comm_ver1.Models;

import java.util.ArrayList;
import java.util.List;

//***************************************Documentaion - of structure holding the categories heirarchy***********************

     /*   the Conceptual model for  heirarchy is:-

        master -List  ---->Category(Like- ['men-s Fashion' , 'womens fashion',.....])
            Sub-list(one sub categories list for each item in categories ) -------> SubCategory for category named 'Mens fashion ' ===> ['Clothing' , 'Shoes' ,...]
                Sub-Sub-list(one sub-sub-categories list for each item in sub-categories ) -------> SubSubCategory for sub-category named 'Clothing ' ===> ['T-Shirts' , 'Shirts' ,...]

*/
public class Category {
    String name ;
    int order;
    String imageURL;

    boolean haveSubCatgr =false;

    ArrayList<SubCategory> subCategoriesList ;
    //SubCategory subCategory;
    //above variables getter and setter are below these class declarations



    public static class SubCategory {
        String name ;
        int order;

        boolean haveSubSubCatgr =false;
        ArrayList<SubSubCategory> subSubCategoryList ;
        //SubSubCategory subSubCategory;
        //above variables getter and setter are below these class declarations

        public static class SubSubCategory {
            String name ;
            int order;


            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getOrder() {
                return order;
            }

            public void setOrder(int order) {
                this.order = order;
            }
        }



        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public boolean isHaveSubSubCatgr() {
            return haveSubSubCatgr;
        }

        public void setHaveSubSubCatgr(boolean haveSubSubCatgr) {
            this.haveSubSubCatgr = haveSubSubCatgr;
        }

        public ArrayList<SubSubCategory> getSubSubCategoryList() {
            return subSubCategoryList;
        }

        public void setSubSubCategoryList(ArrayList<SubSubCategory> subSubCategoryList) {
            this.subSubCategoryList = subSubCategoryList;
        }
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isHaveSubCatgr() {
        return haveSubCatgr;
    }

    public void setHaveSubCatgr(boolean haveSubCatgr) {
        this.haveSubCatgr = haveSubCatgr;
    }

    public ArrayList<SubCategory> getSubCategoriesList() {
        return subCategoriesList;
    }

    public void setSubCategoriesList(ArrayList<SubCategory> subCategoriesList) {
        this.subCategoriesList = subCategoriesList;
    }
}

