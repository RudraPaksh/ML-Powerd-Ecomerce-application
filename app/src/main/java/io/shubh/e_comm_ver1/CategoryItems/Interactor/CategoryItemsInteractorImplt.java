package io.shubh.e_comm_ver1.CategoryItems.Interactor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.shubh.e_comm_ver1.Models.LikedItem;
import io.shubh.e_comm_ver1.Models.ItemsForSale;
import io.shubh.e_comm_ver1.Utils.StaticClassForGlobalInfo;

public class CategoryItemsInteractorImplt implements CategoryItemsInteractor {

    FirebaseFirestore db;
    CallbacksToPresnter mPresenter;
    //int noOfItemsRetrivedTillNo=0;

    int idodLastItemRetrived = 0;
    String ctgrPath = null;
    boolean isPrevLoadMoreCallCompleted =true ;//This boolean is for the purpose //That recyclr view scroll listener when scrolled last ..is firing multiple load more calls
    //and all the calls will show same result ..causing duplicates in rcylcr view  ..so this boolean prevents that

    @Override
    public void init(CallbacksToPresnter mPresenter) {
        db = FirebaseFirestore.getInstance();
        this.mPresenter = mPresenter;


    }

    @Override
    public void getTheFirstItemDocumentAsAReferenceForStartAtFunct(String ctgrName, String ctgrPath, String rootCtgr, String subCtgr, String subSubCtgr, boolean ifItsALoadMorecall) {
        Log.i("******", "first call is made here for: " + ctgrPath + "-------------------------");
        ArrayList<ItemsForSale> itemsList = new ArrayList<>();

        if (ifItsALoadMorecall == false) {
            //  this.ctgrPath=  ctgrPath ;

            //below 15 lines of code can be just replaced by just searching on category path ..but let it be this way for now..bacause it tells me we can do multiple where to in here
            //.whereEqualTo("category", ctgrPath)


            Query query = null;
            if (ctgrPath.indexOf('/') == -1) {
                //means ctgrpath has ctgr only..no subctgr or subsubctgr
                //this means we need to get all documents which belong to this ctgr
                query = db.collection("items for sale")
                        .whereEqualTo("root category", ctgrPath)
                        .whereEqualTo("visibility", true)
                        .orderBy("item id", Query.Direction.ASCENDING)
                        .limit(1);
            } else if (ctgrPath.indexOf('/') != -1 && ctgrPath.indexOf("//") == -1) {
                //means ctgrpath has subctgr no subsubctgr
                //this means we need to get all documents which belong to this subctgr
                query = db.collection("items for sale")
                        .whereEqualTo("sub category", ctgrName)
                        .whereEqualTo("root category", rootCtgr)
                        .whereEqualTo("visibility", true)
                        .orderBy("item id", Query.Direction.ASCENDING)

                        .limit(1);
            } else if (ctgrPath.indexOf('/') != -1 && ctgrPath.indexOf("//") != -1) {
                //means ctgrpath has subsubctgr
                //this means we need to get all documents which belong to this subsubctgr
                query = db.collection("items for sale")
                        .whereEqualTo("sub sub category", ctgrName)
                        .whereEqualTo("root category", rootCtgr)
                        .whereEqualTo("sub category", subCtgr)
                        .whereEqualTo("visibility", true)
                        .orderBy("item id", Query.Direction.ASCENDING)
                        .limit(1);
            }

            query
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult() != null) {


                                    if (task.getResult().size() != 0) {
                                        List<ItemsForSale> list = task.getResult().toObjects(ItemsForSale.class);

                                        itemsList.add(list.get(0));
                                        Log.i("***SizeOfo/pOf1stCall:", String.valueOf(itemsList.size()));

                                        idodLastItemRetrived = itemsList.get(0).getItem_id();

                                        Log.i("******id", String.valueOf(itemsList.get(0).getItem_id()));
                                        Log.i("******name", String.valueOf(itemsList.get(0).getName()));

                                        getItemsFromFirebaseWithResultsOnSeparateCallback(ctgrName, ctgrPath, rootCtgr, subCtgr, subSubCtgr, false, itemsList);
                                    } else {

                                        Log.i("***", "first call result has 0 size ");
                                        mPresenter.onFinishedGettingItems(itemsList, false, ctgrName, ifItsALoadMorecall);

                                    }


                                }

                            } else {
                                Log.e("CategoryItemsInteractor", "Error getting documents: ", task.getException());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("CategoryItemsInteractor", "Error getting documents: ", e);


                        }
                    });

        } else {
            getItemsFromFirebaseWithResultsOnSeparateCallback(ctgrName, ctgrPath, rootCtgr, subCtgr, subSubCtgr, true, itemsList);
        }
    }

    @Override
    public void getItemsFromFirebaseWithResultsOnSeparateCallback(String ctgrName, String ctgrPath, String rootCtgr, String subCtgr, String subSubCtgr, boolean ifItsALoadMorecall, ArrayList<ItemsForSale> itemsList) {

        if(isPrevLoadMoreCallCompleted==true) {
            isPrevLoadMoreCallCompleted=false;
            // ArrayList<ItemsForSale> list_of_data_objects__for_adapter = new ArrayList<>();
            Log.i("****", "2ndcall is made for:" + ctgrPath + "------------------------");

            this.ctgrPath = ctgrPath;
            int pageSize = 0;
            if (ifItsALoadMorecall == true) {
                pageSize = 6;
            } else {
                pageSize = 5;
            }

            Query query = null;
            if (ctgrPath.indexOf('/') == -1) {
                //means ctgrpath has ctgr only..no subctgr or subsubctgr
                //this means we need to get all documents which belong to this ctgr
                query = db.collection("items for sale")
                        .whereEqualTo("root category", ctgrPath)
                        .whereEqualTo("visibility", true)
                        .orderBy("item id", Query.Direction.ASCENDING)
                        .startAfter(idodLastItemRetrived)//<------This below function decides after which document ,all other documents need to be fetched
                        //for first time I will pass it the very first function,after that the last document i have
                        //Also the field of orderBy and startafter should be same
                        .limit(pageSize);

            } else if (ctgrPath.indexOf('/') != -1 && ctgrPath.indexOf("//") == -1) {
                //means ctgrpath has subctgr no subsubctgr
                //this means we need to get all documents which belong to this subctgr
                query = db.collection("items for sale")
                        .whereEqualTo("sub category", ctgrName)
                        .whereEqualTo("root category", rootCtgr)
                        .whereEqualTo("visibility", true)
                        .orderBy("item id", Query.Direction.ASCENDING)
                        .startAfter(idodLastItemRetrived)//<------This below function decides after which document ,all other documents need to be fetched
                        //for first time I will pass it the very first function,after that the last document i have
                        .limit(pageSize);
            } else if (ctgrPath.indexOf('/') != -1 && ctgrPath.indexOf("//") != -1) {
                //means ctgrpath has subsubctgr
                //this means we need to get all documents which belong to this subsubctgr
                query = db.collection("items for sale")
                        .whereEqualTo("sub sub category", ctgrName)
                        .whereEqualTo("root category", rootCtgr)
                        .whereEqualTo("sub category", subCtgr)
                        .whereEqualTo("visibility", true)
                        .orderBy("item id", Query.Direction.ASCENDING)
                        .startAfter(idodLastItemRetrived)//<------This below function decides after which document ,all other documents need to be fetched
                        //for first time I will pass it the very first function,after that the last document i have
                        .limit(pageSize);
            }

            query
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                isPrevLoadMoreCallCompleted=true;
                                if (task.getResult() != null) {
                                    //  Log.i("******", String.valueOf(task.getResult().size()));


                                    if (task.getResult().size() != 0) {


                                        List<ItemsForSale> list = task.getResult().toObjects(ItemsForSale.class);

                                        itemsList.addAll(list);

                                        idodLastItemRetrived = itemsList.get(itemsList.size() - 1).getItem_id();

                                        checkIfTheseItemsExistInSavedItemsList(itemsList, true, ctgrName, ifItsALoadMorecall);
                                        //below line s shfted to above fnct call result ...if Above is not required then use the below line exactly at below line
                                        // mPresenter.onFinishedGettingItems(itemsList, true, ctgrName, ifItsALoadMorecall);


                                        //adding image urls maually now -TODO make a arraylist field for it in firestore document later

                                    } else {

                                        if (itemsList.isEmpty()) {
                                            if (ifItsALoadMorecall == true) {
                                                //TODO-showToast..of No more items found  ..after scrolling to bottom
                                                mPresenter.showToast("No more Items found");
                                            }
                                        } else {
                                            Log.i("****", "Size is 0 of 2nd call ,but it has reached the 2nd call so so it has one item so display it");

                                            checkIfTheseItemsExistInSavedItemsList(itemsList, true, ctgrName, ifItsALoadMorecall);
                                            //below line s shfted to above fnct call result ...if Above is not required then use the below line exactly at below line
                                            //  mPresenter.onFinishedGettingItems(itemsList, true, ctgrName, ifItsALoadMorecall);
                                        }
                                    }


                                }

                            } else {
                                Log.e("CategoryItemsInteractor", "Error getting documents: ", task.getException());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            isPrevLoadMoreCallCompleted=true;
                            Log.e("CategoryItemsInteractor", "Error getting documents: ", e);


                        }
                    });
        }

    }


    @Override
    public void saveItemToUserLikedListWithResultsOnSeparateCallback(LikedItem likedItem, SeparateCallbackToPresnterAfterSavingItemToBuyerLikedItems l) {

        //I m proceeding to save everyone's liked items inside one node 'liked items'...rather than putting it inside collection inside user node
        //because if this would have been used for real purpose ,so then to do statsistic or operation related work on liked items will get easier


        db.collection("liked items").document(likedItem.getLikedItemDocId())
                .set(likedItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        l.onFinished(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        l.onFinished(false);
                    }
                });

    }

    @Override
    public void deleteItemFromUserLikedListWithResultsOnSeparateCallback(String docId, SeparateCallbackToPresnterAfterDeletingFromBuyerLikedItems l) {

        db.collection("liked items").document(docId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        l.onFinished(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        l.onFinished(false);
                    }
                });
    }

    private void checkIfTheseItemsExistInSavedItemsList(ArrayList<ItemsForSale> itemsList, boolean lsitNotEmpty, String ctgrName, boolean ifItsALoadMorecall) {
     //   checkIfTheseItemsExistInSavedItemsListRecrsiveFunction(0, itemsList, lsitNotEmpty, ctgrName, ifItsALoadMorecall);

//before I have implemented the below code using recursive function(fnction is below)..but it was slower due to serial read so.. below is iterative version
        for(int i=0 ;i<itemsList.size();i++){

            int finalI = i;
            db.collection("liked items").document(String.valueOf(itemsList.get(i).getItem_id())+StaticClassForGlobalInfo.UId)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //  Log.d(TAG, "Document exists!");
                            itemsList.get(finalI).setItemLiked(true);

                            if(finalI==itemsList.size()-1){
                                mPresenter.onFinishedGettingItems(itemsList, true, ctgrName, ifItsALoadMorecall);
                            }

                        } else {
                            //  Log.d(TAG, "Document does not exist!");
                            itemsList.get(finalI).setItemLiked(false);


                            if(finalI==itemsList.size()-1){
                                mPresenter.onFinishedGettingItems(itemsList, true, ctgrName, ifItsALoadMorecall);
                            }

                        }
                    } else {
                        Log.d("ctgr item interactor", "Failed on getting liked items: ", task.getException());
                        //even tho this read opr is failing I will send result as success because it can show the item list we have already retrived
                        mPresenter.onFinishedGettingItems(itemsList, true, ctgrName, ifItsALoadMorecall);
                    }
                }
            });

        }
    }







    /*//recrsive function
    public void checkIfTheseItemsExistInSavedItemsListRecrsiveFunction(int index, ArrayList<ItemsForSale> itemsList, boolean lsitNotEmpty, String ctgrName, boolean ifItsALoadMorecall) {

        if (index < itemsList.size()) {

            db.collection("liked items").document(String.valueOf(itemsList.get(index).getItem_id())+StaticClassForGlobalInfo.UId)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                          //  Log.d(TAG, "Document exists!");
                            itemsList.get(index).setItemLiked(true);
                            checkIfTheseItemsExistInSavedItemsListRecrsiveFunction(index+1, itemsList, lsitNotEmpty, ctgrName, ifItsALoadMorecall);
                        } else {
                          //  Log.d(TAG, "Document does not exist!");
                            itemsList.get(index).setItemLiked(false);
                            checkIfTheseItemsExistInSavedItemsListRecrsiveFunction(index+1, itemsList, lsitNotEmpty, ctgrName, ifItsALoadMorecall);
                        }
                    } else {
                        Log.d("ctgr item interactor", "Failed on getting liked items: ", task.getException());
                        //even tho this read opr is failing I will send result as success because it can show the item list we have already retrived
                        mPresenter.onFinishedGettingItems(itemsList, true, ctgrName, ifItsALoadMorecall);
                    }
                }
            });

        } else {
            mPresenter.onFinishedGettingItems(itemsList, true, ctgrName, ifItsALoadMorecall);
        }

    }*/
}
