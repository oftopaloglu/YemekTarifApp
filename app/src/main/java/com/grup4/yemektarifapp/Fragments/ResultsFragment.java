package com.grup4.yemektarifapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grup4.yemektarifapp.Model.FoodRecipe;
import com.grup4.yemektarifapp.adaptor.TariflerAdaptor;
import com.grup4.yemektarifapp.databinding.FragmentResultsBinding;

import java.util.ArrayList;
import java.util.List;

public class ResultsFragment extends Fragment {

    private FragmentResultsBinding binding;
    private TariflerAdaptor tariflerAdaptor;

    public ResultsFragment(List<FoodRecipe> foodRecipes) {
        this.tariflerAdaptor = new TariflerAdaptor(getContext(), foodRecipes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResultsBinding.inflate(inflater, container, false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(tariflerAdaptor);

        return binding.getRoot();
    }

    public void addFavRecipes(String newFav) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference().child("users").child(user.getUid());

        userRef.child("favRecipes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> favRecipes = (ArrayList<String>) dataSnapshot.getValue();
                favRecipes.add(newFav);
                userRef.child("favRecipes").setValue(favRecipes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Hata durumunda buraya bak
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
