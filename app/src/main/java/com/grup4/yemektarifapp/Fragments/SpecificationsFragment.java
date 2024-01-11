package com.grup4.yemektarifapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.grup4.yemektarifapp.Model.FoodRecipe;
import com.grup4.yemektarifapp.R;
import com.grup4.yemektarifapp.adaptor.TariflerAdaptor;
import java.util.ArrayList;
import java.util.List;

public class SpecificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TariflerAdaptor tariflerAdaptor;
    private final List<FoodRecipe> foodRecipes = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_specifications, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        // Firestore'dan verileri çek ve RecyclerView'a set et
        fetchFirestoreData();

        return view;
    }

    private void fetchFirestoreData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("tarifler")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            FoodRecipe foodRecipe = document.toObject(FoodRecipe.class);
                            foodRecipes.add(foodRecipe);
                        }
                        // RecyclerView ve Adapter'ı ayarla
                        setupRecyclerView();
                    } else {
                        // Firestore'dan veri çekme başarısız olursa buraya düşer
                        Exception exception = task.getException();
                        if (exception != null) {
                            exception.printStackTrace();
                            // Hata durumunda kullanıcıya geri bildirimde bulunabilirsiniz.
                        }
                    }
                });
    }


    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Firestore'dan çekilen verileri RecyclerView'a set etmek için Adapter'ı kullan
        tariflerAdaptor = new TariflerAdaptor(getContext(), foodRecipes);
        recyclerView.setAdapter(tariflerAdaptor);
    }
}
