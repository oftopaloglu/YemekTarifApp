package com.grup4.yemektarifapp.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.grup4.yemektarifapp.Model.FoodRecipe;
import com.grup4.yemektarifapp.SingletonList;
import com.grup4.yemektarifapp.firebase.serach.FirestoreCallback;

import java.util.ArrayList;
import java.util.List;

public class GetRecipeList {

    public static void fetchFirestoreData(FirestoreCallback callback) {
        SingletonList singletonList = SingletonList.getInstance();

        // Firebase Authentication kullanarak kullanıcı giriş durumunu kontrol et
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // Kullanıcı giriş yapmışsa Firestore'dan veri çek
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("tarifler")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<FoodRecipe> foodRecipes = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FoodRecipe foodRecipe = document.toObject(FoodRecipe.class);
                                foodRecipes.add(foodRecipe);
                            }

                            // SingletonList içindeki recipeList'e veriyi set et
                            singletonList.setRecipeList(foodRecipes);

                            // Geri çağrım ile işlem tamamlandığını bildir
                            callback.onCallback(foodRecipes);

                        } else {
                            // Firestore'dan veri çekme başarısız olursa buraya düşer
                            Exception exception = task.getException();
                            if (exception != null) {
                                exception.printStackTrace();
                                // Hata durumunda kullanıcıya geri bildirimde bulunabilirsiniz.
                            }
                        }
                    });
        } else {
            // Kullanıcı giriş yapmamışsa burada gerekli işlemleri yapabilirsiniz.
            // Örneğin, kullanıcıyı giriş yapmaya yönlendirebilir veya bir hata mesajı gösterebilirsiniz.
        }
    }
}
