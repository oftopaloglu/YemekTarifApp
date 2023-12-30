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

import androidx.fragment.app.Fragment;

import com.grup4.yemektarifapp.R;

import java.util.ArrayList;
public class AddSpecificationsFragment extends Fragment {

    private ArrayList<String> ingredientsList;
    private ArrayAdapter<String> ingredientsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_specifications, container, false);

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
                    ingredientsList.add(newIngredient);
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
}