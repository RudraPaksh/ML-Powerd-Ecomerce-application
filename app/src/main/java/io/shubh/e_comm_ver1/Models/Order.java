package io.shubh.e_comm_ver1.Models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {

    //below fields are the one which are used in app but are not sent to database
    @Exclude
    ArrayList<BagItem> bagItems;
    @Exclude
    ArrayList<SubOrderItem> subOrderItems;

    //------------------------------------------
    //below are sent to database
    AdressItem adressItem;

    long timeOfCreationOfOrder;
    long timeOfPaymentSuccessOfOrder;
    long timeOfCompletionOfDeliveryOfAllItems;

    String orderId;
    String buyerId;
    String transactionId;
    String totalPrice;

    ArrayList<String> sellersInOrder;


    //if below var is 2 -that means ..order have payment done
    //if below var is 3 -that means ..all the items in order are delivered.
    //if below var is 0 -that means ..none of the above .
    int statusOfOrder = 0;


    public Order() {

    }

    ///below class is basically the indivisual items inside the collective  order
    //this class objects will be stored in the sub collection inside the order document
    public static class SubOrderItem {

        String itemAmount, itemId ,sellerId ,varietyName ,imageUrl , buyerId ,itemPrice ,itemName ;
        String selectedVarietyName = "null";
        long timeOfPackagedOfItem;
        long timeOfShippedOfItem;
        long timeOfDeliveryOfItem;
        long timeOfCancellationOfItemBySeller;
        long timeOfCancellationOfItemByBuyer;
        long timeOfReturnOfItem;

        @Exclude
        String parentOrderId;//parent order id ..//this field aint stored online
        @Exclude
        long timeOfCreationOfOrder;
        @Exclude
        AdressItem adressItem;

        //if below var is 2 -that means ..order have payment done
        //if below var is 3 -that means ..order is packaged and ready to be shipped .
        //if below var is 4 -that means ..order is shipped .
        //if below var is 5 -that means ..order is delivered .
        //if below var is 6 -that means ..order is cancelled by seller .
        //if below var is 7 -that means ..order is returned by buyer .
        //if below var is 8 -that means ..order is cancelled by buyer after pacing the order and before the order is shipped .
        //below var by default is 2 becuase ..this class objects will be created once the payment is done.
        int statusOfOrder = 2;

        @Exclude
        public String getParentOrderId() {
            return parentOrderId;
        }
        @Exclude
        public void setParentOrderId(String parentOrderId) {
            this.parentOrderId = parentOrderId;
        }

        @Exclude
        public long getTimeOfCreationOfOrder() {
            return timeOfCreationOfOrder;
        }
        @Exclude
        public void setTimeOfCreationOfOrder(long timeOfCreationOfOrder) {
            this.timeOfCreationOfOrder = timeOfCreationOfOrder;
        }

        @Exclude
        public AdressItem getAdressItem() {
            return adressItem;
        }
        @Exclude
        public void setAdressItem(AdressItem adressItem) {
            this.adressItem = adressItem;
        }

        //----------------------------------------------------------

        @PropertyName("ItemAmount")
        public String getItemAmount() {
            return itemAmount;
        }
        @PropertyName("ItemAmount")
        public void setItemAmount(String itemAmount) {
            this.itemAmount = itemAmount;
        }

        @PropertyName("ItemId")
        public String getItemId() {
            return itemId;
        }
        @PropertyName("ItemId")
        public void setItemId(String itemId) {
            this.itemId = itemId;
        }



        @PropertyName("TimeOfPackagedOfItem")
        public long getTimeOfPackagedOfItem() {
            return timeOfPackagedOfItem;
        }
        @PropertyName("TimeOfPackagedOfItem")
        public void setTimeOfPackagedOfItem(long timeOfPackagedOfItem) {
            this.timeOfPackagedOfItem = timeOfPackagedOfItem;
        }

        @PropertyName("TimeOfShippedOfItem")
        public long getTimeOfShippedOfItem() {
            return timeOfShippedOfItem;
        }
        @PropertyName("TimeOfShippedOfItem")
        public void setTimeOfShippedOfItem(long timeOfShippedOfItem) {
            this.timeOfShippedOfItem = timeOfShippedOfItem;
        }

        @PropertyName("TimeOfDeliveryOfItem")
        public long getTimeOfDeliveryOfItem() {
            return timeOfDeliveryOfItem;
        }
        @PropertyName("TimeOfDeliveryOfItem")
        public void setTimeOfDeliveryOfItem(long timeOfDeliveryOfItem) {
            this.timeOfDeliveryOfItem = timeOfDeliveryOfItem;
        }

        @PropertyName("TimeOfCancellationOfItem")
        public long getTimeOfCancellationOfItemBySeller() {
            return timeOfCancellationOfItemBySeller;
        }
        @PropertyName("TimeOfCancellationOfItem")
        public void setTimeOfCancellationOfItemBySeller(long timeOfCancellationOfItemBySeller) {
            this.timeOfCancellationOfItemBySeller = timeOfCancellationOfItemBySeller;
        }

        @PropertyName("StatusOfOrder")
        public int getStatusOfOrder() {
            return statusOfOrder;
        }
        @PropertyName("StatusOfOrder")
        public void setStatusOfOrder(int statusOfOrder) {
            this.statusOfOrder = statusOfOrder;
        }

        @PropertyName("seller id")
        public String getSellerId() {
            return sellerId;
        }
        @PropertyName("seller id")
        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        @PropertyName("VarietyName")
        public String getVarietyName() {
            return varietyName;
        }
        @PropertyName("VarietyName")
        public void setVarietyName(String varietyName) {
            this.varietyName = varietyName;
        }

        @PropertyName("SelectedVarietyName")
        public String getSelectedVarietyName() {
            return selectedVarietyName;
        }
        @PropertyName("SelectedVarietyName")
        public void setSelectedVarietyName(String selectedVarietyName) {
            this.selectedVarietyName = selectedVarietyName;
        }

        @PropertyName("image url")
        public String getImageUrl() {
            return imageUrl;
        }
        @PropertyName("image url")
        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        @PropertyName("buyer id")
        public String getBuyerId() {
            return buyerId;
        }
        @PropertyName("buyer id")
        public void setBuyerId(String buyerId) {
            this.buyerId = buyerId;
        }

        @PropertyName("item price")
        public String getItemPrice() {
            return itemPrice;
        }
        @PropertyName("item price")
        public void setItemPrice(String itemPrice) {
            this.itemPrice = itemPrice;
        }

        @PropertyName("item name")
        public String getItemName() {
            return itemName;
        }
        @PropertyName("item name")
        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        @PropertyName("TimeOfCancellationOfItemByBuyer")
        public long getTimeOfCancellationOfItemByBuyer() {
            return timeOfCancellationOfItemByBuyer;
        }
        @PropertyName("TimeOfCancellationOfItemByBuyer")
        public void setTimeOfCancellationOfItemByBuyer(long timeOfCancellationOfItemByBuyer) {
            this.timeOfCancellationOfItemByBuyer = timeOfCancellationOfItemByBuyer;
        }

        @PropertyName("TimeOfReturnOfItem")
        public long getTimeOfReturnOfItem() {
            return timeOfReturnOfItem;
        }
        @PropertyName("TimeOfReturnOfItem")
        public void setTimeOfReturnOfItem(long timeOfReturnOfItem) {
            this.timeOfReturnOfItem = timeOfReturnOfItem;
        }
    }

    @Exclude
    public ArrayList<BagItem> getBagItems() {
        return bagItems;
    }
    @Exclude
    public void setBagItems(ArrayList<BagItem> bagItems) {
        this.bagItems = bagItems;
    }

    @Exclude
    public ArrayList<SubOrderItem> getSubOrderItems() {
        return subOrderItems;
    }
    //I should have used setSubOrderItems while uploading order item..but i went without it..but nayway code is kinds clean without it..but for getting order item I need getSubOrder...
    @Exclude
    public void setSubOrderItems(ArrayList<SubOrderItem> subOrderItems) {
        this.subOrderItems = subOrderItems;
    }

    //--------------------------------------------------------------------------------

    @PropertyName("address item")
    public void setAdressItem(AdressItem adressItem) {
        this.adressItem = adressItem;
    }
    @PropertyName("address item")
    public AdressItem getAdressItem() {
        return adressItem;
    }

    @PropertyName("status of order")
    public int getStatusOfOrder() {
        return statusOfOrder;
    }

    @PropertyName("status of order")
    public void setStatusOfOrder(int statusOfOrder) {
        this.statusOfOrder = statusOfOrder;
    }


    @PropertyName("creation of order")
    public long getTimeOfCreationOfOrder() {
        return timeOfCreationOfOrder;
    }

    @PropertyName("creation of order")
    public void setTimeOfCreationOfOrder(long timeOfCreationOfOrder) {
        this.timeOfCreationOfOrder = timeOfCreationOfOrder;
    }


    @PropertyName("time of payment success of item")
    public long getTimeOfPaymentSuccessOfOrder() {
        return timeOfPaymentSuccessOfOrder;
    }

    @PropertyName("time of payment success of item")
    public void setTimeOfPaymentSuccessOfOrder(long timeOfPaymentSuccessOfOrder) {
        this.timeOfPaymentSuccessOfOrder = timeOfPaymentSuccessOfOrder;
    }

    @PropertyName("time of delivery of all item")
    public long getTimeOfCompletionOfDeliveryOfAllItems() {
        return timeOfCompletionOfDeliveryOfAllItems;
    }
    @PropertyName("time of delivery of all item")
    public void setTimeOfCompletionOfDeliveryOfAllItems(long timeOfCompletionOfDeliveryOfAllItems) {
        this.timeOfCompletionOfDeliveryOfAllItems = timeOfCompletionOfDeliveryOfAllItems;
    }


    @PropertyName("order id")
    public String getOrderId() {
        return orderId;
    }

    @PropertyName("order id")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    @PropertyName("buyer id")
    public String getBuyerId() {
        return buyerId;
    }

    @PropertyName("buyer id")
    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }



    @PropertyName("transaction id")
    public String getTransactionId() {
        return transactionId;
    }

    @PropertyName("transaction id")
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


    @PropertyName("total price")
    public String getTotalPrice() {
        return totalPrice;
    }
    @PropertyName("total price")
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }


    @PropertyName("sellers in order")
    public ArrayList<String> getSellersInOrder() {
        return sellersInOrder;
    }
    @PropertyName("sellers in order")
    public void setSellersInOrder(ArrayList<String> sellersInOrder) {
        this.sellersInOrder = sellersInOrder;
    }
}
