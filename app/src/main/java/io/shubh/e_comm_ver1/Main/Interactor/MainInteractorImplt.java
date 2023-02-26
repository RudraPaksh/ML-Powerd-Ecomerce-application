package io.shubh.e_comm_ver1.Main.Interactor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.shubh.e_comm_ver1.Models.Category;

public class MainInteractorImplt implements MainInteractor {

    FirebaseFirestore db ;
    CallbacksToPresnter mPresenter;

    @Override
    public void init(CallbacksToPresnter mPresenter) {
        db = FirebaseFirestore.getInstance();
        this.mPresenter =mPresenter;




    }

    @Override
    public void getAllCatgrFromFirestoreWithArgAsCallbackFunction(SeparateCallbackToPresnterAfterGettingcategories l) {
        ArrayList<Category> categoriesList = new ArrayList<>();


        db.collection("categories for Items for sale").orderBy("order", Query.Direction.ASCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Category category=  new Category();

                        category.setName(document.getId());
                        category.setOrder(Integer.parseInt((String) document.get("order")));
                        category.setImageURL((String) document.get("image url"));

                        // Log.i("&&&&", "catgr name :"+ document.getId());

                        List<String> subCategoriesStringRetrivedList = (List<String>) document.get("sub categories");

                        if(subCategoriesStringRetrivedList!=null){
                            category.setHaveSubCatgr(true);

                            ArrayList<Category.SubCategory> subCategoryList = new ArrayList<>() ;


                            for(int i=0; i<subCategoriesStringRetrivedList.size() ; i++){
                                Category.SubCategory subCategory  =new Category.SubCategory();

                                subCategory.setName(subCategoriesStringRetrivedList.get(i));
                                subCategory.setOrder(i);


                                List<String> subSubCategoriesStringRetrivedList = (List<String>) document.get(subCategoriesStringRetrivedList.get(i));


                                if(subSubCategoriesStringRetrivedList!=null) {

                                    subCategory.setHaveSubSubCatgr(true);

                                    ArrayList<Category.SubCategory.SubSubCategory> subSubCategoryList = new ArrayList<>() ;

                                    for(int k=0; k<subSubCategoriesStringRetrivedList.size() ; k++) {
                                        Category.SubCategory.SubSubCategory subSubCategory = new Category.SubCategory.SubSubCategory();

                                        subSubCategory.setName(subSubCategoriesStringRetrivedList.get(k));
                                        subSubCategory.setOrder(k);

                                        subSubCategoryList.add(subSubCategory);
                                    }

                                    subCategory.setSubSubCategoryList(subSubCategoryList);

                                }




                                subCategoryList.add(subCategory);
                            }

                            category.setSubCategoriesList(subCategoryList);

                        }

                        categoriesList.add(category);
                    }

                    //==============All catgrs retrived now======================================

                    l.onFinishedGettingCategories(categoriesList);



                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
        //after checking something ..communicating the results back to presenter ..we have a call back

    }

    @Override
    public void checkIfUserisASellerOrNot(SeparateCallbackToPresnterAftercheckingIfUserASellerOrNot l) {
        //checking if the user is becoming the seller for the first time
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference pg = db.collection("users").document(currentFirebaseUser.getUid());
        pg.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    Boolean is_user_a_seller = (Boolean) doc.get("is a seller also ?");

                    l.onFinishedChecking(is_user_a_seller);

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }


}
