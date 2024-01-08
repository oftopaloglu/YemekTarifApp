package com.grup4.yemektarifapp.firebase.serach;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.grup4.yemektarifapp.Model.FoodRecipe;
import com.grup4.yemektarifapp.firebase.serach.FirestoreCallback;

import java.util.ArrayList;
import java.util.List;

public class FirestoreFoodSearch {
    private FirebaseFirestore firestore;

    public FirestoreFoodSearch() {
        firestore = FirebaseFirestore.getInstance();
    }

    public void searchDocumentsByFieldName(String collectionName, String fieldName, String fieldValue, FirestoreCallback callback) {
        List<FoodRecipe> recipesList = new ArrayList<>();

        firestore.collection(collectionName)
                .whereEqualTo(fieldName, fieldValue)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            FoodRecipe recipe = document.toObject(FoodRecipe.class);
                            recipesList.add(recipe);
                        }
                        callback.onCallback(recipesList);
                    } else {
                        System.out.println("Error getting documents: " + task.getException());
                    }
                }).addOnFailureListener(e -> {
                    System.out.println("Query failed: " + e.getMessage());
                });
    }}
/*
kullanım şekli burada gösterildiği gibidir
async await olmadığı için javada callback kullanılarak bu işlem yapıldı

 binding.btnKaydet.setOnClickListener(v -> {
            FirestoreRecipeSearch search = new FirestoreRecipeSearch();
            String collectionName = "tarifler";
            String fieldName = "name";
            String fieldValue = "Puding";

            search.searchDocumentsByFieldName(collectionName, fieldName, fieldValue, new FirestoreCallback() {
                @Override
                public void onCallback(List<FoodRecipe> list) {
                    for (FoodRecipe recipe : list) {
                        System.out.println(recipe);
                    }
                }
            });
        });
 */