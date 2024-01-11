package com.grup4.yemektarifapp.Model;

public class UserModel {
    String id;
    String name;
    String favRecipe;
    String myRecipe;
    public UserModel(String id, String name, String favRecipe, String myRecipe) {
        this.id = id;
        this.name = name;
        this.favRecipe = favRecipe;
        this.myRecipe = myRecipe;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFavRecipe() {
        return favRecipe;
    }

    public void setFavRecipe(String favRecipe) {
        this.favRecipe = favRecipe;
    }

    public String getMyRecipe() {
        return myRecipe;
    }

    public void setMyRecipe(String myRecipe) {
        this.myRecipe = myRecipe;
    }


}
