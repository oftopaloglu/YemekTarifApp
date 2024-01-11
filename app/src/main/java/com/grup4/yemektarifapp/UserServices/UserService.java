package com.grup4.yemektarifapp.UserServices;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grup4.yemektarifapp.Model.UserModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UserService {
    public static void saveUserToDatabase(FirebaseUser user, FirebaseDatabase database) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", user.getUid());
        map.put("name", user.getDisplayName());
        map.put("profile", user.getPhotoUrl().toString());

        // FavRecipes örneği
        ArrayList<String> favRecipes = new ArrayList<>();
        favRecipes.add("null");
        map.put("favRecipes", favRecipes);

        // MyRecipes örneği
        ArrayList<String> myRecipes = new ArrayList<>();
        myRecipes.add("null");
        map.put("MyRecipes", myRecipes);


        database.getReference().child("users").child(user.getUid()).setValue(map);
    }
    public static FirebaseUser getLoggedInUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
    /*public static UserModel getUserModelFromFirebaseUser(FirebaseUser user) {
        if (user != null) {
            String userId = user.getUid();
            String userName = user.getDisplayName();
            String userProfile = user.getPhotoUrl().toString();

            // FavRecipes örneği
            List<String> favRecipes = user.

            // MyRecipes örneği
            List<String> myRecipes = Arrays.("recipeId3", "recipeId4");

            return new UserModel(userId, userName,userProfile, favRecipes, myRecipes);
        }
        return null;
    }*/




}
