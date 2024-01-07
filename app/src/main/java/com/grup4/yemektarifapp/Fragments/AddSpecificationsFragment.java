package com.grup4.yemektarifapp.Fragments;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.grup4.yemektarifapp.R;
import com.grup4.yemektarifapp.databinding.FragmentAddSpecificationsBinding;

public class AddSpecificationsFragment extends Fragment {

    private FragmentAddSpecificationsBinding binding;
    private ArrayList<String> ingredientsList;
    private ArrayAdapter<String> ingredientsAdapter;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddSpecificationsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        db = FirebaseFirestore.getInstance();

        ingredientsList = new ArrayList<>();
        ingredientsAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, ingredientsList);

        binding.btnMalzemeler.setOnClickListener(v -> showAddIngredientDialog());

        binding.btnYapilis.setOnClickListener(v -> showAddCookingStepsDialog());

        return view;
    }

    private void showAddIngredientDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Malzeme Ekle");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_ingredient, null);
        final EditText ingredientEditText = dialogView.findViewById(R.id.edtIngredient);
        final ListView ingredientsListView = dialogView.findViewById(R.id.ingredientsListView);

        ingredientsListView.setAdapter(ingredientsAdapter);

        builder.setView(dialogView);

        builder.setPositiveButton("Ekle", (dialog, which) -> {
            String newIngredient = ingredientEditText.getText().toString().trim();
            if (!newIngredient.isEmpty()) {
                addIngredientToFirebase(newIngredient);
                ingredientsList.add(newIngredient);
                ingredientsAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("İptal", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        ingredientEditText.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == android.view.KeyEvent.ACTION_DOWN) &&
                    (keyCode == android.view.KeyEvent.KEYCODE_ENTER)) {
                String newIngredient = ingredientEditText.getText().toString().trim();
                if (!newIngredient.isEmpty()) {
                    addIngredientToFirebase(newIngredient);
                    ingredientsList.add(newIngredient);
                    ingredientsAdapter.notifyDataSetChanged();
                    ingredientEditText.setText("");
                }
                return true;
            }
            return false;
        });
    }

    private void showAddCookingStepsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Yapılış Ekle");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_cooking_step, null);
        final EditText cookingStepEditText = dialogView.findViewById(R.id.edtCookingStep);
        final ListView cookingStepsListView = dialogView.findViewById(R.id.cookingStepsListView);

        cookingStepsListView.setAdapter(ingredientsAdapter);

        builder.setView(dialogView);

        builder.setPositiveButton("Ekle", (dialog, which) -> {
            String newCookingStep = cookingStepEditText.getText().toString().trim();
            if (!newCookingStep.isEmpty()) {
                addCookingStepToFirebase(newCookingStep);
                ingredientsList.add(newCookingStep);
                ingredientsAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("İptal", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        cookingStepEditText.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == android.view.KeyEvent.ACTION_DOWN) &&
                    (keyCode == android.view.KeyEvent.KEYCODE_ENTER)) {
                String newCookingStep = cookingStepEditText.getText().toString().trim();
                if (!newCookingStep.isEmpty()) {
                    addCookingStepToFirebase(newCookingStep);
                    ingredientsList.add(newCookingStep);
                    ingredientsAdapter.notifyDataSetChanged();
                    cookingStepEditText.setText("");
                }
                return true;
            }
            return false;
        });
    }

    private void addIngredientToFirebase(String ingredient) {
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
    }
}
