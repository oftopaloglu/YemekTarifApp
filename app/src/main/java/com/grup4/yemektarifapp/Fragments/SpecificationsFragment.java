package com.grup4.yemektarifapp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grup4.yemektarifapp.Model.FoodRecipe;
import com.grup4.yemektarifapp.R;
import com.grup4.yemektarifapp.adaptor.TariflerAdaptor;

import java.util.ArrayList;
import java.util.List;

public class SpecificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TariflerAdaptor tariflerAdaptor;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_specifications, container, false);

        // RecyclerView'i bul
        recyclerView = view.findViewById(R.id.recyclerView);

        // Veri oluştur
        List<FoodRecipe> foodRecipes = createDummyData();

        // Adaptor oluştur
        tariflerAdaptor = new TariflerAdaptor(getContext(), foodRecipes);

        // RecyclerView için layout manager belirle (örneğin, LinearLayoutManager)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Adaptoru RecyclerView'e set et
        recyclerView.setAdapter(tariflerAdaptor);

        return view;
    }

    // Dummy verileri oluştur
    private List<FoodRecipe> createDummyData() {
        List<FoodRecipe> dummyData = new ArrayList<>();

        // Örnek bir yemek tarifi oluştur
        FoodRecipe foodRecipe1 = new FoodRecipe();
        foodRecipe1.setName("Example Dish 1");
        List<String> materials1 = new ArrayList<>();
        materials1.add("Ingredient 1");
        materials1.add("Ingredient 2");
        materials1.add("Ingredient 3");
        foodRecipe1.setMaterials(materials1);
        foodRecipe1.setPhotoUrl("https://example.com/photo1.jpg");
        List<String> notes1 = new ArrayList<>();
        notes1.add("Step 1");
        notes1.add("Step 2");
        notes1.add("Step 3");
        foodRecipe1.setNotes(notes1);
        dummyData.add(foodRecipe1);

        // Birkaç tane daha örnek yemek tarifi ekleyebilirsiniz...

        return dummyData;
    }
}
