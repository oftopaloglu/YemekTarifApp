package com.grup4.yemektarifapp;

import com.grup4.yemektarifapp.Model.FoodRecipe;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SingletonList {

    private static SingletonList instance;
    private List<String> itemList;
    private List<FoodRecipe> recipeList; // Yeni eklenen özellik

    private SingletonList() {
        itemList = new CopyOnWriteArrayList<>();
        recipeList = new CopyOnWriteArrayList<>(); // Yeni eklenen özellik
    }

    public static synchronized SingletonList getInstance() {
        if (instance == null) {
            instance = new SingletonList();
        }
        return instance;
    }

    public List<String> getItemList() {
        return itemList;
    }

    public List<FoodRecipe> getRecipeList() {
        return recipeList;
    }

    public void addItem(String item) {
        itemList.add(item);
    }

    public void removeItem(String item) {
        itemList.remove(item);
    }

    public void setRecipeList(List<FoodRecipe> recipes) {
        recipeList.clear();
        recipeList.addAll(recipes);
    }
}
