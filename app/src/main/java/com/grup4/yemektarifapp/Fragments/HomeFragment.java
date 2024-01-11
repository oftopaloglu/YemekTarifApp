package com.grup4.yemektarifapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.grup4.yemektarifapp.Model.FoodRecipe;
import com.grup4.yemektarifapp.R;
import com.grup4.yemektarifapp.databinding.FragmentHomeBinding;
import com.grup4.yemektarifapp.firebase.serach.FirestoreCallback;
import com.grup4.yemektarifapp.firebase.serach.FirestoreFoodSearch;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirestoreFoodSearch firestoreFoodSearch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        firestoreFoodSearch = new FirestoreFoodSearch();
        
        binding.btnTarifAra.setOnClickListener(this::yemekArama);

        return binding.getRoot();
    }
    private void yemekArama(View view) {
        String editTarifAra = binding.editTarifAra.getText().toString();
        firestoreFoodSearch.searchDocumentsByFieldName("tarifler", "name", editTarifAra, new FirestoreCallback() {
            @Override
            public void onCallback(List<FoodRecipe> list) {
                // Handle the list of FoodRecipe objects here
                Toast.makeText(getActivity(), "Found " + list.size() + " results", Toast.LENGTH_SHORT).show();
                // Create a new ResultsFragment with the list of FoodRecipe objects
                ResultsFragment resultsFragment = new ResultsFragment(list);

                // Replace the current fragment with the ResultsFragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, resultsFragment)
                        .addToBackStack(null)
                        .commit();
                System.out.println("Found " + list.size() + " results");
                for (FoodRecipe recipe : list) {
                    System.out.println(recipe.toString());
                }

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
