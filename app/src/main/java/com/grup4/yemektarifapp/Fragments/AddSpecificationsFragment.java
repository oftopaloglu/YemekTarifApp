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
import com.google.firebase.firestore.FirebaseFirestore;
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
                String newIngredient = ingredientEditText.getText().toString().trim();
                if (!newIngredient.isEmpty()) {
                    // Firebase'e malzemeyi ekle ve liste temizle
                    addIngredientToFirebase(newIngredient);

                    ingredientsList.clear();
                    ingredientsAdapter.notifyDataSetChanged();
                    ingredientEditText.setText("");
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

        // EditText'te Enter'a basıldığında malzemeyi ekle
        ingredientEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String newIngredient = ingredientEditText.getText().toString().trim();
                    if (!newIngredient.isEmpty()) {
                        // Firebase'e malzemeyi ekle ve liste temizle
                        addIngredientToFirebase(newIngredient);

                        ingredientsList.clear();
                        ingredientsAdapter.notifyDataSetChanged();
                        ingredientEditText.setText("");
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void addIngredientToFirebase(String ingredient) {
        // "your_collection" kısmını Firestore veritabanınızdaki gerçek koleksiyon adı ile değiştirin
        Map<String, Object> ingredientData = new HashMap<>();
        ingredientData.put("ingredientName", ingredient);

        db.collection("your_collection")
                .add(ingredientData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
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
