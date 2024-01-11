package com.grup4.yemektarifapp.Model;

import java.util.ArrayList;
import java.util.List;

public class UserModel {

    private String id;
    private String name;
    private  String photoUrl;
    private ArrayList<String> favRecipe;
    private ArrayList<String> MyRecipe;
    public UserModel(String id, String name, String photoUrl, ArrayList<String> favRecipe, ArrayList<String> myRecipe) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.favRecipe = favRecipe;
        MyRecipe = myRecipe;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getFavRecipe() {
        return favRecipe;
    }

    public ArrayList<String> getMyRecipe() {
        return MyRecipe;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", favRecipe=" + favRecipe +
                ", MyRecipe=" + MyRecipe +
                '}';
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setFavRecipe(ArrayList<String> favRecipe) {
        this.favRecipe = favRecipe;
    }

    public void setMyRecipe(ArrayList<String> myRecipe) {
        MyRecipe = myRecipe;
    }


}
