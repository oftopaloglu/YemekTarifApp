package com.grup4.yemektarifapp.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.grup4.yemektarifapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddSpecificationsFragment extends Fragment {

    private ArrayList<String> ingredientsList;
    private ArrayAdapter<String> ingredientsAdapter;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_specifications, container, false);

        // Firebase bağlantısını başlat
        db = FirebaseFirestore.getInstance();

        ingredientsList = new ArrayList<>();
        ingredientsAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, ingredientsList);

        view.findViewById(R.id.btnMalzemeler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddIngredientDialog();
            }
        });

        view.findViewById(R.id.btnYapilis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCookingStepsDialog();
            }
        });

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

        builder.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Ekle butonuna basıldığında Firebase'e malzemeyi ekle ve liste temizle
                String newIngredient = ingredientEditText.getText().toString().trim();
                if (!newIngredient.isEmpty()) {
                    addIngredientToFirebase(newIngredient);
                    ingredientsList.add(newIngredient);  // Firebase'e eklenen malzemeyi listeye de ekleyin
                    ingredientsAdapter.notifyDataSetChanged();
                }
            }
        });

        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // EditText'te Enter'a basıldığında malzemeyi listeye ekle
        ingredientEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String newIngredient = ingredientEditText.getText().toString().trim();
                    if (!newIngredient.isEmpty()) {
                        // Listeye malzemeyi ekle ve adapter'a bildirimde bulun
                        addIngredientToFirebase(newIngredient);
                        ingredientsList.add(newIngredient);
                        ingredientsAdapter.notifyDataSetChanged();
                        ingredientEditText.setText("");
                    }
                    return true;
                }
                return false;
            }
        });
    }

    //...

    private void showAddCookingStepsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Yapılış Ekle");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_cooking_step, null);
        final EditText cookingStepEditText = dialogView.findViewById(R.id.edtCookingStep);
        final ListView cookingStepsListView = dialogView.findViewById(R.id.cookingStepsListView);

        cookingStepsListView.setAdapter(ingredientsAdapter);

        builder.setView(dialogView);

        builder.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Ekle butonuna basıldığında Firebase'e yapılışı ekle ve liste temizle
                String newCookingStep = cookingStepEditText.getText().toString().trim();
                if (!newCookingStep.isEmpty()) {
                    addCookingStepToFirebase(newCookingStep);
                    ingredientsList.add(newCookingStep);  // Firebase'e eklenen yapılışı listeye de ekleyin
                    ingredientsAdapter.notifyDataSetChanged();
                }
            }
        });

        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // EditText'te Enter'a basıldığında yapılışı listeye ekle
        cookingStepEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String newCookingStep = cookingStepEditText.getText().toString().trim();
                    if (!newCookingStep.isEmpty()) {
                        // Listeye yapılışı ekle ve adapter'a bildirimde bulun
                        addCookingStepToFirebase(newCookingStep);
                        ingredientsList.add(newCookingStep);
                        ingredientsAdapter.notifyDataSetChanged();
                        cookingStepEditText.setText("");
                    }
                    return true;
                }
                return false;
            }
        });
    }

//...


    private void addIngredientToFirebase(String ingredient) {
        // Firestore'da belgeyi güncellerken mevcut malzemeleri al
        db.collection("yemek_adi")
                .document("Malzemeler")  // Belge ID'sini uygun bir şekilde belirleyin
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Belge varsa malzemeleri al
                            ArrayList<String> existingIngredients = (ArrayList<String>) documentSnapshot.get("malzemeler");

                            // Yeni malzemeyi ekle
                            if (existingIngredients == null) {
                                existingIngredients = new ArrayList<>();
                            }
                            existingIngredients.add(ingredient);

                            // Güncellenmiş malzemelerle belgeyi Firestore'a geri yükle
                            Map<String, Object> updatedData = new HashMap<>();
                            updatedData.put("malzemeler", existingIngredients);

                            db.collection("yemek_adi")
                                    .document("Malzemeler")
                                    .set(updatedData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Veri başarıyla eklendiğinde yapılacak işlemleri buraya ekleyebilirsiniz
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Veri eklenirken bir hata oluştuğunda yapılacak işlemleri buraya ekleyebilirsiniz
                                        }
                                    });
                        } else {
                            // Belge yoksa yeni bir belge oluştur
                            Map<String, Object> newDocument = new HashMap<>();
                            ArrayList<String> newIngredientsList = new ArrayList<>();
                            newIngredientsList.add(ingredient);
                            newDocument.put("malzemeler", newIngredientsList);

                            db.collection("yemek_adi")
                                    .document("Malzemeler")
                                    .set(newDocument)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Veri başarıyla eklendiğinde yapılacak işlemleri buraya ekleyebilirsiniz
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Veri eklenirken bir hata oluştuğunda yapılacak işlemleri buraya ekleyebilirsiniz
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Belgeye erişimde bir hata oluştuğunda yapılacak işlemleri buraya ekleyebilirsiniz
                    }
                });
    }



    private void addCookingStepToFirebase(String cookingStep) {
        Map<String, Object> cookingStepData = new HashMap<>();
        cookingStepData.put("tarif", cookingStep);

        db.collection("yemek_adi")
                .document("Tarif")
                .set(cookingStepData, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Veri başarıyla eklendiğinde yapılacak işlemleri buraya ekleyebilirsiniz
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Veri eklenirken bir hata oluştuğunda yapılacak işlemleri buraya ekleyebilirsiniz
                    }
                });
    }
}
