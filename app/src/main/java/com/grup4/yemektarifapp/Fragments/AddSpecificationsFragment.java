package com.grup4.yemektarifapp.Fragments;

import android.os.Bundle;
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

import com.google.firebase.firestore.FirebaseFirestore;
import com.grup4.yemektarifapp.Model.FoodRecipe;
import com.grup4.yemektarifapp.databinding.FragmentAddSpecificationsBinding;
import com.grup4.yemektarifapp.databinding.DialogYapilisEkleBinding;
import com.grup4.yemektarifapp.databinding.DialogMalzemeEkleBinding;

import java.util.ArrayList;
import android.view.inputmethod.EditorInfo;


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

        binding.editYemekAdi.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Enter tuşuna basıldığında buraya gelir

                foodRecipe.setName(binding.editYemekAdi.getText().toString().trim());
                System.out.println(" ife girdi yemek adı  " + foodRecipe.getName());
                return true; // Olayın tüketildiğini belirt
            }
            return false; // Olayın işlenmediğini belirt
        });
        return view;
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
    
}










   /* private void addIngredientToFirebase(String ingredient) {
        db.collection("yemek_adi")
                .document("Malzemeler")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        ArrayList<String> existingIngredients = (ArrayList<String>) documentSnapshot.get("malzemeler");

                        if (existingIngredients == null) {
                            existingIngredients = new ArrayList<>();
                        }
                        existingIngredients.add(ingredient);

                        Map<String, Object> updatedData = new HashMap<>();
                        updatedData.put("malzemeler", existingIngredients);

                        db.collection("yemek_adi")
                                .document("Malzemeler")
                                .set(updatedData)
                                .addOnSuccessListener(aVoid -> {
                                    // Başarıyla eklendiğinde yapılacak işlemleri buraya ekleyebilirsiniz
                                })
                                .addOnFailureListener(e -> {
                                    // Eklenirken bir hata oluştuğunda yapılacak işlemleri buraya ekleyebilirsiniz
                                });
                    } else {
                        Map<String, Object> newDocument = new HashMap<>();
                        ArrayList<String> newIngredientsList = new ArrayList<>();
                        newIngredientsList.add(ingredient);
                        newDocument.put("malzemeler", newIngredientsList);

                        db.collection("yemek_adi")
                                .document("Malzemeler")
                                .set(newDocument)
                                .addOnSuccessListener(aVoid -> {
                                    // Başarıyla eklendiğinde yapılacak işlemleri buraya ekleyebilirsiniz
                                })
                                .addOnFailureListener(e -> {
                                    // Eklenirken bir hata oluştuğunda yapılacak işlemleri buraya ekleyebilirsiniz
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Erişimde bir hata oluştuğunda yapılacak işlemleri buraya ekleyebilirsiniz
                });
    }

    private void addCookingStepToFirebase(String cookingStep) {
        Map<String, Object> cookingStepData = new HashMap<>();
        cookingStepData.put("tarif", cookingStep);

        db.collection("yemek_adi")
                .document("Tarif")
                .set(cookingStepData)
                .addOnSuccessListener(aVoid -> {
                    // Başarıyla eklendiğinde yapılacak işlemleri buraya ekleyebilirsiniz
                })
                .addOnFailureListener(e -> {
                    // Eklenirken bir hata oluştuğunda yapılacak işlemleri buraya ekleyebilirsiniz
                });
    } */

