package com.grup4.yemektarifapp.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.grup4.yemektarifapp.Model.FoodRecipe;
import com.grup4.yemektarifapp.databinding.FragmentAddSpecificationsBinding;
import com.grup4.yemektarifapp.databinding.DialogYapilisEkleBinding;
import com.grup4.yemektarifapp.databinding.DialogMalzemeEkleBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import android.view.inputmethod.EditorInfo;
import android.widget.TextView;


public class AddSpecificationsFragment extends Fragment {

    private FragmentAddSpecificationsBinding binding;
    private ArrayList<String> MalzemeListesi;
    private ArrayAdapter<String> MalzemeAdapter;
    private ArrayList<String> YapilisListesi;
    private ArrayAdapter<String> YapilisAdapter;
    private FirebaseFirestore db;
    private FoodRecipe foodRecipe;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddSpecificationsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        db = FirebaseFirestore.getInstance();

        foodRecipe = new FoodRecipe();

        MalzemeListesi = new ArrayList<>();
        MalzemeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, MalzemeListesi);

        YapilisListesi = new ArrayList<>();
        YapilisAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, YapilisListesi);

        binding.btnMalzemeler.setOnClickListener(v -> malzemeEkleDialog());
        binding.btnYapilis.setOnClickListener(v -> yapilisEkleDialog());
        binding.btnKaydet.setOnClickListener(v -> firebaseKaydet());

        binding.editYemekAdi.setOnEditorActionListener(this::yemekAdiEkle);

        binding.addphoto.setOnClickListener(v -> fetchAllRecipesFromFirestore());

        return view;
    }

    private void firebaseKaydet() {
        if (!foodRecipe.isEmpty()){
        Gson gson = new Gson();
        String json = gson.toJson(foodRecipe);
        Map<String, Object> tarifMap = gson.fromJson(json, Map.class);
        db.collection("tarifler").document(foodRecipe.getName()).set(tarifMap);
    }}

    

    private boolean yemekAdiEkle(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            foodRecipe.setName(v.getText().toString().trim());
            System.out.println(" ife girdi yemek adı  " + foodRecipe.getName());
            return true;
        }
        return false;
    }
    private void malzemeEkleDialog() {
        System.out.println("1");
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Malzeme Ekle");

        DialogMalzemeEkleBinding malzemeBinding = DialogMalzemeEkleBinding.inflate(getLayoutInflater());
        builder.setView(malzemeBinding.getRoot());

        EditText malzemeEditText = malzemeBinding.editMalzeme;
        ListView malzemeListView = malzemeBinding.malzemeListView;
        malzemeListView.setAdapter(MalzemeAdapter);

        builder.setPositiveButton("Ekle", null);
        builder.setNegativeButton("Tamam", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String malzeme = malzemeEditText.getText().toString().trim();
            if (!malzeme.isEmpty()) {
                listeyeMalzemeEkle(malzeme);
                malzemeEditText.setText("");
            }
        });
    }
    private void listeyeMalzemeEkle(String yeniMalzeme) {
        System.out.println("2");
        if (!yeniMalzeme.isEmpty()) {
            MalzemeListesi.add(yeniMalzeme);
            MalzemeAdapter.notifyDataSetChanged();

            // Eklendiğinde FoodRecipe sınıfındaki material listesine eklemeyi çağır
            foodRecipe.setMaterials(MalzemeListesi);

            // Eklendikten sonra listenin durumunu ekrana yazdır
            System.out.println("getMaterials " + foodRecipe.getMaterials());
        }
    }

    private void yapilisEkleDialog() {
        System.out.println("3");
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Yapılış Ekle");

        final DialogYapilisEkleBinding yapilisBinding = DialogYapilisEkleBinding.inflate(getLayoutInflater());
        builder.setView(yapilisBinding.getRoot());

        final EditText yapilisEditText = yapilisBinding.editYapilis;
        final ListView yapilisListView = yapilisBinding.yapilisListView;

        yapilisListView.setAdapter(YapilisAdapter);

        builder.setPositiveButton("Ekle", null);
        builder.setNegativeButton("Tamam", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String yapilis = yapilisEditText.getText().toString().trim();
            if (!yapilis.isEmpty()) {
                listeyeYapilisEkle(yapilis);
                yapilisEditText.setText("");

            }
        });
    }
    private void listeyeYapilisEkle(String yeniYapilis){
        if (!yeniYapilis.isEmpty()){
            YapilisListesi.add(yeniYapilis);
            YapilisAdapter.notifyDataSetChanged();

            foodRecipe.setNotes(YapilisListesi);

            System.out.println("getNotes " + foodRecipe.getNotes());
        }
    }
    private void fetchDataFromFirestore() {
        db.collection("tarifler").document("musakka")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        FoodRecipe fetchedRecipe = documentSnapshot.toObject(FoodRecipe.class);

                        System.out.println(fetchedRecipe.getMaterials());
                    } else {
                        // Document does not exist
                        System.out.println("olmadı");
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("o2");
                    // Handle failures in fetching data
                    // e.g., Log the error or show a message to the user
                });
    }
    private void fetchAllRecipesFromFirestore() {
        db.collection("tarifler")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Loop through each document and get its data
                        if (documentSnapshot.exists()) {
                            FoodRecipe fetchedRecipe = documentSnapshot.toObject(FoodRecipe.class);

                            if (fetchedRecipe != null) {
                                // Perform operations with fetchedRecipe
                                // For example, log the recipe name
                                System.out.println(fetchedRecipe.toString());

                                // Here you can perform any additional operations with the fetched recipe
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failures in fetching data
                    // e.g., Log the error or show a message to the user
                });
    }
}
