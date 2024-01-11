package com.grup4.yemektarifapp.Model;

import java.util.List;

public class UserModel {
    private String id;
    private String name;
    private List<String> favRecipe;
    private List<String> MyRecipe;

    public UserModel(String id, String name, List<String> favRecipe, List<String> myRecipe) {
        this.id = id;
        this.name = name;
        this.favRecipe = favRecipe;
        MyRecipe = myRecipe;
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getFavRecipe() {
        return favRecipe;
    }

    public List<String> getMyRecipe() {
        return MyRecipe;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", favRecipe=" + favRecipe +
                ", MyRecipe=" + MyRecipe +
                '}';
    }


}
