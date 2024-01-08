package com.grup4.yemektarifapp.firebase.serach;

import com.grup4.yemektarifapp.Model.FoodRecipe;

import java.util.List;

public interface FirestoreCallback {
    void onCallback(List<FoodRecipe> list);
}
